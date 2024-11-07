package store.component;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.domain.ConvenienceStore;
import store.domain.Product;
import store.domain.Promotion;
import store.enums.StoreConfig;

public class ConvenienceStoreGnerator {
    private static final int TABLE_HEADER_ROW = 0;
    private final FileParser fileParser;
    private final PromotionGenerator promotionGenerator;
    private final ProductGenerator productGenerator;

    public ConvenienceStoreGnerator(final FileParser fileParser, final PromotionGenerator promotionGenerator,
                                    final ProductGenerator productGenerator) {
        this.fileParser = fileParser;
        this.promotionGenerator = promotionGenerator;
        this.productGenerator = productGenerator;
    }

    public ConvenienceStore generate() {
        Map<String, Product> products = new LinkedHashMap<>();
        Map<String, Product> promotionProducts = new LinkedHashMap<>();
        generateProduct(products, promotionProducts, generatePromotion());

        return ConvenienceStore.of(products, promotionProducts);
    }

    private Map<String, Optional<Promotion>> generatePromotion() {
        List<String> promotionLines = readFileLines(StoreConfig.PROMOTIONS_VALUE_PATH.getValue());
        return promotionGenerator.generate(promotionLines);
    }

    private void generateProduct(final Map<String, Product> products, final Map<String, Product> promotionProducts,
                                 final Map<String, Optional<Promotion>> promotions) {
        List<String> productLines = readFileLines(StoreConfig.PRODUCTS_VALUE_PATH.getValue());
        productGenerator.generate(promotions, products, promotionProducts, productLines);
    }

    private List<String> readFileLines(final String path) {
        List<String> lines = fileParser.readAllFiles(path);
        lines.remove(TABLE_HEADER_ROW);
        return lines;
    }

}
