# Skill: Git Semantic Commit

## Trigger
Quando uma etapa/subtarefa do plano de alteração for concluída com sucesso e validada (build/testes passando).

## Instructions
1. Verifique o status das alterações pendentes no repositório.
2. Identifique o tipo de alteração realizada:
   - `feat`: Nova funcionalidade ou recurso de domínio.
   - `fix`: Correção de bug ou erro de compilação/teste.
   - `refactor`: Refatoração de estrutura (ex: mover classes para subpastas de domínio) sem alterar comportamento.
   - `docs`: Alterações na documentação, regras ou comentários.
   - `test`: Adição ou ajuste de testes unitários/integrados.
   - `chore`: Atualização de configurações, build ou dependências (ex: `.gitignore`, `.antigravityrules`).
3. Construa a mensagem de commit no formato: `<tipo>(<escopo>): <descrição em português no imperativo>`.
   - Exemplo: `refactor(user): reorganiza classes no pacote domain.user.services`
   - Exemplo: `feat(rotina): cria UseCase para criacao de rotinas`
4. Execute o commit no terminal integrado:
   - `git add .`
   - `git commit -m "<mensagem_formatada>"`