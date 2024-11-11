package store.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import store.domain.product.Product;
import store.domain.product.ProductQuantity;
import store.domain.product.PromotionProductQuantity;
import store.enums.ErrorMessage;

public class ProductInventory {
    private final List<Product> products;
    private final List<ProductQuantity> quantities;
    private final List<PromotionProductQuantity> promotionQuantities;

    public ProductInventory(final List<Product> products, final List<ProductQuantity> quantities,
                            final List<PromotionProductQuantity> promotionQuantities) {
        this.products = products;
        this.quantities = quantities;
        this.promotionQuantities = promotionQuantities;
    }

    public static ProductInventory of(final List<Product> products, final List<ProductQuantity> quantities,
                                      final List<PromotionProductQuantity> promotionQuantities) {
        return new ProductInventory(products, quantities, promotionQuantities);
    }

    public boolean containsProduct(final PurchaseProduct purchaseProduct) {
        return products.stream()
                .filter(product -> product.isMatchProduct(purchaseProduct))
                .anyMatch(product -> true);
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
        Product product = getProduct(purchaseProduct);
        int purchaseQuantity = productQuantity.deductQuantity(purchaseProduct);
        int totalPurchasePrice = product.calculateTotalPrice(purchaseQuantity);

        purchaseProduct.recordRegularPricePurchaseHistory(purchaseQuantity, totalPurchasePrice);
    }

    public void purchaseRegularPricePromotionProduct(final PurchaseProduct purchaseProduct) {
        PromotionProductQuantity promotionProductQuantity = getPromotionQuantity(purchaseProduct);
        Product product = getProduct(purchaseProduct);
        int purchaseQuantity = promotionProductQuantity.deductQuantityWithoutPromotion(purchaseProduct);
        int totalPurchasePrice = product.calculateTotalPrice(purchaseQuantity);

        purchaseProduct.recordRegularPricePurchaseHistory(purchaseQuantity, totalPurchasePrice);
    }

    public void purchasePromotionProduct(final PurchaseProduct purchaseProduct) {
        PromotionProductQuantity promotionProductQuantity = getPromotionQuantity(purchaseProduct);
        Product product = getProduct(purchaseProduct);
        int freeQuantity = promotionProductQuantity.getApplicableFreeQuantity(purchaseProduct);
        int purchaseQuantity = promotionProductQuantity.deductQuantityApplyPromotion(purchaseProduct, freeQuantity);
        int totalPurchasePrice = product.calculateTotalPrice(purchaseQuantity);

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
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXISTS_PRODUCT.getMessage()));
    }

    private ProductQuantity getProductQuantity(final PurchaseProduct purchaseProduct) {
        return quantities.stream()
                .filter(productQuantity -> productQuantity.isMatchProduct(purchaseProduct))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXISTS_PRODUCT.getMessage()));
    }

    private Product getProduct(final PurchaseProduct purchaseProduct) {
        return products.stream()
                .filter(product -> product.isMatchProduct(purchaseProduct))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXISTS_PRODUCT.getMessage()));
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<ProductQuantity> getQuantities() {
        return Collections.unmodifiableList(quantities);
    }

    public List<PromotionProductQuantity> getPromotionQuantities() {
        return Collections.unmodifiableList(promotionQuantities);
    }
}
