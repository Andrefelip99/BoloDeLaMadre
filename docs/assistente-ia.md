# Assistente de gestão por opções

O assistente apresenta opções fixas e devolve respostas automáticas calculadas pelo backend. Não usa um modelo de IA externo, não precisa de chave de API e não envia os dados do negócio a um provedor de modelos. Conversas e mensagens permanecem salvas no banco de dados.

## Opções disponíveis

- **Resumo das vendas do mês:** total, quantidade de vendas e comparação com o mês anterior. Vendas canceladas são excluídas. Se uma venda não tiver total, o resumo soma os subtotais dos itens sem considerar descontos e taxas.
- **Produtos mais vendidos:** ranking dos itens vendidos no mês atual.
- **Estoque de ingredientes:** quantidades e mínimos cadastrados.
- **Ingredientes abaixo do mínimo:** lista os ingredientes ativos que precisam de reposição.
- **Sugerir produtos:** mostra produtos com base no histórico de vendas. O ranking não considera margem, capacidade produtiva nem demanda futura.
- **Estimar ingredientes:** escolha um produto ativo e a quantidade. A estimativa multiplica as quantidades registradas na receita; não converte unidades nem calcula perdas.

## Conversas e mensagens

As rotas continuam protegidas pelo Spring Security:

1. Crie uma conversa com `POST /api/ai-conversas?titulo=Resumo%20do%20negocio`.
2. Envie a opção selecionada para `POST /api/ia/mensagens` usando o `conversaId` retornado.
3. Consulte o histórico em `GET /api/ia/mensagens/conversa/{conversaId}`.

Para estimar produção, o painel solicita produto e quantidade e envia essa seleção como uma mensagem da conversa.
