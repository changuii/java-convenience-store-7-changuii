package store.dto;

public class ProductDTO {
    private final String name;
    private final int price;
    private final int quantity;
    private final String promotionName;

    private ProductDTO(final String name, final int price, final int quantity, final String promotionName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
    }

    public static ProductDTO of(final String name, final int price, final int quantity, final String promotionName) {
        return new ProductDTO(name, price, quantity, promotionName);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalPrice() {
        return price * quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotionName() {
        return promotionName;
    }
}
