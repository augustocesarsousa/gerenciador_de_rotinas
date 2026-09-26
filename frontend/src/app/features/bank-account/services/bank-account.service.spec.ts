import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { BankAccountService } from './bank-account.service';
import { environment } from '../../../../environments/environment';
import {
  BankAccountCreateRequest,
  BankAccountQueryFilter,
  BankAccountResponse,
  BankAccountUpdateRequest,
} from '../models/bank-account.model';
import { IPage } from '../../../shared/interfaces/page.interface';

describe('BankAccountService', () => {
  let service: BankAccountService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.API_URL}/bank-accounts`;

  const mockBankAccountResponse: BankAccountResponse = {
    id: 1,
    bankId: 10,
    bankName: 'Banco do Brasil',
    bankCode: '001',
    accountType: 'CHECKING',
    accountTypeDescription: 'Corrente',
    description: 'Conta Operacional Principal',
    agencyNumber: '1234',
    agencyDigit: '5',
    accountNumber: '123456',
    accountDigit: '7',
    projectOrAgreement: 'Convênio 2026',
    initialBalance: 1500.5,
    initialBalanceDate: '2026-01-01',
    status: 'ACTIVE',
    createdAt: '2026-01-01T10:00:00',
    updatedAt: '2026-01-01T10:00:00',
    userIdEdit: 1,
  };

  const mockPageResponse: IPage<BankAccountResponse> = {
    content: [mockBankAccountResponse],
    totalElements: 1,
    totalPages: 1,
    number: 0,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        BankAccountService,
      ],
    });

    service = TestBed.inject(BankAccountService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('findAll', () => {
    it('should send GET request with default pagination params', () => {
      // given
      let result: IPage<BankAccountResponse> | undefined;

      // when
      service.findAll().subscribe((res) => (result = res));

      const req = httpMock.expectOne((r) => r.url === baseUrl && r.method === 'GET');
      expect(req.request.params.get('page')).toBe('0');
      expect(req.request.params.get('size')).toBe('10');
      req.flush(mockPageResponse);

      // then
      expect(result).toEqual(mockPageResponse);
    });

    it('should include all query filter parameters when provided', () => {
      // given
      const filter: BankAccountQueryFilter = {
        description: 'Principal',
        bankId: 10,
        accountType: 'CHECKING',
        status: 'ACTIVE',
        search: 'termo',
      };

      // when
      service.findAll(filter, 2, 25).subscribe();

      const req = httpMock.expectOne((r) => r.url === baseUrl && r.method === 'GET');

      // then
      expect(req.request.params.get('page')).toBe('2');
      expect(req.request.params.get('size')).toBe('25');
      expect(req.request.params.get('description')).toBe('Principal');
      expect(req.request.params.get('bankId')).toBe('10');
      expect(req.request.params.get('accountType')).toBe('CHECKING');
      expect(req.request.params.get('status')).toBe('ACTIVE');
      expect(req.request.params.get('search')).toBe('termo');
      req.flush(mockPageResponse);
    });
  });

  describe('findById', () => {
    it('should send GET request to specific account endpoint and return account data', () => {
      // given
      const accountId = 1;
      let result: BankAccountResponse | undefined;

      // when
      service.findById(accountId).subscribe((res) => (result = res));

      const req = httpMock.expectOne(`${baseUrl}/${accountId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockBankAccountResponse);

      // then
      expect(result).toEqual(mockBankAccountResponse);
    });

    it('should propagate 404 error when account does not exist', () => {
      // given
      const accountId = 999;
      let errorStatus: number | undefined;

      // when
      service.findById(accountId).subscribe({
        next: () => {
          throw new Error('Deveria ter falhado com 404');
        },
        error: (err) => (errorStatus = err.status),
      });

      const req = httpMock.expectOne(`${baseUrl}/${accountId}`);
      req.flush({ detail: 'Conta não encontrada' }, { status: 404, statusText: 'Not Found' });

      // then
      expect(errorStatus).toBe(404);
    });
  });

  describe('create', () => {
    it('should send POST request with payload and return created account id', () => {
      // given
      const payload: BankAccountCreateRequest = {
        accountType: 'CHECKING',
        bankId: 10,
        description: 'Nova Conta',
        agencyNumber: '1234',
        agencyDigit: '1',
        accountNumber: '998877',
        accountDigit: '2',
        initialBalance: 100,
        initialBalanceDate: '2026-01-01',
        userIdEdit: 1,
      };

      let createdId: number | undefined;

      // when
      service.create(payload).subscribe((id) => (createdId = id));

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(payload);
      req.flush(15);

      // then
      expect(createdId).toBe(15);
    });

    it('should propagate 422 error when business validation fails', () => {
      // given
      const payload: BankAccountCreateRequest = {
        accountType: 'CHECKING',
        description: 'Inválida',
        initialBalance: 0,
        initialBalanceDate: '2026-01-01',
        userIdEdit: 1,
      };

      let errorResponse: any;

      // when
      service.create(payload).subscribe({
        next: () => {
          throw new Error('Deveria ter falhado com 422');
        },
        error: (err) => (errorResponse = err),
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush({ detail: 'Banco é obrigatório para conta corrente' }, { status: 422, statusText: 'Unprocessable Entity' });

      // then
      expect(errorResponse.status).toBe(422);
      expect(errorResponse.error.detail).toBe('Banco é obrigatório para conta corrente');
    });
  });

  describe('update', () => {
    it('should send PUT request to account endpoint with update payload', () => {
      // given
      const accountId = 1;
      const updatePayload: BankAccountUpdateRequest = {
        accountType: 'CHECKING',
        bankId: 10,
        description: 'Conta Alterada',
        agencyNumber: '1234',
        accountNumber: '123456',
        accountDigit: '7',
        initialBalance: 200,
        initialBalanceDate: '2026-01-01',
        status: 'ACTIVE',
        userIdEdit: 1,
      };

      let updatedId: number | undefined;

      // when
      service.update(accountId, updatePayload).subscribe((id) => (updatedId = id));

      const req = httpMock.expectOne(`${baseUrl}/${accountId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updatePayload);
      req.flush(1);

      // then
      expect(updatedId).toBe(1);
    });
  });

  describe('toggleStatus', () => {
    it('should send PATCH request to status endpoint with userIdEdit query param', () => {
      // given
      const accountId = 1;
      const userIdEdit = 42;
      let response: BankAccountResponse | undefined;

      // when
      service.toggleStatus(accountId, userIdEdit).subscribe((res) => (response = res));

      const req = httpMock.expectOne((r) => r.url === `${baseUrl}/${accountId}/status` && r.method === 'PATCH');
      expect(req.request.params.get('userIdEdit')).toBe('42');
      req.flush({ ...mockBankAccountResponse, status: 'INACTIVE' });

      // then
      expect(response?.status).toBe('INACTIVE');
    });
  });

  describe('delete', () => {
    it('should send DELETE request to specific account endpoint', () => {
      // given
      const accountId = 1;
      let completed = false;

      // when
      service.delete(accountId).subscribe(() => (completed = true));

      const req = httpMock.expectOne(`${baseUrl}/${accountId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);

      // then
      expect(completed).toBe(true);
    });
  });
});
