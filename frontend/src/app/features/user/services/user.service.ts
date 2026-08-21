import { Injectable } from '@angular/core';
import { ApiService } from '../../../code/services/api.service';
import { UserFilter } from '../interfaces/user-filter.interface';
import { Observable } from 'rxjs';
import { IPage } from '../../../shared/interfaces/page.interface';
import { IUser } from '../interfaces/user.interface';
import { HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { IUserRole } from '../interfaces/user-role.interface';
import { IUserStatus } from '../interfaces/user-status.interface';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private _apiService: ApiService) {}

  getUserById(userId: string): Observable<IUser> {
    return this._apiService.get<IUser>('users/' + userId);
  }

  getAllUsers(filters: UserFilter, page: number = 0, size: number = 10): Observable<IPage<IUser>> {
    let params = new HttpParams().set('page', page).set('size', size);

    Object.keys(filters).forEach((key) => {
      const value = (filters as any)[key];
      if (value !== null && value !== undefined && value !== '') {
        params = params.set(key, value);
      }
    });

    return this._apiService.get<IPage<IUser>>('users', { params });
  }

  getUserStatus(): Observable<IUserStatus[]> {
    return this._apiService.get<IUserStatus[]>('users/status');
  }

  getUserRoles(): Observable<IUserRole[]> {
    return this._apiService.get<IUserRole[]>('users/roles');
  }

  createUser(user: IUser): Observable<HttpResponse<any>> {
    const body = JSON.stringify(user, (key, value) => {
      return value === null || value === undefined ? undefined : value;
    });

    const options = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      observe: 'response' as const,
    };

    return this._apiService.post('users', body, options);
  }

  editUser(userId: string, user: IUser): Observable<HttpResponse<any>> {
    const body = JSON.stringify(user, (key, value) => {
      return value === null || value === undefined ? undefined : value;
    });

    const options = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      observe: 'response' as const,
    };

    return this._apiService.put('users/' + userId, body, options);
  }
}
