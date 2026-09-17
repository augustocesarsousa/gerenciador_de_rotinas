import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { By } from '@angular/platform-browser';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

import { BankAccountEditComponent } from './bank-account-edit.component';
import { BankAccountService } from '../../services/bank-account.service';
import { BankService } from '../../../bank/services/bank.service';
import { BankAccountResponse } from '../../models/bank-account.model';
import { BankResponse } from '../../../bank/models/bank.model';

describe('BankAccountEditComponent', () => {
  let component: BankAccountEditComponent;
  let fixture: ComponentFixture<BankAccountEditComponent>;

  const mockAccountResponse: BankAccountResponse = {
    id: 1,
    bankId: 2,
    bankName: 'Bradesco',
    bankCode: '237',
    accountType: 'CHECKING',
    description: 'Conta Edição',
    agencyNumber: '4321',
    agencyDigit: '9',
    accountNumber: '876543',
    accountDigit: '0',
    projectOrAgreement: 'Projeto Esperança',
    initialBalance: 5000,
    initialBalanceDate: '2026-01-15',
    status: 'ACTIVE',
    createdAt: '2026-01-15T12:00:00',
    updatedAt: '2026-01-16T15:30:00',
    userIdEdit: 1,
  };

  const mockBanks: BankResponse[] = [
    {
      id: 2,
      code: '237',
      name: 'Banco Bradesco S.A.',
      shortName: 'Bradesco',
      ispb: '60746948',
      status: 'ACTIVE',
      createdAt: '2026-01-01',
      updatedAt: '2026-01-01',
      userIdEdit: 1,
    },
  ];

  const bankAccountServiceMock = {
    findById: vi.fn(),
    update: vi.fn(),
  };

  const bankServiceMock = {
    findAll: vi.fn(),
  };

  const toastrMock = {
    success: vi.fn(),
    error: vi.fn(),
  };

  const routerMock = {
    navigate: vi.fn().mockReturnValue(Promise.resolve(true)),
  };

  const activatedRouteMock = {
    snapshot: {
      paramMap: {
        get: vi.fn().mockReturnValue('1'),
      },
    },
  };

  beforeEach(async () => {
    vi.clearAllMocks();
    activatedRouteMock.snapshot.paramMap.get.mockReturnValue('1');
    bankServiceMock.findAll.mockReturnValue(of({ content: mockBanks, totalElements: 1 }));
    bankAccountServiceMock.findById.mockReturnValue(of(mockAccountResponse));

    await TestBed.configureTestingModule({
      imports: [BankAccountEditComponent, ReactiveFormsModule],
      providers: [
        provideNoopAnimations(),
        { provide: BankAccountService, useValue: bankAccountServiceMock },
        { provide: BankService, useValue: bankServiceMock },
        { provide: ToastrService, useValue: toastrMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(BankAccountEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  describe('Route Param & Data Initialization', () => {
    it('should navigate back to search when id param is missing', () => {
      // given
      activatedRouteMock.snapshot.paramMap.get.mockReturnValue(null);
      const fixtureNoId = TestBed.createComponent(BankAccountEditComponent);
      const compNoId = fixtureNoId.componentInstance;

      // when
      compNoId.ngOnInit();

      // then
      expect(toastrMock.error).toHaveBeenCalledWith('ID da conta bancária não informado.');
      expect(routerMock.navigate).toHaveBeenCalledWith(['/financeiro/cadastros/contas-bancarias']);
    });

    it('should load account data and populate the form on init', () => {
      // then
      expect(bankAccountServiceMock.findById).toHaveBeenCalledWith(1);
      expect(component.bankAccountEditForm.get('description')?.value).toBe('Conta Edição');
      expect(component.bankAccountEditForm.get('bankId')?.value).toBe(2);
      expect(component.bankAccountEditForm.get('agencyNumber')?.value).toBe('4321');
      expect(component.bankAccountEditForm.get('status')?.value).toBe('ACTIVE');
      expect(component.isLoading()).toBe(false);
      expect(component.formReady()).toBe(true);
    });

    it('should keep id, createdAt, and updatedAt controls disabled', () => {
      // then
      expect(component.bankAccountEditForm.get('id')?.disabled).toBe(true);
      expect(component.bankAccountEditForm.get('createdAt')?.disabled).toBe(true);
      expect(component.bankAccountEditForm.get('updatedAt')?.disabled).toBe(true);
    });

    it('should handle error when findById fails and navigate to search', () => {
      // given
      bankAccountServiceMock.findById.mockReturnValue(throwError(() => new Error('Not found')));

      // when
      component.loadAccountData();

      // then
      expect(toastrMock.error).toHaveBeenCalledWith('Erro ao carregar dados da conta bancária.');
      expect(routerMock.navigate).toHaveBeenCalledWith(['/financeiro/cadastros/contas-bancarias']);
    });
  });

  describe('Dynamic Validation on Edit', () => {
    it('should switch to CASH_DESK and clear bank/agency/account validators', () => {
      // when
      component.bankAccountEditForm.get('accountType')?.setValue('CASH_DESK');
      fixture.detectChanges();

      // then
      expect(component.isCashDesk()).toBe(true);
      expect(component.bankAccountEditForm.get('bankId')?.validator).toBeNull();
      expect(component.bankAccountEditForm.get('agencyNumber')?.validator).toBeNull();
      expect(component.bankAccountEditForm.get('accountNumber')?.validator).toBeNull();
    });
  });

  describe('Form Update Action', () => {
    it('should send update request with valid payload and navigate on success', () => {
      // given
      bankAccountServiceMock.update.mockReturnValue(of(1));

      component.bankAccountEditForm.patchValue({
        description: 'Conta Atualizada',
        status: 'INACTIVE',
      });

      // when
      component.updateBankAccount();

      // then
      expect(bankAccountServiceMock.update).toHaveBeenCalledWith(
        1,
        expect.objectContaining({
          description: 'Conta Atualizada',
          status: 'INACTIVE',
          userIdEdit: 1,
        }),
      );
      expect(toastrMock.success).toHaveBeenCalledWith('Conta bancária atualizada com sucesso!');
      expect(routerMock.navigate).toHaveBeenCalledWith(['/financeiro/cadastros/contas-bancarias']);
    });

    it('should show toastr error and not navigate when update fails', () => {
      // given
      bankAccountServiceMock.update.mockReturnValue(
        throwError(() => ({ error: { detail: 'Erro de concorrência' } })),
      );

      // when
      component.updateBankAccount();

      // then
      expect(toastrMock.error).toHaveBeenCalledWith('Erro de concorrência');
      expect(routerMock.navigate).not.toHaveBeenCalled();
    });

    it('should return to search when Cancel button is clicked', () => {
      // when
      component.returnToSearch();

      // then
      expect(routerMock.navigate).toHaveBeenCalledWith(['/financeiro/cadastros/contas-bancarias']);
      expect(bankAccountServiceMock.update).not.toHaveBeenCalled();
    });
  });
});
