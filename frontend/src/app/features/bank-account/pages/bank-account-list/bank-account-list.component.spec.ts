import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { By } from '@angular/platform-browser';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { PageEvent } from '@angular/material/paginator';

import { BankAccountListComponent } from './bank-account-list.component';
import { BankAccountService } from '../../services/bank-account.service';
import { BankService } from '../../../bank/services/bank.service';
import { BankAccountResponse } from '../../models/bank-account.model';
import { BankResponse } from '../../../bank/models/bank.model';

describe('BankAccountListComponent', () => {
  let component: BankAccountListComponent;
  let fixture: ComponentFixture<BankAccountListComponent>;

  const mockBankAccounts: BankAccountResponse[] = [
    {
      id: 1,
      bankId: 10,
      bankName: 'Banco do Brasil',
      bankCode: '001',
      accountType: 'CHECKING',
      description: 'Conta Principal',
      agencyNumber: '1234',
      agencyDigit: '5',
      accountNumber: '998877',
      accountDigit: '0',
      initialBalance: 15000.75,
      initialBalanceDate: '2026-01-01',
      status: 'ACTIVE',
      createdAt: '2026-01-01',
      updatedAt: '2026-01-01',
      userIdEdit: 1,
    },
    {
      id: 2,
      bankId: null,
      accountType: 'CASH_DESK',
      description: 'Caixa Pequeno',
      initialBalance: 350.0,
      initialBalanceDate: '2026-01-01',
      status: 'INACTIVE',
      createdAt: '2026-01-01',
      updatedAt: '2026-01-01',
      userIdEdit: 1,
    },
  ];

  const mockBanks: BankResponse[] = [
    {
      id: 10,
      code: '001',
      name: 'Banco do Brasil S.A.',
      shortName: 'Banco do Brasil',
      ispb: '00000000',
      status: 'ACTIVE',
      createdAt: '2026-01-01',
      updatedAt: '2026-01-01',
      userIdEdit: 1,
    },
  ];

  const bankAccountServiceMock = {
    findAll: vi.fn(),
  };

  const bankServiceMock = {
    findAll: vi.fn(),
  };

  const toastrMock = {
    error: vi.fn(),
  };

  beforeEach(async () => {
    vi.clearAllMocks();
    bankServiceMock.findAll.mockReturnValue(of({ content: mockBanks, totalElements: 1 }));
    bankAccountServiceMock.findAll.mockReturnValue(
      of({ content: mockBankAccounts, totalElements: 2 }),
    );

    await TestBed.configureTestingModule({
      imports: [BankAccountListComponent, ReactiveFormsModule, RouterModule],
      providers: [
        provideNoopAnimations(),
        { provide: BankAccountService, useValue: bankAccountServiceMock },
        { provide: BankService, useValue: bankServiceMock },
        { provide: ToastrService, useValue: toastrMock },
        { provide: ActivatedRoute, useValue: {} },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(BankAccountListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  describe('Initialization', () => {
    it('should create component and fetch banks and bank accounts on init', () => {
      // then
      expect(component).toBeTruthy();
      expect(bankServiceMock.findAll).toHaveBeenCalledWith({ status: 'ACTIVE' }, 0, 100);
      expect(bankAccountServiceMock.findAll).toHaveBeenCalledWith({}, 0, 10);
      expect(component.bankAccounts().length).toBe(2);
      expect(component.totalElements()).toBe(2);
    });

    it('should show toastr error when bank loading fails', () => {
      // given
      bankServiceMock.findAll.mockReturnValue(throwError(() => new Error('Error')));

      // when
      component.loadBanks();

      // then
      expect(toastrMock.error).toHaveBeenCalledWith(
        'Erro ao carregar lista de bancos para filtro.',
      );
    });

    it('should show toastr error when bank accounts query fails', () => {
      // given
      bankAccountServiceMock.findAll.mockReturnValue(throwError(() => new Error('Error')));

      // when
      component.getAllBankAccounts();

      // then
      expect(toastrMock.error).toHaveBeenCalledWith('Erro ao buscar contas bancárias.');
    });
  });

  describe('Table Rendering & Formatting', () => {
    it('should correctly format account types into readable Portuguese', () => {
      // then
      expect(component.formatAccountType('CHECKING')).toBe('Corrente');
      expect(component.formatAccountType('SAVINGS')).toBe('Poupança');
      expect(component.formatAccountType('INVESTMENT')).toBe('Aplicação');
      expect(component.formatAccountType('CASH_DESK')).toBe('Caixa Interno');
      expect(component.formatAccountType('OTHER')).toBe('OTHER');
    });

    it('should apply red styling class to INACTIVE rows', () => {
      // given
      const rows = fixture.debugElement.queryAll(By.css('tr[mat-row]'));

      // then
      expect(rows.length).toBe(2);
      const activeRowClasses = rows[0].nativeElement.className;
      const inactiveRowClasses = rows[1].nativeElement.className;

      expect(activeRowClasses).not.toContain('bg-red-100!');
      expect(inactiveRowClasses).toContain('bg-red-100!');
      expect(inactiveRowClasses).toContain('text-red-600!');
    });
  });

  describe('Filter Form & Pagination', () => {
    it('should call findAll with search form values when submitted', () => {
      // given
      component.searchForm.patchValue({
        description: 'Conta Especial',
        bankId: '10',
        accountType: 'CHECKING',
        status: 'ACTIVE',
      });

      // when
      component.getAllBankAccounts();

      // then
      expect(bankAccountServiceMock.findAll).toHaveBeenCalledWith(
        {
          description: 'Conta Especial',
          bankId: 10,
          accountType: 'CHECKING',
          status: 'ACTIVE',
        },
        0,
        10,
      );
    });

    it('should update pageIndex and pageSize and trigger reload on page change', () => {
      // given
      const pageEvent: PageEvent = {
        pageIndex: 2,
        pageSize: 20,
        length: 50,
      };

      // when
      component.onPageChange(pageEvent);

      // then
      expect(component.pageIndex()).toBe(2);
      expect(component.pageSize()).toBe(20);
      expect(bankAccountServiceMock.findAll).toHaveBeenCalledWith(expect.any(Object), 2, 20);
    });
  });
});
