package store.domain;

public class ProductInfo {
    private final String name;
    private final int price;

    private ProductInfo(final String name, final int price) {
        this.name = name;
        this.price = price;
    }

    public static ProductInfo of(final String name, final int price) {
        return new ProductInfo(name, price);
    }


    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
