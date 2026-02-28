import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserEditComponent } from './user-edit.component';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { By } from '@angular/platform-browser';

describe('UserEditComponent', () => {
  let component: UserEditComponent;
  let fixture: ComponentFixture<UserEditComponent>;

  const mockUser = {
    id: 1,
    name: 'Barry Allen',
    login: 'flash',
    email: 'barry.allen@email.com',
    status: 'ACTIVE',
    roles: [{ id: 1, authority: 'ROLE_ADMIN', description: 'Administrador' }],
    createdAt: new Date('2024-01-01T10:00:00'),
    updatedAt: new Date('2024-01-02T15:00:00'),
  };

  const mockStatus = [
    { index: 1, value: 'ACTIVE', description: 'Ativo' },
    { index: 2, value: 'INACTIVE', description: 'Inativo' },
  ];

  const mockRoles = [
    { id: 1, authority: 'ROLE_ADMIN', description: 'Administrador' },
    { id: 2, authority: 'ROLE_FINANCIAL_MANAGER', description: 'Gerente Financeiro' },
    { id: 3, authority: 'ROLE_FINANCIAL_ASSISTANT', description: 'Assistente Financeiro' },
    { id: 4, authority: 'ROLE_ADMINISTRATIVE_MANAGER', description: 'Gerente Administrativo' },
    { id: 5, authority: 'ROLE_ADMINISTRATIVE_ASSISTANT', description: 'Assistente Administrativo' },
  ];

  const userServiceMock = {
    getUserById: vi.fn(),
    getUserStatus: vi.fn(),
    getUserRoles: vi.fn(),
    editUser: vi.fn(),
  };

  const toastrMock = { success: vi.fn(), error: vi.fn() };
  const routerMock = { navigate: vi.fn() };
  const activatedRouteMock = {
    snapshot: { paramMap: { get: vi.fn() } },
  };

  beforeEach(async () => {
    vi.clearAllMocks();
    userServiceMock.getUserById.mockReturnValue(of(mockUser));
    userServiceMock.getUserStatus.mockReturnValue(of(mockStatus));
    userServiceMock.getUserRoles.mockReturnValue(of(mockRoles));
    userServiceMock.editUser.mockReturnValue(of({ status: 200 }));
    activatedRouteMock.snapshot.paramMap.get.mockReturnValue('1');

    await TestBed.configureTestingModule({
      imports: [UserEditComponent, ReactiveFormsModule],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: ToastrService, useValue: toastrMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UserEditComponent);
    component = fixture.componentInstance;
  });

  describe('Initialization', () => {
    it('should create the component', () => {
      fixture.detectChanges();
      expect(component).toBeTruthy();
    });

    it('should build the form only after all data is loaded', () => {
      fixture.detectChanges();

      expect(component.formReady).toBe(true);
      expect(component.userEditForm).toBeDefined();
      expect(component.userEditForm.get('name')?.value).toBe(mockUser.name);
      expect(component.userEditForm.get('id')?.disabled).toBe(true);
    });

    it('should handle 404 error when user is not found', () => {
      userServiceMock.getUserById.mockReturnValue(
        throwError(() => ({
          status: 404,
          error: { message: 'Usuário não encontrado' },
        })),
      );

      fixture.detectChanges();

      expect(toastrMock.error).toHaveBeenCalledWith('Usuário não encontrado');
      expect(routerMock.navigate).toHaveBeenCalledWith(['/users']);
    });

    it('should show error and redirect if ID is missing in route', () => {
      activatedRouteMock.snapshot.paramMap.get.mockReturnValue(null);

      fixture.detectChanges();

      expect(toastrMock.error).toHaveBeenCalledWith(
        'Ocorreu um erro, contate o administrador do sistema',
      );
      expect(routerMock.navigate).toHaveBeenCalledWith(['/users']);
    });
  });

  describe('Actions', () => {
    it('should call editUser with correct payload and mapped roles', () => {
      fixture.detectChanges();

      expect(component.formReady).toBe(true);
      expect(component.userEditForm).toBeDefined();

      component.userEditForm.get('name')?.setValue('Wally West');
      component.editUser();

      expect(userServiceMock.editUser).toHaveBeenCalledWith('1', {
        name: 'Wally West',
        login: 'flash',
        email: 'barry.allen@email.com',
        status: 'ACTIVE',
        password: '',
        roles: [{ id: 1 }],
        userIdEdit: 1,
      });
      expect(toastrMock.success).toHaveBeenCalledWith('Usuário editado com sucesso!');
      expect(routerMock.navigate).toHaveBeenCalledWith(['/users']);
    });

    it('should handle 404 error when user is not found', () => {
      activatedRouteMock.snapshot.paramMap.get.mockReturnValue('100');
      userServiceMock.getUserById.mockReturnValue(
        throwError(() => ({
          status: 404,
          error: { message: 'Usuário não encontrado' },
        })),
      );

      fixture.detectChanges();

      expect(toastrMock.error).toHaveBeenCalledWith('Usuário não encontrado');
      expect(routerMock.navigate).toHaveBeenCalledWith(['/users']);
    });

    it('should show error and redirect when ID is missing in route', () => {
      activatedRouteMock.snapshot.paramMap.get.mockReturnValue(null);

      fixture.detectChanges();

      expect(toastrMock.error).toHaveBeenCalledWith(
        'Ocorreu um erro, contate o administrador do sistema',
      );
      expect(routerMock.navigate).toHaveBeenCalledWith(['/users']);
    });

    it('should reset form and navigate on cancel', () => {
      fixture.detectChanges();
      const resetSpy = vi.spyOn(component.userEditForm, 'reset');

      component.returnToUserSearch();

      expect(resetSpy).toHaveBeenCalled();
      expect(routerMock.navigate).toHaveBeenCalledWith(['/users']);
    });
  });

  describe('UI Rendering', () => {
    it('should not show form if formReady is false', () => {
      activatedRouteMock.snapshot.paramMap.get.mockReturnValue(null);
      fixture.detectChanges();

      const form = fixture.debugElement.query(By.css('form'));

      expect(component.formReady).toBe(false);
      expect(form).toBeNull();
    });

    it('should render correct number of radio buttons for status', () => {
      fixture.detectChanges();
      const radios = fixture.debugElement.queryAll(By.css('mat-radio-button'));
      expect(radios.length).toBe(mockStatus.length);
    });
  });
});
