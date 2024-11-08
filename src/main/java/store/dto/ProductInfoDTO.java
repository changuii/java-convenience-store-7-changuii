package store.dto;

import store.domain.ProductInfo;

public class ProductInfoDTO {
    private final String name;
    private final int price;

    private ProductInfoDTO(final String name, final int price) {
        this.name = name;
        this.price = price;
    }

    public static ProductInfoDTO of(final String name, final int price) {
        return new ProductInfoDTO(name, price);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
