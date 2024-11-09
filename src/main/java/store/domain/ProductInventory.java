package store.domain;

import java.time.LocalDate;
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

    public static ProductInventory of(final Map<String, ProductInfo> infos,
                                      final Map<String, ProductQuantity> quantities,
                                      final Map<String, PromotionProductQuantity> promotionQuantities) {
        return new ProductInventory(infos, quantities, promotionQuantities);
    }

    public boolean containsProduct(final PurchaseProduct purchaseProduct){
        return infos.keySet().stream()
                .filter(purchaseProduct::isMatchProductName)
                .anyMatch(productName -> true);
    }

    public boolean isLessThanQuantity(final PurchaseProduct purchaseProduct, final LocalDate today) {
        ProductQuantity productQuantity = getProductQuantity(purchaseProduct);
        if (isPromotionProduct(purchaseProduct, today)) {
            PromotionProductQuantity promotionQuantity = getPromotionQuantity(purchaseProduct);
            return purchaseProduct.isLessThanQuantity(promotionQuantity, productQuantity);
        }
        return purchaseProduct.isLessThanQuantity(productQuantity);
    }

    public boolean isPromotionProduct(final PurchaseProduct purchaseProduct, final LocalDate today) {
        return promotionQuantities.keySet().stream()
                .filter(purchaseProduct::isMatchProductName)
                .map(promotionQuantities::get)
                .filter(promotionQuantity -> promotionQuantity.isValidToday(today))
                .findAny()
                .isPresent();
    }

    private PromotionProductQuantity getPromotionQuantity(final PurchaseProduct purchaseProduct){
        return promotionQuantities.keySet().stream()
                .filter(purchaseProduct::isMatchProductName)
                .map(promotionQuantities::get)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

    private ProductQuantity getProductQuantity(final PurchaseProduct purchaseProduct){
        return quantities.keySet().stream()
                .filter(purchaseProduct::isMatchProductName)
                .map(quantities::get)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }


    public int purchaseProduct(final String productName, final int purchaseQuantity) {
        quantities.get(productName).deductQuantity(purchaseQuantity);
        return infos.get(productName).calculateTotalPrice(purchaseQuantity);
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
