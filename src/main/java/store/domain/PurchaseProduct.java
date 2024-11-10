package store.domain;

import store.domain.product.ProductQuantity;
import store.domain.product.Promotion;
import store.domain.product.PromotionProductQuantity;

public class PurchaseProduct {
    private final String name;
    private int currentQuantity;

    private PurchaseProduct(final String name, final int currentQuantity) {
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

    public int deductQuantity(final int quantity){
        if(currentQuantity > quantity){
            currentQuantity -= quantity;
            return quantity;
        }
        int quantityDifference = quantity - currentQuantity;
        currentQuantity = 0;
        return quantityDifference;
    }

    public int calculateNeedQuantity(Promotion promotion){
        return promotion.calculatePromotionEnoughQuantity(currentQuantity);
    }

    public int calculateQuantityAtRegularPrice(PromotionProductQuantity promotionProductQuantity){
        return currentQuantity - promotionProductQuantity.calculateApplicablePromotionProduct();
    }

    public PurchaseHistory generatePurchaseHistory(final int purchaseQuantity, final int totalPurchasePrice){
        return PurchaseHistory.of(name, totalPurchasePrice, purchaseQuantity, 0, 0);
    }

    public boolean isLessThanQuantity(final ProductQuantity quantity){
        return quantity.isLessThanQuantity(this.currentQuantity);
    }

    public String getName(){
        return name;
    }
}
