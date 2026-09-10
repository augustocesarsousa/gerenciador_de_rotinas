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
    'editar',
  ];

  searchForm: FormGroup = this.fb.group({
    code: [''],
    name: [''],
    shortName: [''],
    ispb: [''],
    status: [''],
  });

  ngOnInit(): void {
    this.getAllBanks();
  }

  getAllBanks(): void {
    this.isLoading.set(true);

    const filterVal = this.searchForm.value;
    const filter: BankQueryFilter = {
      code: filterVal.code?.trim() || undefined,
      name: filterVal.name?.trim() || undefined,
      shortName: filterVal.shortName?.trim() || undefined,
      ispb: filterVal.ispb?.trim() || undefined,
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

  onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.getAllBanks();
  }

  openCreateModal(): void {
    const dialogRef = this.dialog.open(BankFormModalComponent, {
      width: '540px',
      disableClose: true,
    });

    dialogRef.afterClosed().subscribe((saved: boolean) => {
      if (saved) {
        this.getAllBanks();
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
        this.getAllBanks();
      }
    });
  }
}
