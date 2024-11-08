package store.domain;

public class PromotionProductQuantity {
    private final ProductQuantity quantity;
    private final Promotion promotion;

    private PromotionProductQuantity(final ProductQuantity quantity, final Promotion promotion) {
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static PromotionProductQuantity of(final int quantity, final Promotion promotion) {
        return new PromotionProductQuantity(ProductQuantity.from(quantity), promotion);
    }

    public int getQuantity() {
        return quantity.getQuantity();
    }

    public String getPromotion() {
        return promotion.getPromotionName();
    }
}
