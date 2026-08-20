# Skill: RFC 7807 Exception Handler

## Trigger
Quando o usuário solicitar a criação de uma nova exceção de negócio/domínio ou o mapeamento de um novo erro HTTP centralizado no backend.

## Instructions
Ao receber o nome da exceção (ex: `ResourceNotFoundException`, `BusinessRuleException`), o status HTTP correspondente (ex: `NOT_FOUND`, `BAD_REQUEST`, `UNPROCESSABLE_ENTITY`) e o domínio alvo:

1. **Criação da Exceção de Domínio (`domain/[dominio]/exceptions/` ou `common/exceptions/`):**
   - Se for uma exceção específica de um domínio (ex: `UserAlreadyExistsException`), crie-a sob `domain/[dominio]/exceptions/`.
   - Se for uma exceção reutilizável por múltiplos domínios, crie-a sob `common/exceptions/`.
   - A classe deve estender `RuntimeException`.
   - Implemente construtores padrão aceitando `String message` e `String message, Throwable cause`.

2. **Mapeamento no Handler Global (`common/exceptions/GlobalExceptionHandler.java`):**
   - Localize a classe anotada com `@RestControllerAdvice`.
   - Adicione o método de tratamento com a anotação `@ExceptionHandler([NomeDaExcecao].class)`.
   - Configure o retorno utilizando o padrão **RFC 7807** nativo do Spring Boot 3 (`ProblemDetail`):
     ```java
     @ExceptionHandler([NomeDaExcecao].class)
     public ProblemDetail handle[NomeDaExcecao]([NomeDaExcecao] ex) {
         ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
             HttpStatus.[STATUS_HTTP], 
             ex.getMessage()
         );
         problemDetail.setTitle("[Título Amigável do Erro]");
         problemDetail.setProperty("timestamp", Instant.now());
         log.warn("Exceção de negócio capturada: {}", ex.getMessage());
         return problemDetail;
     }
     ```
   - Garanta o log adequado utilizando a anotação `@Slf4j` (`WARN` para erros de negócio recuperáveis/esperados e `ERROR` para falhas graves do sistema).

3. **Atualização da Documentação OpenAPI na Controller:**
   - Adicione/atualize a anotação `@ApiResponse` nos endpoints afetados da Controller do domínio informando o código HTTP correspondente e o Schema de erro RFC 7807 (`ProblemDetail`).

4. **Criação de Testes Unitários de Exceção:**
   - Crie/atualize o teste no `@RestControllerAdviceTest` ou no teste da Controller/UseCase validando que o lançamento da exceção resulta exatamente no código HTTP e na estrutura `ProblemDetail` esperados.
   - Nomenclatura obrigatória do teste: `deveRetornar[StatusHttp]Quando[CenarioDaExcecao]`.

5. **Validação Automática:**
   - Execute o teste unitário via terminal integrado para validar o contrato RFC 7807 e a resposta JSON do manipulador.