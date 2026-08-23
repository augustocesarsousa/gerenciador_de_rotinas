import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
  FormControl,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ActivatedRoute, Router } from '@angular/router';
import { PersonService } from '../../services/person.service';
import { ToastrService } from 'ngx-toastr';
import { IPerson } from '../../interfaces/person.interface';
import { IPersonType } from '../../interfaces/person-type.interface';
import { IPersonStatus } from '../../interfaces/person-status.interface';



@Component({
  selector: 'app-person-edit',
  templateUrl: './person-edit.component.html',
  styleUrl: './person-edit.component.css',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
  ],
})
export class PersonEditComponent implements OnInit {
  private _fb = inject(NonNullableFormBuilder);
  private _activatedRoute = inject(ActivatedRoute);
  private _router = inject(Router);
  private _personService = inject(PersonService);
  private _toastr = inject(ToastrService);
  private _changeDetectorRef = inject(ChangeDetectorRef);

  personForm!: FormGroup;
  private statusLoaded = false;
  private typesLoaded = false;
  private personLoaded = false;
  formReady = false;
  private personId = '';
  private readonly userIdEdit = 1;

  personEdit!: IPerson;
  personStatus: IPersonStatus[] = [];
  personTypes: IPersonType[] = [];

  ngOnInit(): void {
    this.personId = this._activatedRoute.snapshot.paramMap.get('id')!;
    if (this.personId) {
      this.getPersonById(this.personId);
      this.getPersonStatus();
      this.getPersonTypes();
    } else {
      this._toastr.error('Ocorreu um erro ao recuperar o ID da pessoa.');
      this.returnToPersonSearch();
    }
  }

  getPersonById(personId: string): void {
    this._personService.getPersonById(personId).subscribe({
      next: (response) => {
        this.personEdit = response;
        this.personLoaded = true;
        this.tryBuildForm();
      },
      error: (err) => {
        if (err.status === 404) {
          this._toastr.error(err.error?.message || 'Pessoa não encontrada.');
          this.returnToPersonSearch();
        } else {
          this._toastr.error('Erro ao recuperar dados da pessoa.');
          this.returnToPersonSearch();
        }
      },
    });
  }

  getPersonStatus(): void {
    this._personService.getPersonStatus().subscribe({
      next: (response) => {
        this.personStatus = response;
        this.statusLoaded = true;
        this.tryBuildForm();
      },
      error: () => {
        this._toastr.error('Ocorreu um erro ao carregar os status');
        this.returnToPersonSearch();
      },
    });
  }

  getPersonTypes(): void {
    this._personService.getPersonTypes().subscribe({
      next: (response) => {
        this.personTypes = response;
        this.typesLoaded = true;
        this.tryBuildForm();
      },
      error: () => {
        this._toastr.error('Ocorreu um erro ao carregar os tipos');
        this.returnToPersonSearch();
      },
    });
  }

  private buildForm(): void {
    const isPhysical = this.personEdit.type === 'PHYSICAL';

    this.personForm = this._fb.group({
      id: [{ value: this.personEdit.id!, disabled: true }],
      name: [this.personEdit.name, [Validators.required]],
      type: [this.personEdit.type, [Validators.required]],
      status: [this.personEdit.status!, [Validators.required]],
      cpf: [this.personEdit.cpf || '', isPhysical ? [Validators.required] : []],
      cnpj: [this.personEdit.cnpj || '', !isPhysical ? [Validators.required] : []],
      address: [this.personEdit.address || ''],
      number: [this.personEdit.number != null ? this.personEdit.number : null as number | null, [Validators.min(0)]],
      neighborhood: [this.personEdit.neighborhood || ''],
      city: [this.personEdit.city || ''],
      state: [this.personEdit.state || ''],
      zipcode: [this.personEdit.zipcode || '', [Validators.pattern(/^(\d{5}-\d{3}|\d{8})?$/)]],
      phone: [this.personEdit.phone || '', [Validators.pattern(/^(\d{10,11})?$/)]],
      email: [this.personEdit.email || '', [Validators.email]],
      createdAt: [{ value: this.formatDate(this.personEdit.createdAt!), disabled: true }],
      updatedAt: [{ value: this.formatDate(this.personEdit.updatedAt!), disabled: true }],
    });

    this.setupDynamicValidation();
  }

  private setupDynamicValidation(): void {
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

  private tryBuildForm(): void {
    if (this.personLoaded && this.statusLoaded && this.typesLoaded) {
      this.buildForm();
      this.formReady = true;
      this._changeDetectorRef.detectChanges();
    }
  }

  formatDate(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleString('pt-BR');
  }

  editPerson(): void {
    if (this.personForm.valid) {
      const formValue = this.personForm.getRawValue(); // pega valores inclusive desativados se necessário

      const updatedPerson: IPerson = {
        name: formValue.name,
        type: formValue.type,
        status: formValue.status,
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

      this._personService.editPerson(this.personId, updatedPerson).subscribe({
        next: (response) => {
          if (response.status === 200) {
            this._toastr.success('Pessoa editada com sucesso!');
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
            this._toastr.error('Erro de validação ao salvar alterações.');
          }
        },
      });
    }
  }

  returnToPersonSearch(): void {
    if (this.formReady) {
      this.personForm.reset();
      this._changeDetectorRef.detectChanges();
    }
    this._router.navigate(['/persons']);
  }
}
