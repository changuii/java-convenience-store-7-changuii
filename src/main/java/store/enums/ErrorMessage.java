package store.enums;

public enum ErrorMessage {

    // input
    INVALID_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");


    private static final String ERROR_PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ERROR_PREFIX + message;
    }
}
