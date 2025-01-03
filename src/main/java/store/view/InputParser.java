package store.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import store.dto.PurchaseProductDTO;
import store.enums.ErrorMessage;

public class InputParser {
    private static final String PRODUCTS_DELIMITER = ",";
    private static final String PRODUCT_DELIMITER = "-";
    private static final String PRODUCT_BRACKETS = "[\\[\\]]";
    private static final String EMPTY = "";


    public List<PurchaseProductDTO> parsePurchaseProducts(final String products) {
        return Arrays.stream(products.split(PRODUCTS_DELIMITER))
                .map(this::parsePurchaseProduct)
                .collect(Collectors.toList());
    }

    public boolean parseAnswer(final String answer) {
        return answer.equals("Y");
    }

    private PurchaseProductDTO parsePurchaseProduct(final String product) {
        String[] values = product.replaceAll(PRODUCT_BRACKETS, EMPTY).split(PRODUCT_DELIMITER);
        String productName = values[0];
        int productCount = parseInt(values[1]);
        return PurchaseProductDTO.of(productName, productCount);
    }

    private int parseInt(final String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }
}
