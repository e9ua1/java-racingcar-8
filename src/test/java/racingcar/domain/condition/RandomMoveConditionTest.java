package racingcar.domain.condition;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("무작위 이동 조건 테스트")
class RandomMoveConditionTest {

    @Test
    @DisplayName("이동 조건은 boolean 값을 반환한다")
    void isSatisfiedReturnsBoolean() {
        // given
        MoveCondition condition = new RandomMoveCondition();

        // when
        boolean result = condition.isSatisfied();

        // then
        assertThat(result).isInstanceOf(Boolean.class);
    }

    @RepeatedTest(100)
    @DisplayName("반복 테스트 시 true와 false가 모두 나타난다")
    void isSatisfiedReturnsVariousResults() {
        // given
        MoveCondition condition = new RandomMoveCondition();

        // when
        boolean result = condition.isSatisfied();

        // then
        assertThat(result).isIn(true, false);
    }
}
