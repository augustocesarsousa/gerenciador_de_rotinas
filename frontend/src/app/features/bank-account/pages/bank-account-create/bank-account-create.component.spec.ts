import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

import { BankAccountCreateComponent } from './bank-account-create.component';
import { BankAccountService } from '../../services/bank-account.service';
import { BankService } from '../../../bank/services/bank.service';
import { BankResponse } from '../../../bank/models/bank.model';

describe('BankAccountCreateComponent', () => {
  let component: BankAccountCreateComponent;
  let fixture: ComponentFixture<BankAccountCreateComponent>;

  const mockBanks: BankResponse[] = [
    {
      id: 1,
      code: '001',
      name: 'Banco do Brasil S.A.',
      shortName: 'Banco do Brasil',
      ispb: '00000000',
      status: 'ACTIVE',
      createdAt: '2026-01-01',
      updatedAt: '2026-01-01',
      userIdEdit: 1,
    },
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
    create: vi.fn(),
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

  beforeEach(async () => {
    vi.clearAllMocks();
    bankServiceMock.findAll.mockReturnValue(of({ content: mockBanks, totalElements: 2 }));

    await TestBed.configureTestingModule({
      imports: [BankAccountCreateComponent, ReactiveFormsModule],
      providers: [
        provideNoopAnimations(),
        { provide: BankAccountService, useValue: bankAccountServiceMock },
        { provide: BankService, useValue: bankServiceMock },
        { provide: ToastrService, useValue: toastrMock },
        { provide: Router, useValue: routerMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(BankAccountCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  describe('Component Initialization', () => {
    it('should create the component instance', () => {
      // then
      expect(component).toBeTruthy();
    });

    it('should initialize the form with default values and invalid status', () => {
      // then
      expect(component.bankAccountCreateForm.invalid).toBe(true);
      expect(component.bankAccountCreateForm.get('accountType')?.value).toBe('CHECKING');
      expect(component.bankAccountCreateForm.get('initialBalance')?.value).toBe(0.0);
      expect(component.isCashDesk()).toBe(false);
    });

    it('should load active banks into banks signal on init', () => {
      // then
      expect(bankServiceMock.findAll).toHaveBeenCalledWith({ status: 'ACTIVE' }, 0, 100);
      expect(component.banks()).toEqual(mockBanks);
    });

    it('should show toastr error when loading banks fails', () => {
      // given
      bankServiceMock.findAll.mockReturnValue(throwError(() => new Error('Error')));

      // when
      component.loadBanks();

      // then
      expect(toastrMock.error).toHaveBeenCalledWith('Erro ao carregar lista de bancos.');
    });
  });

  describe('Conditional Validations (Conventional vs CASH_DESK)', () => {
    it('should enforce bankId, agency and account fields when accountType is CHECKING', () => {
      // given
      component.bankAccountCreateForm.patchValue({ accountType: 'CHECKING' });

      // when
      const bankCtrl = component.bankAccountCreateForm.get('bankId');
      const agencyCtrl = component.bankAccountCreateForm.get('agencyNumber');
      const accountCtrl = component.bankAccountCreateForm.get('accountNumber');
      const accountDigitCtrl = component.bankAccountCreateForm.get('accountDigit');

      bankCtrl?.setValue('');
      agencyCtrl?.setValue('');
      accountCtrl?.setValue('');
      accountDigitCtrl?.setValue('');

      // then
      expect(bankCtrl?.hasError('required')).toBe(true);
      expect(agencyCtrl?.hasError('required')).toBe(true);
      expect(accountCtrl?.hasError('required')).toBe(true);
      expect(accountDigitCtrl?.hasError('required')).toBe(true);
    });

    it('should enforce maximum lengths for agency (5), account (12), and accountDigit (2)', () => {
      // given
      const agencyCtrl = component.bankAccountCreateForm.get('agencyNumber');
      const accountCtrl = component.bankAccountCreateForm.get('accountNumber');
      const accountDigitCtrl = component.bankAccountCreateForm.get('accountDigit');

      // when
      agencyCtrl?.setValue('123456'); // 6 chars
      accountCtrl?.setValue('1234567890123'); // 13 chars
      accountDigitCtrl?.setValue('123'); // 3 chars

      // then
      expect(agencyCtrl?.hasError('maxlength')).toBe(true);
      expect(accountCtrl?.hasError('maxlength')).toBe(true);
      expect(accountDigitCtrl?.hasError('maxlength')).toBe(true);
    });

    it('should clear validators and reset values when switching to CASH_DESK', () => {
      // given
      component.bankAccountCreateForm.patchValue({
        bankId: 1,
        agencyNumber: '1234',
        agencyDigit: '0',
        accountNumber: '56789',
        accountDigit: '1',
      });

      // when
      component.bankAccountCreateForm.get('accountType')?.setValue('CASH_DESK');
      fixture.detectChanges();

      // then
      expect(component.isCashDesk()).toBe(true);
      expect(component.bankAccountCreateForm.get('bankId')?.value).toBe('');
      expect(component.bankAccountCreateForm.get('bankId')?.validator).toBeNull();
      expect(component.bankAccountCreateForm.get('agencyNumber')?.value).toBe('');
      expect(component.bankAccountCreateForm.get('agencyNumber')?.validator).toBeNull();
      expect(component.bankAccountCreateForm.get('accountNumber')?.value).toBe('');
      expect(component.bankAccountCreateForm.get('accountNumber')?.validator).toBeNull();
      expect(component.bankAccountCreateForm.get('accountDigit')?.value).toBe('');
      expect(component.bankAccountCreateForm.get('accountDigit')?.validator).toBeNull();
    });

    it('should be valid with only description and initial balance for CASH_DESK', () => {
      // given
      component.bankAccountCreateForm.get('accountType')?.setValue('CASH_DESK');

      // when
      component.bankAccountCreateForm.patchValue({
        description: 'Caixa Cantina',
        initialBalance: 250.0,
        initialBalanceDate: '2026-01-01',
      });

      // then
      expect(component.bankAccountCreateForm.valid).toBe(true);
    });
  });

  describe('Form Submission & Actions', () => {
    it('should keep Salvar button disabled when form is invalid', () => {
      // given
      component.bankAccountCreateForm.patchValue({ description: '' });
      fixture.detectChanges();

      // when
      const saveBtn = fixture.debugElement.query(By.css('button.bg-emerald-600\\!')).nativeElement;

      // then
      expect(component.bankAccountCreateForm.invalid).toBe(true);
      expect(saveBtn.disabled).toBe(true);
    });

    it('should call createBankAccount with sanitised payload and navigate on success', () => {
      // given
      bankAccountServiceMock.create.mockReturnValue(of(10));

      component.bankAccountCreateForm.patchValue({
        accountType: 'CHECKING',
        bankId: 1,
        description: 'Conta Principal',
        agencyNumber: '1234',
        agencyDigit: '0',
        accountNumber: '998877',
        accountDigit: '2',
        projectOrAgreement: 'Edital 01',
        initialBalance: 1000,
        initialBalanceDate: '2026-01-01',
      });

      // when
      component.createBankAccount();

      // then
      expect(bankAccountServiceMock.create).toHaveBeenCalledWith(
        expect.objectContaining({
          accountType: 'CHECKING',
          bankId: 1,
          description: 'Conta Principal',
          agencyNumber: '1234',
          agencyDigit: '0',
          accountNumber: '998877',
          accountDigit: '2',
          initialBalance: 1000,
          userIdEdit: 1,
        }),
      );
      expect(toastrMock.success).toHaveBeenCalledWith('Conta bancária cadastrada com sucesso!');
      expect(routerMock.navigate).toHaveBeenCalledWith(['/financeiro/cadastros/contas-bancarias']);
    });

    it('should set bankId and agency/account fields to null when submitting CASH_DESK', () => {
      // given
      bankAccountServiceMock.create.mockReturnValue(of(11));

      component.bankAccountCreateForm.get('accountType')?.setValue('CASH_DESK');
      component.bankAccountCreateForm.patchValue({
        description: 'Caixa de Doações',
        initialBalance: 50,
        initialBalanceDate: '2026-01-01',
      });

      // when
      component.createBankAccount();

      // then
      expect(bankAccountServiceMock.create).toHaveBeenCalledWith(
        expect.objectContaining({
          accountType: 'CASH_DESK',
          bankId: null,
          agencyNumber: null,
          agencyDigit: null,
          accountNumber: null,
          accountDigit: null,
        }),
      );
    });

    it('should display error message on toastr when create service fails', () => {
      // given
      bankAccountServiceMock.create.mockReturnValue(
        throwError(() => ({ error: { detail: 'Conta já cadastrada' } })),
      );

      component.bankAccountCreateForm.patchValue({
        accountType: 'CHECKING',
        bankId: 1,
        description: 'Conta Repetida',
        agencyNumber: '1234',
        accountNumber: '998877',
        accountDigit: '2',
        initialBalance: 1000,
        initialBalanceDate: '2026-01-01',
      });

      // when
      component.createBankAccount();

      // then
      expect(toastrMock.error).toHaveBeenCalledWith('Conta já cadastrada');
      expect(routerMock.navigate).not.toHaveBeenCalled();
    });

    it('should navigate to search page without submitting when cancel button is clicked', () => {
      // when
      component.returnToSearch();

      // then
      expect(routerMock.navigate).toHaveBeenCalledWith(['/financeiro/cadastros/contas-bancarias']);
      expect(bankAccountServiceMock.create).not.toHaveBeenCalled();
    });
  });

  describe('DOM Rendering & Template Display', () => {
    it('should hide bank, agency, and account inputs when isCashDesk is true', () => {
      // given
      component.bankAccountCreateForm.get('accountType')?.setValue('CASH_DESK');
      fixture.detectChanges();

      // when
      const bankSelect = fixture.debugElement.query(By.css('mat-select[formControlName="bankId"]'));
      const agencyInput = fixture.debugElement.query(By.css('input[formControlName="agencyNumber"]'));
      const accountInput = fixture.debugElement.query(By.css('input[formControlName="accountNumber"]'));

      // then
      expect(bankSelect).toBeNull();
      expect(agencyInput).toBeNull();
      expect(accountInput).toBeNull();
    });

    it('should show bank, agency, and account inputs when isCashDesk is false', () => {
      // given
      component.bankAccountCreateForm.get('accountType')?.setValue('SAVINGS');
      fixture.detectChanges();

      // when
      const bankSelect = fixture.debugElement.query(By.css('mat-select[formControlName="bankId"]'));
      const agencyInput = fixture.debugElement.query(By.css('input[formControlName="agencyNumber"]'));
      const accountInput = fixture.debugElement.query(By.css('input[formControlName="accountNumber"]'));

      // then
      expect(bankSelect).toBeTruthy();
      expect(agencyInput).toBeTruthy();
      expect(accountInput).toBeTruthy();
    });
  });
});
