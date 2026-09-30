package com.example.BoloDeLaMadre.services.iaService;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BoloDeLaMadre.entities.Ingrediente;
import com.example.BoloDeLaMadre.entities.Produto;
import com.example.BoloDeLaMadre.entities.Receita;
import com.example.BoloDeLaMadre.entities.enums.StatusVenda;
import com.example.BoloDeLaMadre.entities.vendas.Venda;
import com.example.BoloDeLaMadre.repositories.IngredienteRepository;
import com.example.BoloDeLaMadre.repositories.ProdutoRepository;
import com.example.BoloDeLaMadre.repositories.ReceitaRepository;
import com.example.BoloDeLaMadre.repositories.vendasRepository.VendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessContextService {
    private static final Pattern YEAR_MONTH = Pattern.compile("(?<!\\d)(\\d{4})[-/](0?[1-9]|1[0-2])(?!\\d)");
    private static final Pattern MONTH_YEAR = Pattern.compile("(?<!\\d)(0?[1-9]|1[0-2])/(\\d{4})(?!\\d)");
    private static final Pattern QUANTITY = Pattern.compile("(?<!\\d)(\\d+(?:[.,]\\d+)?)\\s*(?:unidades?|unid\\.?|bolos?|tortas?)?", Pattern.CASE_INSENSITIVE);

    private final VendaRepository vendaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ReceitaRepository receitaRepository;

    @Transactional(readOnly = true)
    public String buildContext(String question) {
        String normalized = normalize(question);
        if (containsAny(normalized, "producao", "produzir", "fabric", "receita", "estimar", "calcular ingrediente")) {
            return productionContext(question, normalized);
        }
        if (containsAny(normalized, "estoque", "ingrediente", "repor", "abaixo", "minimo", "comprar", "falta")) {
            return inventoryContext();
        }
        if (containsAny(normalized, "sugest", "recomend", "mais vendido", "popular", "vender")) {
            return bestSellersContext(selectPeriod(question, normalized));
        }
        return monthlySalesContext(selectPeriod(question, normalized));
    }

    @SuppressWarnings("null")
    private String monthlySalesContext(YearMonth period) {
        List<Venda> sales = nonCancelledSales(period);
        BigDecimal revenue = sales.stream().map(this::recordedSaleValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal average = sales.isEmpty() ? BigDecimal.ZERO
                : revenue.divide(BigDecimal.valueOf(sales.size()), 2, java.math.RoundingMode.HALF_UP);
        YearMonth previousPeriod = period.minusMonths(1);
        BigDecimal previousRevenue = nonCancelledSales(previousPeriod).stream()
                .map(this::recordedSaleValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        String comparison = previousRevenue.signum() == 0 ? "Sem receita registrada no período anterior para comparar."
                : "Variação contra o mês anterior: " + percentageChange(revenue, previousRevenue) + "% ("
                        + previousPeriod.format(DateTimeFormatter.ofPattern("MM/yyyy")) + ": R$ "
                        + previousRevenue.toPlainString() + ").";
        return "Resumo de vendas para " + period.format(DateTimeFormatter.ofPattern("MM/yyyy")) + ": "
                + sales.size() + " venda(s) não cancelada(s); valor registrado/estimado R$ " + revenue.toPlainString()
                + "; média por venda R$ " + average.toPlainString() + ". " + comparison
                + " A receita usa o total da venda quando preenchido; quando não há total, soma os subtotais dos itens. "
                + "Essa soma dos itens não considera descontos nem taxas. Vendas canceladas são excluídas.";
    }

    private String inventoryContext() {
        @SuppressWarnings("null")
        List<Ingrediente> ingredients = ingredienteRepository.findAll().stream()
                .filter(item -> Boolean.TRUE.equals(item.getAtivo()))
                .sorted(Comparator.comparing(Ingrediente::getNome, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .limit(100)
                .toList();
        if (ingredients.isEmpty()) {
            return "Não há ingredientes ativos cadastrados no estoque.";
        }
        String rows = ingredients.stream().map(item -> String.format(Locale.ROOT,
                "- %s: estoque %.2f %s; mínimo %.2f %s; %s",
                item.getNome(), safe(item.getEstoqueAtual()), item.getUnidade(), safe(item.getEstoqueMinimo()),
                item.getUnidade(), belowMinimum(item) ? "abaixo do mínimo" : "dentro do mínimo"))
                .collect(Collectors.joining("\n"));
        return "Estoque atual cadastrado (quantidades não convertidas):\n" + rows;
    }

    @SuppressWarnings("null")
    private String productionContext(String question, String normalized) {
        List<Receita> recipes = receitaRepository.findAllWithProdutoAndIngrediente().stream()
                .filter(recipe -> Boolean.TRUE.equals(recipe.getAtivo()))
                .toList();
        List<Produto> activeProducts = produtoRepository.findAll().stream()
                .filter(product -> Boolean.TRUE.equals(product.getAtivo())).toList();
        String matchedProduct = activeProducts.stream().map(Produto::getNome).filter(Objects::nonNull)
                .filter(name -> normalized.contains(normalize(name)))
                .max(Comparator.comparingInt(name -> normalize(name).length())).orElse(null);
        Double quantity = parseQuantity(question);

        if (matchedProduct == null || quantity == null) {
            String available = activeProducts.stream().map(Produto::getNome).filter(Objects::nonNull)
                    .sorted(String.CASE_INSENSITIVE_ORDER).limit(50).collect(Collectors.joining(", "));
            return "Para estimar ingredientes, preciso do nome de um produto ativo e da quantidade de unidades. "
                    + "Produtos ativos cadastrados: " + (available.isBlank() ? "nenhum" : available) + ". "
                    + "Exemplo: 'estimar ingredientes para 12 unidades de Bolo de Cenoura'.";
        }

        String productName = matchedProduct;
        List<Receita> productRecipes = recipes.stream()
                .filter(recipe -> recipe.getProduto() != null && productName.equalsIgnoreCase(recipe.getProduto().getNome()))
                .toList();
        if (productRecipes.isEmpty()) {
            return "O produto " + productName + " não tem ingredientes cadastrados na receita; não consigo calcular uma estimativa confiável.";
        }

        Map<String, List<Receita>> byIngredient = productRecipes.stream().collect(Collectors.groupingBy(
                recipe -> recipe.getIngrediente().getNome() + " (" + recipe.getUnidade() + ")"));
        String rows = byIngredient.entrySet().stream().map(entry -> {
            Receita recipe = entry.getValue().get(0);
            double total = entry.getValue().stream().mapToDouble(Receita::getQuantidade).sum() * quantity;
            Ingrediente ingredient = recipe.getIngrediente();
            return String.format(Locale.ROOT, "- %s: %.3f %s necessários; estoque atual %.3f %s",
                    ingredient.getNome(), total, recipe.getUnidade(), safe(ingredient.getEstoqueAtual()), ingredient.getUnidade());
        }).collect(Collectors.joining("\n"));
        return String.format(Locale.ROOT,
                "Estimativa para produzir %.3f unidade(s) de %s, multiplicando as quantidades cadastradas na receita. "
                        + "Não foram feitas conversões entre unidades; confira se a unidade da receita é compatível com o estoque.\n%s",
                quantity, productName, rows);
    }

    private String bestSellersContext(YearMonth period) {
        List<Venda> sales = nonCancelledSales(period);
        Map<String, Double> quantities = sales.stream().filter(sale -> sale.getItens() != null)
                .flatMap(sale -> sale.getItens().stream())
                .filter(item -> item.getProduto() != null && item.getProduto().getNome() != null)
                .collect(Collectors.groupingBy(item -> item.getProduto().getNome(),
                        Collectors.summingDouble(item -> safe(item.getQuantidade()))));
        String ranking = quantities.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .map(entry -> String.format(Locale.ROOT, "- %s: %.2f unidade(s)", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
        return "Produtos mais vendidos em " + period.format(DateTimeFormatter.ofPattern("MM/yyyy")) + ":\n"
                + (ranking.isBlank() ? "Não há itens vendidos registrados para o período." : ranking)
                + "\nSugestões devem se apoiar neste histórico; ele não comprova margem, capacidade produtiva ou demanda futura.";
    }

    private List<Venda> nonCancelledSales(YearMonth period) {
        return vendaRepository.findByPeriodo(period.atDay(1).atStartOfDay(), period.plusMonths(1).atDay(1).atStartOfDay()).stream()
                .filter(sale -> sale.getStatus() != StatusVenda.CANCELADA).toList();
    }

    @SuppressWarnings("null")
    private BigDecimal recordedSaleValue(Venda sale) {
        if (sale.getTotal() != null) {
            return sale.getTotal();
        }
        if (sale.getItens() == null) {
            return BigDecimal.ZERO;
        }
        return sale.getItens().stream().map(item -> item.getSubtotal() == null ? BigDecimal.ZERO : item.getSubtotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String percentageChange(BigDecimal current, BigDecimal previous) {
        return current.subtract(previous).multiply(BigDecimal.valueOf(100))
                .divide(previous, 2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private YearMonth selectPeriod(String question, String normalized) {
        Matcher yearMonth = YEAR_MONTH.matcher(question);
        if (yearMonth.find()) {
            return YearMonth.of(Integer.parseInt(yearMonth.group(1)), Integer.parseInt(yearMonth.group(2)));
        }
        Matcher monthYear = MONTH_YEAR.matcher(question);
        if (monthYear.find()) {
            return YearMonth.of(Integer.parseInt(monthYear.group(2)), Integer.parseInt(monthYear.group(1)));
        }
        YearMonth current = YearMonth.now();
        if (normalized.contains("mes passado") || normalized.contains("mes anterior")) {
            return current.minusMonths(1);
        }
        return current;
    }

    private Double parseQuantity(String question) {
        Matcher matcher = QUANTITY.matcher(question);
        if (!matcher.find()) {
            return null;
        }
        try {
            double value = Double.parseDouble(matcher.group(1).replace(',', '.'));
            return value > 0 && Double.isFinite(value) ? value : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean belowMinimum(Ingrediente ingredient) {
        return ingredient.getEstoqueAtual() != null && ingredient.getEstoqueMinimo() != null
                && ingredient.getEstoqueAtual() < ingredient.getEstoqueMinimo();
    }

    private double safe(Double value) {
        return value == null ? 0 : value;
    }

    private boolean containsAny(String value, String... terms) {
        for (String term : terms) {
            if (value.contains(term)) return true;
        }
        return false;
    }

    private String normalize(String value) {
        return Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }
}
