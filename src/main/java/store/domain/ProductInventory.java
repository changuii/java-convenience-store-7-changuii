package store.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import store.domain.product.ProductInfo;
import store.domain.product.ProductQuantity;
import store.domain.product.PromotionProductQuantity;

public class ProductInventory {
    private final List<ProductInfo> infos;
    private final List<ProductQuantity> quantities;
    private final List<PromotionProductQuantity> promotionQuantities;

    public ProductInventory(final List<ProductInfo> infos, final List<ProductQuantity> quantities,
                            final List<PromotionProductQuantity> promotionQuantities) {
        this.infos = infos;
        this.quantities = quantities;
        this.promotionQuantities = promotionQuantities;
    }

    public static ProductInventory of(final List<ProductInfo> infos, final List<ProductQuantity> quantities,
                                      final List<PromotionProductQuantity> promotionQuantities) {
        return new ProductInventory(infos, quantities, promotionQuantities);
    }

    public boolean containsProduct(final PurchaseProduct purchaseProduct) {
        return infos.stream()
                .filter(productInfo -> productInfo.isMatchProduct(purchaseProduct))
                .anyMatch(productInfo -> true);
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
        return promotionQuantities.stream()
                .filter(promotionProductQuantity -> promotionProductQuantity.isMatchProduct(purchaseProduct))
                .filter(promotionProductQuantity -> promotionProductQuantity.isValidToday(today))
                .anyMatch(promotionProductQuantity -> true);
    }

    public PurchaseHistory purchaseProduct(final PurchaseProduct purchaseProduct){
        ProductQuantity productQuantity = getProductQuantity(purchaseProduct);
        ProductInfo productInfo = getProductInfo(purchaseProduct);
        int purchaseQuantity = productQuantity.deductQuantity(purchaseProduct);
        int totalPurchasePrice = productInfo.calculateTotalPrice(purchaseQuantity);

        return purchaseProduct.generatePurchaseHistory(purchaseQuantity, totalPurchasePrice);
    }


    private PromotionProductQuantity getPromotionQuantity(final PurchaseProduct purchaseProduct) {
        return promotionQuantities.stream()
                .filter(promotionProductQuantity -> promotionProductQuantity.isMatchProduct(purchaseProduct))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

    private ProductQuantity getProductQuantity(final PurchaseProduct purchaseProduct) {
        return quantities.stream()
                .filter(productQuantity -> productQuantity.isMatchProduct(purchaseProduct))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

    private ProductInfo getProductInfo(final PurchaseProduct purchaseProduct){
        return infos.stream()
                .filter(productInfo -> productInfo.isMatchProduct(purchaseProduct))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

    public List<ProductInfo> getInfos() {
        return Collections.unmodifiableList(infos);
    }

    public List<ProductQuantity> getQuantities() {
        return Collections.unmodifiableList(quantities);
    }

    public List<PromotionProductQuantity> getPromotionQuantities() {
        return Collections.unmodifiableList(promotionQuantities);
    }
}
