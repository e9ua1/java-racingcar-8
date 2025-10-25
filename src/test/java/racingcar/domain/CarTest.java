package racingcar.domain;

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
}
