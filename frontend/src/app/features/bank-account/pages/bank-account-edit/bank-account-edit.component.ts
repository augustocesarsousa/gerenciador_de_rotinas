import { ChangeDetectorRef, Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ToastrService } from 'ngx-toastr';
import { BankAccountService } from '../../services/bank-account.service';
import { BankAccountResponse, BankAccountType, BankAccountUpdateRequest, EntityStatus } from '../../models/bank-account.model';
import { BankService } from '../../../bank/services/bank.service';
import { BankResponse } from '../../../bank/models/bank.model';

@Component({
  selector: 'app-bank-account-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatRadioModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './bank-account-edit.component.html',
})
export class BankAccountEditComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly bankAccountService = inject(BankAccountService);
  private readonly bankService = inject(BankService);
  private readonly toastr = inject(ToastrService);
  private readonly cdr = inject(ChangeDetectorRef);

  readonly isLoading = signal<boolean>(true);
  readonly formReady = signal<boolean>(false);
  readonly isCashDesk = signal<boolean>(false);
  readonly banks = signal<BankResponse[]>([]);

  accountId!: number;

  bankAccountEditForm: FormGroup = this.fb.group({
    id: [{ value: '', disabled: true }],
    status: ['ACTIVE' as EntityStatus, [Validators.required]],
    accountType: ['CHECKING' as BankAccountType, [Validators.required]],
    bankId: ['', [Validators.required]],
    description: ['', [Validators.required, Validators.maxLength(100)]],
    agencyNumber: ['', [Validators.required, Validators.maxLength(5)]],
    agencyDigit: ['', [Validators.maxLength(2)]],
    accountNumber: ['', [Validators.required, Validators.maxLength(12)]],
    accountDigit: ['', [Validators.required, Validators.maxLength(2)]],
    projectOrAgreement: ['', [Validators.maxLength(150)]],
    initialBalance: [{ value: 0, disabled: false }, [Validators.required]],
    initialBalanceDate: [{ value: '', disabled: false }, [Validators.required]],
    createdAt: [{ value: '', disabled: true }],
    updatedAt: [{ value: '', disabled: true }],
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) {
      this.toastr.error('ID da conta bancária não informado.');
      this.returnToSearch();
      return;
    }
    this.accountId = Number(idParam);
    this.loadBanksAndAccount();
    this.setupAccountTypeWatcher();
  }

  loadBanksAndAccount(): void {
    this.bankService.findAll({ status: 'ACTIVE' }, 0, 100).subscribe({
      next: (bankResponse) => {
        this.banks.set(bankResponse.content);
        this.loadAccountData();
      },
      error: () => {
        this.toastr.error('Erro ao carregar lista de bancos.');
        this.loadAccountData();
      },
    });
  }

  loadAccountData(): void {
    this.bankAccountService.findById(this.accountId).subscribe({
      next: (account: BankAccountResponse) => {
        const isCash = account.accountType === 'CASH_DESK';
        this.isCashDesk.set(isCash);

        this.bankAccountEditForm.patchValue({
          id: account.id,
          status: account.status,
          accountType: account.accountType,
          bankId: account.bankId || '',
          description: account.description,
          agencyNumber: account.agencyNumber || '',
          agencyDigit: account.agencyDigit || '',
          accountNumber: account.accountNumber || '',
          accountDigit: account.accountDigit || '',
          projectOrAgreement: account.projectOrAgreement || '',
          initialBalance: account.initialBalance,
          initialBalanceDate: account.initialBalanceDate,
          createdAt: this.formatDateTime(account.createdAt),
          updatedAt: this.formatDateTime(account.updatedAt),
        });

        this.applyCashDeskValidation(isCash);
        this.isLoading.set(false);
        this.formReady.set(true);
        this.cdr.detectChanges();
      },
      error: () => {
        this.toastr.error('Erro ao carregar dados da conta bancária.');
        this.returnToSearch();
      },
    });
  }

  setupAccountTypeWatcher(): void {
    this.bankAccountEditForm.get('accountType')?.valueChanges.subscribe((type: BankAccountType) => {
      const isCash = type === 'CASH_DESK';
      this.isCashDesk.set(isCash);
      this.applyCashDeskValidation(isCash);
    });
  }

  applyCashDeskValidation(isCash: boolean): void {
    const bankControl = this.bankAccountEditForm.get('bankId');
    const agencyNumControl = this.bankAccountEditForm.get('agencyNumber');
    const accountNumControl = this.bankAccountEditForm.get('accountNumber');
    const accountDigitControl = this.bankAccountEditForm.get('accountDigit');

    if (isCash) {
      bankControl?.clearValidators();
      bankControl?.setValue('');
      agencyNumControl?.clearValidators();
      agencyNumControl?.setValue('');
      accountNumControl?.clearValidators();
      accountNumControl?.setValue('');
      accountDigitControl?.clearValidators();
      accountDigitControl?.setValue('');
    } else {
      bankControl?.setValidators([Validators.required]);
      agencyNumControl?.setValidators([Validators.required, Validators.maxLength(5)]);
      accountNumControl?.setValidators([Validators.required, Validators.maxLength(12)]);
      accountDigitControl?.setValidators([Validators.required, Validators.maxLength(2)]);
    }

    bankControl?.updateValueAndValidity();
    agencyNumControl?.updateValueAndValidity();
    accountNumControl?.updateValueAndValidity();
    accountDigitControl?.updateValueAndValidity();
  }

  formatDateTime(dateStr: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleString('pt-BR');
  }

  updateBankAccount(): void {
    if (this.bankAccountEditForm.invalid) {
      this.bankAccountEditForm.markAllAsTouched();
      return;
    }

    const formValue = this.bankAccountEditForm.getRawValue();
    const isCash = formValue.accountType === 'CASH_DESK';

    const payload: BankAccountUpdateRequest = {
      accountType: formValue.accountType,
      bankId: isCash ? null : Number(formValue.bankId),
      description: formValue.description.trim(),
      agencyNumber: isCash ? null : (formValue.agencyNumber?.trim() || null),
      agencyDigit: isCash ? null : (formValue.agencyDigit?.trim() || null),
      accountNumber: isCash ? null : (formValue.accountNumber?.trim() || null),
      accountDigit: isCash ? null : (formValue.accountDigit?.trim() || null),
      projectOrAgreement: formValue.projectOrAgreement?.trim() || null,
      initialBalance: Number(formValue.initialBalance) || 0,
      initialBalanceDate: formValue.initialBalanceDate,
      status: formValue.status as EntityStatus,
      userIdEdit: 1,
    };

    this.bankAccountService.update(this.accountId, payload).subscribe({
      next: () => {
        this.toastr.success('Conta bancária atualizada com sucesso!');
        this.returnToSearch();
      },
      error: (err) => {
        const detail = err?.error?.detail || 'Erro ao atualizar conta bancária.';
        this.toastr.error(detail);
      },
    });
  }

  returnToSearch(): void {
    this.router.navigate(['/financeiro/cadastros/contas-bancarias']);
  }
}
