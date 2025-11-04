# 📋 Requisitos

Este documento descreve os **requisitos funcionais e não funcionais** da aplicação, ou seja, **o que o sistema deve fazer** e **como ele deve se comportar tecnicamente**.

O objetivo é manter uma visão clara e objetiva sobre as responsabilidades da aplicação, servindo de referência durante o desenvolvimento, testes e evolução do projeto.

---

## ✅ Requisitos Funcionais

Os requisitos funcionais descrevem as funcionalidades essenciais que o sistema deve oferecer para atender às regras de negócio propostas.

- **RF01** – 👤 O sistema deve permitir que os usuários criem contas e façam login.
- **RF02** – 📧 O sistema deve enviar e-mail com código de confirmação no momento do registro.
- **RF03** – 🕓 O sistema deve conter entidades com campos úteis para auditoria (ex: data de criação, atualização, autor).
- **RF04** – 🌐 O sistema deve permitir a listagem de posts e comentários por qualquer usuário (acesso público).
- **RF05** – ✍️ O sistema deve permitir que usuários autenticados criem e excluam seus próprios posts e comentários.
- **RF06** – 🛡️ Apenas o autor do post ou um administrador pode excluí-lo.
- **RF07** – 🛡️ Apenas o autor do comentário, o autor do post associado ou um administrador pode excluí-lo.

---

## ⚙️ Requisitos Não Funcionais

Os requisitos não funcionais definem aspectos técnicos e restrições da aplicação. Eles garantem que o sistema seja estável, seguro, performático e de fácil manutenção.

- **RNF01** – 🔁 A API deve seguir o Padrão REST (não de forma "purista").
- **RNF02** – 🔐 A autenticação inicialmente deve ser baseada em JWT.
- **RNF03** – 🧱 O backend deve seguir princípios de Arquitetura Limpa.
- **RNF04** – 🗄️ O banco de dados utilizado será relacional (PostgreSQL).
- **RNF05** – 📦 As respostas da API devem seguir o formato JSON.
- **RNF06** – 🚧 A aplicação deve manter um padrão mínimo de segurança nas rotas privadas.

---

> 📌 **Nota:** Estes requisitos poderão evoluir com o tempo, acompanhando as mudanças na arquitetura, tecnologias utilizadas e escopo da aplicação.
