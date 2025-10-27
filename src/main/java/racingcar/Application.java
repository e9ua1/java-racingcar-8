package racingcar;

import camp.nextstep.edu.missionutils.Console;

import racingcar.controller.RacingGameController;

public class Application {

    public static void main(String[] args) {
        try {
            RacingGameController controller = new RacingGameController();
            controller.run();
        } finally {
            Console.close();
        }
    }
}
