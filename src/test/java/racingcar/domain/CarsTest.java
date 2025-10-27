package racingcar.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import racingcar.domain.condition.MoveCondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("자동차 집합 테스트")
class CarsTest {

    @Nested
    @DisplayName("자동차 집합을 생성한다")
    class CreateTest {

        @Test
        @DisplayName("이름 목록으로 여러 자동차를 생성한다")
        void createWithNames() {
            // given
            List<String> names = List.of("pobi", "crong", "honux");

            // when
            Cars cars = new Cars(names);

            // then
            assertThat(cars.getCars()).hasSize(3);
        }

        @Test
        @DisplayName("빈 목록으로 생성하면 예외가 발생한다")
        void createWithEmptyList() {
            // given
            List<String> emptyNames = List.of();

            // when & then
            assertThatThrownBy(() -> new Cars(emptyNames))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("최소");
        }
    }

    @Nested
    @DisplayName("자동차를 일괄 이동한다")
    class MoveAllTest {

        private Cars cars;

        @BeforeEach
        void setUp() {
            List<String> names = List.of("pobi", "crong", "honux");
            cars = new Cars(names);
        }

        @Test
        @DisplayName("모든 자동차가 이동 조건을 확인한다")
        void moveAllCars() {
            // given
            MoveCondition alwaysTrue = () -> true;

            // when
            cars.moveAll(alwaysTrue);

            // then
            cars.getCars().forEach(car ->
                    assertThat(car.getPosition()).isEqualTo(1)
            );
        }

        @Test
        @DisplayName("조건에 따라 각 자동차는 독립적으로 이동한다")
        void moveIndependently() {
            // given
            MoveCondition alwaysFalse = () -> false;

            // when
            cars.moveAll(alwaysFalse);

            // then
            cars.getCars().forEach(car ->
                    assertThat(car.getPosition()).isZero()
            );
        }
    }

    @Nested
    @DisplayName("우승자를 판정한다")
    class WinnerTest {

        @Test
        @DisplayName("가장 많이 전진한 자동차의 최대 위치를 반환한다")
        void getMaxPosition() {
            // given
            Cars cars = new Cars(List.of("pobi", "crong", "honux"));
            MoveCondition twoTimesMoveCondition = new NTimesTrueMoveCondition(2);

            // when
            cars.moveAll(twoTimesMoveCondition);

            // then
            assertThat(cars.getMaxPosition()).isEqualTo(1);
        }

        @Test
        @DisplayName("단독 우승자를 찾는다")
        void findSingleWinner() {
            // given
            Cars cars = new Cars(List.of("pobi", "crong", "honux"));
            MoveCondition oneTimeMoveCondition = new NTimesTrueMoveCondition(1);

            // when
            cars.moveAll(oneTimeMoveCondition);
            List<String> winners = cars.getWinners();

            // then
            assertThat(winners).containsExactly("pobi");
        }
    }

    // 테스트 헬퍼 클래스
    private static class NTimesTrueMoveCondition implements MoveCondition {

        private final int times;
        private int count;

        public NTimesTrueMoveCondition(int times) {
            this.times = times;
            this.count = 0;
        }

        @Override
        public boolean isSatisfied() {
            return count++ < times;
        }
    }
}