package racingcar.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import racingcar.domain.condition.MoveCondition;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("경주 게임 테스트")
class RacingGameTest {

    @Nested
    @DisplayName("게임을 생성한다")
    class CreateTest {

        @Test
        @DisplayName("게임이 정상적으로 생성된다")
        void createGame() {
            // given
            Cars cars = new Cars(List.of("pobi", "crong"));
            int tryCount = 5;
            MoveCondition alwaysTrue = () -> true;

            // when
            RacingGame game = new RacingGame(cars, tryCount, alwaysTrue);

            // then
            assertThat(game).isNotNull();
        }

        @Test
        @DisplayName("시도 횟수가 1 미만이면 예외가 발생한다")
        void createWithInvalidTryCount() {
            // given
            Cars cars = new Cars(List.of("pobi", "crong"));
            MoveCondition condition = () -> true;
            int invalidTryCount = 0;

            // when & then
            assertThatThrownBy(() -> new RacingGame(cars, invalidTryCount, condition))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("시도 횟수");
        }
    }

    @Nested
    @DisplayName("게임을 진행한다")
    class PlayTest {

        @Test
        @DisplayName("라운드를 진행하면 모든 자동차가 이동한다")
        void playRound() {
            // given
            Cars cars = new Cars(List.of("pobi", "crong"));
            MoveCondition alwaysTrue = () -> true;
            RacingGame game = new RacingGame(cars, 5, alwaysTrue);

            // when
            game.playRound();

            // then
            cars.getCars().forEach(car ->
                    assertThat(car.getPosition()).isEqualTo(1)
            );
        }

        @Test
        @DisplayName("지정된 횟수만큼 라운드가 존재한다")
        void hasNextRound() {
            // given
            Cars cars = new Cars(List.of("pobi"));
            MoveCondition alwaysTrue = () -> true;
            RacingGame game = new RacingGame(cars, 3, alwaysTrue);

            // when & then
            assertThat(game.hasNextRound()).isTrue();
            game.playRound();

            assertThat(game.hasNextRound()).isTrue();
            game.playRound();

            assertThat(game.hasNextRound()).isTrue();
            game.playRound();

            assertThat(game.hasNextRound()).isFalse();
        }
    }

    @Nested
    @DisplayName("게임 결과를 확인한다")
    class ResultTest {

        @Test
        @DisplayName("게임 종료 후 우승자를 반환한다")
        void getWinners() {
            // given
            Cars cars = new Cars(List.of("pobi", "crong", "honux"));
            MoveCondition alwaysTrue = () -> true;
            RacingGame game = new RacingGame(cars, 1, alwaysTrue);

            // when
            game.playRound();
            List<String> winners = game.getWinners();

            // then
            assertThat(winners).hasSize(3);
        }

        @Test
        @DisplayName("현재 자동차 상태를 조회한다")
        void getCars() {
            // given
            Cars cars = new Cars(List.of("pobi", "crong"));
            RacingGame game = new RacingGame(cars, 5, () -> true);

            // when
            Cars result = game.getCars();

            // then
            assertThat(result).isEqualTo(cars);
        }
    }
}