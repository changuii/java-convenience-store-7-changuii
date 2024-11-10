package store.domain.product;

import java.time.LocalDate;
import store.domain.PurchaseProduct;

public class PromotionProductQuantity {
    private final String productName;
    private int quantity;
    private final Promotion promotion;

    private PromotionProductQuantity(final String productName, final int quantity, final Promotion promotion) {
        this.productName = productName;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static PromotionProductQuantity of(final String productName, final int quantity, final Promotion promotion) {
        return new PromotionProductQuantity(productName, quantity, promotion);
    }

    public boolean isLessThanQuantity(final int quantity, final ProductQuantity productQuantity) {
        return productQuantity.isLessThanQuantity(quantity - this.quantity);
    }

    public boolean isValidToday(final LocalDate today) {
        return promotion.isValidNow(today);
    }

    public boolean isMatchProduct(final PurchaseProduct purchaseProduct) {
        return purchaseProduct.isMatchProductName(productName);
    }

    public boolean isQuantityEnough(final PurchaseProduct purchaseProduct) {
        return purchaseProduct.calculateNeedQuantity(promotion) <= quantity;
    }

    public int calculateApplicablePromotionProduct() {
        return promotion.calculateApplicablePromotionQuantity(quantity);
    }

    public int deductQuantity(final PurchaseProduct purchaseProduct){
        int deductQuantity = purchaseProduct.deductQuantity(quantity);
        quantity -= deductQuantity;
        return deductQuantity;
    }

    public int deductQuantity(final PurchaseProduct purchaseProduct, final int freeQuantity) {
        int deductQuantity = purchaseProduct.deductQuantity(calculateApplicablePromotionProduct());
        quantity -= deductQuantity;
        return deductQuantity - freeQuantity;
    }

    public int calculateFreeQuantity() {
        return promotion.calculateApplicableFreeQuantity(quantity);
    }


    public boolean isMatchProductName(final String name) {
        return this.productName.equals(name);
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion.getPromotionName();
    }
}
