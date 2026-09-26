import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { ToastrService } from 'ngx-toastr';
import { BankAccountService } from '../../services/bank-account.service';
import { BankAccountResponse } from '../../models/bank-account.model';
import { BankService } from '../../../bank/services/bank.service';
import { BankResponse } from '../../../bank/models/bank.model';

@Component({
  selector: 'app-bank-account-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
  ],
  templateUrl: './bank-account-list.component.html',
})
export class BankAccountListComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly bankAccountService = inject(BankAccountService);
  private readonly bankService = inject(BankService);
  private readonly toastr = inject(ToastrService);

  readonly bankAccounts = signal<BankAccountResponse[]>([]);
  readonly banks = signal<BankResponse[]>([]);
  readonly totalElements = signal<number>(0);
  readonly pageSize = signal<number>(10);
  readonly pageIndex = signal<number>(0);
  readonly isLoading = signal<boolean>(false);

  searchForm: FormGroup = this.fb.group({
    description: [''],
    bankId: [''],
    accountType: [''],
    status: [''],
  });

  readonly displayedColumns: string[] = [
    'description',
    'bank',
    'accountType',
    'agencyAccount',
    'initialBalance',
    'status',
    'editar',
  ];

  ngOnInit(): void {
    this.loadBanks();
    this.getAllBankAccounts();
  }

  loadBanks(): void {
    this.bankService.findAll({ status: 'ACTIVE' }, 0, 100).subscribe({
      next: (response) => {
        this.banks.set(response.content);
      },
      error: () => {
        this.toastr.error('Erro ao carregar lista de bancos para filtro.');
      },
    });
  }

  getAllBankAccounts(): void {
    this.isLoading.set(true);
    const formValue = this.searchForm.value;

    this.bankAccountService
      .findAll(
        {
          description: formValue.description?.trim() || undefined,
          bankId: formValue.bankId ? Number(formValue.bankId) : undefined,
          accountType: formValue.accountType || undefined,
          status: formValue.status || undefined,
        },
        this.pageIndex(),
        this.pageSize()
      )
      .subscribe({
        next: (response) => {
          this.bankAccounts.set(response.content);
          this.totalElements.set(response.totalElements);
          this.isLoading.set(false);
        },
        error: () => {
          this.toastr.error('Erro ao buscar contas bancárias.');
          this.isLoading.set(false);
        },
      });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.getAllBankAccounts();
  }

  formatAccountType(type: string): string {
    switch (type) {
      case 'CHECKING':
        return 'Corrente';
      case 'SAVINGS':
        return 'Poupança';
      case 'INVESTMENT':
        return 'Aplicação';
      case 'CASH_DESK':
        return 'Caixa Interno';
      default:
        return type;
    }
  }
}
