# Skill: Angular Feature Generator

## Trigger
Quando o usuário solicitar a criação ou inicialização de uma nova funcionalidade/módulo de negócio no frontend Angular 21.

## Instructions
Ao receber o nome da funcionalidade (ex: `rotina`, `dashboard`), crie o módulo de negócio sob `src/app/features/[nome-feature]`:

1. **Estrutura de Diretórios:**
   Crie as subpastas: `pages`, `components`, `services`, `models`.

2. **Model (`models/[nome-feature].model.ts`):**
   - Declare as interfaces TypeScript estritas correspondentes às entidades/DTOs consumidos da API backend (sem uso de `any`).

3. **Service (`services/[nome-feature].service.ts`):**
   - Anote com `@Injectable({ providedIn: 'root' })` ou escopo da feature.
   - Injete o `HttpClient` via `private http = inject(HttpClient);`.
   - Crie os métodos de consumo de API expondo retornos tipados com RxJS ou Signals/Resource.

4. **Page Component (`pages/[nome-feature]-page.component.ts`):**
   - Marque como `standalone: true`.
   - Adicione os imports do Angular Material e componentes compartilhados no array `imports`.
   - Utilize a sintaxe funcional `inject()` para injetar serviços e router.
   - Gerencie estados da página usando Signals (`signal()`, `computed()`).
   - Aplique o layout Mobile-First com Tailwind CSS e o bloco de controle nativo (`@if`, `@for`).

5. **Configuração de Rotas (`[nome-feature].routes.ts`):**
   - Crie o arquivo de rotas exportando o array `Routes`.
   - Garanta o apontamento para o componente principal via Lazy Loading.

6. **Validação Automática:**
   - Execute a verificação de compilação do TypeScript para garantir ausência de erros de sintaxe ou tipagem.