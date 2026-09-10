import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BankService } from '../../services/bank.service';
import { BankResponse, BankUpdateRequest, EntityStatus } from '../../models/bank.model';

@Component({
  selector: 'app-bank-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './bank-edit.component.html',
})
export class BankEditComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly bankService = inject(BankService);
  private readonly toastr = inject(ToastrService);

  bankEditForm!: FormGroup;
  bankEdit!: BankResponse;
  formReady = false;
  bankId!: number;

  ngOnInit(): void {
    const idParam = this.activatedRoute.snapshot.paramMap.get('id');
    if (idParam) {
      this.bankId = Number(idParam);
      this.getBankById(this.bankId);
    } else {
      this.toastr.error('Identificador de instituição bancária não fornecido.');
      this.returnToBankSearch();
    }
  }

  getBankById(id: number): void {
    this.bankService.findById(id).subscribe({
      next: (response) => {
        this.bankEdit = response;
        this.buildForm();
        this.formReady = true;
      },
      error: (err) => {
        const detail = err?.error?.detail || 'Instituição bancária não encontrada.';
        this.toastr.error(detail);
        this.returnToBankSearch();
      },
    });
  }

  private buildForm(): void {
    this.bankEditForm = this.fb.group({
      id: [{ value: this.bankEdit.id, disabled: true }],
      status: [this.bankEdit.status, [Validators.required]],
      code: [this.bankEdit.code, [Validators.required, Validators.pattern(/^\d{3}$/)]],
      ispb: [this.bankEdit.ispb || '', [Validators.pattern(/^(\d{8})?$/)]],
      shortName: [this.bankEdit.shortName, [Validators.required, Validators.minLength(2), Validators.maxLength(60)]],
      name: [this.bankEdit.name, [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
      createdAt: [{ value: this.formatDate(this.bankEdit.createdAt), disabled: true }],
      updatedAt: [{ value: this.formatDate(this.bankEdit.updatedAt), disabled: true }],
    });
  }

  formatDate(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleString('pt-BR');
  }

  editBank(): void {
    if (this.bankEditForm.invalid) {
      this.bankEditForm.markAllAsTouched();
      return;
    }

    const formValue = this.bankEditForm.value;
    const payload: BankUpdateRequest = {
      code: formValue.code.trim(),
      ispb: formValue.ispb?.trim() || null,
      shortName: formValue.shortName.trim(),
      name: formValue.name.trim(),
      status: formValue.status as EntityStatus,
      userIdEdit: 1,
    };

    this.bankService.update(this.bankId, payload).subscribe({
      next: () => {
        this.toastr.success('Instituição bancária editada com sucesso!');
        this.returnToBankSearch();
      },
      error: (err) => {
        const detail = err?.error?.detail || 'Erro ao atualizar instituição bancária.';
        this.toastr.error(detail);
      },
    });
  }

  returnToBankSearch(): void {
    this.router.navigate(['/financeiro/cadastros/bancos']);
  }
}
