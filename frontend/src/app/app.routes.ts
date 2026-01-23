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
        component: ContentComponent,
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
