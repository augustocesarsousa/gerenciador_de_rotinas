# Skill: UseCase CRUD Generator

## Trigger
Quando o usuário solicitar a criação ou adição de uma nova operação de negócio (CRUD ou fluxo customizado) para um domínio existente no backend.

## Instructions
Ao receber o nome da ação (ex: `InactivateUser`, `UpdateRotinaStatus`) e o domínio alvo (ex: `user`):

1. **Validação da Estrutura de Destino:**
   Localize o pacote `com.acsousa.gerenciador_de_rotinas.domain.[dominio]`.
   Certifique-se de que as subpastas `records/`, `usecases/`, `services/`, `controllers/` existem.

2. **Criação/Ajuste de Records (`records/`):**
   - Se a operação exigir dados de entrada, crie ou altere o `[Acao]RequestRecord` utilizando Bean Validation (`@NotNull`, `@NotBlank`, etc.) e anotações `@Schema` do OpenAPI nas propriedades do Record.
   - Se a saída for específica, atualize o `[Acao]ResponseRecord` contendo o método de conversão estático `fromEntity()`.

3. **Criação do UseCase (`usecases/[Acao]UseCase.java`):**
   - Anote a classe com `@Service`.
   - Adicione `@RequiredArgsConstructor` do Lombok (proibido `@Autowired` em campos).
   - Adicione `@Slf4j` para rastreabilidade de eventos e logs adequados (`INFO` para eventos, `DEBUG` para fluxo).
   - Implemente um único método público de execução: `execute(...)`.
   - Aplique `@Transactional` caso a operação modifique o estado da entidade no banco de dados.

4. **Atualização da Service Facade Concreta (`services/[Dominio]Service.java`):**
   - Injetar o novo `[Acao]UseCase` via construtor (`@RequiredArgsConstructor`).
   - Adicionar o método público correspondente que delega a chamada diretamente para `[Acao]UseCase.execute(...)`.
   - Proibido criar interfaces para a Service.

5. **Exposição na Controller (`controllers/[Dominio]Controller.java`):**
   - Mapear o endpoint HTTP correto (`@PostMapping`, `@PutMapping`, `@GetMapping`, `@PatchMapping`, `@DeleteMapping`).
   - Adicionar as anotações do OpenAPI (`@Operation`, `@ApiResponse`).
   - Garantir o uso de `@Valid` no `@RequestBody` quando houver DTO de entrada.
   - Chamar o método recém-criado da `[Dominio]Service`.

6. **Geração de Testes Unitários (`src/test/java/.../domain/[dominio]/usecases/`):**
   - Criar a classe `[Acao]UseCaseTest.java`.
   - Utilizar `@ExtendWith(MockitoExtension.class)`.
   - Cobrir os cenários de sucesso (*happy path*) e de exceção.
   - Seguir a nomenclatura obrigatória: `deve[ComportamentoEsperado]Quando[Cenario]`.

7. **Validação Automática:**
   - Acesse o terminal e execute os testes da classe criada para garantir aprovação de 100%.