import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ToastrService } from 'ngx-toastr';
import { BankService } from '../../services/bank.service';
import { BankCreateRequest, BankResponse, BankUpdateRequest, EntityStatus } from '../../models/bank.model';

export interface BankDialogData {
  bank?: BankResponse;
}

@Component({
  selector: 'app-bank-form-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatSlideToggleModule,
  ],
  templateUrl: './bank-form-modal.component.html',
})
export class BankFormModalComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly dialogRef = inject(MatDialogRef<BankFormModalComponent>);
  readonly data = inject<BankDialogData>(MAT_DIALOG_DATA, { optional: true });
  private readonly bankService = inject(BankService);
  private readonly toastr = inject(ToastrService);

  readonly isEditing = signal<boolean>(false);
  readonly isLoading = signal<boolean>(false);

  form: FormGroup = this.fb.group({
    code: ['', [Validators.required, Validators.pattern(/^\d{3}$/)]],
    ispb: ['', [Validators.pattern(/^(\d{8})?$/)]],
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
    shortName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(60)]],
    status: ['ACTIVE' as EntityStatus, [Validators.required]],
  });

  ngOnInit(): void {
    if (this.data?.bank) {
      this.isEditing.set(true);
      this.form.patchValue({
        code: this.data.bank.code,
        ispb: this.data.bank.ispb || '',
        name: this.data.bank.name,
        shortName: this.data.bank.shortName,
        status: this.data.bank.status,
      });
    }
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    const formVal = this.form.value;

    if (this.isEditing() && this.data?.bank) {
      const updatePayload: BankUpdateRequest = {
        code: formVal.code.trim(),
        ispb: formVal.ispb ? formVal.ispb.trim() : null,
        name: formVal.name.trim(),
        shortName: formVal.shortName.trim(),
        status: formVal.status,
        userIdEdit: 1,
      };

      this.bankService.update(this.data.bank.id, updatePayload).subscribe({
        next: () => {
          this.toastr.success('Instituição bancária atualizada com sucesso!');
          this.isLoading.set(false);
          this.dialogRef.close(true);
        },
        error: (err) => {
          this.isLoading.set(false);
          const detail = err?.error?.detail || 'Erro ao atualizar instituição bancária.';
          this.toastr.error(detail);
        },
      });
    } else {
      const createPayload: BankCreateRequest = {
        code: formVal.code.trim(),
        ispb: formVal.ispb ? formVal.ispb.trim() : null,
        name: formVal.name.trim(),
        shortName: formVal.shortName.trim(),
        status: formVal.status,
        userIdEdit: 1,
      };

      this.bankService.create(createPayload).subscribe({
        next: () => {
          this.toastr.success('Instituição bancária cadastrada com sucesso!');
          this.isLoading.set(false);
          this.dialogRef.close(true);
        },
        error: (err) => {
          this.isLoading.set(false);
          const detail = err?.error?.detail || 'Erro ao cadastrar instituição bancária.';
          this.toastr.error(detail);
        },
      });
    }
  }

  cancel(): void {
    this.dialogRef.close(false);
  }
}
