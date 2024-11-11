package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {
    @Test
    void 파일에_있는_상품_목록_출력() {
        assertSimpleTest(() -> {
            run("[물-1]", "N", "N");
            assertThat(output()).contains(
                    "- 콜라 1,000원 10개 탄산2+1",
                    "- 콜라 1,000원 10개",
                    "- 사이다 1,000원 8개 탄산2+1",
                    "- 사이다 1,000원 7개",
                    "- 오렌지주스 1,800원 9개 MD추천상품",
                    "- 오렌지주스 1,800원 재고 없음",
                    "- 탄산수 1,200원 5개 탄산2+1",
                    "- 탄산수 1,200원 재고 없음",
                    "- 물 500원 10개",
                    "- 비타민워터 1,500원 6개",
                    "- 감자칩 1,500원 5개 반짝할인",
                    "- 감자칩 1,500원 5개",
                    "- 초코바 1,200원 5개 MD추천상품",
                    "- 초코바 1,200원 5개",
                    "- 에너지바 2,000원 5개",
                    "- 정식도시락 6,400원 8개",
                    "- 컵라면 1,700원 1개 MD추천상품",
                    "- 컵라면 1,700원 10개"
            );
        });
    }

    @Test
    void 여러_개의_일반_상품_구매() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void 기간에_해당하지_않는_프로모션_적용() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 프로모션_적용_상품에_대해_고객이_해당_수량보다_적게_가져온_경우_Y를_입력하면_수량을_추가하여_결제한다() {
        assertSimpleTest(() -> {
            run("[콜라-2]", "Y", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("콜라1").contains("총구매액33,000").contains("행사할인-1,000");
        });
    }

    @Test
    void 프로모션_적용_상품에_대해_고객이_해당_수량보다_적게_가져온_경우_N을_입력하면_수량을_추가하지않고_결제한다() {
        assertSimpleTest(() -> {
            run("[콜라-2]", "N", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("콜라2").contains("총구매액22,000").contains("행사할인-0");
        });
    }

    @Test
    void 일부상품에_대해_정가로_구매해야_하는_경우_Y를_입력하면_일부_수량에_대해_정가로_구매한다() {
        assertSimpleTest(() -> {
            run("[콜라-11]", "Y", "Y", "N");
            assertThat(output().replaceAll("\\s", ""))
                    .contains("콜라11").contains("총구매액1111,000").contains("멤버십할인-600");
        });
    }

    @Test
    void 일부수량에_대해_정가로_구매해야_하는_경우_N을_입력하면_정가로_구매해야하는_수량을_제거하고_구매한다() {
        assertSimpleTest(() -> {
            run("[콜라-11]", "N", "Y", "N");
            assertThat(output().replaceAll("\\s", ""))
                    .contains("콜라9").contains("총구매액99,000").contains("멤버십할인-0");
        });
    }

    @Test
    void 멤버십_할인에_Y를_입력한다면_일반_구매내역에_대하여_30퍼센트_할인을_적용한다() {
        assertSimpleTest(() -> {
            run("[물-10]", "Y", "N");
            assertThat(output().replaceAll("\\s", ""))
                    .contains("물105,000").contains("총구매액105,000").contains("멤버십할인-1,500").contains("내실돈3,500");
        });
    }

    @Test
    void 멤버십_할인은_프로모션_적용_금액에는_적용하지_않는다() {
        assertSimpleTest(() -> {
            run("[콜라-9]", "Y", "N");
            assertThat(output().replaceAll("\\s", ""))
                    .contains("콜라99,000").contains("총구매액99,000").contains("멤버십할인-0").contains("내실돈6,000");
        });
    }

    @Test
    void 멤버십_할인에_N을_입력한다면_할인을_적용하지않는다() {
        assertSimpleTest(() -> {
            run("[물-10]", "N", "N");
            assertThat(output().replaceAll("\\s", ""))
                    .contains("물105,000").contains("총구매액105,000").contains("멤버십할인-0").contains("내실돈5,000");
        });
    }

    @Test
    void 고객이_상품을_구매한다면_결제된_수량만큼_해당_상품의_재고에서_차감한다() {
        assertSimpleTest(() -> {
            runException("[물-10]", "N", "Y", "[물-1]");
            assertThat(output().replaceAll("\\s", "")).contains("물105,000").contains("총구매액105,000")
                    .contains("물500원재고없음").contains("[ERROR]재고수량을초과하여구매할수없습니다.다시입력해주세요.");
        });
    }

    @Test
    void 프로모션_기간이라면_프로모션_재고를_우선적으로_차감한다() {
        assertSimpleTest(() -> {
            runException("[콜라-1]", "N", "Y", "[콜라-20]");
            assertThat(output().replaceAll("\\s", "")).contains("콜라11,000").contains("총구매액11,000")
                    .contains("콜라1,000원9개탄산2+1").contains("[ERROR]재고수량을초과하여구매할수없습니다.다시입력해주세요.");
        });
    }

    @Test
    void 프로모션_기간이지만_재고가_없는_경우_일반_재고를_사용한다(){
        assertSimpleTest(() -> {
            runException("[컵라면-2]", "Y", "N", "Y", "[컵라면-10]");
            assertThat(output().replaceAll("\\s", "")).contains("컵라면23,400").contains("총구매액23,400")
                    .contains("컵라면1,700원재고없음MD추천상품").contains("[ERROR]재고수량을초과하여구매할수없습니다.다시입력해주세요.");
        });
    }

    @Test
    void 구매할_제품과_수량의_형식이_올바르지_않다면_예외가_발생한다() {
        assertSimpleTest(() -> {
            runException("[컵라면-1],콜라-5");
            assertThat(output()).contains("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 존재하지_않는_상품을_구매하려_한다면_예외가_발생한다() {
        assertSimpleTest(() -> {
            runException("[감자-5]");
            assertThat(output()).contains("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 구매하려는_수량이_전체_재고보다_많다면_예외가_발생한다() {
        assertSimpleTest(() -> {
            runException("[콜라-21]");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }


    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
