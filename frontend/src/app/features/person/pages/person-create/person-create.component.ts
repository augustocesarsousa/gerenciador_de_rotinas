import { Component, OnInit, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
  FormControl,
} from '@angular/forms';
import { Router } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatFormField, MatLabel, MatError, MatFormFieldModule } from '@angular/material/form-field';
import { MatOption, MatSelectModule } from '@angular/material/select';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { PersonService } from '../../services/person.service';
import { IPerson } from '../../interfaces/person.interface';
import { IPersonType } from '../../interfaces/person-type.interface';
import { IPersonStatus } from '../../interfaces/person-status.interface';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-person-create',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIcon,
    MatFormField,
    MatLabel,
    MatError,
    MatOption,
    MatSelectModule,
    MatButtonModule,
  ],
  templateUrl: './person-create.component.html',
  styleUrl: './person-create.component.css',
})
export class PersonCreateComponent implements OnInit {
  private _fb = inject(NonNullableFormBuilder);
  private _router = inject(Router);
  private _personService = inject(PersonService);
  private _toastr = inject(ToastrService);

  private readonly userIdEdit = 1;

  personTypes: IPersonType[] = [];
  personStatus: IPersonStatus[] = [];

  personForm = this._fb.group({
    name: ['', [Validators.required]],
    type: ['PHYSICAL', [Validators.required]],
    status: ['ACTIVE', [Validators.required]],
    cpf: ['', [Validators.required]],
    cnpj: [''],
    address: [''],
    number: [null as number | null, [Validators.min(0)]],
    neighborhood: [''],
    city: [''],
    state: [''],
    zipcode: ['', [Validators.pattern(/^(\d{5}-\d{3}|\d{8})?$/)]],
    phone: ['', [Validators.pattern(/^(\d{10,11})?$/)]],
    email: ['', [Validators.email]],
  });

  constructor() {}

  ngOnInit(): void {
    this.getPersonTypes();
    this.getPersonStatus();
    this.setupDynamicValidation();
  }

  getPersonTypes(): void {
    this._personService.getPersonTypes().subscribe({
      next: (response) => {
        this.personTypes = response;
      },
      error: () => {
        this._toastr.error('Ocorreu um erro ao carregar os tipos de pessoa');
      },
    });
  }

  getPersonStatus(): void {
    this._personService.getPersonStatus().subscribe({
      next: (response) => {
        this.personStatus = response;
      },
      error: () => {
        this._toastr.error('Ocorreu um erro ao carregar os status');
      },
    });
  }

  setupDynamicValidation(): void {
    this.personForm.get('type')?.valueChanges.subscribe((type) => {
      const cpfControl = this.personForm.get('cpf');
      const cnpjControl = this.personForm.get('cnpj');

      if (type === 'PHYSICAL') {
        cpfControl?.setValidators([Validators.required]);
        cnpjControl?.clearValidators();
        cnpjControl?.setValue('');
      } else {
        cnpjControl?.setValidators([Validators.required]);
        cpfControl?.clearValidators();
        cpfControl?.setValue('');
      }

      cpfControl?.updateValueAndValidity();
      cnpjControl?.updateValueAndValidity();
    });
  }

  createPerson(): void {
    if (this.personForm.valid) {
      const formValue = this.personForm.value;

      const newPerson: IPerson = {
        name: formValue.name!,
        type: formValue.type!,
        status: formValue.status!,
        cpf: formValue.type === 'PHYSICAL' ? formValue.cpf?.replace(/\D/g, '') : undefined,
        cnpj: formValue.type === 'LEGAL' ? formValue.cnpj?.replace(/\D/g, '') : undefined,
        address: formValue.address || undefined,
        number: formValue.number != null ? formValue.number : undefined,
        neighborhood: formValue.neighborhood || undefined,
        city: formValue.city || undefined,
        state: formValue.state || undefined,
        zipcode: formValue.zipcode || undefined,
        phone: formValue.phone || undefined,
        email: formValue.email || undefined,
        userIdEdit: this.userIdEdit,
      };

      this._personService.createPerson(newPerson).subscribe({
        next: (response) => {
          if (response.status === 201) {
            this._toastr.success('Pessoa cadastrada com sucesso!');
            this.returnToPersonSearch();
          }
        },
        error: (err) => {
          if (err.status === 500) {
            this._toastr.error('Ocorreu um erro interno. Contate o administrador.');
          } else if (err.status === 422 && err.error?.message) {
            this._toastr.error(err.error.message);
          } else if (err.status === 409 && err.error?.message) {
            this._toastr.error(err.error.message);
          } else {
            this._toastr.error('Erro de validação ao salvar dados.');
          }
        },
      });
    }
  }

  returnToPersonSearch(): void {
    this.personForm.reset();
    this._router.navigate(['/persons']);
  }
}
