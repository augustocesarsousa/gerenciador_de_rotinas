import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PersonSearchComponent } from './person-search.component';
import { PersonService } from '../../services/person.service';
import { ToastrService } from 'ngx-toastr';
import { Router, provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('PersonSearchComponent', () => {
  let component: PersonSearchComponent;
  let fixture: ComponentFixture<PersonSearchComponent>;

  const mockTypes = [
    { index: 0, value: 'PHYSICAL', description: 'Pessoa Física' },
    { index: 1, value: 'LEGAL', description: 'Pessoa Jurídica' },
  ];

  const mockStatus = [
    { index: 0, value: 'ACTIVE', description: 'Ativo' },
    { index: 1, value: 'INACTIVE', description: 'Inativo' },
  ];

  const mockPersonsPage = {
    content: [
      {
        id: 1,
        name: 'Clark Kent',
        type: 'PHYSICAL',
        status: 'ACTIVE',
        cpf: '12345678901',
        email: 'clark@dailyplanet.com',
        userIdEdit: 1,
      },
    ],
    totalElements: 1,
    totalPages: 1,
    size: 10,
    number: 0,
  };

  const personServiceMock = {
    getPersonTypes: vi.fn(),
    getPersonStatus: vi.fn(),
    getAllPersons: vi.fn(),
  };

  const toastrMock = {
    success: vi.fn(),
    error: vi.fn(),
  };

  const routerMock = {
    navigate: vi.fn(),
  };

  beforeEach(async () => {
    personServiceMock.getPersonTypes.mockReturnValue(of(mockTypes));
    personServiceMock.getPersonStatus.mockReturnValue(of(mockStatus));
    personServiceMock.getAllPersons.mockReturnValue(of(mockPersonsPage));
    toastrMock.error.mockClear();
    toastrMock.success.mockClear();

    await TestBed.configureTestingModule({
      imports: [PersonSearchComponent, ReactiveFormsModule, BrowserAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: PersonService, useValue: personServiceMock },
        { provide: ToastrService, useValue: toastrMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PersonSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load status and types on init', () => {
    expect(component.personStatus).toEqual(mockStatus);
    expect(component.personTypes).toEqual(mockTypes);
  });

  it('should load persons on init', () => {
    expect(component.personsTable.data.length).toBe(1);
    expect(component.personsTable.data[0].name).toBe('Clark Kent');
  });

  it('should handle error when loading persons', () => {
    personServiceMock.getAllPersons.mockReturnValue(throwError(() => new Error('Error')));
    component.getAllPersons();
    expect(toastrMock.error).toHaveBeenCalledWith('Ocorreu um erro ao carregar as pessoas');
  });
});
