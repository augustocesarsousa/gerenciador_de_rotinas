# Skill: Angular Component Generator

## Trigger
Quando o usuário solicitar a criação de um novo componente visual (secundário ou reutilizável) no frontend.

## Instructions
Ao receber o nome do componente (ex: `rotina-card`), o tipo (`shared` ou pertencente a uma `feature`) e os requisitos de UI:

1. **Localização de Destino:**
   - Se for um componente de negócio, salve em `src/app/features/[feature]/components/[nome-componente]/`.
   - Se for reutilizável em várias telas, salve em `src/app/shared/components/[nome-componente]/`.

2. **Declaração do Componente Standalone:**
   - Defina `@Component({ standalone: true, ... })`.
   - Declare entradas funcionais com `input()` e `input.required()`.
   - Declare saídas funcionais com `output()`.
   - Injete dependências necessárias via `inject()`.

3. **Template HTML & Estilização:**
   - Construa o layout adotando a abordagem **Mobile-First** com Tailwind CSS (utilize prefixos responsivos `sm:`, `md:`, `lg:`).
   - Integre componentes do `@angular/material` conforme exigido pelo design system.
   - Utilize exclusivamente a sintaxe do Control Flow nativo (`@if`, `@for`, `@switch`).
   - Mantenha o arquivo `.scss` local limpo, delegando estilos utilitários ao Tailwind.

4. **Acessibilidade (A11y):**
   - Garanta atributos `aria-label`, suporte a navegação via teclado e papéis semânticos.

5. **Validação:**
   - Verifique se não há diretivas legadas (`*ngIf`, `*ngFor`), uso de `@Input()` ou injeção via construtor.