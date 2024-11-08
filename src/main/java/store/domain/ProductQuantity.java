package store.domain;

public class ProductQuantity {
    private int quantity;

    private ProductQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public static ProductQuantity from(final int quantity) {
        return new ProductQuantity(quantity);
    }

    public int getQuantity() {
        return quantity;
    }
}
