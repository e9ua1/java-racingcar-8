package racingcar.domain;

import java.util.Collections;
import java.util.List;

import racingcar.domain.condition.MoveCondition;

public class Cars {

    private static final int MIN_CAR_COUNT = 1;
    private static final int DEFAULT_POSITION = 0;

    private final List<Car> cars;

    public Cars(List<String> names) {
        validateCarCount(names);
        this.cars = createCars(names);
    }

    private void validateCarCount(List<String> names) {
        if (names.size() < MIN_CAR_COUNT) {
            throw new IllegalArgumentException(
                    "자동차는 최소 " + MIN_CAR_COUNT + "대 이상이어야 합니다.");
        }
    }

    private List<Car> createCars(List<String> names) {
        return names.stream()
                .map(Car::new)
                .toList();
    }

    public void moveAll(MoveCondition condition) {
        for (Car car : cars) {
            car.move(condition.isSatisfied());
        }
    }

    public List<String> getWinners() {
        int maxPosition = getMaxPosition();
        return cars.stream()
                .filter(car -> car.isAt(maxPosition))
                .map(Car::getName)
                .toList();
    }

    public int getMaxPosition() {
        return cars.stream()
                .mapToInt(Car::getPosition)
                .max()
                .orElse(DEFAULT_POSITION);
    }

    public List<Car> getCars() {
        return Collections.unmodifiableList(cars);
    }
}