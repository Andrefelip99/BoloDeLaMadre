# 🍰 BoloDeLaMadre - Sistema de Gestão Empresarial

## 📋 Sobre o Projeto

O BoloDeLaMadre é um sistema Back-End desenvolvido em Java com Spring Boot para gerenciamento operacional e financeiro de uma empresa do ramo de confeitaria.

O projeto foi criado como uma evolução de versões anteriores, incorporando recursos mais avançados de segurança, organização da aplicação e boas práticas de desenvolvimento utilizadas no mercado.

A aplicação permite o gerenciamento de produtos, vendas, compras, despesas, fluxo de caixa e usuários, oferecendo diferentes níveis de acesso de acordo com o perfil de cada colaborador.

---

## 🚀 Tecnologias Utilizadas

* Java
* Spring Boot
* Spring Security
* JWT (JSON Web Token)
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Lombok

---

## 🏗️ Arquitetura

```text
Controller
    ↓
DTO
    ↓
Service
    ↓
Repository
    ↓
Model
```

O sistema foi desenvolvido seguindo o padrão de arquitetura em camadas, promovendo separação de responsabilidades, escalabilidade e facilidade de manutenção.

---

## 🔐 Segurança e Autenticação

A aplicação utiliza Spring Security integrado com JWT para controle de acesso e autenticação dos usuários.

### Funcionalidades de Segurança

* Login autenticado
* Geração de Token JWT
* Controle de permissões
* Proteção de endpoints
* Autorização baseada em perfis

---

## 👥 Controle de Acesso

### Administrador

Possui acesso completo ao sistema:

* Cadastro de usuários
* Alteração de usuários
* Exclusão de registros
* Controle financeiro
* Gerenciamento de produtos
* Gerenciamento de vendas
* Gerenciamento de compras
* Relatórios gerenciais

### Colaboradores

Possuem acesso restrito às funcionalidades operacionais.

* Consulta de informações
* Cadastro de registros permitidos
* Atualização de dados específicos

---

## 📦 Principais Módulos

### Produtos

Gerenciamento dos produtos comercializados pela empresa.

### Compras

Controle de compras realizadas.

### Vendas

Registro e gerenciamento das vendas.

### Fluxo de Caixa

Controle financeiro de entradas e saídas.

### Despesas

Gerenciamento de gastos operacionais.

### Custos Variáveis

Controle dos custos relacionados à produção.

### Investimentos

Registro de despesas voltadas para crescimento do negócio.

### Lucro Líquido

Monitoramento dos resultados financeiros.

### Resumo Mensal

Consolidação dos indicadores financeiros.

### Usuários

Gerenciamento dos usuários do sistema e seus níveis de acesso.

---

## 📚 Conceitos Aplicados

### Back-End

* API REST
* Programação Orientada a Objetos
* Arquitetura em Camadas
* Injeção de Dependência

### Segurança

* Spring Security
* JWT Authentication
* Controle de Acesso
* Proteção de Rotas

### Persistência

* PostgreSQL
* JPA
* Hibernate

### Organização

* DTO Pattern
* Tratamento Global de Exceções
* Separação de Responsabilidades
* Boas Práticas de Desenvolvimento

---

## ⚠️ Tratamento de Exceções

O projeto possui tratamento centralizado de exceções para fornecer respostas padronizadas e melhorar a experiência de consumo da API.

Exemplos:

* Recurso não encontrado
* Dados inválidos
* Erros de autenticação
* Erros de autorização
* Regras de negócio

---

## ▶️ Como Executar

### Pré-requisitos

* Java 17+
* Maven
* PostgreSQL

### Clonar o Projeto

```bash
git clone https://github.com/Andrefelip99/BoloDeLaMadre
```

### Configurar Banco de Dados

Ajustar as configurações do PostgreSQL no arquivo:

```properties
application.properties
```

### Executar

```bash
mvn spring-boot:run
```

---

## 🎯 Objetivo do Projeto

Desenvolver uma solução corporativa mais completa e segura para gestão empresarial, aplicando tecnologias modernas do ecossistema Spring e boas práticas utilizadas em aplicações profissionais.

O projeto representa uma evolução técnica em relação aos sistemas anteriores, incorporando autenticação JWT, Spring Security, DTOs e tratamento global de exceções.

---

## 🚀 Melhorias Futuras

* Swagger/OpenAPI
* Docker
* Testes Unitários
* Testes de Integração
* Relatórios em PDF
* Dashboard Administrativo
* Monitoramento com Spring Actuator
* Deploy em ambiente cloud

---

## 👨‍💻 Autor

André Felipe da Silva Leal

Estudante de Análise e Desenvolvimento de Sistemas, desenvolvedor Back-End com foco em Java e Spring Boot, buscando construir aplicações escaláveis, seguras e alinhadas às práticas do mercado.
