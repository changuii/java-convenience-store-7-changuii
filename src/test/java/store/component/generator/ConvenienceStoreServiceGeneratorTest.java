package store.component.generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.Constants;
import store.component.FileParser;
import store.domain.ProductInventory;
import store.domain.product.Promotion;
import store.enums.GeneratorConstants;
import store.service.ConvenienceStoreService;

public class ConvenienceStoreServiceGeneratorTest {
    private static final String TABLE_HEADER = "";

    private ConvenienceStoreServiceGnerator convenienceStoreServiceGnerator;
    private MockFileParse mockFileParse;
    private MockPromotionGenerator mockPromotionGenerator;
    private MockProductInventoryGenerator mockProductInventoryGenerator;

    @BeforeEach
    void init() {
        mockFileParse = new MockFileParse();
        mockPromotionGenerator = new MockPromotionGenerator();
        mockProductInventoryGenerator = new MockProductInventoryGenerator();
        convenienceStoreServiceGnerator = new ConvenienceStoreServiceGnerator(mockFileParse, mockPromotionGenerator,
                mockProductInventoryGenerator);
    }

    @Test
    void generate를_호출하면_fileParser를_통해_md_파일을_읽어온다() {
        List<String> expected = List.of(GeneratorConstants.PROMOTIONS_VALUE_PATH.getValue(),
                GeneratorConstants.PRODUCTS_VALUE_PATH.getValue());
        mockFileParse.addFileLine(new ArrayList<>());
        mockFileParse.addFileLine(new ArrayList<>());

        convenienceStoreServiceGnerator.generate();

        assertThat(mockFileParse.getInputPaths()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void generate를_호출하면_promotionGenerator의_generate를_호출한다() {
        List<String> expected = new ArrayList<>(List.of(TABLE_HEADER));
        mockFileParse.addFileLine(expected);
        mockFileParse.addFileLine(new ArrayList<>());

        convenienceStoreServiceGnerator.generate();

        assertThat(mockPromotionGenerator.getPromotionLines()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void generate를_호출하면_productInventoryGenerator의_generate를_호출한다() {
        List<String> expectedLines = new ArrayList<>(List.of(TABLE_HEADER));
        mockFileParse.addFileLine(new ArrayList<>());
        mockFileParse.addFileLine(expectedLines);

        convenienceStoreServiceGnerator.generate();

        assertThat(mockProductInventoryGenerator.getProductLines()).usingRecursiveComparison()
                .isEqualTo(expectedLines);
        assertThat(
                mockProductInventoryGenerator.getPromotions().get(Constants.PROMOTION_NAME)).usingRecursiveAssertion()
                .isEqualTo(Optional.empty());
    }

    @Test
    void 생성되는_ConvenienceStoreService_테스트() {
        ConvenienceStoreService expected = ConvenienceStoreService.of(
                ProductInventory.of(List.of(), List.of(), List.of()), new TodayGenerator());
        mockFileParse.addFileLine(new ArrayList<>());
        mockFileParse.addFileLine(new ArrayList<>());

        ConvenienceStoreService actual = convenienceStoreServiceGnerator.generate();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }


    private class MockFileParse extends FileParser {
        private List<String> inputPaths;
        private Queue<List<String>> fileLinesQueue = new ArrayDeque<>();

        @Override
        public List<String> readAllFiles(String filePath) {
            inputPaths.add(filePath);
            return fileLinesQueue.poll();
        }

        public MockFileParse() {
            this.inputPaths = new ArrayList<>();
        }

        public void addFileLine(List<String> fileLines) {
            fileLines.add(TABLE_HEADER);
            this.fileLinesQueue.offer(fileLines);
        }

        public List<String> getInputPaths() {
            return inputPaths;
        }


    }

    private class MockPromotionGenerator extends PromotionGenerator {

        private List<String> promotionLines;

        @Override
        public Map<String, Optional<Promotion>> generate(List<String> promotionLines) {
            this.promotionLines = promotionLines;
            return Map.of(Constants.PROMOTION_NAME, Optional.empty());
        }

        public List<String> getPromotionLines() {
            return promotionLines;
        }
    }

    private class MockProductInventoryGenerator extends ProductInventoryGenerator {

        private Map<String, Optional<Promotion>> promotions;
        private List<String> productLines;

        @Override
        public ProductInventory generate(final Map<String, Optional<Promotion>> promotions,
                                         final List<String> productLines) {
            this.promotions = promotions;
            this.productLines = productLines;
            return ProductInventory.of(List.of(), List.of(), List.of());
        }

        public Map<String, Optional<Promotion>> getPromotions() {
            return promotions;
        }

        public List<String> getProductLines() {
            return productLines;
        }
    }
}
