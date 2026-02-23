import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCreateComponent } from './user-create.component';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { By } from '@angular/platform-browser';

describe('UserCreateComponent', () => {
  let component: UserCreateComponent;
  let fixture: ComponentFixture<UserCreateComponent>;

  const mockRoles = [
    { id: 1, authority: 'ROLE_ADMIN', description: 'Administrador' },
    { id: 2, authority: 'ROLE_FINANCIAL_MANAGER', description: 'Gerente Financeiro' },
    { id: 3, authority: 'ROLE_FINANCIAL_ASSISTANT', description: 'Assistente Financeiro' },
    { id: 4, authority: 'ROLE_ADMINISTRATIVE_MANAGER', description: 'Gerente Administrativo' },
    { id: 5, authority: 'ROLE_ADMINISTRATIVE_ASSISTANT', description: 'Assistente Administrativo' },
  ];

  const userServiceMock = {
    getUserRoles: vi.fn(),
    createUser: vi.fn(),
  };

  const toastrMock = {
    success: vi.fn(),
    error: vi.fn(),
  };

  const routerMock = {
    navigate: vi.fn().mockReturnValue(Promise.resolve(true)),
  };

  beforeEach(async () => {
    userServiceMock.getUserRoles.mockReturnValue(of(mockRoles));
    userServiceMock.createUser.mockClear();
    toastrMock.error.mockClear();
    toastrMock.success.mockClear();
    routerMock.navigate.mockClear();

    await TestBed.configureTestingModule({
      imports: [UserCreateComponent, ReactiveFormsModule],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: ToastrService, useValue: toastrMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UserCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  describe('Initialization', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should initialize form as invalid', () => {
      expect(component.userCreateForm.invalid).toBeTruthy();
    });

    it('should render button "Salvar"', () => {
      const compiled = fixture.nativeElement as HTMLElement;
      const saveBtn = compiled.querySelector('button[mat-flat-button].bg-green-600\\!');
      expect(saveBtn).toBeTruthy();
      expect(saveBtn?.textContent).toContain('Salvar');
    });

    it('should render button "Cancelar"', () => {
      const compiled = fixture.nativeElement as HTMLElement;
      const cancelBtn = compiled.querySelector('button[mat-flat-button].bg-red-600\\!');
      expect(cancelBtn).toBeTruthy();
      expect(cancelBtn?.textContent).toContain('Cancelar');
    });

    it('should call getUserRoles in ngOnInit', () => {
      const spyRoles = vi.spyOn(component, 'getUserRoles');
      component.ngOnInit();
      expect(spyRoles).toHaveBeenCalled();
    });

    it('should load userRoles from service', () => {
      userServiceMock.getUserRoles.mockReturnValue(of(mockRoles));
      component.getUserRoles();
      expect(component.userRoles).toEqual(mockRoles);
    });

    it('should handle error when loading userRoles from service', () => {
      userServiceMock.getUserRoles.mockReturnValue(throwError(() => ({ status: 500 })));
      component.getUserRoles();
      expect(toastrMock.error).toHaveBeenCalledWith('Ocorreu um erro ao carregar os perfis');
    });
  });

  describe('Form validations and UI', () => {
    it('should validate field "nome" is required', () => {
      const formControl = component.userCreateForm.get('name');

      formControl?.setValue('');
      expect(formControl?.valid).toBe(false);
      expect(formControl?.hasError('required')).toBe(true);
    });

    it('should validate field "login" is required', () => {
      const formControl = component.userCreateForm.get('login');

      formControl?.setValue('');
      expect(formControl?.valid).toBe(false);
      expect(formControl?.hasError('required')).toBe(true);
    });

    it('should validate field "password" is required', () => {
      const formControl = component.userCreateForm.get('password');

      formControl?.setValue('');
      expect(formControl?.valid).toBe(false);
      expect(formControl?.hasError('required')).toBe(true);
    });

    it('should validate field "email" is required', () => {
      const formControl = component.userCreateForm.get('email');

      formControl?.setValue('');
      expect(formControl?.valid).toBe(false);
      expect(formControl?.hasError('required')).toBe(true);
    });

    it('should validate field "email" is a valid email', () => {
      const formControl = component.userCreateForm.get('email');

      formControl?.setValue('invalid_email');
      expect(formControl?.valid).toBe(false);
      expect(formControl?.hasError('email')).toBe(true);
    });

    it('should validate select "roles" is required', () => {
      const formControl = component.userCreateForm.get('roles');

      formControl?.setValue([]);
      expect(formControl?.valid).toBe(false);
      expect(formControl?.hasError('required')).toBe(true);
    });

    it('should show message "Nome é obrigatório" when name form is touched', async () => {
      const formControl = component.userCreateForm.get('name');
      formControl?.setValue('');
      formControl?.markAsTouched();

      fixture.detectChanges();
      await fixture.whenStable();

      const errorElement = fixture.debugElement.query(By.css('mat-error'));
      expect(errorElement.nativeElement.textContent).toContain('Nome é obrigatório');
    });

    it('should show message "Login é obrigatório" when login form is touched', async () => {
      const formControl = component.userCreateForm.get('login');
      formControl?.setValue('');
      formControl?.markAsTouched();

      fixture.detectChanges();
      await fixture.whenStable();

      const errorElement = fixture.debugElement.query(By.css('mat-error'));
      expect(errorElement.nativeElement.textContent).toContain('Login é obrigatório');
    });

    it('should show message "Senha é obrigatória" when password form is touched', async () => {
      const formControl = component.userCreateForm.get('password');
      formControl?.setValue('');
      formControl?.markAsTouched();

      fixture.detectChanges();
      await fixture.whenStable();

      const errorElement = fixture.debugElement.query(By.css('mat-error'));
      expect(errorElement.nativeElement.textContent).toContain('Senha é obrigatória');
    });

    it('should show message "E-mail é obrigatório" when email form is touched', async () => {
      const formControl = component.userCreateForm.get('email');
      formControl?.setValue('');
      formControl?.markAsTouched();

      fixture.detectChanges();
      await fixture.whenStable();

      const errorElement = fixture.debugElement.query(By.css('mat-error'));
      expect(errorElement.nativeElement.textContent).toContain('E-mail é obrigatório');
    });

    it('should show message "Perfil é obrigatório" when profile form is touched', async () => {
      const formControl = component.userCreateForm.get('roles');
      formControl?.setValue([]);
      formControl?.markAsTouched();

      fixture.detectChanges();
      await fixture.whenStable();

      const errorElement = fixture.debugElement.query(By.css('mat-error'));
      expect(errorElement.nativeElement.textContent).toContain('Perfil é obrigatório');
    });

    it('should keep button "Salvar" disabled while form is invalid', () => {
      const saveBtn = fixture.debugElement.query(
        By.css('button[mat-flat-button].bg-green-600\\!'),
      ).nativeElement;
      expect(component.userCreateForm.invalid).toBeTruthy();
      expect(saveBtn.disabled).toBeTruthy();
    });

    it('should change button "Salvar" to enabled when form is valid', async () => {
      const saveBtn = fixture.debugElement.query(
        By.css('button[mat-flat-button].bg-green-600\\!'),
      ).nativeElement;
      component.userCreateForm.patchValue({
        name: 'Wally West',
        login: 'flash',
        password: '1234',
        email: 'wally.west@email.com',
        roles: [1, 2],
      });

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.userCreateForm.valid).toBeTruthy();
      expect(saveBtn.disabled).not.toBeTruthy();
    });
  });

  describe('Actions', () => {
    it('should call createUser from service when valid form', () => {
      userServiceMock.createUser.mockReturnValue(of({ status: 201 }));
      component.userCreateForm.patchValue({
        name: 'Wally West',
        login: 'flash',
        password: '1234',
        email: 'wally.west@email.com',
        roles: [1, 2],
      });

      expect(component.userCreateForm.valid).toBe(true);
      component.createUser();
      expect(userServiceMock.createUser).toHaveBeenCalled();
      expect(userServiceMock.createUser).toHaveBeenCalledWith(
        expect.objectContaining({
          name: 'Wally West',
          login: 'flash',
          password: '1234',
          email: 'wally.west@email.com',
          roles: [{ id: 1 }, { id: 2 }],
          userIdEdit: 1,
        }),
      );
    });

    it('should not call createUser when invalid form', () => {
      expect(component.userCreateForm.valid).toBe(false);
      component.createUser();
      expect(userServiceMock.createUser).not.toHaveBeenCalled();
    });

    it('should reset form and navigate to search page when cancel button is clicked', async () => {
      const resetSpy = vi.spyOn(component.userCreateForm, 'reset');

      component.userCreateForm.patchValue({
        name: 'Wally West',
        login: 'flash',
      });

      const cancelBtn = fixture.debugElement.query(By.css('button.bg-red-600\\!')).nativeElement;

      cancelBtn.click();
      fixture.detectChanges();
      await fixture.whenStable();

      expect(resetSpy).toHaveBeenCalled();
      expect(component.userCreateForm.get('name')?.value).toBeNull();
      expect(routerMock.navigate).toHaveBeenCalledWith(['/users']);
    });
  });
});
