import { Injectable, inject } from '@angular/core';
import { ApiService } from '../../../code/services/api.service';
import { PersonFilter } from '../interfaces/person-filter.interface';
import { Observable } from 'rxjs';
import { IPage } from '../../../shared/interfaces/page.interface';
import { IPerson } from '../interfaces/person.interface';
import { HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { IPersonType } from '../interfaces/person-type.interface';
import { IPersonStatus } from '../interfaces/person-status.interface';

@Injectable({
  providedIn: 'root',
})
export class PersonService {
  private _apiService = inject(ApiService);

  getPersonById(personId: string): Observable<IPerson> {
    return this._apiService.get<IPerson>('persons/' + personId);
  }

  getAllPersons(filters: PersonFilter, page: number = 0, size: number = 10): Observable<IPage<IPerson>> {
    let params = new HttpParams().set('page', page).set('size', size);

    Object.keys(filters).forEach((key) => {
      const value = (filters as any)[key];
      if (value !== null && value !== undefined && value !== '') {
        params = params.set(key, value);
      }
    });

    return this._apiService.get<IPage<IPerson>>('persons', { params });
  }

  getPersonTypes(): Observable<IPersonType[]> {
    return this._apiService.get<IPersonType[]>('persons/types');
  }

  getPersonStatus(): Observable<IPersonStatus[]> {
    return this._apiService.get<IPersonStatus[]>('users/status');
  }

  createPerson(person: IPerson): Observable<HttpResponse<any>> {
    const body = JSON.stringify(person, (key, value) => {
      return value === null || value === undefined ? undefined : value;
    });

    const options = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      observe: 'response' as const,
    };

    return this._apiService.post('persons', body, options);
  }

  editPerson(personId: string, person: IPerson): Observable<HttpResponse<any>> {
    const body = JSON.stringify(person, (key, value) => {
      return value === null || value === undefined ? undefined : value;
    });

    const options = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      observe: 'response' as const,
    };

    return this._apiService.put('persons/' + personId, body, options);
  }
}
