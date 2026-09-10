import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { IPage } from '../../../shared/interfaces/page.interface';
import {
  BankCreateRequest,
  BankQueryFilter,
  BankResponse,
  BankUpdateRequest,
} from '../models/bank.model';

@Injectable({
  providedIn: 'root',
})
export class BankService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.API_URL}/banks`;

  findAll(
    filter: BankQueryFilter = {},
    page: number = 0,
    size: number = 10
  ): Observable<IPage<BankResponse>> {
    let params = new HttpParams().set('page', page.toString()).set('size', size.toString());

    if (filter.search?.trim()) {
      params = params.set('search', filter.search.trim());
    }
    if (filter.code?.trim()) {
      params = params.set('code', filter.code.trim());
    }
    if (filter.name?.trim()) {
      params = params.set('name', filter.name.trim());
    }
    if (filter.shortName?.trim()) {
      params = params.set('shortName', filter.shortName.trim());
    }
    if (filter.ispb?.trim()) {
      params = params.set('ispb', filter.ispb.trim());
    }
    if (filter.status) {
      params = params.set('status', filter.status);
    }

    return this.http.get<IPage<BankResponse>>(this.baseUrl, { params });
  }

  findById(id: number): Observable<BankResponse> {
    return this.http.get<BankResponse>(`${this.baseUrl}/${id}`);
  }

  create(bank: BankCreateRequest): Observable<number> {
    return this.http.post<number>(this.baseUrl, bank);
  }

  update(id: number, bank: BankUpdateRequest): Observable<number> {
    return this.http.put<number>(`${this.baseUrl}/${id}`, bank);
  }

  toggleStatus(id: number, userIdEdit?: number): Observable<BankResponse> {
    let params = new HttpParams();
    if (userIdEdit) {
      params = params.set('userIdEdit', userIdEdit.toString());
    }
    return this.http.patch<BankResponse>(`${this.baseUrl}/${id}/status`, null, { params });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
