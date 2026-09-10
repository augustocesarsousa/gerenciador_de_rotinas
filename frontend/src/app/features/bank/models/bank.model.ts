export type EntityStatus = 'ACTIVE' | 'INACTIVE';

export interface BankResponse {
  id: number;
  code: string;
  ispb?: string | null;
  name: string;
  shortName: string;
  status: EntityStatus;
  createdAt: string;
  updatedAt: string;
  userIdEdit: number;
}

export interface BankCreateRequest {
  code: string;
  ispb?: string | null;
  name: string;
  shortName: string;
  status: EntityStatus;
  userIdEdit: number;
}

export interface BankUpdateRequest {
  code: string;
  ispb?: string | null;
  name: string;
  shortName: string;
  status: EntityStatus;
  userIdEdit: number;
}

export interface BankQueryFilter {
  search?: string;
  code?: string;
  name?: string;
  shortName?: string;
  status?: EntityStatus;
}
