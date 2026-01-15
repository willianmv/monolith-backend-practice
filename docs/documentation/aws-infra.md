# ☁️ AWS e Infraestrutura

Esta seção detalha a configuração e uso dos recursos AWS locais e reais usados pela aplicação, como S3, SNS e SES, além do uso do LocalStack para ambiente de desenvolvimento e testes locais.

## Infraestrutura com Terraform

- A infraestrutura AWS é provisionada usando Terraform para garantir consistência e versionamento dos recursos.
- Os principais recursos criados são:
  - Bucket S3 (`simple-blog-email-bucket`) para armazenar dados de e-mail e arquivos.
  - Tópico SNS (`simple-blog-email-topic`) para notificações de eventos.
  - Identidade SES (`no-reply@localstack.local`) para envio de e-mails.
  
## LocalStack para Desenvolvimento Local

- O LocalStack é usado para simular os serviços AWS (S3, SNS, SES) localmente.
- Permite testes da aplicação sem a necessidade de acessar a AWS real.
- Configurações importantes:
  - Endpoint customizado apontando para `http://localstack:4566`.
  - Variáveis de ambiente AWS configuradas com chaves `test` para acesso simulado.
- Para validar recursos no LocalStack, use AWS CLI com o parâmetro `--endpoint-url=http://localhost:4566` e o profile configurado.

## AWS Real (Produção)

- No ambiente de produção, a aplicação se conecta diretamente aos serviços AWS reais usando as credenciais configuradas via variáveis de ambiente.
- A configuração no código permite alternar facilmente entre LocalStack e AWS real, mantendo o endpoint configurável e reutilizando as mesmas propriedades de credenciais e região.

---
