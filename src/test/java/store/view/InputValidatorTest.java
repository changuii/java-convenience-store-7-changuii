package store.view;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import store.enums.ErrorMessage;

public class InputValidatorTest {

    private final InputValidator inputValidator;

    public InputValidatorTest() {
        this.inputValidator = new InputValidator();
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"김치-1]", "[김치-1", "[김치-]", "[김치1]", "[-1]", "[김치-1],", "[김치-1],[감자-]"})
    void 잘못된_형식의_상품들이_입력으로_들어오면_예외가_발생한다(String products) {
        assertThatThrownBy(() -> inputValidator.validateProducts(products))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"[김치-1]", "[김치-2],[참치-3]", "[김치-2],[참치-3],[꽁치-56213]"})
    void 올바른_형식의_상품들이_입력으로_들어오면_예외가_발생하지_않는다(String products){
        inputValidator.validateProducts(products);
    }
}
