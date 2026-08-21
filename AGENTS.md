# Regras de Desenvolvimento - Backend Java 21 / Spring Boot

Este documento estabelece os padrões obrigatórios de arquitetura, organização e código a serem seguidos pelo agente Antigravity nas tarefas de evolução, correção e refatoração do backend.

## 1. Arquitetura e Organização de Pacotes (Package-by-Feature)
O código deve ser estruturado por **domínio de negócio** em vez de camadas técnicas horizontais. A árvore de pacotes deve respeitar rigorosamente a separação por responsabilidade e a subestruturação por pastas dentro de cada recurso:

*   **Pacote Global (`com.acsousa.gerenciador_de_rotinas.common`):** Reservado para componentes transversais reusáveis por múltiplos domínios.
    *   `common.config`: Configurações do Spring, Security, Jackson, OpenAPI/Swagger.
    *   `common.exceptions`: Tratamento global de exceções (`GlobalExceptionHandler` com `@RestControllerAdvice`) e RFC 7807 (`ProblemDetail`).
    *   `common.enums`: Enums genéricos e reutilizáveis por mais de um domínio (ex: `Status`).
    *   `common.utils`: Classes utilitárias puras e sem estado.

*   **Pacotes de Domínio (`com.acsousa.gerenciador_de_rotinas.domain.[nome_dominio]`):** Cada pasta sob `domain` representa um recurso/funcionalidade autocontido (ex: `domain.user`). É **estritamente obrigatório** organizar os componentes nos seguintes subpacotes dedicados:
    *   `controllers/`: Classes REST Controller (`@RestController`) anotadas com OpenAPI. As Controllers dependem exclusivamente da classe concreta do serviço do domínio (ex: `UserService`).
    *   `models/`: Entidades JPA (`@Entity`) do domínio (ex: `UserModel`).
    *   `repositories/`: Interfaces do Spring Data JPA (`@Repository`).
    *   `records/`: DTOs imutáveis de entrada (`UserCreateRecord`, `UserUpdateRecord`) e saída (`UserResponseRecord`).
    *   `services/`: Contém **exclusivamente a classe concreta do serviço agregador** (ex: `UserService` com `@Service`), sem a criação de interfaces espelho ou sufixos `Impl`. O serviço atua como uma Facade direta, orquestrando e delegando a execução para os UseCases do domínio.
    *   `usecases/`: Classes que contêm as regras de negócio individuais e granulares de cada operação (ex: `CreateUserUseCase`, `FindUserByIdUseCase`). Cada UseCase possui responsabilidade única e implementa exclusivamente um método público de execução (ex: `execute()`).
    *   `specifications/`: Filtros e consultas dinâmicas utilizando Spring Data JPA Specifications.

## 2. DTOs, Imutabilidade e Mapeamento
*   **Java Records Nativos:** Toda entrada e saída de dados na camada Controller deve utilizar exclusivamente Java Records (`record`). É proibido manter pastas separadas para DTOs genéricos ou classes com Lombok `@Data`.
*   **Proibição de Entidades na API:** É estritamente proibido retornar ou aceitar JPA Entities (`@Entity`) em Controllers ou DTOs.
*   **Encapsulamento do Mapeamento:** A conversão entre DTOs e Entities deve ser encapsulada no próprio `record` do domínio através de métodos estáticos de fábrica (ex: `UserResponseRecord.fromEntity(UserModel entity)`) ou métodos de conversão (`toEntity()`), mantendo a lógica de conversão coesa no DTO e fora dos UseCases e da camada de serviço.

## 3. Arquitetura e Use Cases
*   **Isolamento de Regras:** As regras de negócio e validações granulares devem ficar isoladas nos UseCases dentro do pacote correspondente à funcionalidade (`domain.[dominio].usecases`).
*   **Responsabilidade Única:** Cada UseCase deve ter uma única responsabilidade (Single Responsibility Principle) e ser anotado com `@Component`.

## 4. Injeção de Dependências e Boas Práticas (Java 21)
*   **Constructor Injection:** É proibido usar `@Autowired` em campos (`field injection`). Toda injeção deve ser feita via construtor (utilizando `@RequiredArgsConstructor` do Lombok ou construtor explícito nos casos necessários).
*   **Proibição de Interfaces Redundantes:** Proibido criar interfaces do tipo `UserService` para ter apenas uma única implementação `UserServiceImpl`. A classe `UserService` deve ser utilizada como classe concreta diretamente.
*   **Recursos Nativos Java 21:** Priorizar o uso de recursos modernos da linguagem sempre que aplicável, como *Pattern Matching* para `switch` e `instanceof`, e *Sealed Classes* para hierarquias fechadas.

