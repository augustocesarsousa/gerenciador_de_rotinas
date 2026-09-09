import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PersonEditComponent } from './person-edit.component';
import { PersonService } from '../../services/person.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpResponse } from '@angular/common/http';

describe('PersonEditComponent', () => {
  let component: PersonEditComponent;
  let fixture: ComponentFixture<PersonEditComponent>;

  const mockTypes = [
    { index: 0, value: 'PHYSICAL', description: 'Pessoa Física' },
    { index: 1, value: 'LEGAL', description: 'Pessoa Jurídica' },
  ];

  const mockStatus = [
    { index: 0, value: 'ACTIVE', description: 'Ativo' },
    { index: 1, value: 'INACTIVE', description: 'Inativo' },
  ];

  const mockPerson = {
    id: 1,
    name: 'Clark Kent',
    type: 'PHYSICAL',
    status: 'ACTIVE',
    cpf: '12345678901',
    cnpj: '',
    address: 'Metropolis',
    number: 100,
    neighborhood: 'Centro',
    city: 'Metropolis',
    state: 'KS',
    zipcode: '12345-678',
    phone: '11999998888',
    email: 'clark@dailyplanet.com',
    createdAt: '2026-01-01T00:00:00Z',
    updatedAt: '2026-01-01T00:00:00Z',
    userIdEdit: 1,
  };

  const personServiceMock = {
    getPersonTypes: vi.fn(),
    getPersonStatus: vi.fn(),
    getPersonById: vi.fn(),
    editPerson: vi.fn(),
  };

  const toastrMock = {
    success: vi.fn(),
    error: vi.fn(),
  };

  const routerMock = {
    navigate: vi.fn().mockReturnValue(Promise.resolve(true)),
  };

  const activatedRouteMock = {
    snapshot: {
      paramMap: {
        get: vi.fn().mockReturnValue('1'),
      },
    },
  };

  beforeEach(async () => {
    personServiceMock.getPersonTypes.mockReturnValue(of(mockTypes));
    personServiceMock.getPersonStatus.mockReturnValue(of(mockStatus));
    personServiceMock.getPersonById.mockReturnValue(of(mockPerson));
    personServiceMock.editPerson.mockClear();
    toastrMock.error.mockClear();
    toastrMock.success.mockClear();
    routerMock.navigate.mockClear();

    await TestBed.configureTestingModule({
      imports: [PersonEditComponent, ReactiveFormsModule, BrowserAnimationsModule],
      providers: [
        { provide: PersonService, useValue: personServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: Router, useValue: routerMock },
        { provide: ToastrService, useValue: toastrMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PersonEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and load person data', () => {
    expect(component).toBeTruthy();
    expect(component.formReady).toBeTruthy();
    expect(component.personForm.get('name')?.value).toBe('Clark Kent');
  });

  it('should call editPerson service when form is submitted', () => {
    personServiceMock.editPerson.mockReturnValue(of(new HttpResponse({ status: 200 })));

    component.personForm.patchValue({
      name: 'Clark Kent Updated',
    });

    component.editPerson();

    expect(personServiceMock.editPerson).toHaveBeenCalled();
    expect(toastrMock.success).toHaveBeenCalledWith('Pessoa editada com sucesso!');
    expect(routerMock.navigate).toHaveBeenCalledWith(['/persons']);
  });
});
