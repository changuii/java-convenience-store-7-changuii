package store.enums;

public enum StoreConfig {

    PRODUCTS_VALUE_PATH("src/main/resources/products.md"),
    PROMOTIONS_VALUE_PATH("src/main/resources/promotions.md"),
    TABLE_ROW_DELIMITER(",");


    private final String value;

    StoreConfig(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
