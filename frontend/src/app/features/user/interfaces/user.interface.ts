import { IUserRole } from './user-role.interface';

export interface IUser {
  id?: number;
  name: string;
  login: string;
  email: string;
  password: string;
  status?: string;
  roles: IUserRole[];
  createdAt?: Date;
  updatedAt?: Date;
  userIdEdit: number;
}
