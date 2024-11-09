package store.view;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.dto.PurchaseProductDTO;
import store.enums.ErrorMessage;

public class InputValidatorTest {

    private final InputValidator inputValidator;

    public InputValidatorTest() {
        this.inputValidator = new InputValidator();
    }

    private static Stream<List<PurchaseProductDTO>> providePurchaseProductDTO() {
        return Stream.of(
                List.of(PurchaseProductDTO.of("사이다", 2), PurchaseProductDTO.of("사이다", 2)),
                List.of(PurchaseProductDTO.of("김치", 6), PurchaseProductDTO.of("김치", 2)),
                List.of(PurchaseProductDTO.of("밤티라미수", 6), PurchaseProductDTO.of("김치", 2),
                        PurchaseProductDTO.of("밤티라미수", 5))
        );
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"김치-1]", "[김치-1", "[김치-]", "[김치1]", "[-1]", "[김치-1],", "[김치-1],[감자-]"})
    void 잘못된_형식의_상품들이_입력으로_들어오면_예외가_발생한다(String products) {
        assertThatException(inputValidator::validatePurchaseProducts, products);
    }

    @ParameterizedTest
    @MethodSource("providePurchaseProductDTO")
    void 같은_상품들이_입력으로_들어오면_예외가_발생한다(List<PurchaseProductDTO> inputs) {
        assertThatException(inputValidator::validatePurchaseProductDTOs, inputs);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -100, -500})
    void 구매_상품의_수량이_최소_0개이하인_경우_예외가_발생한다(int purchaseQuantity) {
        List<PurchaseProductDTO> input = List.of(PurchaseProductDTO.of("밤티라미수", purchaseQuantity));
        assertThatException(inputValidator::validatePurchaseProductDTOs, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"[김치-1]", "[김치-2],[참치-3]", "[김치-2],[참치-3],[꽁치-56213]"})
    void 올바른_형식의_상품들이_입력으로_들어오면_예외가_발생하지_않는다(String products) {
        inputValidator.validatePurchaseProducts(products);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"Y ", "N ", " Y", " N", "A", "[", "]", "-", "W", "0", "q", "n", "y"})
    void Y혹은N_외의_입력이_들어오면_예외가_발생한다(String answer) {
        assertThatException(inputValidator::validateAnswer, answer);
    }


    @ParameterizedTest
    @ValueSource(strings = {"Y", "N"})
    void Y혹은N이_입력으로_들어오면_예외가_발생하지_않는다(String answer) {
        inputValidator.validateAnswer(answer);
    }

    private <T> void assertThatException(Consumer<T> logic, T data) {
        assertThatThrownBy(() -> logic.accept(data))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FORMAT.getMessage());
    }
}
