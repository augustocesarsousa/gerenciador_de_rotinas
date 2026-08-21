# Skill: Domain Feature Generator

## Trigger
Quando o usuário solicitar a criação ou inicialização de um novo domínio ou recurso no backend.

## Instructions
Ao receber o nome do domínio (ex: `rotina`), execute a criação dos artefatos sob o pacote `com.acsousa.gerenciador_de_rotinas.domain.[dominio]`:

1. **Estrutura de Diretórios:**
   Crie as subpastas: `controllers`, `models`, `repositories`, `records`, `services`, `usecases`, `specifications`.

2. **Model (`models/[Entidade].java`):**
   - Anote com `@Entity`, `@Table(name = "tb_[dominio]")`, `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`.
   - Adicione ID com estratégia explícita (`IDENTITY` ou `SEQUENCE`).

3. **Repository (`repositories/[Entidade]Repository.java`):**
   - Crie a interface estendendo `JpaRepository<[Entidade], Long>` e `JpaSpecificationExecutor<[Entidade]>`.

4. **Records (`records/`):**
   - Crie o `[Entidade]CreateRecord` com Bean Validation (`@NotBlank`, `@NotNull`, etc.) e anotações `@Schema` do OpenAPI.
   - Crie o `[Entidade]ResponseRecord` com métodos estáticos de fábrica `fromEntity([Entidade] entity)`.

5. **UseCases (`usecases/`):**
   - Crie o UseCase principal (ex: `Create[Entidade]UseCase.java`) anotado com `@Service`.
   - Injete dependências via `@RequiredArgsConstructor`.
   - Aplique `@Transactional` em mutações.

6. **Service Facade (`services/[Entidade]Service.java`):**
   - Crie a **classe concreta direta** (sem interface) anotada com `@Service`.
   - Injete os UseCases do domínio via `@RequiredArgsConstructor` e delegue as chamadas.

7. **Controller (`controllers/[Entidade]Controller.java`):**
   - Anote com `@RestController`, `@RequestMapping("/api/v1/[dominio]s")`, `@Tag(name = "...")` e `@RequiredArgsConstructor`.
   - Injete exclusivamente o `[Entidade]Service` concreto.

8. **Testes Unitários:**
   - Crie a suíte de testes do UseCase sob `src/test/java/.../domain/[dominio]/usecases/` com JUnit 5 + Mockito no padrão `should[ComportamentoEsperado]When[Cenario]`.