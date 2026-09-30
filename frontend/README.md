# Frontend BoloDeLaMadre

Aplicação administrativa em Vue 3, Vite e Vue Router. A interface consome a API definida por `VITE_API_URL`, com padrão em `https://bolodelamadre.onrender.com`.

## Executar

```sh
npm install
npm run dev
```

Para gerar a versão de produção: `npm run build`. O diretório publicado é `dist/`. Em desenvolvimento, o Vite encaminha `/api` ao Render como proxy de mesma origem para evitar o preflight CORS no navegador. Em produção, configure `VITE_API_URL` para a URL pública da API; o host de publicação do frontend também precisa estar liberado no CORS do backend. O arquivo `.env.example` mostra essa configuração; não coloque chaves privadas ou credenciais de banco no frontend.

## Autenticação e limites da API atual

O código Spring disponível configura HTTP Basic e form login. Não há endpoint de login que gere JWT nem endpoint que informe o usuário autenticado. Por isso, o frontend valida as credenciais usando a requisição somente leitura `GET /api/produtos` (200 indica autenticação aceita, 401 indica credenciais inválidas) e mantém o cabeçalho Basic em `sessionStorage` até fechar a aba ou sair. `/api/usuarios` possui apenas POST; usá-lo com GET provoca erro no backend.

Como a API não oferece endpoint de perfil, o login `admin` é tratado como ADMIN; outros logins ficam no ambiente de menor privilégio USER. Esse fallback atende às contas de demonstração, mas uma API real deve expor um endpoint autenticado de perfil para classificar usuários sem inferência pelo nome.

Não foi encontrada configuração CORS nos fontes do backend. Se o frontend for servido em outro domínio, o navegador pode bloquear as chamadas antes que a API responda. O frontend mostra essa situação como falha de conexão; a correção CORS precisa acontecer no backend, fora desta pasta.

## Módulos e endpoints mapeados

| Módulo | Endpoints reais utilizados | Acesso observado nos controllers |
| --- | --- | --- |
| Painel | `GET /api/v1/relatorios/kpis`, `/insights`, `/api/vendas`; resumo financeiro quando ADMIN | KPI, insights e vendas: ADMIN e USER; financeiro: ADMIN |
| Produtos | `GET/POST/PUT/DELETE /api/produtos` | Consulta: ambos; escrita e exclusão: ADMIN |
| Vendas | `GET/POST /api/vendas` | Consulta/criação: ambos; edição/cancelamento/detalhe: ADMIN |
| Compras | `GET/POST /api/compras` | Lista/criação: ambos; detalhe por ID: ADMIN |
| Clientes | `GET/POST/PUT/DELETE /api/clientes` | Lista/criação/edição: ambos; exclusão/detalhe: ADMIN |
| Fornecedores | `GET/POST/PUT/DELETE /api/fornecedores` | Lista/criação: ambos; edição/exclusão/detalhe: ADMIN |
| Categorias | `GET/POST/PUT/DELETE /api/categorias` | Lista/criação: ambos; edição/exclusão/detalhe: ADMIN |
| Ingredientes | `GET/POST/PUT/DELETE /api/ingredientes` | Lista/criação: ambos; edição/exclusão/detalhe: ADMIN; criação/edição usa query params |
| Receitas | `GET/POST/PUT/DELETE /api/receitas` | Lista/criação: ambos; edição/exclusão/detalhe: ADMIN; escrita usa query params |
| Estoque | `GET/POST /api/movimentacoes-estoque` | Ambos |
| Financeiro | `GET /api/lancamentos-financeiros`, `/resumo`; `POST` | ADMIN |
| Funcionários | `GET/POST/PUT/DELETE /api/funcionarios` | ADMIN |
| Usuários | `POST /api/usuarios` | ADMIN; a API não oferece endpoint GET/listagem |
| Assistente IA | `/api/ai-conversas` e `/api/ia/mensagens` | Conversas e mensagens: ambos; exclusão de conversa: ADMIN |

Os DTOs existentes são respeitados nos cadastros de produto, categoria, venda, compra e financeiro. A API devolve respostas inconsistentes entre módulos (DTOs e entidades JPA), portanto as tabelas exibem os campos presentes na resposta e tratam valores ausentes sem criar dados fictícios. Busca, paginação e CSV são locais: o backend não expõe parâmetros de paginação/filtro nesses controllers.

## Segurança e telas

- Login é a página inicial; todas as rotas internas passam pelo guard.
- O menu administrativo só aparece para ADMIN; rotas administrativas redirecionam USER para 403.
- A autorização visual é apenas conveniência: o Spring Security continua sendo a autoridade real.
- O chat da IA é carregado apenas dentro da área autenticada e usa os endpoints de conversas e mensagens existentes.
- A tela informa o cold start do plano gratuito do Render e que os dados são uma simulação.
- A versão desktop usa sidebar; em telas menores, a navegação vira menu lateral recolhível e as tabelas permanecem roláveis.

## Mapa de rotas

| Rota | Tela | Acesso |
| --- | --- | --- |
| `/login` | Autenticação e informações de demonstração | Pública |
| `/dashboard` | Painel administrativo ou operacional conforme perfil | Autenticado |
| `/produtos`, `/vendas`, `/compras`, `/clientes`, `/fornecedores`, `/categorias`, `/ingredientes`, `/receitas`, `/estoque` | Módulos operacionais | Autenticado |
| `/admin/financeiro`, `/admin/funcionarios`, `/admin/usuarios` | Administração | ADMIN |
| `/403` | Acesso negado | Autenticado |
| demais caminhos | 404 | Público |

## Pontos que dependem de evolução do backend

- Implementar endpoint de autenticação JWT e endpoint autenticado `/me` (ou equivalente) para retornar role/perfil.
- Configurar CORS para a origem onde o frontend será publicado.
- Criar endpoint GET/listagem de usuários; atualmente só existe POST.
- Para permitir edição segura de produto pela interface, ampliar o DTO de resposta: o `ProdutoResponseDTO` atual não inclui descrição, categoria ou estado ativo, campos que o DTO de escrita recebe.
- Não há endpoints de série temporal para vendas, então o gráfico compara apenas a receita mensal atual e anterior, exatamente conforme o KPI retornado pela API.
- O controller de funcionários serializa a entidade diretamente, que possui o campo `senha`; o serviço também grava essa senha sem hash. A tela remove esse campo dos dados exibidos, mas apenas uma correção no backend pode impedir que a API o envie e persista em texto puro. Por isso, a interface não oferece alteração de senha de funcionário.
- A rota de vendas só permite editar/cancelar para ADMIN; o frontend integra listagem e criação, mas não implementa edição/cancelamento nesta versão.
