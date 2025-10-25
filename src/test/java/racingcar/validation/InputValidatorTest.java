package racingcar.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("입력 검증 테스트")
class InputValidatorTest {

    @Nested
    @DisplayName("자동차 이름 검증")
    class CarNameValidationTest {

        @ParameterizedTest
        @ValueSource(strings = {"neo", "brown", "sally", "w", "cloud"})
        @DisplayName("정상적인 이름은 통과한다")
        void validateValidName(String validName) {
            // when & then
            assertThatCode(() -> InputValidator.validateCarName(validName))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "  "})
        @DisplayName("빈 이름은 예외가 발생한다")
        void validateBlankName(String blankName) {
            // when & then
            assertThatThrownBy(() -> InputValidator.validateCarName(blankName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이름");
        }

        @ParameterizedTest
        @ValueSource(strings = {"james!", "sophia1", "oliver123"})
        @DisplayName("5자를 초과하는 이름은 예외가 발생한다")
        void validateLongName(String longName) {
            // when & then
            assertThatThrownBy(() -> InputValidator.validateCarName(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("5자");
        }
    }

    @Nested
    @DisplayName("자동차 이름 목록 검증")
    class CarNamesValidationTest {

        @Test
        @DisplayName("정상적인 이름 목록은 통과한다")
        void validateValidNames() {
            // given
            List<String> names = List.of("neo", "brown", "sally");

            // when & then
            assertThatCode(() -> InputValidator.validateCarNames(names))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("빈 목록은 예외가 발생한다")
        void validateEmptyList() {
            // given
            List<String> emptyNames = List.of();

            // when & then
            assertThatThrownBy(() -> InputValidator.validateCarNames(emptyNames))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("최소");
        }

        @Test
        @DisplayName("목록에 잘못된 이름이 있으면 예외가 발생한다")
        void validateNamesWithInvalidName() {
            // given
            List<String> names = List.of("neo", "verylongname", "sally");

            // when & then
            assertThatThrownBy(() -> InputValidator.validateCarNames(names))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("시도 횟수 검증")
    class TryCountValidationTest {

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 100})
        @DisplayName("양수는 통과한다")
        void validatePositiveCount(int positiveCount) {
            // when & then
            assertThatCode(() -> InputValidator.validateTryCount(positiveCount))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10})
        @DisplayName("0 이하는 예외가 발생한다")
        void validateNonPositiveCount(int nonPositiveCount) {
            // when & then
            assertThatThrownBy(() -> InputValidator.validateTryCount(nonPositiveCount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("1 이상");
        }
    }
}