package store.component;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.service.ConvenienceStoreService;
import store.domain.ProductInventory;
import store.domain.product.Promotion;
import store.enums.StoreConfig;

public class ConvenienceStoreServiceGnerator {
    private static final int TABLE_HEADER_ROW = 0;
    private final FileParser fileParser;
    private final PromotionGenerator promotionGenerator;
    private final ProductInventoryGenerator productInventoryGenerator;

    public ConvenienceStoreServiceGnerator(final FileParser fileParser, final PromotionGenerator promotionGenerator,
                                           final ProductInventoryGenerator productInventoryGenerator) {
        this.fileParser = fileParser;
        this.promotionGenerator = promotionGenerator;
        this.productInventoryGenerator = productInventoryGenerator;
    }

    public ConvenienceStoreService generate() {
        return ConvenienceStoreService.of(generateProductInventory(generatePromotion()), new TodayGenerator());
    }

    private Map<String, Optional<Promotion>> generatePromotion() {
        List<String> promotionLines = readFileLines(StoreConfig.PROMOTIONS_VALUE_PATH.getValue());
        return promotionGenerator.generate(promotionLines);
    }

    private ProductInventory generateProductInventory(final Map<String, Optional<Promotion>> promotions) {
        List<String> productLines = readFileLines(StoreConfig.PRODUCTS_VALUE_PATH.getValue());
        return productInventoryGenerator.generate(promotions, productLines);
    }

    private List<String> readFileLines(final String path) {
        List<String> lines = fileParser.readAllFiles(path);
        lines.remove(TABLE_HEADER_ROW);
        return lines;
    }

}
