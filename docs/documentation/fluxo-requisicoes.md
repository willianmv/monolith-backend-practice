# ⚙️ Fluxos da Aplicação

Nesta seção são descritos os fluxos de uso essenciais da aplicação, divididos por contexto de negócio. Cada fluxo representa o caminho completo percorrido pelos dados, desde a entrada via HTTP até a execução da lógica de negócio e persistência, seguindo os princípios da arquitetura limpa.

### Os fluxos estão organizados da seguinte forma:

- [🔐 Autenticação](#-autenticação)
- [📝 Postagens](#-postagens)
- [💬 Respostas](#-respostas)

---

Todos os fluxos seguem a estrutura da Clean Architecture, com entrada via controllers, validação de DTOs, delegação para casos de uso e comunicação com serviços e repositórios por meio de gateways definidos no core.

Cada etapa do processo é tratada de forma isolada, respeitando os princípios de separação de responsabilidades e inversão de dependência.

### 🧩 Padrão dos Fluxos
```text
[HTTP Request]
     ↓
[Controller (infra)] → Valida DTO
     ↓
[UseCase (core)] → Executa lógica via gateways
     ↓
[Gateways (core)] → Interfaces
     ↓
[Implementações (infra)] → Acesso ao banco, serviços, eventos
```

---

## 🔐 Autenticação
> A seguir estão descritos os fluxos completos dos três principais processos de autenticação da aplicação:

- Registro de novo usuário:  ``RegisterUseCaseImpl``
- Ativação de conta: ``ActivateAccountUseCaseImpl``
- Login com geração de token JWT: ``LoginUseCaseImpl``

### ✍️ Register Use Case
```text
[HTTP Client]
     |
     v
[Controller /auth/register] --> Valida DTO
     |
     v
[RegisterUserUseCaseImpl]
     |
     ├──> Verifica se username/email já existem [UserRepository]
     ├──> Criptografa senha [PasswordEncoder]
     ├──> Cria e salva User inativo [UserRepository]
     ├──> Gera código de validação [ValidationCodeService]
     └──> Envia e-mail com código [EmailService]
```

#### 🔄 Fluxo do processo de registro
1. O caso de uso recebe os dados de entrada: nome, username, email e senha.


2. Verifica se já existe um usuário com o **email**:
    - Se sim, e estiver **ativado**, lança exceção.
    - Se não estiver ativado, busca o último código:
        - Se o código **ainda não expirou**, lança exceção para evitar spam.
        - Se expirou, gera novo código, salva e envia novamente.


3. Se nenhum usuário com o email existe:
    - Verifica se o **username já está em uso**, e se sim, lança exceção.
    - Gera senha codificada e cria novo usuário com perfil REGULAR.
    - Persiste o usuário no banco.
    - Gera código de ativação e o envia por evento.
---
 

### ⚠️ Activate Account Use Case
```text
[HTTP Client]
     |
     v
[Controller /auth/activate] --> Recebe código de ativação
     |
     v
[ActivateAccountUseCaseImpl]
     |
     ├──> Busca código de ativação [ValidationCodeRepository]
     ├──> Verifica se o código expirou
     ├──> Verifica se já foi utilizado
     ├──> Marca o código como validado
     ├──> Ativa o usuário [UserRepository]
     └──> Publica evento de ativação [EventPublisher]
```
#### 🔄 Fluxo do processo de ativação de conta

1. O caso de uso recebe um **código de ativação** como entrada.


2. Verifica se o **código é válido**:
    - Se estiver **expirado**, lança exceção.
    - Se já tiver sido **utilizado**, lança exceção.


3. Se o código for válido:
    - Marca o código como **validado**.
    - Ativa o usuário associado ao código.
    - Persiste as alterações no banco.
    - Publica um evento de conta ativada (`ActivatedAccountEvent`).

---

### ✔️ Login Use Case
```text
[HTTP Client]
     |
     v
[Controller /auth/login] --> Recebe email e senha
     |
     v
[LoginUseCaseImpl]
     |
     ├──> Busca usuário pelo e-mail [UserRepository]
     ├──> Verifica se conta está ativa
     ├──> Valida a senha [PasswordService]
     ├──> Gera JWT com claims [JwtService]
     └──> Retorna token e tempo de expiração
```

#### 🔄 Fluxo do processo de login
1. O caso de uso recebe os dados de entrada: **email** e **senha**.


2. Verifica se o usuário com o email informado existe:
    - Se **não existir**, lança exceção "User not found by e-mail".


3. Verifica se a conta do usuário está **ativa**:
    - Se **não estiver ativada**, lança exceção "User account not active yet".


4. Verifica se a senha informada corresponde à senha armazenada:
    - Se **inválida**, lança exceção "Invalid password".


5. Se todas as validações passarem:
    - Gera um token JWT com informações do usuário (`id`, `username`, `email`).
    - Define o tempo de expiração do token (ex: `900000ms` = 15 minutos).
    - Retorna o token e a expiração.

---
## 📝 Postagens

> A seguir estão descritos os fluxos principais relacionados às postagens, que envolvem criação, listagem, detalhamento e exclusão.

### ✍️ Create Post Use Case

````text
[HTTP Client]
     |
     v
[Controller /posts] --> Valida DTO
     |
     v
[CreatePostUseCase]
     |
     ├──> Busca autor autenticado [UserRepository]
     ├──> Cria domínio Post com título, conteúdo e tags
     ├──> Persiste Post [PostRepository]
     └──> Retorna dados resumidos do novo post

````

#### 🔄 Fluxo de criação de post

1. O controlador recebe o DTO com título, conteúdo e tags, imagem, junto ao usuário autenticado.
2. O caso de uso:
   - Verifica se o usuário existe e está ativo.
   - Cria a entidade de domínio Post via método fábrica (Post.create()).
   - Define status inicial como ativo.
   - Persiste no repositório.
3. Retorna ao cliente os dados do novo post (id, título, data, autor).

---

### 📄 Get Posts Use Case (com filtros e paginação)

````text
[HTTP Client]
     |
     v
[Controller /posts] --> Recebe filtros (title, tags, authorId, sortOrder)
     |
     v
[GetPostsUseCase]
     |
     ├──> Monta filtro de busca [PostQueryFilter]
     ├──> Executa busca paginada [PostRepository]
     ├──> Converte entidades em resumo [SummaryPostOutput]
     └──> Retorna DomainPage<SummaryPostOutput>

````

#### 🔄 Fluxo de listagem de posts
1. O cliente pode filtrar por título, autor ou tags e definir ordenação (mais recentes, antigos, etc.).
2. O caso de uso delega a busca ao repositório com os filtros e paginação.
3. O repositório retorna uma DomainPage contendo:
   - Lista de SummaryPostOutput (dados resumidos),
   - Número da página,
   - Total de páginas e registros.
4. O controlador retorna os dados no formato padronizado de paginação.

---

### 🔍 Get Post By ID Use Case

````text
[HTTP Client]
     |
     v
[Controller /posts/{id}]
     |
     v
[GetPostByIdUseCase]
     |
     ├──> Busca post completo [PostRepository]
     ├──> Retorna dados detalhados (autor, conteúdo, tags, contagem de replies)
     └──> Pode ser combinado com listagem paginada de replies
````

#### 🔄 Fluxo de obtenção de post por ID

1. O caso de uso busca o post completo pelo ID informado.
2. Retorna dados mais detalhados que a listagem:
   - Autor, conteúdo completo, tags, data, contagem de respostas, etc.
3. Obs: As respostas completas não são carregadas aqui, apenas a contagem.

---

###  ❌ Delete Post Use Case

````text
[HTTP Client]
     |
     v
[Controller /posts/{id}] --> DELETE
     |
     v
[DeletePostUseCase]
     |
     ├──> Busca post [PostRepository]
     ├──> Busca usuário autenticado [UserRepository]
     ├──> Verifica permissão (dono ou admin)
     ├──> Marca como deletado (soft delete)
     └──> Persiste alteração [PostRepository]
````

#### 🔄 Fluxo de exclusão de post
1. O caso de uso recebe o ID do post e o ID do usuário autenticado.
2. Verifica se o post já foi excluído.
3. Verifica se o usuário é o autor ou possui perfil ADMIN.
4. Marca o post como excluído e salva as alterações.
5. Não remove fisicamente o registro (soft delete).

---
## 💬 Respostas

> Os fluxos de respostas envolvem criação, listagem e exclusão de comentários (replies) associados a um post.

### 💬 Create Reply Use Case

````text
[HTTP Client]
     |
     v
[Controller /replies] --> Valida DTO
     |
     v
[CreateReplyUseCase]
     |
     ├──> Busca autor autenticado [UserRepository]
     ├──> Busca post associado [PostRepository]
     ├──> Cria domínio Reply com conteúdo, autor e post
     ├──> Persiste Reply [ReplyRepository]
     └──> Retorna dados do comentário criado
````

#### 🔄 Fluxo de criação de resposta

1. O cliente envia o conteúdo do comentário e o ID do post.
2. O caso de uso busca o autor e o post.
3. Cria o domínio Reply e salva no repositório.
4. Retorna os dados do comentário criado (autor, conteúdo, título do post).

---

### 📜 Get Replies By Post Use Case

````text
[HTTP Client]
     |
     v
[Controller /posts/{id}/replies] --> Parâmetros de paginação
     |
     v
[GetRepliesByPostUseCase]
     |
     ├──> Busca post [PostRepository]
     ├──> Busca replies associadas [ReplyRepository]
     ├──> Aplica paginação
     └──> Retorna DomainPage<ReplySummaryOutput>
````

#### 🔄 Fluxo de listagem de respostas de um post

1. O cliente informa o ID do post e parâmetros de página e tamanho.
2. O caso de uso busca apenas as respostas daquele post.
3. Retorna uma DomainPage contendo os comentários paginados:
   - Autor, conteúdo, data, etc.
4. Essa rota é usada para navegar entre respostas sem recarregar o post completo.

---

### ❌ Delete Reply Use Case

````text
[HTTP Client]
     |
     v
[Controller /replies/{id}] --> DELETE
     |
     v
[DeleteReplyUseCase]
     |
     ├──> Busca reply [ReplyRepository]
     ├──> Busca usuário autenticado [UserRepository]
     ├──> Verifica permissão:
     │       ├── Autor da reply
     │       ├── Autor do post original
     │       └── Usuário admin
     ├──> Marca reply como deletada (soft delete)
     └──> Persiste alteração [ReplyRepository]
````

#### 🔄 Fluxo de exclusão de resposta

1. O caso de uso recebe o ID da resposta e o usuário autenticado.
2. Verifica se a resposta já foi excluída.
3. Permite exclusão se o usuário for:
   - Autor da resposta, Autor do post original, ou Administrador.
4. Marca a resposta como deletada e salva.

---

#### 🔎 Observação geral sobre replies

- O GetPostById pode retornar apenas a contagem de respostas.
- O GetRepliesByPostUseCase é responsável pela listagem paginada das respostas.
- Essa separação evita consultas pesadas e mantém as operações desacopladas, respeitando os princípios da Clean Architecture.