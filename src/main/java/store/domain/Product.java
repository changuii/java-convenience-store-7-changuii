package store.domain;

import java.util.Optional;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;
    private final Optional<Promotion> promotion;

    private Product(final String name, final int price, final int quantity, final Optional<Promotion> promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static Product of(final String name, final int price, final int quantity,
                             final Optional<Promotion> promotion) {
        return new Product(name, price, quantity, promotion);
    }

    public boolean isPromotionProduct() {
        return promotion.isPresent();
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

    public Optional<Promotion> getPromotion() {
        return promotion;
    }

}