## 5. Spring Data JPA e Persistência
*   **Estratégia de IDs:** Proibido utilizar `@GeneratedValue(strategy = GenerationType.AUTO)`. Utilize estratégias explícitas como `IDENTITY` ou `SEQUENCE` conforme a tabela.
*   **Paginação Obrigatória:** Toda busca que retorne coleções/listas de dados deve obrigatoriamente utilizar paginação via interface `Pageable` do Spring Data.
*   **Evitar Problema N+1:** Prevenir o problema N+1 em consultas com relacionamentos utilizando `JOIN FETCH` (em JPQL) ou `@EntityGraph` (do Spring Data JPA).
*   **Transações Espelhadas:** Operações que alterem estado no banco de dados (inserção, atualização, remoção) devem ser anotadas com `@Transactional` na camada de UseCase ou no método correspondente do Service.

## 6. Documentação de API (OpenAPI / Swagger)
*   **Contrato de API:** Todos os endpoints declarados nas Controllers devem conter as anotações do Springdoc/OpenAPI (`@Operation`, `@ApiResponse`, `@Tag`).
*   **Descrição de Schemas:** Propriedades dos Records de entrada/saída devem possuir anotações `@Schema(description = "...", example = "...")` para garantir a geração automática de documentação rica.

## 7. Logs e Observabilidade
*   **Proibição de Output Bruto:** É estritamente proibido o uso de `System.out.println()` ou `e.printStackTrace()`.
*   **SLF4J do Lombok:** Utilize exclusivamente a anotação `@Slf4j` do Lombok para instanciar o logger da classe.
*   **Níveis de Log:**
    *   `DEBUG`: Detalhes de execução interna e fluxo de depuração.
    *   `INFO`: Eventos importantes de negócio e ciclo de vida da aplicação.
    *   `WARN`: Falhas recuperáveis ou avisos importantes de comportamento inesperado.
    *   `ERROR`: Exceções não tratadas e falhas graves (deve incluir o stacktrace completo no log).

## 8. Tratamento Global de Exceções
*   **Sem Exceções Genéricas:** Nunca retornar exceções genéricas (`Exception` ou `RuntimeException`) para o cliente HTTP.
*   **Exceções de Domínio:** Lançar exceções de domínio personalizadas (ex: `ResourceNotFoundException`) e tratá-las de forma centralizada em `common.exceptions.GlobalExceptionHandler` (`@RestControllerAdvice`), retornando a estrutura padronizada RFC 7807 (`ProblemDetail`).

## 9. Validação de Dados
*   **Bean Validation:** Validar DTOs de entrada na camada Controller utilizando Bean Validation (`@Valid`, `@NotNull`, `@NotBlank`, `@Size`, etc.). As anotações de validação devem ser inseridas diretamente nos campos dos Records de entrada.

## 10. Padrão de Testes Automáticos
*   **JUnit 5 e Mockito:** Toda nova funcionalidade ou refatoração deve obrigatoriamente incluir testes unitários utilizando JUnit 5 e Mockito.
*   **Localização dos Testes:** Os arquivos de teste devem espelhar a estrutura do código principal sob `src/test/java/com/acsousa/gerenciador_de_rotinas/domain/[dominio]`.
*   **Nomenclatura Legível:** Nomes dos métodos de teste devem seguir o padrão legível: `should[ComportamentoEsperado]When[Cenario]` (ex: `shouldUpdateThrowResourceNotFoundExceptionWhenNotExistingId`).

## 11. Automação de Commits Semânticos
* **Commit por Etapa:** A cada alteração realizada e validada referente a uma etapa do plano de execução, o agente deve obrigatoriamente realizar um commit no Git antes de passar para a próxima instrução.
* **Padrão Obrigatório:** As mensagens de commit devem seguir estritamente o padrão **Conventional Commits** (`feat`, `fix`, `refactor`, `test`, `docs`, `chore`) descrevendo com precisão o escopo do domínio afetado (ex: `refactor(person): move DTOs para o pacote records`).

---

# Regras de Desenvolvimento - Frontend Angular 21

Este documento estabelece os padrões obrigatórios de desenvolvimento a serem seguidos pelo agente Antigravity nas tarefas de evolução, correção e refatoração do frontend em Angular 21.

## 1. Arquitetura de Pastas e Módulos
*   **Estrutura Modular por Camadas:** Toda a aplicação sob a pasta `src/app` deve seguir a divisão:
    *   `/core`: Serviços singleton (ex: autenticação, tema), guards de rotas, interceptors HTTP, modelos e interfaces de dados globais e gerenciamento de estado global.
    *   `/shared`: Componentes reutilizáveis de UI, pipes utilitários, diretivas personalizadas e importações reusáveis do Angular Material.
    *   `/layout`: Componentes estruturais do frame principal (ex: header, sidebar, footer, main shell).
    *   `/features`: Módulos de negócio da aplicação divididos por domínio de funcionalidade (ex: `/features/auth`, `/features/dashboard`).
