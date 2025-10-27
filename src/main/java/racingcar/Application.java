package racingcar;

import camp.nextstep.edu.missionutils.Console;

import racingcar.controller.RacingGameController;
import racingcar.view.InputView;
import racingcar.view.OutputView;

public class Application {

    public static void main(String[] args) {
        try {
            InputView inputView = new InputView();
            OutputView outputView = new OutputView();
            RacingGameController controller = new RacingGameController(inputView, outputView);
            controller.run();
        } finally {
            Console.close();
        }
    }
}
