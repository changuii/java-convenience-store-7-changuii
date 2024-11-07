package store.dto;

public class ProductDTO {
    private final String name;
    private final int price;
    private final int quantity;
    private final String promotion;

    private ProductDTO(final String name, final int price, final int quantity, final String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static ProductDTO of(final String name, final int price, final int quantity, final String promotion) {
        return new ProductDTO(name, price, quantity, promotion);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }
}
