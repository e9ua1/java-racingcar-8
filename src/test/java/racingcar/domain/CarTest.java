package racingcar.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("자동차 테스트")
public class CarTest {
    @Nested
    @DisplayName("자동차 생성")
    class CreateTest {

        @Test
        @DisplayName("이름으로 자동차를 생성한다")
        void createWithName() {
            // given & when
            Car car = new Car("pobi");

            // then
            assertThat(car.getName()).isEqualTo("pobi");
        }

        @Test
        @DisplayName("초기 위치는 0이다")
        void initialPositionIsZero() {
            // given & when
            Car car = new Car("pobi");

            // then
            assertThat(car.getPosition()).isEqualTo(0);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "  "})
        @DisplayName("이름이 빈 값이거나 공백이면 예외가 발생한다")
        void createWithBlankName(String invalidName) {
            // when & then
            assertThatThrownBy(() -> new Car(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이름");
        }

        @ParameterizedTest
        @ValueSource(strings = {"doheeh", "doheeha", "doheehaa"})
        @DisplayName("이름이 5글자 초과하면 예외가 발생한다")
        void createWithLongName(String invalidName) {
            // when & then
            assertThatThrownBy(() -> new Car(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("5자");
        }
    }

    @Nested
    @DisplayName("자동차 이동")
    class MoveTest {

        private Car car;

        @BeforeEach
        void setUp() {
            car = new Car("pobi");
        }

        @Test
        @DisplayName("전진 조건이 참이면 위치가 1 증가한다")
        void moveWhenConditionTrue() {
            // when
            car.move(true);

            // then
            assertThat(car.getPosition()).isEqualTo(1);
        }

        @Test
        @DisplayName("전진 조건이 거짓이면 위치가 변하지 않는다")
        void notMoveWhenConditionFalse() {
            // when
            car.move(false);

            // then
            assertThat(car.getPosition()).isZero();
        }

        @Test
        @DisplayName("여러 번 전진할 수 있다")
        void moveMultipleTimes() {
            // when
            car.move(true);
            car.move(true);
            car.move(false);
            car.move(true);

            // then
            assertThat(car.getPosition()).isEqualTo(3);
        }
    }
}
