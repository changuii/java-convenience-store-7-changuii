package store.view;

import java.util.List;
import store.dto.BillDTO;
import store.dto.ProductDTO;
import store.dto.ProductInventoryDTO;
import store.enums.OutputMessage;

public class OutputView {
    private static final String EMPTY = "";
    private static final int EMPTY_NUM = 0;
    private static final String NEGATIVE = "-";
    private static final int TAB_GROUPPING = 3;
    private static final int TAB_MAX_WIDTH = 4;

    public void printLineBreak() {
        System.out.print(System.lineSeparator());
    }

    public void printWelcomeMessage() {
        print(OutputMessage.STORE_WELCOME);
        print(OutputMessage.INTRODUCE_STORE_PRODUCTS);
    }

    public void printErrorMessage(final IllegalArgumentException e) {
        printLineBreak();
        print(e.getMessage());
    }

    public void printStoreProductsIntroduce(final ProductInventoryDTO productInventoryDTO) {
        printLineBreak();
        printStoreProducts(productInventoryDTO);
    }

    private void printStoreProducts(final ProductInventoryDTO productInventoryDTO) {
        productInventoryDTO.getProducts().stream()
                .map(this::formatProductDTO)
                .forEach(this::print);
        printLineBreak();
    }

    public void printPurchaseProductsInputMessage() {
        print(OutputMessage.PURCHASE_PRODUCTS_INPUT);
    }

    public void printPurchaseQuantityAtRegularPrice(final String productName, final int quantity) {
        printLineBreak();
        print(OutputMessage.PURCHASE_QUANTITY_AT_REGULAR_PRICE_INPUT_FORMAT, productName, quantity);
    }

    public void printAdditionPromotionProductQuantityMessage(final String productName) {
        printLineBreak();
        print(OutputMessage.ADDITION_PROMOTION_PRODUCT_QUANTITY_INPUT_FORMAT, productName);
    }

    public void printMembershipDiscountRequestMessage() {
        printLineBreak();
        print(OutputMessage.MEMBERSHIP_DISCOUNT_INPUT);
    }

    public void printBill(BillDTO billDTO) {
        printBillTitleMessage();
        printPurchaseProducts(billDTO.getPurchaseProducts());
        printFreeProducts(billDTO.getFreeProducts());
        printBillResult(billDTO);
    }

    private void printPurchaseProducts(List<ProductDTO> puchaseProduct) {
        puchaseProduct.forEach(this::printPurchaseProduct);
    }

    private void printPurchaseProduct(ProductDTO purchaseProduct) {
        print(formatBillRow(formatTab(purchaseProduct.getName()), purchaseProduct.getQuantity(),
                formatPrice(purchaseProduct.getTotalPrice())));
    }

    private void printFreeProducts(List<ProductDTO> freeProducts) {
        if (freeProducts.size() > EMPTY_NUM) {
            printBillFreeTitleMessage();
        }
        freeProducts.forEach(this::printFreeProduct);
    }

    private void printFreeProduct(ProductDTO freeProduct) {
        print(formatBillRow(formatTab(freeProduct.getName()), freeProduct.getQuantity(), EMPTY));
    }

    private void printBillResult(BillDTO billDTO) {
        printBillResultTitleMessage();
        printAllPurchasePrice(billDTO.getTotalPurchaseQuantity(), billDTO.getTotalPurchasePrice());
        printPromotionDiscount(billDTO.getPromotionDiscount());
        printMembershipDiscount(billDTO.getMembershipDiscount());
        printCheckoutPrice(billDTO.getCheckoutPrice());
    }

    private void printAllPurchasePrice(final int totalPurchaseQuantity, final int totalPurchasePrice) {
        print(formatBillRow(formatTab(OutputMessage.BILL_RESULT_TOTAL_PRICE), totalPurchaseQuantity,
                formatPrice(totalPurchasePrice)));
    }

    private void printPromotionDiscount(final int promotionDiscount) {
        print(formatBillRow(formatTab(OutputMessage.BILL_RESULT_PROMOTION_DISCOUNT), EMPTY,
                formatNegative(formatPrice((promotionDiscount)))));
    }

    private void printMembershipDiscount(final int membershipDiscount) {
        print(formatBillRow(formatTab(OutputMessage.BILL_RESULT_MEMBERSHIP_DISCOUNT), EMPTY,
                formatNegative(formatPrice((membershipDiscount)))));
    }

    private void printCheckoutPrice(final int checkoutPrice) {
        print(formatBillRow(formatTab(OutputMessage.BILL_RESULT_CHECKOUT_PRICE), EMPTY, formatPrice(checkoutPrice)));
    }

    private void printBillTitleMessage() {
        print(OutputMessage.BILL_TITLE);
        print(formatBillRow(formatTab(OutputMessage.BILL_HEADER_PRODUCT_NAME), OutputMessage.BILL_HEADER_QUANTITY,
                OutputMessage.BILL_HEADER_PRICE));
    }

    private void printBillFreeTitleMessage() {
        print(OutputMessage.BILL_FREE_PRODUCTS_TITLE);
    }

    private void printBillResultTitleMessage() {
        print(OutputMessage.BILL_RESULT_TITLE);
    }

    public void printRequestContinueCheckoutMessage() {
        printLineBreak();
        print(OutputMessage.CONTINUE_CHECKOUT_INPUT);
    }

    private String formatBillRow(final Object... values) {
        return formatMessage(OutputMessage.BILL_ROW_FORMAT, values);
    }

    private String formatProductDTO(final ProductDTO productDTO) {
        return formatMessage(OutputMessage.INTRODUCE_PRODUCT_INFO_FORMAT, productDTO.getName(),
                formatPrice(productDTO.getPrice()),
                formatProductQuantity(productDTO.getQuantity()), productDTO.getPromotionName());
    }

    private String formatPrice(final int price) {
        return formatMessage(OutputMessage.PRICE_FORMAT, price);
    }

    private String formatNegative(final String number) {
        return NEGATIVE+number;
    }

    private String formatTab(final Object value) {
        return value.toString() + "\t".repeat(getTabCount(value.toString()));
    }

    private int getTabCount(final String value) {
        return Math.abs(value.length() / TAB_GROUPPING - TAB_MAX_WIDTH);
    }

    private String formatMessage(final Object formatMessage, final Object... values) {
        return String.format(formatMessage.toString(), values);
    }

    private String formatProductQuantity(final int quantity) {
        if (quantity == 0) {
            return OutputMessage.INTRODUCE_PRODUCT_QUANTITY_ZERO.toString();
        }
        return formatMessage(OutputMessage.INTRODUCE_PRODUCT_QUANTITY_FORMAT, quantity);
    }

    private void print(Object message, Object... values) {
        System.out.println(formatMessage(message.toString(), values));
    }

}
