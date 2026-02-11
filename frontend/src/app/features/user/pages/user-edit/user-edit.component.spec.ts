import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserEditComponent } from './user-edit.component';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

describe('UserEditComponent', () => {
  let component: UserEditComponent;
  let fixture: ComponentFixture<UserEditComponent>;
  let toastrService: ToastrService;
  let toastrMock: any;
  let routerMock: any;
  let activatedRouteMock: any;

  beforeEach(async () => {
    toastrMock = { error: vi.fn(), success: vi.fn() };
    routerMock = { navigate: vi.fn(), url: '/' };
    activatedRouteMock = {
      params: of({ id: '1' }),
      snapshot: {
        paramMap: {
          get: (key: string) => '1',
        },
      },
    };

    await TestBed.configureTestingModule({
      imports: [UserEditComponent],
      providers: [
        { provide: ToastrService, useValue: toastrMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UserEditComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
