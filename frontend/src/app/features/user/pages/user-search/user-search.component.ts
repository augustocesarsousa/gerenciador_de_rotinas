import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIcon } from '@angular/material/icon';
import { UserService } from '../../services/user.service';
import { IUser } from '../../interfaces/user.interface';
import { StatusPipe } from '../../../../shared/pipes/status.pipe';

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
  ],
})
export class UserSearchComponent implements OnInit {
  searchForm!: FormGroup;

  displayedColumns: string[] = ['id', 'name', 'login', 'email', 'status', 'editar'];
  usersTable = new MatTableDataSource<IUser>([]);
  totalUsers = 0;

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
    this.findAllUsers();
  }

  ngAfterViewInit() {
    this.usersTable.paginator = this.paginator;
    this.usersTable.sort = this.sort;
  }

  findAllUsers() {
    const filters = this.searchForm.value;
    const pageNumber = this.paginator ? this.paginator.pageIndex : 0;
    const pageSize = this.paginator ? this.paginator.pageSize : 10;

    this._userService.findAll(filters, pageNumber, pageSize).subscribe((findAllResponse) => {
      this.usersTable.data = findAllResponse.content;
    });
  }
}
