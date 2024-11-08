package store.dto;

public class ProductQuantityDTO {

    private final int quantity;

    private ProductQuantityDTO(final int quantity) {
        this.quantity = quantity;
    }

    public static ProductQuantityDTO from(final int quantity) {
        return new ProductQuantityDTO(quantity);
    }

    public int getQuantity() {
        return quantity;
    }
}
