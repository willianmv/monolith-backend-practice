# 🧪 Testes

## 📚 Sumário de Casos de Uso Testados

### 🔐 Auth: 
- [✅ Registro de Usuário](#-testes-registro-de-usuário)
- [💡 Ativação de Conta](#-testes-ativação-de-conta)
- [✔️ Login ](#-testes-login)

### 📄 Post


### 💭 Reply

---

> Todos os testes foram implementados utilizando `JUnit` com `Mockito`

## ✅ Testes Registro de Usuário:

Este caso de uso é responsável por registrar um novo usuário com base nos dados fornecidos (nome de usuário, e-mail e senha). Ele também trata casos em que o usuário já existe (ativo ou inativo), e realiza o envio ou reenvio de código de ativação conforme necessário.

### 📌 Regras de Negócio

- 📧 O e-mail deve ser único na base de dados.
- 🧑 O nome de usuário também deve ser único.
- 🔐 A senha fornecida deve ser codificada antes de ser persistida.
- 📮 Um código de ativação deve ser gerado e enviado ao e-mail do usuário.
- 🔁 Se o usuário já existe e estiver inativo, deve-se verificar a validade do código:
  - Se o código ainda estiver válido, impedir novo envio.
  - Se o código expirou, um novo código é gerado e reenviado.
- 🚫 Não deve ser permitido o registro se o usuário já estiver ativo com aquele e-mail ou username.

### 🧪 Casos de Teste
| Cenário                                   | Descrição                                                                        |
| ----------------------------------------- | -------------------------------------------------------------------------------- |
| ✅ Registro de novo usuário                | Deve salvar o usuário, codificar a senha e publicar evento com código de ativação. |
| ❌ E-mail já em uso por usuário ativo      | Deve lançar `DomainException` informando que e-mail já está sendo usado.         |
| ❌ Usuário inativo com código ainda válido | Deve lançar `DomainException` solicitando que o usuário verifique o e-mail.      |
| 🔁 Usuário inativo com código expirado    | Deve gerar novo código e publicar novo evento de ativação.           |
| ❌ Nome de usuário já em uso               | Deve lançar `DomainException` informando que o nome de usuário já está em uso.   |


---


## 💡 Testes Ativação de Conta

Este caso de uso é responsável por ativar uma conta de usuário a partir de um código de ativação. A validação inclui checagem de expiração e reutilização do código.

### 📌 Regras de Negócio

- 🔍 O código de ativação deve existir e estar vinculado a um usuário.
- ⏱️ O código não pode estar expirado.
- 🚫 O código não pode ter sido utilizado anteriormente.
- 🟢 Caso válido, o código é marcado como validado e o usuário ativado.
- 📣 Um evento de ativação de conta deve ser publicado.

### 🧪 Casos de Teste

| Cenário                      | Descrição                                                             |
| ---------------------------- | --------------------------------------------------------------------- |
| ✅ Ativação com código válido | Deve ativar a conta, marcar código como validado e publicar o evento. |
| ❌ Código expirado            | Deve lançar `DomainException` informando que o código está expirado.  |
| ❌ Código já utilizado        | Deve lançar `DomainException` informando que o código já foi usado.   |


---


## ✔️ Testes Login

Este caso de uso é responsável por realizar a autenticação de um usuário por meio de e-mail e senha. Ele valida as credenciais fornecidas e, caso estejam corretas, gera um token JWT que pode ser utilizado para autenticação nas próximas requisições.

### 📌 Regras de Negócio

- 🔍 O usuário deve existir na base de dados, identificado por e-mail.
- ✅ A conta do usuário precisa estar ativa para efetuar login.
- 🔐 A senha informada deve coincidir com a senha armazenada (utiliza PasswordServiceGateway).
- 🪪 Um token JWT é gerado em caso de sucesso utilizando JwtServiceGateway, contendo as claims:
  - id: ID do usuário
  - username: nome de usuário

- ⏳ O tempo de expiração do token é fixo em 900000 milissegundos (15 minutos).


### 🧪 Casos de Teste

| Cenário                         | Descrição                                                                 |
| ------------------------------- | ------------------------------------------------------------------------- |
| ✅ Login com credenciais válidas | Deve gerar um token JWT válido e retornar o tempo de expiração padrão.    |
| ❌ Usuário não encontrado        | Deve lançar `DomainException` informando que o e-mail não foi encontrado. |
| ❌ Conta inativa                 | Deve lançar `DomainException` informando que a conta está inativa.        |
| ❌ Senha inválida                | Deve lançar `DomainException` informando que a senha está incorreta.      |


---

