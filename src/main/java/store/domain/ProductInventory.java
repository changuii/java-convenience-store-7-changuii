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

    public boolean isLessThanQuantityForPurchase(final PurchaseProduct purchaseProduct, final LocalDate today) {
        ProductQuantity productQuantity = getProductQuantity(purchaseProduct);
        if (isPromotionInProgress(purchaseProduct, today)) {
            PromotionProductQuantity promotionQuantity = getPromotionQuantity(purchaseProduct);
            return purchaseProduct.isLessThanQuantity(promotionQuantity, productQuantity);
        }
        return purchaseProduct.isLessThanQuantity(productQuantity);
    }

    public boolean isPromotionInProgress(final PurchaseProduct purchaseProduct, final LocalDate today) {
        return promotionQuantities.stream()
                .filter(promotionProductQuantity -> promotionProductQuantity.isMatchProduct(purchaseProduct))
                .filter(promotionProductQuantity -> promotionProductQuantity.isValidToday(today))
                .anyMatch(promotionProductQuantity -> true);
    }

    public void purchaseRegularPriceProduct(final PurchaseProduct purchaseProduct) {
        ProductQuantity productQuantity = getProductQuantity(purchaseProduct);
        ProductInfo productInfo = getProductInfo(purchaseProduct);
        int purchaseQuantity = productQuantity.deductQuantity(purchaseProduct);
        int totalPurchasePrice = productInfo.calculateTotalPrice(purchaseQuantity);

        purchaseProduct.recordRegularPricePurchaseHistory(purchaseQuantity, totalPurchasePrice);
    }

    public void purchaseRegularPricePromotionProduct(final PurchaseProduct purchaseProduct) {
        PromotionProductQuantity promotionProductQuantity = getPromotionQuantity(purchaseProduct);
        ProductInfo productInfo = getProductInfo(purchaseProduct);
        int purchaseQuantity = promotionProductQuantity.deductQuantityWithoutPromotion(purchaseProduct);
        int totalPurchasePrice = productInfo.calculateTotalPrice(purchaseQuantity);

        purchaseProduct.recordRegularPricePurchaseHistory(purchaseQuantity, totalPurchasePrice);
    }

    public void purchasePromotionProduct(final PurchaseProduct purchaseProduct) {
        PromotionProductQuantity promotionProductQuantity = getPromotionQuantity(purchaseProduct);
        ProductInfo productInfo = getProductInfo(purchaseProduct);
        int freeQuantity = promotionProductQuantity.getApplicableFreeQuantity();
        int purchaseQuantity = promotionProductQuantity.deductQuantityApplyPromotion(purchaseProduct, freeQuantity);
        int totalPurchasePrice = productInfo.calculateTotalPrice(purchaseQuantity);

        purchaseProduct.recordPromotionAppliedPurchaseHistory(totalPurchasePrice, purchaseQuantity, freeQuantity);
    }

    public boolean isQuantitySufficientForApplyPromotion(final PurchaseProduct purchaseProduct) {
        PromotionProductQuantity promotionProductQuantity = getPromotionQuantity(purchaseProduct);
        return purchaseProduct.hasQuantitySufficientForApplyPromotion(promotionProductQuantity);
    }

    public boolean isRequirementMetForApplyPromotion(final PurchaseProduct purchaseProduct) {
        PromotionProductQuantity promotionQuantity = getPromotionQuantity(purchaseProduct);
        return promotionQuantity.isRequirementMetForApplyPromotion(purchaseProduct);
    }

    public int calculateQuantityAtRegularPrice(final PurchaseProduct purchaseProduct) {
        PromotionProductQuantity promotionQuantity = getPromotionQuantity(purchaseProduct);
        return purchaseProduct.calculateQuantityAtRegularPrice(promotionQuantity);
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

    private ProductInfo getProductInfo(final PurchaseProduct purchaseProduct) {
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
