package store.component;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.domain.ConvenienceStore;
import store.domain.DateRange;
import store.domain.Product;
import store.domain.Promotion;
import store.enums.ErrorMessage;

public class ConvenienceStoreGnerator {
    private static final int TABLE_HEADER_ROW = 0;
    private final String productPath;
    private final String promotionPath;
    private final String tableRowDelimiter;
    private final FileParser fileParser;

    public ConvenienceStoreGnerator(final String productPath, final String promotionPath,
                                    final String tableRowDelimiter, final FileParser fileParser) {
        this.productPath = productPath;
        this.promotionPath = promotionPath;
        this.tableRowDelimiter = tableRowDelimiter;
        this.fileParser = fileParser;
    }

    public ConvenienceStore generate() {
        Map<String, Optional<Promotion>> promotions = generatePromotions();
        Map<String, Product> products = new LinkedHashMap<>();
        Map<String, Product> promotionProducts = new LinkedHashMap<>();
        generateProducts(promotions, products, promotionProducts);

        return ConvenienceStore.of(products, promotionProducts);
    }

    private void generateProducts(final Map<String, Optional<Promotion>> promotions,
                                  final Map<String, Product> products,
                                  final Map<String, Product> promotionProducts) {
        List<String> productLines = readFileLines(productPath);

        productLines.stream()
                .map(productLine -> parseProduct(productLine, promotions))
                .forEach(product -> product.registerProduct(products, promotionProducts));
    }


    private Product parseProduct(final String productLine, final Map<String, Optional<Promotion>> promotions) {
        String[] product = productLine.split(tableRowDelimiter);
        String productName = product[0];
        int productPrice = parseInt(product[1]);
        int productQuantity = parseInt(product[2]);
        Optional<Promotion> promotion = promotions.get(product[3]);
        return Product.of(productName, productPrice, productQuantity, promotion);
    }

    private Map<String, Optional<Promotion>> generatePromotions() {
        Map<String, Optional<Promotion>> promotions = initPromotions();
        List<String> promotionLines = readFileLines(promotionPath);
        registerPromotions(promotions, promotionLines);

        return promotions;
    }

    private void registerPromotions(Map<String, Optional<Promotion>> promotions, List<String> promotionLines) {
        promotionLines.stream()
                .map(this::parsePromotion)
                .forEach(promotion -> promotion.registerPromotion(promotions));
    }

    private Map<String, Optional<Promotion>> initPromotions() {
        Map<String, Optional<Promotion>> promotions = new HashMap<>();
        promotions.put("null", Optional.empty());
        return promotions;
    }

    private Promotion parsePromotion(final String promotion) {
        String[] column = promotion.split(tableRowDelimiter);
        String promotionName = column[0];
        int purchaseCount = parseInt(column[1]);
        DateRange dateRange = parseDateRange(column[3], column[4]);

        return Promotion.of(promotionName, purchaseCount, dateRange);
    }

    private DateRange parseDateRange(final String startDate, final String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return DateRange.of(start, end);
    }

    private int parseInt(final String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }

    private List<String> readFileLines(final String path) {
        List<String> lines = fileParser.readAllFiles(path);
        lines.remove(TABLE_HEADER_ROW);
        return lines;
    }


}
