export interface IPerson {
  id?: number;
  name: string;
  type: string; // PHYSICAL | LEGAL
  status?: string; // ACTIVE | INACTIVE
  cpf?: string;
  cnpj?: string;
  address?: string;
  number?: number;
  neighborhood?: string;
  city?: string;
  state?: string;
  zipcode?: string;
  phone?: string;
  email?: string;
  createdAt?: string;
  updatedAt?: string;
  userIdEdit: number;
}
