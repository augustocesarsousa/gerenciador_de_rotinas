import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'user_status',
})
export class StatusPipe implements PipeTransform {
  transform(value: string): string {
    switch (value) {
      case 'ACTIVE':
        return 'Ativo';
      case 'INACTIVE':
        return 'Inativo';
      default:
        return value;
    }
  }
}
