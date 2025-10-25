package racingcar.domain;

import java.util.List;

import racingcar.domain.condition.MoveCondition;

public class RacingGame {
    private final Cars cars;
    private final int tryCount;
    private final MoveCondition condition;
    private int currentRound;

    public RacingGame(Cars cars, int tryCount, MoveCondition condition) {
        this.cars = cars;
        this.tryCount = tryCount;
        this.condition = condition;
        this.currentRound = 0;
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