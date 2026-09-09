import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { ContentComponent } from './layout/content/content.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', component: ContentComponent },
      {
        path: 'users',
        loadChildren: () => import('./features/user/user.routes').then((u) => u.routes),
      },
      {
        path: 'persons',
        loadChildren: () => import('./features/person/person.routes').then((p) => p.personRoutes),
      },
      { path: 'finance/cadastros', component: ContentComponent },
      { path: 'finance/fluxo', component: ContentComponent },
    ],
  },
  {
    path: 'login',
    loadChildren: () => import('./features/login/login.routes').then((l) => l.routes),
  },
];
