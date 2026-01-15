# 🚀 Como Executar a Aplicação

Este guia descreve os passos para preparar e executar a aplicação localmente, usando **Docker Compose**, **LocalStack** e **Terraform**.

---

## ⚙️ Pré-requisitos

Antes de iniciar, verifique se você possui:

- [Docker](https://www.docker.com/) e Docker Compose instalados
- [AWS CLI](https://aws.amazon.com/cli/) instalado
- [Terraform](https://developer.hashicorp.com/terraform) instalado

---

## 🐳 Passo 1: Subir containers com Docker Compose

- Na raiz da aplicação execute:

```bash
docker-compose build
```

- Isso vai gerar as imagens necessárias, em seguida suba os containers para rodar a aplicação:

```bash
docker-compose up
```

## 🌱 Passo 2: Provisionar infraestrutura com Terraform

- Navegue até a pasta ``infra/terraform``:

- Inicialize o Terraform:

```bash
terraform init
```

- Aplique os recursos necessários (S3, SNS, SES, etc.):

```bash
terraform apply
```

> Confirme digitando `yes`

---

## ☁️ Passo 3: Configurar AWS CLI para LocalStack

- Crie um profile de teste no AWS CLI:

```bash
aws configure --profile localstack
```

    - Defina qualquer chave/secret (LocalStack aceita qualquer valor)

    - Região: us-east-1

    - Formato padrão: json

- Verifique se os recursos foram criados:

```bash
aws --endpoint-url=http://localhost:4566 s3 ls --profile localstack
aws --endpoint-url=http://localhost:4566 sns list-topics --profile localstack
aws --endpoint-url=http://localhost:4566 ses list-identities --profile localstack
```

> ⚠️ O SES não cria identidades automaticamente. É necessário adicionar manualmente:

```bash
aws --endpoint-url=http://localhost:4566 ses verify-email-identity \
    --email-address no-reply@localstack.local \
    --profile localstack
```

- Depois, confirme novamente:

```bash
aws --endpoint-url=http://localhost:4566 ses list-identities --profile localstack
```

---

## ☕ Passo 4: Acessar ``http://localhost:8080/api/swagger-ui.html`` e testar os endpoints

---

## 📬 Passo 5: Testar envio de e-mails

- Cada e-mail enviado pelo SES local é armazenado dentro do container LocalStack:

```bash
docker exec -it localstack-simple-blog-app ls /tmp/localstack/state/ses/
```

- Para visualizar o conteúdo de um e-mail:

```bash
docker exec -it localstack-simple-blog-app cat /tmp/localstack/state/ses/<nome_do_arquivo>.json
```

---

## 💾 Passo 6: Verificar arquivos no S3

- Listar objetos dentro do bucket:

```bash
aws --endpoint-url=http://localhost:4566 s3 ls s3://simple-blog-email-bucket --profile localstack
```

> Qualquer arquivo enviado ou gerado pela aplicação estará disponível nesse bucket.

---

## ⚠️ Observações

- No ambiente produção, basta remover o endpoint do LocalStack e usar chaves reais da AWS.

- Toda a configuração do Terraform pode ser reaproveitada para a AWS real.

- O LocalStack deve estar 100% iniciado antes de rodar o Terraform, caso contrário os recursos podem não ser criados.