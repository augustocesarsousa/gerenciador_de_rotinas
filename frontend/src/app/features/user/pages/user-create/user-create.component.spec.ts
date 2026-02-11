import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCreateComponent } from './user-create.component';
import { ToastrService } from 'ngx-toastr';
import { provideRouter, Router } from '@angular/router';

describe('UserCreateComponent', () => {
  let component: UserCreateComponent;
  let fixture: ComponentFixture<UserCreateComponent>;
  let toastrMock: any;
  let routerMock: any;

  beforeEach(async () => {
    toastrMock = { error: vi.fn(), success: vi.fn() };
    routerMock = { navigate: vi.fn(), url: '/' };

    await TestBed.configureTestingModule({
      imports: [UserCreateComponent],
      providers: [
        { provide: ToastrService, useValue: toastrMock },
        { provide: Router, useValue: routerMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UserCreateComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
