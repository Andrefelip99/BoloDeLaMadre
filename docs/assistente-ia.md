# Assistente de gestão com IA

O assistente usa a OpenAI Responses API com `gpt-4.1-mini` por padrão. Antes de enviar cada pergunta, o backend consulta os repositórios do BoloDeLaMadre e prepara um contexto curto com os fatos pertinentes. O modelo transforma esses fatos em uma resposta em português; ele não recebe ferramentas para gravar vendas, alterar estoque ou executar outras ações.

## Configuração

Defina a chave da API no ambiente em que o backend será executado. Opcionalmente, escolha outro modelo compatível com a Responses API:

```powershell
$env:OPENAI_API_KEY = "sua-chave"
$env:OPENAI_MODEL = "gpt-4.1-mini"
mvn spring-boot:run
```

No Bash, use `export OPENAI_API_KEY="sua-chave"` e `export OPENAI_MODEL="gpt-4.1-mini"` antes de iniciar a aplicação.

Não coloque a chave em `application.properties`, no código ou no Git. Sem a chave, a aplicação inicia normalmente e o envio de mensagens do assistente responde HTTP 503 com uma mensagem de configuração ausente.

## Criar uma conversa e enviar pergunta

Os endpoints continuam protegidos pelo Spring Security existente.

1. Crie a conversa: `POST /api/ai-conversas?titulo=Resumo%20do%20negocio`.
2. Use o `id` retornado no envio: `POST /api/ia/mensagens`.

```json
{
  "conversaId": "UUID-DA-CONVERSA",
  "content": "Como foi o desempenho em 2025-04?"
}
```

A resposta da criação da mensagem é a fala do assistente. A pergunta e a resposta também são persistidas e aparecem em `GET /api/ia/mensagens/conversa/{conversaId}`. São enviadas ao modelo no máximo as 12 mensagens mais recentes da conversa.

## Perguntas que recebem dados do sistema

- Vendas e resumo mensal: pergunte, por exemplo, “resuma as vendas em 2025-04” ou “qual foi o desempenho do mês passado?”. Vendas canceladas são excluídas.
- Estoque: pergunte “quais ingredientes estão abaixo do mínimo?” ou “como está o estoque?”.
- Estimativa de produção: informe quantidade e nome exato do produto, por exemplo “estimar ingredientes para 12 unidades de Bolo de Cenoura”. O produto precisa estar ativo e ter receita cadastrada.
- Sugestões: pergunte “quais produtos devo destacar?”. A lista considera os itens vendidos no mês atual; informe `AAAA-MM` ou `MM/AAAA` para selecionar outro mês.

Estimativas multiplicam as quantidades registradas na receita. Não convertem unidades, calculam perdas de produção ou garantem disponibilidade. Sugestões usam o histórico de vendas e não conhecem margem, capacidade produtiva nem demanda futura. No resumo mensal, quando o total da venda não foi preenchido, o contexto usa a soma dos subtotais dos itens e avisa que esse valor não inclui descontos e taxas.

O backend envia a pergunta, o histórico recente e o contexto necessário à OpenAI para gerar a resposta. O pedido à Responses API inclui `store: false`. As conversas continuam armazenadas no banco de dados da aplicação.
