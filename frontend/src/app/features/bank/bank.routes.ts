import { Routes } from '@angular/router';

export const bankRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/bank-list/bank-list.component').then((c) => c.BankListComponent),
  },
];
