## 👩🏽‍❤️‍👨🏿 US-Two Gether

**US-Two Gether** é um aplicativo mobile voltado para casais (ou famílias) que desejam organizar sua rotina de forma colaborativa, com **cadastro de tarefas, lembretes e anotações compartilhadas**.

Este repositório representa o backend do projeto, desenvolvido com foco em **escalabilidade, arquitetura de microserviços, comunicação assíncrona e boas práticas de desenvolvimento moderno.**

---

## 🧠 Objetivo do Projeto

Criar um sistema simples e funcional que ajude casais a:

- Gerenciar tarefas do dia a dia
- Compartilhar lembretes com notificações
- Salvar e acessar anotações em tempo real
- Manter a rotina organizada de forma conjunta

---

## 🛠️ Tecnologias Utilizadas

| Camada            | Linguagem | Descrição                                         |
|-------------------|-----------|---------------------------------------------------|
| API Gateway       | Nginx     | Roteamento e segurança básica                     |
| Load Balancer     | Nginx     | Balanceamento de carga entre serviços             |
| Microserviço 1    | Java 17   | CRUD de tarefas e anotações (banco relacional)    |
| Mensageria        | RabbitMQ  | Comunicação assíncrona entre os serviços          |
| Microserviço 2    | Go        | Envio de notificações (push/email)                |
| Bancos de Dados   | PostgreSQL (Java) / MongoDB (Go) | Persistência orientada ao contexto de cada serviço |

---

## ⚙️ Arquitetura do Sistema

```plaintext
                     ┌──────────────┐
                     │   Frontend   │ (mobile app)
                     └─────┬────────┘
                           ↓
                     ┌───────────────┐
                     │  API Gateway  │
                     └──────┬────────┘
                            ↓
                     ┌───────────────┐
                     │ Load Balancer │
                     └──────┬────────┘
                 ┌──────────┴──────────┐
                 ↓                     ↓
        ┌─────────────────┐   ┌─────────────────────┐
        │Java Microservice│   │   Go Notification   │
        │(Tarefas e CRUD) │   │   (Notificações)    │
        └────────┬────────┘   └──────────┬──────────┘
                 ↓                       ↑
             PostgreSQL         RabbitMQ ⇄ MongoDB
```

## ✨ Diferenciais Técnicos

- Arquitetura orientada a **microserviços**
- Comunicação assíncrona com **RabbitMQ**
- Uso de **duas linguagens (Java e Go)** com propósitos distintos
- Separação clara de responsabilidades e persistência independente
- Preparação futura para **Kubernetes** e deployment em cluster

---

## 🔁 Organização do Projeto

Este projeto está estruturado em um modelo **multi-repositório**, garantindo melhor separação de responsabilidades e manutenção a longo prazo:

| Repositório               | Responsável por                                |
|---------------------------|------------------------------------------------|
| `us-two-gether-backend`   | Backend com microserviços Java e Go            |
| `us-two-gether-frontend`  | Aplicativo mobile desenvolvido em Flutter      |
| `us-two-gether-infra`     | Infraestrutura: Docker, CI/CD, Kubernetes etc. |

---

## 🚧 Status Atual - Backend

- ✅ Levantamento de Requísitos e Documentações no JIRA
- ✅ Criação de Issues no JIRA
- ✅ API de CRUD de tarefas em Java com PostgreSQL (Inicialmente em MSSQL)
- ✅ Implementação de autenticação (JWT)
- ✅ Automação de teste unitários e integrados
- ⬜ Documentação dos testes
- ⬜ Documentação da API
- ⬜ Estrutura de microserviços inicial
- ⬜ Integração com RabbitMQ
- ⬜ Serviço de notificações iniciado em Go
- ⬜ Implementação de autenticação OAuth2
- ⬜ Deploy em ambiente cloud com Kubernetes

---

## 📦 Como rodar localmente (em breve)

Será incluído um ambiente de desenvolvimento via Docker Compose para facilitar a execução local dos serviços.

---

## 🙋‍♂️ Sobre o autor

Este projeto faz parte de uma **transição de carreira para backend moderno**, e está sendo desenvolvido com foco em **aprendizado prático** e aplicação de **boas práticas de engenharia de software**.
