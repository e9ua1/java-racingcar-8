package racingcar.controller;

import camp.nextstep.edu.missionutils.Console;

import java.util.List;

import racingcar.domain.Cars;
import racingcar.domain.RacingGame;
import racingcar.domain.condition.MoveCondition;
import racingcar.domain.condition.RandomMoveCondition;
import racingcar.view.InputView;
import racingcar.view.OutputView;

public class RacingGameController {

    private final InputView inputView;
    private final OutputView outputView;

    public RacingGameController() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    public void run() {
        try {
            List<String> carNames = inputView.readCarNames();
            int tryCount = inputView.readTryCount();

            Cars cars = new Cars(carNames);
            MoveCondition condition = new RandomMoveCondition();
            RacingGame game = new RacingGame(cars, tryCount, condition);

            playGame(game);
            printWinners(game);
        } finally {
            Console.close();
        }
    }

    private void playGame(RacingGame game) {
        outputView.printResultHeader();
        while (game.hasNextRound()) {
            game.playRound();
            outputView.printRoundResult(game.getCars());
        }
    }

    private void printWinners(RacingGame game) {
        List<String> winners = game.getWinners();
        outputView.printWinners(winners);
    }
}