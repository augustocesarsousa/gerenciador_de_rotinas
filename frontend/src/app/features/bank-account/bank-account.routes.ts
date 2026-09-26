import { Routes } from '@angular/router';

export const bankAccountRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/bank-account-list/bank-account-list.component').then((c) => c.BankAccountListComponent),
  },
  {
    path: 'novo',
    loadComponent: () =>
      import('./pages/bank-account-create/bank-account-create.component').then((c) => c.BankAccountCreateComponent),
  },
  {
    path: 'editar/:id',
    loadComponent: () =>
      import('./pages/bank-account-edit/bank-account-edit.component').then((c) => c.BankAccountEditComponent),
  },
];
