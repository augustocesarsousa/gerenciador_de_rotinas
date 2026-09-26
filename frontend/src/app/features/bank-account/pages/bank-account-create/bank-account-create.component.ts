import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ToastrService } from 'ngx-toastr';
import { BankAccountService } from '../../services/bank-account.service';
import { BankAccountCreateRequest, BankAccountType } from '../../models/bank-account.model';
import { BankService } from '../../../bank/services/bank.service';
import { BankResponse } from '../../../bank/models/bank.model';

@Component({
  selector: 'app-bank-account-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './bank-account-create.component.html',
})
export class BankAccountCreateComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly bankAccountService = inject(BankAccountService);
  private readonly bankService = inject(BankService);
  private readonly toastr = inject(ToastrService);

  readonly banks = signal<BankResponse[]>([]);
  readonly isCashDesk = signal<boolean>(false);

  bankAccountCreateForm: FormGroup = this.fb.group({
    accountType: ['CHECKING' as BankAccountType, [Validators.required]],
    bankId: ['', [Validators.required]],
    description: ['', [Validators.required, Validators.maxLength(100)]],
    agencyNumber: ['', [Validators.required, Validators.maxLength(5)]],
    agencyDigit: ['', [Validators.maxLength(2)]],
    accountNumber: ['', [Validators.required, Validators.maxLength(12)]],
    accountDigit: ['', [Validators.required, Validators.maxLength(2)]],
    projectOrAgreement: ['', [Validators.maxLength(150)]],
    initialBalance: [0.00, [Validators.required]],
    initialBalanceDate: [new Date().toISOString().substring(0, 10), [Validators.required]],
  });

  ngOnInit(): void {
    this.loadBanks();
    this.setupAccountTypeWatcher();
  }

  loadBanks(): void {
    this.bankService.findAll({ status: 'ACTIVE' }, 0, 100).subscribe({
      next: (response) => {
        this.banks.set(response.content);
      },
      error: () => {
        this.toastr.error('Erro ao carregar lista de bancos.');
      },
    });
  }

  setupAccountTypeWatcher(): void {
    this.bankAccountCreateForm.get('accountType')?.valueChanges.subscribe((type: BankAccountType) => {
      const isCash = type === 'CASH_DESK';
      this.isCashDesk.set(isCash);

      const bankControl = this.bankAccountCreateForm.get('bankId');
      const agencyNumControl = this.bankAccountCreateForm.get('agencyNumber');
      const accountNumControl = this.bankAccountCreateForm.get('accountNumber');
      const accountDigitControl = this.bankAccountCreateForm.get('accountDigit');

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
    });
  }

  createBankAccount(): void {
    if (this.bankAccountCreateForm.invalid) {
      this.bankAccountCreateForm.markAllAsTouched();
      return;
    }

    const formValue = this.bankAccountCreateForm.value;
    const isCash = formValue.accountType === 'CASH_DESK';

    const payload: BankAccountCreateRequest = {
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
      userIdEdit: 1,
    };

    this.bankAccountService.create(payload).subscribe({
      next: () => {
        this.toastr.success('Conta bancária cadastrada com sucesso!');
        this.returnToSearch();
      },
      error: (err) => {
        const detail = err?.error?.detail || 'Erro ao cadastrar conta bancária.';
        this.toastr.error(detail);
      },
    });
  }

  returnToSearch(): void {
    this.router.navigate(['/financeiro/cadastros/contas-bancarias']);
  }
}
