import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PersonCreateComponent } from './person-create.component';
import { PersonService } from '../../services/person.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpResponse } from '@angular/common/http';

describe('PersonCreateComponent', () => {
  let component: PersonCreateComponent;
  let fixture: ComponentFixture<PersonCreateComponent>;

  const mockTypes = [
    { index: 0, value: 'PHYSICAL', description: 'Pessoa Física' },
    { index: 1, value: 'LEGAL', description: 'Pessoa Jurídica' },
  ];

  const mockStatus = [
    { index: 0, value: 'ACTIVE', description: 'Ativo' },
    { index: 1, value: 'INACTIVE', description: 'Inativo' },
  ];

  const personServiceMock = {
    getPersonTypes: vi.fn(),
    getPersonStatus: vi.fn(),
    createPerson: vi.fn(),
  };

  const toastrMock = {
    success: vi.fn(),
    error: vi.fn(),
  };

  const routerMock = {
    navigate: vi.fn().mockReturnValue(Promise.resolve(true)),
  };

  beforeEach(async () => {
    personServiceMock.getPersonTypes.mockReturnValue(of(mockTypes));
    personServiceMock.getPersonStatus.mockReturnValue(of(mockStatus));
    personServiceMock.createPerson.mockClear();
    toastrMock.error.mockClear();
    toastrMock.success.mockClear();
    routerMock.navigate.mockClear();

    await TestBed.configureTestingModule({
      imports: [PersonCreateComponent, ReactiveFormsModule, BrowserAnimationsModule],
      providers: [
        { provide: PersonService, useValue: personServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: ToastrService, useValue: toastrMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PersonCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have invalid form on init because CPF is empty for PHYSICAL type', () => {
    expect(component.personForm.invalid).toBeTruthy();
  });

  it('should require CPF when type is PHYSICAL', () => {
    component.personForm.patchValue({
      name: 'Bruce Wayne',
      type: 'PHYSICAL',
      status: 'ACTIVE',
      cpf: '',
    });
    expect(component.personForm.invalid).toBeTruthy();
    expect(component.personForm.get('cpf')?.errors?.['required']).toBeTruthy();
  });

  it('should require CNPJ and clear CPF when type is changed to LEGAL', () => {
    component.personForm.patchValue({
      type: 'LEGAL',
    });
    fixture.detectChanges();

    expect(component.personForm.get('cnpj')?.errors?.['required']).toBeTruthy();
    expect(component.personForm.get('cpf')?.errors).toBeNull();
  });

  it('should call createPerson service when form is valid', () => {
    personServiceMock.createPerson.mockReturnValue(of(new HttpResponse({ status: 201 })));

    component.personForm.patchValue({
      name: 'Clark Kent',
      type: 'PHYSICAL',
      status: 'ACTIVE',
      cpf: '12345678901',
    });

    component.createPerson();

    expect(personServiceMock.createPerson).toHaveBeenCalled();
    expect(toastrMock.success).toHaveBeenCalledWith('Pessoa cadastrada com sucesso!');
    expect(routerMock.navigate).toHaveBeenCalledWith(['/persons']);
  });
});
