package racingcar.view;

import java.util.List;

import racingcar.domain.Car;
import racingcar.domain.Cars;

public class OutputView {

    private static final String RESULT_HEADER = "\n실행 결과";
    private static final String WINNER_PREFIX = "최종 우승자 : ";
    private static final String NAME_POSITION_SEPARATOR = " : ";
    private static final String WINNER_DELIMITER = ", ";

    public void printHeader() {
        System.out.println(RESULT_HEADER);
    }

    public void printRound(Cars cars) {
        for (Car car : cars.getCars()) {
            printCarStatus(car);
        }
        System.out.println();
    }

    public void printWinners(List<String> winners) {
        System.out.println(WINNER_PREFIX + String.join(WINNER_DELIMITER, winners));
    }

    private void printCarStatus(Car car) {
        System.out.println(car.getName() + NAME_POSITION_SEPARATOR + car.getStatusBar());
    }
}