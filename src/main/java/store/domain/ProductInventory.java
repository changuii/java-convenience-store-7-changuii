package store.domain;

import java.util.Collections;
import java.util.Map;

public class ProductInventory {
    private final Map<String, ProductInfo> infos;
    private final Map<String, ProductQuantity> quantities;
    private final Map<String, PromotionProductQuantity> promotionQuantities;

    public ProductInventory(final Map<String, ProductInfo> infos, final Map<String, ProductQuantity> quantities,
                            final Map<String, PromotionProductQuantity> promotionQuantities) {
        this.infos = infos;
        this.quantities = quantities;
        this.promotionQuantities = promotionQuantities;
    }

    public static ProductInventory from(final Map<String, ProductInfo> infos,
                                        final Map<String, ProductQuantity> quantities,
                                        final Map<String, PromotionProductQuantity> promotionQuantities) {
        return new ProductInventory(infos, quantities, promotionQuantities);
    }

    public Map<String, ProductInfo> getInfos() {
        return Collections.unmodifiableMap(infos);
    }

    public Map<String, ProductQuantity> getQuantities() {
        return Collections.unmodifiableMap(quantities);
    }

    public Map<String, PromotionProductQuantity> getPromotionQuantities() {
        return Collections.unmodifiableMap(promotionQuantities);
    }
}
