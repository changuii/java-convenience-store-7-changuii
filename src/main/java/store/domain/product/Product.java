package store.domain.product;

import store.domain.PurchaseProduct;

public class Product {
    private final String name;
    private final int price;

    private Product(final String name, final int price) {
        this.name = name;
        this.price = price;
    }

    public static Product of(final String name, final int price) {
        return new Product(name, price);
    }

    public int calculateTotalPrice(final int quantity) {
        return price * quantity;
    }

    public boolean isMatchProduct(final PurchaseProduct purchaseProduct) {
        return purchaseProduct.isMatchProductName(name);
    }

    public boolean isMatchProductName(final String name) {
        return this.name.equals(name);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
