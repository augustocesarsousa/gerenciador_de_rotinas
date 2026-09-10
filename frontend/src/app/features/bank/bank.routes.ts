import { Routes } from '@angular/router';

export const bankRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/bank-list/bank-list.component').then((c) => c.BankListComponent),
  },
  {
    path: 'novo',
    loadComponent: () =>
      import('./pages/bank-create/bank-create.component').then((c) => c.BankCreateComponent),
  },
  {
    path: 'editar/:id',
    loadComponent: () =>
      import('./pages/bank-edit/bank-edit.component').then((c) => c.BankEditComponent),
  },
];
