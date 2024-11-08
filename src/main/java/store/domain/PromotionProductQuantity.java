package store.domain;

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

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion.getPromotionName();
    }
}
