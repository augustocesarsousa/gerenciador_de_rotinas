import { Injectable } from '@angular/core';
import { ApiService } from '../../../code/services/api.service';
import { UserFilter } from '../interfaces/user-filter.interface';
import { Observable } from 'rxjs';
import { IPage } from '../../../shared/interfaces/page.interface';
import { IUser } from '../interfaces/user.interface';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private apiService: ApiService) {}

  findAll(filters: UserFilter, page: number = 0, size: number = 10): Observable<IPage<IUser>> {
    let params = new HttpParams().set('page', page).set('size', size);

    Object.keys(filters).forEach((key) => {
      const value = (filters as any)[key];
      if (value !== null && value !== undefined && value !== '') {
        params = params.set(key, value);
      }
    });

    return this.apiService.get<IPage<IUser>>('users', { params });
  }
}
