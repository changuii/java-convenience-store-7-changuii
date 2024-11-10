package store.view;

import java.text.DecimalFormat;
import store.dto.ProductDTO;
import store.dto.ProductInventoryDTO;

public class OutputView {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INTRODUCE_STORE_PRODUCTS_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_INFO_FORMAT = "- %s %s원 %s %s";
    private static final String PRODUCT_QUANTITY_FORMAT = "%d개";
    private static final String PRODUCT_QUANTITY_ZERO = "재고 없음";
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("###,###,###,###");
    private static final String PURCHASE_PRODUCTS_INPUT_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String CONTINUE_CHECKOUT_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String PURCHASE_QUANTITY_REGULAR_PRICE_MESSAGE =
            "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String ADDITION_PROMOTION_PRODUCT_QUANTITY_MESSAGE =
            "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String MEMBERSHIP_DISCOUNT_REQUEST_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";


    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printRequestContinueCheckoutMessage() {
        printLineBreak();
        System.out.println(CONTINUE_CHECKOUT_MESSAGE);
    }

    public void printErrorMessage(final IllegalArgumentException e) {
        printLineBreak();
        System.out.println(e.getMessage());
    }

    public void printStoreIntroduce(final ProductInventoryDTO productInventoryDTO) {
        System.out.println(INTRODUCE_STORE_PRODUCTS_MESSAGE);
        printLineBreak();
        printStoreProducts(productInventoryDTO);
        printLineBreak();
    }

    public void printPurchaseProductsInputMessage() {
        System.out.println(PURCHASE_PRODUCTS_INPUT_MESSAGE);
    }

    public void printPurchaseQuantityAtRegularPrice(final String productName, final int quantity) {
        printLineBreak();
        System.out.println(String.format(PURCHASE_QUANTITY_REGULAR_PRICE_MESSAGE, productName, quantity));
    }

    public void printAdditionPromotionProductQuantityMessage(final String productName) {
        printLineBreak();
        System.out.println(String.format(ADDITION_PROMOTION_PRODUCT_QUANTITY_MESSAGE, productName));
    }

    public void printMembershipDiscountRequestMessage() {
        printLineBreak();
        System.out.println(MEMBERSHIP_DISCOUNT_REQUEST_MESSAGE);
    }

    private void printStoreProducts(final ProductInventoryDTO productInventoryDTO) {
        productInventoryDTO.getProducts().stream().forEach(this::printProduct);
        printLineBreak();
    }

    private void printProduct(final ProductDTO productDTO) {
        System.out.println(formatProductDTO(productDTO));
    }

    private String formatProductDTO(final ProductDTO productDTO) {
        String price = formatProductPrice(productDTO.getPrice());
        String quantity = formatProductQuantity(productDTO.getQuantity());
        return String.format(PRODUCT_INFO_FORMAT, productDTO.getName(), price, quantity, productDTO.getPromotionName());
    }

    private String formatProductPrice(final int price) {
        return DECIMAL_FORMATTER.format(price);
    }


    private String formatProductQuantity(final int quantity) {
        if (quantity == 0) {
            return PRODUCT_QUANTITY_ZERO;
        }
        return String.format(PRODUCT_QUANTITY_FORMAT, quantity);
    }

    public void printLineBreak() {
        System.out.print(System.lineSeparator());
    }

}
