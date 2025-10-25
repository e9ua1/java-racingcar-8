package racingcar.validation;

import java.util.List;

public class InputValidator {
    private static final int MAX_NAME_LENGTH = 5;
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MIN_CAR_COUNT = 1;
    private static final int MIN_TRY_COUNT = 1;

    private InputValidator() {
    }

    public static void validateCarName(String name) {
        validateNameNotBlank(name);
        validateNameLength(name);
    }

    public static void validateCarNames(List<String> names) {
        validateNamesNotEmpty(names);
        for (String name : names) {
            validateCarName(name);
        }
    }

    public static void validateTryCount(int count) {
        if (count < MIN_TRY_COUNT) {
            throw new IllegalArgumentException("시도 횟수는 " + MIN_TRY_COUNT + " 이상이어야 합니다.");
        }
    }

    private static void validateNameNotBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("자동차 이름은 빈 값일 수 없습니다.");
        }
    }

    private static void validateNameLength(String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("자동차 이름은 " + MIN_NAME_LENGTH + "자 이상 "
                    + MAX_NAME_LENGTH + "자 이하여야 합니다.");
        }
    }

    private static void validateNamesNotEmpty(List<String> names) {
        if (names.isEmpty()) {
            throw new IllegalArgumentException("자동차는 최소 " + MIN_CAR_COUNT + "대 이상이어야 합니다.");
        }
    }
}