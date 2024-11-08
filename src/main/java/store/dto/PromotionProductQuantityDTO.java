package store.dto;


public class PromotionProductQuantityDTO {

    private final ProductQuantityDTO quantity;
    private final String promotion;

    private PromotionProductQuantityDTO(final ProductQuantityDTO quantity, final String promotion) {
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static PromotionProductQuantityDTO of(final int quantity, final String promotion) {
        return new PromotionProductQuantityDTO(ProductQuantityDTO.from(quantity), promotion);
    }

    public ProductQuantityDTO getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }
}
