# 📝 Blog API - Backend

> Este projeto foi criado como um estudo aprofundado de arquitetura backend em Java, inspirado nos princípios de Robert C. Martin (Uncle Bob) e da Clean Architecture.
O objetivo é compreender, na prática, como projetar aplicações independentes de frameworks, com regras de negócio isoladas, alta testabilidade e baixo acoplamento entre camadas.
O código foi desenvolvido com foco em clareza arquitetural, aplicando conceitos de injeção de dependência, design orientado a domínio (DDD) e separação rigorosa de responsabilidades dentro do ecossistema Spring Boot.

---

## 🎯 Objetivo da Aplicação

A Blog API é uma aplicação monolítica REST desenvolvida para simular um ambiente real de backend com usuários, postagens e comentários.
Apesar do domínio simples, o projeto foi construído com foco em explorar conceitos arquiteturais avançados e boas práticas de engenharia de software, incluindo:

    - Aplicação dos princípios da Arquitetura Limpa, separando o core de domínio das implementações técnicas.

    - Estrutura modular com camadas bem definidas: core, infra e interface (controllers).

    - Uso de gateways para abstrair dependências e permitir fácil substituição de frameworks.

    - Integração com PostgreSQL, JWT para autenticação, e envio de e-mails.

    - Suporte a testes automatizados com JUnit e Mockito. 

O sistema serve como um laboratório prático de arquitetura backend, ideal para consolidar o entendimento de como construir aplicações robustas, coesas e evolutivas em Java.

---

## 🛠️ Tecnologias Utilizadas

![Java](https://img.shields.io/badge/Java-ED8B00?logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?logo=java&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-45C4B0?logo=testng&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?logo=jsonwebtokens&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?logo=postgresql&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker_Compose-384D54?logo=docker&logoColor=white)
![Email Notification](https://img.shields.io/badge/Email_Notification-0078D4?logo=gmail&logoColor=white)
![Swagger UI](https://img.shields.io/badge/Swagger_UI-85EA2D?logo=swagger&logoColor=black)


![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?logo=springboot&logoColor=white)
![Spring WEB](https://img.shields.io/badge/Spring_Web-6DB33F?logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?logo=spring&logoColor=white)
![Spring Mail](https://img.shields.io/badge/Spring_Mail-6DB33F?logo=minutemailer&logoColor=white)
![Spring Test](https://img.shields.io/badge/Spring_Test-6DB33F?logo=spring&logoColor=white)


---

## 📚 Sumário da Documentação 

### 📦 Base
> O documento de requisitos detalha as funcionalidades essenciais (requisitos funcionais) e os aspectos técnicos e restrições (requisitos não funcionais) que a aplicação deve atender, servindo como referência para desenvolvimento, testes e evolução do sistema.

- [ 📋 Requisitos](docs/documentation/requisitos.md)

### 🧠 Qualidade da Modelagem
> Um dos principais diferenciais deste estudo está na profundidade da modelagem de dados.
O domínio foi planejado em múltiplos níveis — **conceitual, lógico e físico** — com diagramas, DER e dicionário de dados completo, garantindo consistência entre o modelo do domínio e o schema relacional.

- [ 🧩 Modelagem de Dados](docs/documentation/modelagem-dados.md)

###  🏛️ Arquitetura da Aplicação
> Esta seção apresenta a organização da aplicação seguindo os princípios da Clean Architecture, garantindo que o core de domínio e os casos de uso permaneçam independentes de frameworks, bancos de dados e implementações externas. A documentação mostra como a aplicação foi estruturada em camadas concêntricas, com gateways abstraindo dependências, permitindo que a lógica de negócio seja testável isoladamente e que a infraestrutura possa ser modificada sem impactar o núcleo.
- [ 🏗️ Arquitetura da Aplicação](docs/documentation/arquitetura-aplicacao.md)

### ⚙️ Fluxos da Aplicação
> Esta seção descreve os principais fluxos da aplicação, mostrando como os dados percorrem o sistema desde a entrada via HTTP até a persistência, seguindo os princípios da Clean Architecture. Todos os fluxos respeitam a separação de responsabilidades, com validação de DTOs nos controllers, delegação para casos de uso no core e comunicação com repositórios ou serviços externos por meio de gateways.
- [ 🔄 Fluxo das Requisições](docs/documentation/fluxo-requisicoes.md)

### 🧪 Testes
> A seção de testes apresenta apenas os casos de uso detalhando regras de negócio, cenários de teste e exceções esperadas.
- [ 🧪 Testes ](docs/documentation/testes.md)

---

