package store.domain.product;

import java.time.LocalDate;

public class PromotionProductQuantity {
    private int quantity;
    private final Promotion promotion;

    private PromotionProductQuantity(final int quantity, final Promotion promotion) {
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static PromotionProductQuantity of(final int quantity, final Promotion promotion) {
        return new PromotionProductQuantity(quantity, promotion);
    }

    public boolean isLessThanQuantity(final int quantity, final ProductQuantity productQuantity) {
        return productQuantity.isLessThanQuantity(quantity - this.quantity);
    }

    public boolean isValidToday(final LocalDate today){
        return promotion.isValidNow(today);
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion.getPromotionName();
    }
}
