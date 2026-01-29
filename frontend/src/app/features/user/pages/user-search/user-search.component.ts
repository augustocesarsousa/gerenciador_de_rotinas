import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIcon } from '@angular/material/icon';
import { UserService } from '../../services/user.service';
import { IUser } from '../../interfaces/user.interface';
import { StatusPipe } from '../../../../shared/pipes/status.pipe';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-search',
  templateUrl: './user-search.component.html',
  styleUrl: './user-search.component.css',
  imports: [
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    MatIcon,
    StatusPipe,
    RouterLink,
  ],
})
export class UserSearchComponent implements OnInit {
  searchForm!: FormGroup;

  displayedColumns: string[] = ['id', 'name', 'login', 'email', 'status', 'editar'];
  usersTable = new MatTableDataSource<IUser>([]);
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private _fb: FormBuilder,
    private _userService: UserService,
  ) {
    this.searchForm = this._fb.group({
      id: [''],
      name: [''],
      login: [''],
      email: [''],
      status: [''],
    });
  }

  ngOnInit(): void {
    this.getAllUsers();
  }

  ngAfterViewInit() {
    this.usersTable.sort = this.sort;
  }

  getAllUsers() {
    const filters = this.searchForm.value;

    this._userService
      .getAllUsers(filters, this.pageIndex, this.pageSize)
      .subscribe((findAllResponse) => {
        this.usersTable.data = findAllResponse.content;
        this.totalElements = findAllResponse.totalElements;
      });
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.getAllUsers();
  }
}
