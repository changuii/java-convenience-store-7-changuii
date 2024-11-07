package store.domain;

import java.util.Map;
import java.util.Optional;
import store.dto.ProductDTO;

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

    public void registerProduct(final Map<String, Product> products, final Map<String, Product> promotionProducts) {
        Product product = this;
        if (promotion.isPresent()) {
            product = new Product(name, price, 0, Optional.empty());
            promotionProducts.put(name, this);
        }
        products.put(name, product);
    }

    public ProductDTO toDTO() {
        String promotionName = "";
        if (promotion.isPresent()) {
            promotionName = promotion.get().toString();
        }
        return ProductDTO.of(name, price, quantity, promotionName);
    }


}
