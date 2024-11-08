package store.dto;

public class PurchaseProductDTO {
    private final String name;
    private final int quantity;

    private PurchaseProductDTO(final String name, final int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public static PurchaseProductDTO of(final String name, final int quantity) {
        return new PurchaseProductDTO(name, quantity);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