*   **Organização de Feature:** Dentro de cada pasta de funcionalidade `/features/[nome-feature]`, os arquivos devem ser categorizados em subpastas:
    *   `/components`: Componentes internos secundários usados nas páginas da feature.
    *   `/services`: Serviços específicos do domínio da feature.
    *   `/models`: Modelos de dados e interfaces exclusivas da feature.
    *   `/pages`: Componentes principais que atuam como páginas de rotas.
*   **Gerenciamento de Assets:** Arquivos estáticos (imagens, ícones, fontes) devem residir obrigatoriamente na pasta `public/assets/`. É proibido referenciar assets fora desta pasta ou utilizar caminhos relativos quebrados.

## 2. Componentes e Angular Moderno (Angular 21)
*   **Strict Standalone Components:** Todos os novos componentes, diretivas e pipes devem ser obrigatoriamente declarados como standalone (`standalone: true`).
*   **Proibição de NgModules:** É estritamente proibido o uso de `@NgModule`.
*   **Injeção Funcional de Dependências:** Utilizar a função `inject()` no lugar da injeção via construtor (ex: `private userService = inject(UserService);`).
*   **Signals API:** Utilizar nativamente APIs de Signals (`signal()`, `computed()`, `effect()`, `resource()`) para o controle de reatividade e gerenciamento de estado dos componentes.
*   **Modern Inputs & Outputs:** Utilizar a nova declaração funcional de entradas e saídas (`input()`, `output()`) no lugar de `@Input()` e `@Output()`.
*   **Control Flow Moderno:** Utilizar a nova sintaxe do bloco de controle (`@if`, `@for`, `@switch`) para renderização condicional e repetições no template HTML, vedando o uso de diretivas legadas (`*ngIf`, `*ngFor`, `*ngSwitchCase`).

## 3. Estilização, Design System e Mobile-First
*   **Mobile-First Approach:** Desenvolver layouts adotando Mobile-First com Tailwind CSS. As classes utilitárias padrão devem estilizar telas menores, aplicando prefixos responsivos (`sm:`, `md:`, `lg:`, `xl:`) para ajustar a UI para dispositivos maiores.
*   **Integração Tailwind CSS + Angular Material:** Utilizar elementos do Angular Material (`@angular/material`) estilizados e posicionados através de utility classes do Tailwind CSS para manter consistência visual de layout e espaçamento.
*   **Estilos Limpos:** Evitar a escrita de CSS/SCSS puro em arquivos `.scss` locais dos componentes, restringindo CSS nativo apenas a customizações de estilo profundas do Angular Material que não possam ser feitas via classes utilitárias.

## 4. Comunicação e Consumo de API
*   **Feature-Specific Services:** Serviços de comunicação HTTP com backend devem ficar na feature que os consome, sendo promovidos para `/core` apenas em caso de reuso global.
*   **HttpClient com Signals/RxJS:** Consumir endpoints utilizando o `HttpClient` em conjunto com a função `inject()`. Tratar estados assíncronos preferencialmente via `resource()` / `rxResource()` para expor estado de carregamento e dados reativos diretamente como Signals.
*   **Global Exception Interceptors:** Tratar falhas de comunicação HTTP de forma uniforme usando HTTP Interceptors globais localizados em `/core/interceptors`.

## 5. Qualidade e Boas Práticas
*   **TypeScript Estrito:** Proibido o uso de tipagem genérica `any`. Todas as variáveis, retornos de função e parâmetros devem possuir tipo definido.
*   **Acessibilidade (A11y):** Garantir que a semântica HTML e os componentes do Angular Material utilizem recursos nativos de acessibilidade (ARIA labels, focus trap, contraste adequado).
*   **Lazy Loading:** Todas as rotas de funcionalidades principais em `/features` devem ser carregadas via Lazy Loading no arquivo de rotas principal (`loadComponent` ou `loadChildren`).

## 6. Automação de Commits Semânticos
*   **Commit por Etapa:** A cada alteração realizada e validada referente a uma etapa do plano de execução frontend, o agente deve obrigatoriamente realizar um commit no Git antes de passar para a próxima instrução.
*   **Padrão Obrigatório:** As mensagens de commit devem seguir estritamente o padrão **Conventional Commits** (`feat`, `fix`, `refactor`, `style`, `test`, `chore`) descrevendo com precisão a feature afetada (ex: `feat(dashboard): cria componente de resumo com Tailwind CSS`).
