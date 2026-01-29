import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly apiUrl = environment.API_URL;

  constructor(private http: HttpClient) {}

  get<T>(endpoint: string, options?: { params?: HttpParams }) {
    return this.http.get<T>(`${this.apiUrl}/${endpoint}`, options);
  }

  post<T>(endpoint: string, body: any, options?: object) {
    return this.http.post<T>(`${this.apiUrl}/${endpoint}`, body, options);
  }

  put<T>(endpoint: string, body: any, options?: object) {
    return this.http.put<T>(`${this.apiUrl}/${endpoint}`, body, options);
  }

  delete<T>(endpoint: string) {
    return this.http.delete<T>(`${this.apiUrl}/${endpoint}`);
  }
}
