package store.view;

import java.util.regex.Pattern;
import store.enums.ErrorMessage;

public class InputValidator {


    private static final String PRODUCTS_FORMAT_REGEX
            = "^\\[[A-Za-z0-9가-힣]+-[0-9]+\\](,\\[[A-Za-z0-9가-힣]+-[0-9]+\\])*$";
    private static final Pattern PRODUCTS_FORMAT = Pattern.compile(PRODUCTS_FORMAT_REGEX);


    public void validateProducts(String products) {
        if (!PRODUCTS_FORMAT.matcher(products).matches()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }

}
