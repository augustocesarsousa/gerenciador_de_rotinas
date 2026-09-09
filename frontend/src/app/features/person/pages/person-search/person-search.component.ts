import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { NonNullableFormBuilder, FormGroup, FormsModule, ReactiveFormsModule, FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIcon } from '@angular/material/icon';
import { PersonService } from '../../services/person.service';
import { IPerson } from '../../interfaces/person.interface';
import { StatusPipe } from '../../../../shared/pipes/status.pipe';
import { CpfCnpjPipe } from '../../../../shared/pipes/cpf-cnpj.pipe';
import { RouterLink } from '@angular/router';
import { IPersonStatus } from '../../interfaces/person-status.interface';
import { IPersonType } from '../../interfaces/person-type.interface';
import { ToastrService } from 'ngx-toastr';



@Component({
  selector: 'app-person-search',
  templateUrl: './person-search.component.html',
  styleUrl: './person-search.component.css',
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
    CpfCnpjPipe,
    RouterLink,
  ],
})
export class PersonSearchComponent implements OnInit {
  private _fb = inject(NonNullableFormBuilder);
  private _personService = inject(PersonService);
  private _toastr = inject(ToastrService);

  searchForm!: FormGroup;

  personStatus: IPersonStatus[] = [];
  personTypes: IPersonType[] = [];

  displayedColumns: string[] = ['id', 'name', 'type', 'document', 'email', 'status', 'editar'];
  personsTable = new MatTableDataSource<IPerson>([]);
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    this.searchForm = this._fb.group({
      id: [null as number | null],
      name: [''],
      type: [''],
      status: [''],
      cpf: [''],
      cnpj: [''],
    });
  }

  ngOnInit(): void {
    this.getPersonStatus();
    this.getPersonTypes();
    this.getAllPersons();
  }

  ngAfterViewInit(): void {
    this.personsTable.sort = this.sort;
  }

  getPersonStatus(): void {
    this._personService.getPersonStatus().subscribe({
      next: (response: IPersonStatus[]) => {
        this.personStatus = response;
      },
      error: () => {
        this._toastr.error('Ocorreu um erro ao carregar os status');
      },
    });
  }

  getPersonTypes(): void {
    this._personService.getPersonTypes().subscribe({
      next: (response: IPersonType[]) => {
        this.personTypes = response;
      },
      error: () => {
        this._toastr.error('Ocorreu um erro ao carregar os tipos de pessoa');
      },
    });
  }

  getAllPersons(): void {
    const filters = this.searchForm.value;

    this._personService.getAllPersons(filters, this.pageIndex, this.pageSize).subscribe({
      next: (response) => {
        this.personsTable.data = response.content;
        this.totalElements = response.totalElements;
      },
      error: () => {
        this._toastr.error('Ocorreu um erro ao carregar as pessoas');
      },
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.getAllPersons();
  }
}
