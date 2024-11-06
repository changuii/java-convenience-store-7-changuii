package store.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.enums.ErrorMessage;

public class FileParserTest {

    private final FileParser fileParser;

    public FileParserTest() {
        this.fileParser = new FileParser();
    }

    @DisplayName("존재하지 않는 파일이거나, 올바른 경로가 아닌 경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"src/main/text.txt", "src", "/src/main/java/resources/promotion"})
    void 올바르지_않는_경로_혹은_파일_테스트(String path) {
        assertThatThrownBy(() -> fileParser.readAllFiles(path))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
    }

    @DisplayName("올바른 경로인 경우 예외가 발생하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"src/main/resources/products.md", "src/main/resources/promotions.md"})
    void 올바른_경로_테스트(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        List<String> expectedLines = new ArrayList<>();
        while (br.ready()) {
            expectedLines.add(br.readLine());
        }

        List<String> actualLines = fileParser.readAllFiles(path);

        assertThat(actualLines)
                .usingRecursiveComparison()
                .isEqualTo(expectedLines);
    }
}
