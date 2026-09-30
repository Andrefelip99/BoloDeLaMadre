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
* OpenAI Responses API (modelo padrão: gpt-4.1-mini)
* Maven
* Lombok
* Testes de integração e unitarios

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

### Assistente de gestão com IA

O assistente usa a OpenAI Responses API para interpretar perguntas em português e responder com base em informações consultadas pelo backend. As conversas e as mensagens do usuário e do assistente ficam registradas no banco de dados da aplicação.

Ele pode ajudar com:

* Consultas e resumos de vendas por mês, comparando com o mês anterior.
* Consulta de ingredientes ativos e alerta de estoque abaixo do mínimo.
* Estimativa de ingredientes para produzir uma quantidade informada de um produto com receita cadastrada.
* Sugestões de produtos com base nos itens vendidos no período.

As estimativas usam as quantidades e unidades da receita, sem conversão de unidades ou cálculo de perdas. Sugestões baseadas em vendas não consideram margem de lucro, capacidade de produção ou demanda futura. Quando uma venda não tem total preenchido, o resumo usa a soma dos subtotais dos itens e não inclui descontos nem taxas.

O modelo não pode alterar vendas, produtos ou estoque. O backend consulta os dados necessários e envia à OpenAI apenas a pergunta, até 12 mensagens recentes da conversa e o contexto relacionado. A chamada usa `store: false`; as conversas continuam salvas no banco da aplicação.

#### Configurar a chave da OpenAI

Defina a chave no ambiente em que o backend será iniciado. No PowerShell:

```powershell
$env:OPENAI_API_KEY = "sua-chave"
$env:OPENAI_MODEL = "gpt-4.1-mini"
mvn spring-boot:run
```

`OPENAI_MODEL` é opcional; o padrão é `gpt-4.1-mini`. Não salve a chave no código nem no Git. Sem `OPENAI_API_KEY`, a aplicação inicia normalmente, mas o envio de mensagens do assistente retorna HTTP 503 informando que falta configuração.

#### Usar o assistente

Os endpoints exigem autenticação pelo Spring Security configurado na aplicação.

1. Crie uma conversa com `POST /api/ai-conversas?titulo=Resumo%20do%20negocio` e guarde o `id` retornado.
2. Envie uma pergunta para `POST /api/ia/mensagens`:

```json
{
  "conversaId": "UUID-DA-CONVERSA",
  "content": "Como foi o desempenho em 2025-04?"
}
```

3. Consulte o histórico em `GET /api/ia/mensagens/conversa/{conversaId}`.

Mais exemplos e limites estão em [docs/assistente-ia.md](docs/assistente-ia.md).

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

* Java 21
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
