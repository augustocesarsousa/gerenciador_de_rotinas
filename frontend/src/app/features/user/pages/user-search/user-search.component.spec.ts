import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSearchComponent } from './user-search.component';
import { UserService } from '../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { By } from '@angular/platform-browser';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { IUser } from '../../interfaces/user.interface';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

describe('UserSearchComponent', () => {
  let component: UserSearchComponent;
  let fixture: ComponentFixture<UserSearchComponent>;
  let userService: UserService;
  let toastrService: ToastrService;

  beforeEach(async () => {
    const userServiceMock = {
      getUserStatus: vi.fn().mockReturnValue(of([])),
      getAllUsers: vi.fn().mockReturnValue(of({ content: [], totalElements: 0 })),
    };
    const toastrMock = { error: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [UserSearchComponent, MatTableModule, MatPaginatorModule, MatSortModule],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: ToastrService, useValue: toastrMock },
        provideRouter([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UserSearchComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    toastrService = TestBed.inject(ToastrService);
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getUserStatus in ngOnInit', () => {
    const spyStatus = vi.spyOn(component, 'getUserStatus');
    component.ngOnInit();
    expect(spyStatus).toHaveBeenCalled();
  });

  it('should call getAllUsers in ngOnInit', () => {
    const spyStatus = vi.spyOn(component, 'getAllUsers');
    component.ngOnInit();
    expect(spyStatus).toHaveBeenCalled();
  });

  it('should load userStatus from service', () => {
    const mockStatus = [
      { index: 0, value: 'ACTIVE', description: 'Ativo' },
      { index: 1, value: 'INACTIVE', description: 'Inativo' },
    ];
    (userService.getUserStatus as any).mockReturnValue(of(mockStatus));
    component.getUserStatus();
    expect(component.userStatus).toEqual(mockStatus);
  });

  it('should handle error when loading userStatus from service', () => {
    (userService.getUserStatus as any).mockReturnValue(throwError(() => ({ status: 500 })));
    component.getUserStatus();
    expect(toastrService.error).toHaveBeenCalledWith('Ocorreu um erro ao carregar os status');
  });

  it('should load users from service', () => {
    const mockResponse = {
      content: [
        { id: 1, name: 'Bruce Wayne', login: 'batman', email: 'bruce@wayne.com', status: 'ACTIVE' },
      ],
      totalElements: 1,
    };
    (userService.getAllUsers as any).mockReturnValue(of(mockResponse));
    component.getAllUsers();
    expect(component.usersTable.data).toEqual(mockResponse.content);
    expect(component.totalElements).toBe(1);
  });

  it('should handle error when loading users from service', () => {
    (userService.getAllUsers as any).mockReturnValue(throwError(() => ({ status: 500 })));
    component.getAllUsers();
    expect(toastrService.error).toHaveBeenCalledWith('Ocorreu um erro ao carregar os usuários');
  });

  it('should update pagination when call getAllUsers', () => {
    const spy = vi.spyOn(component, 'getAllUsers');
    const event = { pageIndex: 2, pageSize: 20 } as PageEvent;
    component.onPageChange(event);
    expect(component.pageIndex).toBe(2);
    expect(component.pageSize).toBe(20);
    expect(spy).toHaveBeenCalled();
  });

  it('should render page title', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Consulta de usuários');
  });

  it('should render form fields', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.querySelector('input[formControlName="id"]')).toBeTruthy();
    expect(compiled.querySelector('input[formControlName="name"]')).toBeTruthy();
    expect(compiled.querySelector('input[formControlName="login"]')).toBeTruthy();
    expect(compiled.querySelector('input[formControlName="email"]')).toBeTruthy();
    expect(compiled.querySelector('mat-select[formControlName="status"]')).toBeTruthy();
  });

  it('should render button "Pesquisar"', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    const searchBtn = compiled.querySelector('button[type="submit"]');

    expect(searchBtn?.textContent).toContain('Pesquisar');
  });

  it('should render button "Cadastrar"', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    const buttons = Array.from(compiled.querySelectorAll('button'));
    const createBtn = buttons.find((btn) => btn.textContent?.includes('Cadastrar'));

    expect(createBtn).toBeTruthy();
    expect(createBtn?.textContent).toContain('Cadastrar');
  });

  it('should render table with columns', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;

    const headers = Array.from(compiled.querySelectorAll('th')).map((th) => th.textContent?.trim());
    expect(headers).toContain('Id');
    expect(headers).toContain('Nome');
    expect(headers).toContain('Login');
    expect(headers).toContain('E-mail');
    expect(headers).toContain('Status');
  });

  it('should render table with data', async () => {
    component.displayedColumns = ['id', 'name', 'login', 'email', 'status', 'editar'];
    const mockData: IUser[] = [
      { id: 1, name: 'Bruce Wayne', login: 'batman', status: 'ACTIVE' } as IUser,
      { id: 2, name: 'Clark Kent', login: 'superman', status: 'ACTIVE' } as IUser,
    ];
    component.usersTable = new MatTableDataSource(mockData);

    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const rows = fixture.debugElement.queryAll(By.css('.mat-mdc-table tbody tr'));

    expect(rows.length).toBe(2);
    expect(rows[0].nativeElement.textContent).toContain('Bruce Wayne');
    expect(rows[1].nativeElement.textContent).toContain('Clark Kent');
  });

  it('should apply red background class when user status is INACTIVE', async () => {
    component.displayedColumns = ['id', 'name', 'login', 'email', 'status', 'editar'];
    const mockData: IUser[] = [
      { id: 1, name: 'Bruce Wayne', login: 'batman', status: 'ACTIVE' } as IUser,
      { id: 2, name: 'Clark Kent', login: 'superman', status: 'INACTIVE' } as IUser,
    ];

    component.usersTable = new MatTableDataSource(mockData);

    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const rows = fixture.debugElement.queryAll(By.css('.mat-mdc-table tbody tr'));

    expect(rows[0].nativeElement.classList.contains('bg-red-100!')).toBe(false);

    const inactiveRow = rows[1].nativeElement;
    expect(inactiveRow.classList.contains('bg-red-100!')).toBe(true);
    expect(inactiveRow.classList.contains('text-red-600!')).toBe(true);
  });

  it('should render the status column using the UserStatusPipe', async () => {
    component.displayedColumns = ['id', 'name', 'login', 'email', 'status', 'editar'];
    const mockData: IUser[] = [
      { id: 1, name: 'Bruce Wayne', login: 'batman', status: 'ACTIVE' } as IUser,
    ];

    component.usersTable = new MatTableDataSource(mockData);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const rows = fixture.debugElement.queryAll(By.css('.mat-mdc-table tbody tr'));

    expect(rows[0].nativeElement.textContent).toContain('Ativo');
  });

  it('should render paginator', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    const paginator = compiled.querySelector('mat-paginator');
    expect(paginator).toBeTruthy();
  });

  it('should have correct paginator initial values', () => {
    component.totalElements = 100;
    component.pageSize = 20;
    component.pageIndex = 0;

    fixture.detectChanges();

    const paginator = fixture.debugElement.query(By.css('mat-paginator')).componentInstance;
    expect(paginator.length).toBe(100);
    expect(paginator.pageSize).toBe(20);
    expect(paginator.pageIndex).toBe(0);
  });

  it('should call onPageChange when paginator emits event', () => {
    fixture.detectChanges();
    const paginator = fixture.debugElement.query(By.css('mat-paginator')).componentInstance;

    const event = { pageIndex: 1, pageSize: 20, length: 100 };
    const spy = vi.spyOn(component, 'onPageChange');

    paginator.page.emit(event);
    fixture.detectChanges();

    expect(spy).toHaveBeenCalledWith(event);
  });

  it('should reload users when page changes', () => {
    const spy = vi.spyOn(component, 'getAllUsers');
    fixture.detectChanges();

    const paginator = fixture.debugElement.query(By.css('mat-paginator')).componentInstance;
    paginator.page.emit({ pageIndex: 1, pageSize: 20, length: 100 });
    fixture.detectChanges();

    expect(spy).toHaveBeenCalled();
  });
});
