export interface IPage<T> {
  content: T[];
  number: number;
  totalElements: number;
  totalPages: number;
}
