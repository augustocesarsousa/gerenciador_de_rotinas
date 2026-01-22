import { Component, signal } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatSlideToggleModule],
  template: ` <router-outlet />
    <p>Frontent iniciado</p>`,
})
export class App {}
