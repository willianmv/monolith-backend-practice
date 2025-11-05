# 🧪 Testes

> Todos os testes foram implementados utilizando `JUnit` com `Mockito`.  
> A arquitetura limpa facilitou testar os **use cases** isoladamente, simulando repositórios e serviços externos.

## 📚 Sumário de Casos de Uso Testados

### 🔐 Auth
- **Registro de Usuário**: valida unicidade de e-mail e username, codifica senha, gera e envia código de ativação.
- **Ativação de Conta**: verifica validade e expiração do código, marca o usuário como ativo e publica evento.
- **Login**: autentica usuário ativo, valida senha e gera token JWT.

### 📄 Post
- **Criar/Deletar Post**: apenas o autor ou admin podem deletar; posts são marcados como deletados sem remoção física.
- **Obter Detalhes do Post**: inclui autor, conteúdo, tags e contagem de replies.
- **Listar Posts Resumidos**: suporta filtros por título, tags, autor e ordenação (novo/antigo), com paginação.

### 💭 Reply
- **Criar Reply**: adiciona reply ao post e publica evento notificando autor do post.
- **Deletar Reply**: apenas autor do reply, autor do post ou admin podem deletar; replies são marcadas como deletadas.
- **Listar Replies**: retorna replies de um post com paginação e informações básicas do autor.

---

## 🔑 Benefícios da Arquitetura

- **Isolamento de Use Cases**: permite testar a lógica de negócio sem depender de banco ou serviços externos.
- **Flexibilidade nos Testes**: repositórios e serviços são facilmente simulados com mocks.
- **Manutenção Facilitada**: alterações na camada de infraestrutura não impactam os testes de negócio.
- **Segurança e Integridade**: regras de autorização e marcação de deletados são testadas de forma consistente.

---
