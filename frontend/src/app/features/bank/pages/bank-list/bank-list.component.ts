import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { BankService } from '../../services/bank.service';
import { BankQueryFilter, BankResponse, EntityStatus } from '../../models/bank.model';
import { BankFormModalComponent } from '../../components/bank-form-modal/bank-form-modal.component';

@Component({
  selector: 'app-bank-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatDialogModule,
  ],
  templateUrl: './bank-list.component.html',
})
export class BankListComponent implements OnInit {
  private readonly bankService = inject(BankService);
  private readonly dialog = inject(MatDialog);
  private readonly toastr = inject(ToastrService);
  private readonly fb = inject(FormBuilder);

  readonly banks = signal<BankResponse[]>([]);
  readonly isLoading = signal<boolean>(false);
  readonly totalElements = signal<number>(0);
  readonly pageSize = signal<number>(10);
  readonly pageIndex = signal<number>(0);

  readonly displayedColumns: string[] = [
    'code',
    'shortName',
    'name',
    'ispb',
    'status',
    'actions',
  ];

  filterForm: FormGroup = this.fb.group({
    search: [''],
    status: [''],
  });

  ngOnInit(): void {
    this.loadBanks();
  }

  loadBanks(): void {
    this.isLoading.set(true);

    const filterVal = this.filterForm.value;
    const filter: BankQueryFilter = {
      search: filterVal.search?.trim() || undefined,
      status: (filterVal.status as EntityStatus) || undefined,
    };

    this.bankService
      .findAll(filter, this.pageIndex(), this.pageSize())
      .subscribe({
        next: (page) => {
          this.banks.set(page.content);
          this.totalElements.set(page.totalElements);
          this.isLoading.set(false);
        },
        error: (err) => {
          this.isLoading.set(false);
          const detail = err?.error?.detail || 'Erro ao carregar lista de instituições bancárias.';
          this.toastr.error(detail);
        },
      });
  }

  onFilter(): void {
    this.pageIndex.set(0);
    this.loadBanks();
  }

  resetFilter(): void {
    this.filterForm.reset({
      search: '',
      status: '',
    });
    this.pageIndex.set(0);
    this.loadBanks();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadBanks();
  }

  openCreateModal(): void {
    const dialogRef = this.dialog.open(BankFormModalComponent, {
      width: '540px',
      disableClose: true,
    });

    dialogRef.afterClosed().subscribe((saved: boolean) => {
      if (saved) {
        this.loadBanks();
      }
    });
  }

  openEditModal(bank: BankResponse): void {
    const dialogRef = this.dialog.open(BankFormModalComponent, {
      width: '540px',
      disableClose: true,
      data: { bank },
    });

    dialogRef.afterClosed().subscribe((saved: boolean) => {
      if (saved) {
        this.loadBanks();
      }
    });
  }

  toggleStatus(bank: BankResponse): void {
    const novoStatus = bank.status === 'ACTIVE' ? 'Inativo' : 'Ativo';
    this.bankService.toggleStatus(bank.id, 1).subscribe({
      next: () => {
        this.toastr.success(`Status da instituição alterado para ${novoStatus}!`);
        this.loadBanks();
      },
      error: (err) => {
        const detail = err?.error?.detail || 'Erro ao alternar status da instituição.';
        this.toastr.error(detail);
      },
    });
  }

  deleteBank(bank: BankResponse): void {
    const confirmed = window.confirm(
      `Deseja realmente excluir a instituição bancária "${bank.shortName}" (${bank.code})?`
    );

    if (!confirmed) {
      return;
    }

    this.bankService.delete(bank.id).subscribe({
      next: () => {
        this.toastr.success('Instituição bancária excluída com sucesso!');
        this.loadBanks();
      },
      error: (err) => {
        const detail =
          err?.error?.detail ||
          'Não é possível excluir esta instituição bancária pois existem registros vinculados.';
        this.toastr.error(detail);
      },
    });
  }
}
