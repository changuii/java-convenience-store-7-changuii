package store.enums;

public enum StoreConfig {

    PRODUCTS_VALUE_PATH("src/main/resources/products.md"),
    PROMOTIONS_VALUE_PATH("src/main/resources/promotions.md"),
    TABLE_ROW_DELIMITER(","),
    PROMOTION_VALUE_REGEX("^([^,]+),([0-9]+),([0-9]+)(,[0-9]{4}-[0-9]{2}-[0-9]{2}){2}$"),
    PRODUCT_VALUE_REGEX("^([^,])+,([0-9])+,([0-9])+,([^,])+$");


    private final String value;

    StoreConfig(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
