import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { IRole } from '../../interfaces/role.interface';
import { IUser } from '../../interfaces/user.interface';
import { MatIcon } from '@angular/material/icon';
import { MatFormField, MatLabel, MatError, MatFormFieldModule } from '@angular/material/form-field';
import { MatOption, MatSelectModule } from '@angular/material/select';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatAnchor } from '@angular/material/button';
import { UserService } from '../../services/user.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-create',
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
    MatAnchor,
    MatSnackBarModule,
  ],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.css',
})
export class UserCreateComponent implements OnInit {
  userCreateForm!: FormGroup;
  private readonly userIdEdit = 1;

  roles: IRole[] = [];

  constructor(
    private _fb: FormBuilder,
    private _router: Router,
    private _userService: UserService,
    private _snackBar: MatSnackBar,
    private _toastr: ToastrService,
  ) {
    this.userCreateForm = this._fb.group({
      name: ['', Validators.required],
      login: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      roles: [[], Validators.required],
    });
  }

  ngOnInit(): void {
    this.getUserRoles();
  }

  getUserRoles() {
    this._userService.getUserRoles().subscribe({
      next: (response) => {
        this.roles = response;
      },
      error: (err) => {
        this._toastr.error('Ocorreu um erro ao carregar os perfis');
      },
    });
  }

  createUser() {
    if (this.userCreateForm.valid) {
      const formValue = this.userCreateForm.value;
      const newUser: IUser = {
        name: formValue.name,
        login: formValue.login,
        password: formValue.password,
        email: formValue.email,
        roles: formValue.roles.map((id: number) => ({ id })),
        userIdEdit: this.userIdEdit,
      };

      this._userService.createUser(newUser).subscribe({
        next: (response) => {
          if (response.status === 201) {
            this._toastr.success('Usuário criado com sucesso!');
            this.returnToUserSearch();
          }
        },
        error: (err) => {
          if (err.status === 500) {
            this._snackBar.open('Ocorreu um erro, contate o administrador do sistema', 'Fechar', {
              duration: 4000,
              panelClass: ['bg-red-600!', 'text-white!'],
            });
          } else if (err.status === 422 && err.error?.errors) {
            err.error.errors.forEach((validationError: any) => {
              this._toastr.error(validationError.message);
            });
          }
        },
      });
    }
  }

  returnToUserSearch() {
    this.userCreateForm.reset();
    this._router.navigate(['/users']);
  }
}
