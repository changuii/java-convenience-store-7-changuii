package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.dto.PurchaseProductDTO;

public class InputView {
    private final InputValidator inputValidator;
    private final InputParser inputParser;

    public InputView(final InputValidator inputValidator, final InputParser inputParser) {
        this.inputValidator = inputValidator;
        this.inputParser = inputParser;
    }

    public List<PurchaseProductDTO> readPurchaseProducts() {
        final String input = Console.readLine();
        inputValidator.validatePurchaseProducts(input);
        final List<PurchaseProductDTO> purchaseProductDTOs = inputParser.parsePurchaseProducts(input);
        inputValidator.validatePurchaseProductDTOs(purchaseProductDTOs);
        return purchaseProductDTOs;
    }

    public boolean readAnswer() {
        final String input = Console.readLine();
        inputValidator.validateAnswer(input);
        return inputParser.parseAnswer(input);
    }

}
