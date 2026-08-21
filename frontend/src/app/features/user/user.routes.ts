import { Route } from '@angular/router';
import { UserSearchComponent } from './pages/user-search/user-search.component';
import { UserCreateComponent } from './pages/user-create/user-create.component';
import { UserEditComponent } from './pages/user-edit/user-edit.component';

export const routes: Route[] = [
  { path: '', component: UserSearchComponent },
  { path: 'create', component: UserCreateComponent },
  { path: 'edit/:id', component: UserEditComponent },
];
