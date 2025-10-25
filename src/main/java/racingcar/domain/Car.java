package racingcar.domain;

public class Car {
    private static final int MAX_NAME_LENGTH = 5;
    private static final String POSITION_MARKER = "-";

    private final String name;
    private int position;

    public Car(String name) {
        validateName(name);
        this.name = name;
        this.position = 0;
    }

    private void validateName(String name) {
        validateNameNotBlank(name);
        validateNameLength(name);
    }

    private void validateNameNotBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("자동차 이름은 빈 값일 수 없습니다.");
        }
    }

    private void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("자동차 이름은 5자 이하여야 합니다.");
        }
    }

    public void move(boolean canMove) {
        if (canMove) {
            position++;
        }
    }

    public String getStatusBar() {
        return POSITION_MARKER.repeat(position);
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}