package store.domain;

import java.util.List;
import java.util.Map;

public class ConvenienceStore {
    private final Map<String, Product> products;
    private final Map<String, Product> promotionProducts;

    private ConvenienceStore(final Map<String, Product> products, Map<String, Product> promotionProducts) {
        this.products = products;
        this.promotionProducts = promotionProducts;
    }

    public static ConvenienceStore of(final Map<String, Product> products, Map<String, Product> promotionProducts) {
        return new ConvenienceStore(products, promotionProducts);
    }

}
