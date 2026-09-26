import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { IPage } from '../../../shared/interfaces/page.interface';
import {
  BankAccountCreateRequest,
  BankAccountQueryFilter,
  BankAccountResponse,
  BankAccountUpdateRequest,
} from '../models/bank-account.model';

@Injectable({
  providedIn: 'root',
})
export class BankAccountService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.API_URL}/bank-accounts`;

  findAll(
    filter: BankAccountQueryFilter = {},
    page: number = 0,
    size: number = 10
  ): Observable<IPage<BankAccountResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (filter.description?.trim()) {
      params = params.set('description', filter.description.trim());
    }
    if (filter.bankId) {
      params = params.set('bankId', filter.bankId.toString());
    }
    if (filter.accountType) {
      params = params.set('accountType', filter.accountType);
    }
    if (filter.status) {
      params = params.set('status', filter.status);
    }
    if (filter.search?.trim()) {
      params = params.set('search', filter.search.trim());
    }

    return this.http.get<IPage<BankAccountResponse>>(this.baseUrl, { params });
  }

  findById(id: number): Observable<BankAccountResponse> {
    return this.http.get<BankAccountResponse>(`${this.baseUrl}/${id}`);
  }

  create(bankAccount: BankAccountCreateRequest): Observable<number> {
    return this.http.post<number>(this.baseUrl, bankAccount);
  }

  update(id: number, bankAccount: BankAccountUpdateRequest): Observable<number> {
    return this.http.put<number>(`${this.baseUrl}/${id}`, bankAccount);
  }

  toggleStatus(id: number, userIdEdit?: number): Observable<BankAccountResponse> {
    let params = new HttpParams();
    if (userIdEdit) {
      params = params.set('userIdEdit', userIdEdit.toString());
    }
    return this.http.patch<BankAccountResponse>(`${this.baseUrl}/${id}/status`, null, { params });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
