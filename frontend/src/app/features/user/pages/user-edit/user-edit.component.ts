import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { ActivatedRoute, Router } from '@angular/router';
import { IUserRole } from '../../interfaces/user-role.interface';
import { UserService } from '../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { IUserStatus } from '../../interfaces/user-status.interface';
import { IUser } from '../../interfaces/user.interface';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrl: './user-edit.component.css',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
  ],
})
export class UserEditComponent implements OnInit {
  userEditForm!: FormGroup;
  private statusLoaded = false;
  private rolesLoaded = false;
  private userLoaded = false;
  formReady = false;
  private userId = '';
  private readonly userIdEdit = 1;

  userEdit!: IUser;
  userStatus: IUserStatus[] = [];
  userRoles: IUserRole[] = [];

  constructor(
    private _fb: FormBuilder,
    private _activatedRoute: ActivatedRoute,
    private _router: Router,
    private _userService: UserService,
    private _toastr: ToastrService,
    private _changeDetectorRef: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.userId = this._activatedRoute.snapshot.paramMap.get('id')!;
    if (this.userId) {
      this.getUserById(this.userId);
      this.getUserStatus();
      this.getUserRoles();
    } else {
      this._toastr.error('Ocorreu um erro, contate o administrador do sistema');
      this.returnToUserSearch();
    }
  }

  getUserById(userId: string) {
    this._userService.getUserById(userId).subscribe({
      next: (response) => {
        this.userEdit = response;
        this.userLoaded = true;
        this.tryBuildForm();
      },
      error: (err) => {
        if (err.status === 404) {
          this._toastr.error(err.error.message);
          this.returnToUserSearch();
        } else {
          this._toastr.error('Ocorreu um erro, contate o administrador do sistema');
          this.returnToUserSearch();
        }
      },
    });
  }

  getUserStatus() {
    this._userService.getUserStatus().subscribe({
      next: (response) => {
        this.userStatus = response;
        this.statusLoaded = true;
        this.tryBuildForm();
      },
      error: () => {
        this._toastr.error('Ocorreu um erro ao carregar os status');
        this.returnToUserSearch();
      },
    });
  }

  getUserRoles() {
    this._userService.getUserRoles().subscribe({
      next: (response) => {
        this.userRoles = response;
        this.rolesLoaded = true;
        this.tryBuildForm();
      },
      error: () => {
        this._toastr.error('Ocorreu um erro ao carregar os perfis');
        this.returnToUserSearch();
      },
    });
  }

  private buildForm() {
    this.userEditForm = this._fb.group({
      id: [{ value: this.userEdit.id, disabled: true }, []],
      status: [this.userEdit.status, Validators.required],
      name: [this.userEdit.name, Validators.required],
      login: [this.userEdit.login, Validators.required],
      password: [''],
      email: [this.userEdit.email, [Validators.required, Validators.email]],
      roles: [this.userEdit.roles.map((r) => r.id), Validators.required],
      createdAt: [{ value: this.formatDate(this.userEdit.createdAt!), disabled: true }],
      updatedAt: [{ value: this.formatDate(this.userEdit.updatedAt!), disabled: true }],
    });
  }

  private tryBuildForm() {
    if (this.userLoaded && this.statusLoaded && this.rolesLoaded) {
      this.buildForm();
      this.formReady = true;
      this._changeDetectorRef.detectChanges();
    }
  }

  formatDate(dateString: Date): string {
    const date = new Date(dateString);
    return date.toLocaleString('pt-BR');
  }

  editUser() {
    const formValue = this.userEditForm.value;
    const userToEdit: IUser = {
      name: formValue.name,
      login: formValue.login,
      password: formValue.password,
      email: formValue.email,
      status: formValue.status,
      roles: formValue.roles.map((id: number) => ({ id })),
      userIdEdit: this.userIdEdit,
    };

    this._userService.editUser(this.userId, userToEdit).subscribe({
      next: (response) => {
        if (response.status === 200) {
          this._toastr.success('Usuário editado com sucesso!');
          this.returnToUserSearch();
        }
      },
      error: (err) => {
        if (err.status === 500) {
          this._toastr.error('Ocorreu um erro, contate o administrador do sistema');
          this.returnToUserSearch();
        } else if (err.status === 422 && err.error?.errors) {
          err.error.errors.forEach((validationError: any) => {
            this._toastr.error(validationError.message);
          });
        }
      },
    });
  }

  returnToUserSearch() {
    if (this.formReady) {
      this.userEditForm.reset();
      this._changeDetectorRef.detectChanges();
    }
    this._router.navigate(['/users']);
  }
}
