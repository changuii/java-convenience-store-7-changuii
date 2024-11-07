package store.enums;

public enum StoreConfig {

    PRODUCTS_VALUE_PATH("src/main/resources/products.md"),
    PROMOTIONS_VALUE_PATH("src/main/resources/promotions.md"),
    TABLE_ROW_DELIMITER(","),
    PROMOTION_VALUE_REGEX("^([^,]+),(\\d+),(\\d+),(\\d{4}-\\d{2}-\\d{2}),(\\d{4}-\\d{2}-\\d{2})$");


    private final String value;

    StoreConfig(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
