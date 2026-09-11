export type BankAccountType = 'CHECKING' | 'SAVINGS' | 'INVESTMENT' | 'CASH_DESK';

export type EntityStatus = 'ACTIVE' | 'INACTIVE';

export interface BankAccountResponse {
  id: number;
  bankId?: number | null;
  bankName?: string | null;
  bankCode?: string | null;
  accountType: BankAccountType;
  accountTypeDescription?: string | null;
  description: string;
  agencyNumber?: string | null;
  agencyDigit?: string | null;
  accountNumber?: string | null;
  accountDigit?: string | null;
  projectOrAgreement?: string | null;
  initialBalance: number;
  initialBalanceDate: string;
  status: EntityStatus;
  createdAt: string;
  updatedAt: string;
  userIdEdit: number;
}

export interface BankAccountCreateRequest {
  bankId?: number | null;
  accountType: BankAccountType;
  description: string;
  agencyNumber?: string | null;
  agencyDigit?: string | null;
  accountNumber?: string | null;
  accountDigit?: string | null;
  projectOrAgreement?: string | null;
  initialBalance: number;
  initialBalanceDate: string;
  status?: EntityStatus;
  userIdEdit: number;
}

export interface BankAccountUpdateRequest {
  bankId?: number | null;
  accountType: BankAccountType;
  description: string;
  agencyNumber?: string | null;
  agencyDigit?: string | null;
  accountNumber?: string | null;
  accountDigit?: string | null;
  projectOrAgreement?: string | null;
  initialBalance?: number | null;
  initialBalanceDate?: string | null;
  status: EntityStatus;
  userIdEdit: number;
}

export interface BankAccountQueryFilter {
  id?: number;
  bankId?: number;
  accountType?: BankAccountType | '';
  description?: string;
  status?: EntityStatus | '';
  search?: string;
}
