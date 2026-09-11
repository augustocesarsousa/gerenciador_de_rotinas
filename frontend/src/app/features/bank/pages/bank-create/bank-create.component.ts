import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BankService } from '../../services/bank.service';
import { BankCreateRequest } from '../../models/bank.model';

@Component({
  selector: 'app-bank-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './bank-create.component.html',
})
export class BankCreateComponent {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly bankService = inject(BankService);
  private readonly toastr = inject(ToastrService);

  bankCreateForm: FormGroup = this.fb.group({
    code: ['', [Validators.required, Validators.pattern(/^\d{3}$/)]],
    ispb: ['', [Validators.pattern(/^(\d{8})?$/)]],
    shortName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(60)]],
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
  });

  createBank(): void {
    if (this.bankCreateForm.invalid) {
      this.bankCreateForm.markAllAsTouched();
      return;
    }

    const formValue = this.bankCreateForm.value;
    const payload: BankCreateRequest = {
      code: formValue.code.trim(),
      ispb: formValue.ispb?.trim() || null,
      shortName: formValue.shortName.trim(),
      name: formValue.name.trim(),
      status: 'ACTIVE',
      userIdEdit: 1,
    };

    this.bankService.create(payload).subscribe({
      next: () => {
        this.toastr.success('Instituição bancária cadastrada com sucesso!');
        this.returnToBankSearch();
      },
      error: (err) => {
        const detail = err?.error?.detail || 'Erro ao cadastrar instituição bancária.';
        this.toastr.error(detail);
      },
    });
  }

  returnToBankSearch(): void {
    this.router.navigate(['/financeiro/cadastros/bancos']);
  }
}
