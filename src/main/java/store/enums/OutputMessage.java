package store.enums;

public enum OutputMessage {

    STORE_WELCOME("안녕하세요. W편의점입니다."),
    INTRODUCE_STORE_PRODUCTS("현재 보유하고 있는 상품입니다."),
    INTRODUCE_PRODUCT_INFO_FORMAT("- %s %s원 %s %s"),
    INTRODUCE_PRODUCT_QUANTITY_FORMAT("%d개"),
    INTRODUCE_PRODUCT_QUANTITY_ZERO("재고 없음"),
    PRICE_FORMAT("%,d"),
    PURCHASE_PRODUCTS_INPUT("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    CONTINUE_CHECKOUT_INPUT("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"),
    PURCHASE_QUANTITY_AT_REGULAR_PRICE_INPUT_FORMAT("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    ADDITION_PROMOTION_PRODUCT_QUANTITY_INPUT_FORMAT("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    MEMBERSHIP_DISCOUNT_INPUT("멤버십 할인을 받으시겠습니까? (Y/N)"),
    BILL_TITLE("==============W 편의점================"),
    BILL_ROW_FORMAT("%s%s\t\t\t%s"),
    BILL_FREE_PRODUCTS_TITLE("=============증\t\t정==============="),
    BILL_RESULT_TITLE("===================================="),
    BILL_HEADER_PRODUCT_NAME("상품명"),
    BILL_HEADER_QUANTITY("수량"),
    BILL_HEADER_PRICE("금액"),
    BILL_RESULT_TOTAL_PRICE("총구매액"),
    BILL_RESULT_PROMOTION_DISCOUNT("행사할인"),
    BILL_RESULT_MEMBERSHIP_DISCOUNT("멤버십할인"),
    BILL_RESULT_CHECKOUT_PRICE("내실돈");

    private final String message;

    OutputMessage(final String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
