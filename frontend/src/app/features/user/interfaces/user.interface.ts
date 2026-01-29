import { IRole } from './role.interface';

export interface IUser {
  id?: number;
  name: string;
  login: string;
  email: string;
  password: string;
  status?: string;
  roles: IRole[];
  createdAt?: Date;
  updatedAt?: Date;
  userIdEdit: number;
}
