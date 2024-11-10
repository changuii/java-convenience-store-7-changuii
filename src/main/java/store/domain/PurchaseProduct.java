package store.domain;

import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;

public class PurchaseProduct {
    private final PurchaseHistory purchaseHistory;
    private final String name;
    private int currentQuantity;

    private PurchaseProduct(final String name, final int currentQuantity) {
        this.purchaseHistory = PurchaseHistory.from(name);
        this.name = name;
        this.currentQuantity = currentQuantity;
    }

    public static PurchaseProduct of(final String name, final int currentQuantity) {
        return new PurchaseProduct(name, currentQuantity);
    }

    public boolean isMatchProductName(final String productName) {
        return name.equals(productName);
    }

    public boolean isLessThanQuantity(final PromotionProductQuantity promotionQuantity,
                                      final ProductQuantity quantity) {
        return promotionQuantity.isLessThanQuantity(currentQuantity, quantity);
    }

    public int deductQuantity(final int quantity) {
        if (currentQuantity > quantity) {
            currentQuantity -= quantity;
            return quantity;
        }
        int quantityDifference = quantity - currentQuantity;
        currentQuantity = 0;
        return quantityDifference;
    }

    public int calculateNeedQuantity(Promotion promotion) {
        return promotion.calculatePromotionEnoughQuantity(currentQuantity);
    }

    public int calculateQuantityAtRegularPrice(PromotionProductQuantity promotionProductQuantity) {
        return currentQuantity - promotionProductQuantity.calculateApplicablePromotionProduct();
    }

    public boolean isPurchaseCompleted() {
        return currentQuantity == 0;
    }

    public void writePurchaseHistory(final int quantity, final int price) {
        purchaseHistory.addPurchasePrice(price);
        purchaseHistory.addQuantity(quantity);
    }

    public CompletedPurchaseHistory generateCompletedPurchaseHistory() {
        return purchaseHistory.purchaseComplete();
    }

    public boolean isLessThanQuantity(final ProductQuantity quantity) {
        return quantity.isLessThanQuantity(this.currentQuantity);
    }

    public String getName() {
        return name;
    }
}
