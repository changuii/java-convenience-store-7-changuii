package store.enums;

public enum ErrorMessage {

    // input
    INVALID_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),

    // component
    INVALID_FILE_FORMAT("파일의 형식이 잘못되었습니다."),

    // service
    NOT_EXISTS_PRODUCT_NAME("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    MORE_THAN_PURCHASE_PRODUCT_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");


    private static final String ERROR_PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return ERROR_PREFIX + message;
    }
}
