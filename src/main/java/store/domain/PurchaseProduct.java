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

    public int purchaseUntilAvailable(final int quantity) {
        if (currentQuantity > quantity) {
            currentQuantity -= quantity;
            return quantity;
        }
        final int purchasedQuantity = currentQuantity;
        currentQuantity = 0;
        return purchasedQuantity;
    }

    public int getRequiredQuantityForApplyPromotion(final Promotion promotion) {
        return promotion.getRequiredQuantityForApplyPromotion(currentQuantity);
    }

    public int getApplicableFreeQuantity(final Promotion promotion) {
        return promotion.getApplicableFreeQuantity(currentQuantity);
    }

    public int calculateQuantityAtRegularPrice(final PromotionProductQuantity promotionProductQuantity) {
        return currentQuantity - promotionProductQuantity.getApplicableQuantity();
    }

    public boolean hasQuantitySufficientForApplyPromotion(final PromotionProductQuantity promotionProductQuantity) {
        return promotionProductQuantity.getRequiredQuantityForApplyPromotion(currentQuantity) <= currentQuantity;
    }

    public void additionQuantityForApplyPromotion() {
        currentQuantity++;
    }

    public boolean isPurchaseCompleted() {
        return currentQuantity == 0;
    }

    public void recordRegularPricePurchaseHistory(final int quantity, final int price) {
        purchaseHistory.addPurchasePrice(price);
        purchaseHistory.addQuantity(quantity);
    }

    public void recordPromotionAppliedPurchaseHistory(final int price, final int promotionQuantity,
                                                      final int freeQuantity) {
        purchaseHistory.addPurchasePrice(price);
        purchaseHistory.addPromotionQuantity(promotionQuantity);
        purchaseHistory.addFreeQuantity(freeQuantity);
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
