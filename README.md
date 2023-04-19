# backend-g3

Código sendo desenvolvido pela equipe 3 da turma de CSW 2023/02 PUCRS, ministrada pelo professor Eduardo Arruda

Até o momento, é suportado o crud voltado para os clientes.
Todo o sistema de gerenciamento de senhas de cadastros são armazenados no keycloak
Seguindo o seguinte fluxo:

Login:
```mermaid
sequenceDiagram
  actor A as Cliente
  participant B as Api
  participant C as Keycloak
  A ->>+ B: Realiza login
  B -->> C: Solicita o login, com o clientId da aplicação.
  C -->> B: Devolve dados de autenticação.
  B ->>- A: Devolve Tokens de acesso.
```

Busca Todos usuarios:
```mermaid
sequenceDiagram
  actor A as Cliente
  participant B as Api
  participant C as Keycloak
  A ->>+ B: Solicita usuários
  B ->> C: Valida token de acesso.
  break Quando token tiver Inválido
    C ->> B: Token inválido.
    B ->> A: Token não é valido
  end
  C ->> B: Token valido
  B ->> C: Solicita lista de users.
  C -->> B: Devolve todos os users.
  B -->> B: Filtra somente os ativos
  B ->>- A: Devolve Lista de usuários.
```
