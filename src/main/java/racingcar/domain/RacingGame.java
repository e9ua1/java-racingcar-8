package racingcar.domain;

import java.util.List;

import racingcar.domain.condition.MoveCondition;

public class RacingGame {

    private static final int MIN_TRY_COUNT = 1;

    private final Cars cars;
    private final int tryCount;
    private final MoveCondition condition;
    private int currentRound;

    public RacingGame(Cars cars, int tryCount, MoveCondition condition) {
        validateTryCount(tryCount);
        this.cars = cars;
        this.tryCount = tryCount;
        this.condition = condition;
        this.currentRound = 0;
    }

    private void validateTryCount(int tryCount) {
        if (tryCount < MIN_TRY_COUNT) {
            throw new IllegalArgumentException("시도 횟수는 " + MIN_TRY_COUNT + " 이상이어야 합니다.");
        }
    }

    public void playRound() {
        cars.moveAll(condition);
        currentRound++;
    }

    public boolean hasNextRound() {
        return currentRound < tryCount;
    }

    public Cars getCars() {
        return cars;
    }

    public List<String> getWinners() {
        return cars.getWinners();
    }
}