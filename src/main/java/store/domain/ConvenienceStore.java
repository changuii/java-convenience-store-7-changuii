package store.domain;

import java.util.Collections;
import java.util.Map;

public class ConvenienceStore {
    private final Map<String, Product> products;
    private final Map<String, Product> promotionProducts;

    private ConvenienceStore(final Map<String, Product> products, final Map<String, Product> promotionProducts) {
        this.products = products;
        this.promotionProducts = promotionProducts;
    }

    public static ConvenienceStore of(final Map<String, Product> products,
                                      final Map<String, Product> promotionProducts) {
        return new ConvenienceStore(products, promotionProducts);
    }

    public Map<String, Product> getProducts() {
        return Collections.unmodifiableMap(products);
    }

    public Map<String, Product> getPromotionProducts() {
        return Collections.unmodifiableMap(promotionProducts);
    }
}
