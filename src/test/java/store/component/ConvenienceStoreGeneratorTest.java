package store.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.ConvenienceStore;
import store.domain.DateRange;
import store.domain.Product;
import store.domain.Promotion;
import store.enums.StoreConfig;

public class ConvenienceStoreGeneratorTest {

    private static final String TABLE_HEADER = "header";
    private final ConvenienceStoreGnerator convenienceStoreGnerator;

    public ConvenienceStoreGeneratorTest() {
        String productPath = StoreConfig.PRODUCTS_VALUE_PATH.getValue();
        String promotionPath = StoreConfig.PROMOTIONS_VALUE_PATH.getValue();
        String tableDelimiter = StoreConfig.TABLE_ROW_DELIMITER.getValue();
        List<String> promotionDatas = new ArrayList<>(List.of(TABLE_HEADER, "탄산2+1,2,1,2024-01-01,2024-12-31"));
        List<String> productDatas = new ArrayList<>(List.of(TABLE_HEADER, "콜라,1000,10,탄산2+1"));
        MockFileParser mockFileParser = new MockFileParser(List.of(promotionDatas, productDatas));
        this.convenienceStoreGnerator =
                new ConvenienceStoreGnerator(productPath, promotionPath, tableDelimiter, mockFileParser);
    }

    @DisplayName("파일에서 가져온 문자열들을 올바로 파싱하는지 테스트")
    @Test
    void generateTest() {
        DateRange dateRange = DateRange.of(LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31"));
        Promotion promotion = Promotion.of("탄산2+1", 2, dateRange);
        Product expectedPromotionProduct = Product.of("콜라", 1000, 10, Optional.of(promotion));
        Product expectedProduct = Product.of("콜라", 1000, 0, Optional.empty());

        ConvenienceStore convenienceStore = convenienceStoreGnerator.generate();
        assertThat(convenienceStore)
                .extracting("products")
                .usingRecursiveComparison()
                .isEqualTo(Map.of("콜라", expectedProduct));
        assertThat(convenienceStore)
                .extracting("promotionProducts")
                .usingRecursiveComparison()
                .isEqualTo(Map.of("콜라", expectedPromotionProduct));

    }

    private class MockFileParser extends FileParser {
        private final Queue<List<String>> fileLines;

        public MockFileParser(List<List<String>> fileDatas) {
            fileLines = new ArrayDeque<>(fileDatas);
        }

        @Override
        public List<String> readAllFiles(String filePath) {
            if (fileLines.isEmpty()) {
                return null;
            }
            return fileLines.poll();
        }
    }

}
