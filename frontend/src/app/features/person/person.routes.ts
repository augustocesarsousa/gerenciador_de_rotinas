import { Routes } from '@angular/router';

export const personRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/person-search/person-search.component').then(
        (c) => c.PersonSearchComponent
      ),
  },
  {
    path: 'create',
    loadComponent: () =>
      import('./pages/person-create/person-create.component').then(
        (c) => c.PersonCreateComponent
      ),
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./pages/person-edit/person-edit.component').then(
        (c) => c.PersonEditComponent
      ),
  },
];
