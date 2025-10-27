# 자동차 경주 게임

## 프로젝트 개요

초간단 자동차 경주 게임을 구현합니다.
- 주어진 횟수 동안 n대의 자동차가 전진 또는 정지합니다.
- 각 자동차는 0에서 9 사이의 무작위 값을 기반으로 4 이상일 때 전진합니다.
- 경주가 끝나면 가장 많이 전진한 자동차를 우승자로 선정합니다.

---

## 학습 목표

- **테스트 주도 개발(TDD)**: 테스트 코드를 먼저 작성하고 구현하는 방식 학습
- **단일 책임 원칙(SRP)**: 하나의 클래스/메서드는 하나의 책임만 가지도록 설계
- **함수 분리**: 큰 함수를 작은 단위로 분리하여 가독성과 유지보수성 향상
- **객체 지향 설계**: 책임 주도 설계를 통한 객체 간 협력 구조 구축
- **의존성 주입**: 제어의 역전(IoC)을 통한 유연한 설계

---

## 기능 요구사항

### 입력
- [x] 경주할 자동차 이름을 쉼표(`,`)로 구분하여 입력받는다.
- [x] 시도할 횟수를 입력받는다.

### 자동차 생성 및 검증
- [x] 자동차 이름은 5자 이하만 가능하다.
- [x] 자동차 이름이 빈 값이면 예외를 발생시킨다.
- [x] 자동차 이름이 5자를 초과하면 예외를 발생시킨다.
- [x] 입력된 이름으로 자동차 객체들을 생성한다.

### 경주 진행
- [x] 0에서 9 사이의 무작위 값을 생성한다.
- [x] 무작위 값이 4 이상이면 전진한다.
- [x] 무작위 값이 4 미만이면 정지한다.
- [x] 각 자동차는 독립적으로 전진 여부를 판단한다.
- [x] 주어진 횟수만큼 경주를 반복한다.
- [x] 매 회차마다 각 자동차의 이동 결과를 출력한다.

### 우승자 판정
- [x] 가장 많이 전진한 자동차를 찾는다.
- [x] 우승자가 여러 명이면 모두 출력한다.
- [x] 우승자가 여러 명일 경우 쉼표(`,`)와 공백으로 구분한다.

### 출력
- [x] 각 회차마다 자동차 이름과 전진 상태를 출력한다.
  - 형식: `{자동차이름} : {-의 개수}`
- [x] 최종 우승자를 출력한다.
  - 단독 우승: `최종 우승자 : {이름}`
  - 공동 우승: `최종 우승자 : {이름1}, {이름2}`

### 예외 처리
- [x] 잘못된 값 입력 시 `IllegalArgumentException`을 발생시킨다.
- [x] 예외 발생 후 애플리케이션은 종료된다.

---

## 프로젝트 설계

### 패키지 구조
```
src/main/java/racingcar
├── Application.java
├── controller
│   └── RacingGameController.java
├── domain
│   ├── Car.java
│   ├── Cars.java
│   ├── RacingGame.java
│   └── condition
│       ├── MoveCondition.java
│       └── RandomMoveCondition.java
└── view
    ├── InputParser.java
    ├── InputView.java
    └── OutputView.java

src/test/java/racingcar
├── ApplicationTest.java
└── domain
    ├── CarTest.java
    ├── CarsTest.java
    ├── RacingGameTest.java
    └── condition
        └── RandomMoveConditionTest.java
```

---

### 클래스 다이어그램
```
                ┌─────────────────────────┐
                │      Application        │
                │─────────────────────────│
                │ + main()                │
                └─────────┬───────────────┘
                          │ creates
                          ▼
                ┌─────────────────────────┐
                │  RacingGameController   │
                │─────────────────────────│
                │ - inputView             │◀── 생성자 주입
                │ - outputView            │◀── 생성자 주입
                │─────────────────────────│
                │ + run()                 │
                │ - playGame()            │
                │ - printWinners()        │
                └─────────┬───────────────┘
                          │
              ┌───────────┴───────────────┐
              │                           │
              ▼                           ▼
    ┌──────────────────┐        ┌──────────────────────┐
    │   InputView      │        │    OutputView        │
    │──────────────────│        │──────────────────────│
    │+ readCarNames()  │        │+ printResultHeader() │
    │+ readTryCount()  │        │+ printRoundResult()  │
    └────────┬─────────┘        │+ printWinners()      │
             │                  └──────────────────────┘
             │ uses
             ▼
    ┌─────────────────────┐
    │    InputParser      │
    │─────────────────────│
    │+ parseCarNames()    │ (static)
    │+ parseTryCount()    │ (static)
    └─────────────────────┘
             │
             │ creates
             ▼
    ┌─────────────────────┐
    │    RacingGame       │
    │─────────────────────│
    │ - cars              │
    │ - tryCount          │
    │ - condition         │
    │ - currentRound      │
    │─────────────────────│
    │ + playRound()       │
    │ + hasNextRound()    │
    │ + getCars()         │
    │ + getWinners()      │
    └──────────┬──────────┘
               │ has
               ▼
    ┌─────────────────────┐            ┌──────────────────┐
    │       Cars          │◆──────────▶│       Car        │
    │─────────────────────│            │──────────────────│
    │ - cars              │            │ - name           │
    │─────────────────────│            │ - position       │
    │ + moveAll()         │            │──────────────────│
    │ + getWinners()      │            │ + move()         │
    │ + getMaxPosition()  │            │ + isAt()         │◀── Tell, Don't Ask
    │ + getCars()         │            │ + getPosition()  │
    └─────────────────────┘            │ + getName()      │
                                       │ + getStatusBar() │
                                       └────────┬─────────┘
                                                │ uses
                                                ▼
                                ┌──────────────────────────┐
                                │    <<interface>>         │
                                │    MoveCondition         │
                                │──────────────────────────│
                                │ + isSatisfied(): boolean │
                                └────────────┬─────────────┘
                                             │ implements
                                             ▼
                                ┌─────────────────────────────┐
                                │  RandomMoveCondition        │
                                │─────────────────────────────│
                                │ - MOVE_THRESHOLD = 4        │
                                │ - MIN_RANDOM_VALUE = 0      │
                                │ - MAX_RANDOM_VALUE = 9      │
                                │─────────────────────────────│
                                │ + isSatisfied(): boolean    │
                                └─────────────────────────────┘
```

---

### 클래스별 책임

#### **도메인 계층 (domain)**

##### `Car` - 자동차
**책임:**
- 자동차의 이름과 위치를 관리한다.
- 주어진 조건에 따라 전진한다.
- 자신의 위치 상태를 판단한다. (Tell, Don't Ask 적용)
- 현재 위치를 시각적으로 표현한다.
- 생성 시 이름 유효성을 검증한다.

**주요 메서드:**
- `Car(String name)`: 이름으로 자동차 생성 (이름 검증 포함)
- `void move(boolean canMove)`: 조건이 참이면 전진
- `boolean isAt(int position)`: 특정 위치에 있는지 확인 (캡슐화)
- `int getPosition()`: 현재 위치 반환
- `String getName()`: 자동차 이름 반환
- `String getStatusBar()`: 위치만큼 `-` 문자 반환

**검증 규칙:**
- 이름은 1자 이상 5자 이하
- 이름은 공백만으로 구성될 수 없음

---

##### `Cars` - 자동차 집합
**책임:**
- 여러 자동차를 관리한다. (일급 컬렉션)
- 모든 자동차를 일괄 이동시킨다.
- 우승자를 판정한다.
- 생성 시 자동차 개수를 검증한다.

**주요 메서드:**
- `Cars(List<String> names)`: 이름 목록으로 자동차들 생성 (검증 포함)
- `void moveAll(MoveCondition condition)`: 모든 자동차 이동
- `List<String> getWinners()`: 우승자 이름 목록 반환
- `int getMaxPosition()`: 최대 전진 거리 반환
- `List<Car> getCars()`: 불변 자동차 목록 반환

**검증 규칙:**
- 최소 1대 이상의 자동차 필요

**상수:**
- `DEFAULT_POSITION = 0`: 자동차가 없을 때 기본 위치

---

##### `MoveCondition` - 이동 조건 인터페이스
**책임:**
- 전진 가능 여부를 판단하는 계약을 정의한다. (전략 패턴)

**주요 메서드:**
- `boolean isSatisfied()`: 전진 조건 만족 여부 반환

---

##### `RandomMoveCondition` - 무작위 이동 조건
**책임:**
- 0~9 사이의 무작위 값을 생성한다.
- 무작위 값이 4 이상인지 판단한다.

**주요 메서드:**
- `boolean isSatisfied()`: 무작위 값이 4 이상이면 true

**상수:**
- `MOVE_THRESHOLD = 4`: 전진 기준값
- `MIN_RANDOM_VALUE = 0`: 최소 무작위 값
- `MAX_RANDOM_VALUE = 9`: 최대 무작위 값

---

##### `RacingGame` - 경주 게임
**책임:**
- 경주의 전체 흐름을 관리한다.
- 지정된 횟수만큼 라운드를 진행한다.
- 각 라운드의 결과를 제공한다.
- 생성 시 시도 횟수를 검증한다.

**주요 메서드:**
- `RacingGame(Cars cars, int tryCount, MoveCondition condition)`: 게임 생성 (검증 포함)
- `void playRound()`: 한 라운드 진행
- `boolean hasNextRound()`: 다음 라운드 존재 여부
- `Cars getCars()`: 현재 자동차 상태 반환
- `List<String> getWinners()`: 최종 우승자 반환

**검증 규칙:**
- 시도 횟수는 1 이상

---

#### **뷰 계층 (view)**

##### `InputView` - 입력 뷰
**책임:**
- 사용자로부터 입력을 받는다.
- 입력 메시지를 출력한다.

**주요 메서드:**
- `String readCarNames()`: 자동차 이름 문자열 입력
- `String readTryCount()`: 시도 횟수 문자열 입력

**입력 형식:**
- 자동차 이름: `pobi,crong,honux`
- 시도 횟수: `5`

---

##### `InputParser` - 입력 파싱 유틸리티
**책임:**
- 입력 문자열을 적절한 타입으로 변환한다.
- 파싱 중 발생하는 예외를 처리한다.

**주요 메서드:**
- `static List<String> parseCarNames(String input)`: 쉼표로 구분된 이름을 리스트로 변환
- `static int parseTryCount(String input)`: 문자열을 정수로 변환

**설계 이유:**
- View는 입출력만, Parser는 데이터 변환만 담당 (단일 책임 원칙)
- 파싱 로직을 독립적으로 테스트 가능

---

##### `OutputView` - 출력 뷰
**책임:**
- 게임 진행 상황을 출력한다.
- 최종 우승자를 출력한다.
- 사용자에게 안내 메시지를 출력한다.

**주요 메서드:**
- `void printResultHeader()`: "실행 결과" 헤더 출력
- `void printRoundResult(Cars cars)`: 라운드 결과 출력
- `void printWinners(List<String> winners)`: 우승자 출력

**출력 형식:**
```
pobi : ---
crong : ----
honux : --
```

---

#### **컨트롤러 계층 (controller)**

##### `RacingGameController` - 게임 컨트롤러
**책임:**
- 애플리케이션의 전체 흐름을 제어한다.
- View와 Domain을 연결한다.
- 예외를 전파한다.

**주요 메서드:**
- `RacingGameController(InputView, OutputView)`: 의존성 주입 생성자
- `void run()`: 게임 실행
- `void playGame(RacingGame game)`: 게임 진행
- `void printWinners(RacingGame game)`: 우승자 출력

**실행 흐름:**
1. 자동차 이름 입력 및 파싱
2. 시도 횟수 입력 및 파싱
3. 게임 진행 및 결과 출력
4. 우승자 발표

**의존성 주입:**
- InputView와 OutputView를 생성자로 주입받음
- 테스트 용이성 및 유연성 확보

---

#### **애플리케이션 진입점**

##### `Application`
**책임:**
- 프로그램의 시작점
- 의존성 생성 및 조립
- 리소스 관리 (Console.close())

**주요 메서드:**
- `static void main(String[] args)`: 프로그램 진입점

---

## 객체 간 협력 흐름

### 전체 흐름도
```
Application (의존성 생성 및 조립)
    ↓
    ├─ InputView
    ├─ OutputView
    └─ RacingGameController (의존성 주입) ←→ InputParser
            ↓
        RacingGame
            ↓
        Cars (일급 컬렉션)
            ↓
        Car[] + MoveCondition
```

### 상세 협력 과정

#### 1️⃣ 애플리케이션 시작 및 의존성 주입
```
Application.main()
    ↓
InputView, OutputView 생성
    ↓
RacingGameController 생성 (의존성 주입)
    ↓
controller.run()
```

#### 2️⃣ 게임 초기화
```
Controller → InputView.readCarNames()
         ↓
    InputParser.parseCarNames() → List<String>
         ↓
    Cars 생성 → Car 객체들 생성 (각자 이름 검증)
         ↓
Controller → InputView.readTryCount()
         ↓
    InputParser.parseTryCount() → int
         ↓
    RacingGame 생성 (Cars, tryCount 검증, MoveCondition)
```

#### 3️⃣ 라운드 진행 (반복)
```
Controller → RacingGame.playRound()
         ↓
    Cars.moveAll(condition)
         ↓
    각 Car.move(condition.isSatisfied())
         ↓
    RandomMoveCondition (0~9 무작위 → 4 이상이면 true)
         ↓
    Car.position 증가 또는 유지
         ↓
Controller → OutputView.printRoundResult(Cars)
         ↓
    각 Car의 이름과 위치(-) 출력
```

#### 4️⃣ 우승자 판정
```
Controller → RacingGame.getWinners()
         ↓
    Cars.getWinners()
         ↓
    Cars.getMaxPosition() → 최대 위치 계산
         ↓
    각 Car.isAt(maxPosition) 확인 (Tell, Don't Ask)
         ↓
    최대 위치와 같은 Car들의 이름 수집
         ↓
Controller → OutputView.printWinners(winners)
         ↓
    쉼표로 구분된 우승자 출력
```

### 계층별 책임

| 계층 | 클래스 | 책임 |
|------|--------|------|
| **Application** | Application | 의존성 생성 및 조립, 리소스 관리 |
| **Controller** | RacingGameController | 전체 흐름 제어, View와 Domain 연결 |
| **View** | InputView | 사용자 입력 받기 |
| **View** | InputParser | 입력 문자열 파싱 및 변환 |
| **View** | OutputView | 결과 출력 |
| **Domain** | RacingGame | 게임 진행 관리, tryCount 검증 |
| **Domain** | Cars | 자동차 집합 관리 (일급 컬렉션), 개수 검증 |
| **Domain** | Car | 개별 자동차 상태 및 이동, 이름 검증 |
| **Domain** | MoveCondition | 전진 조건 판단 (전략 패턴) |

---

## 테스트 전략

### 테스트 계층 구조

#### `CarTest` - 자동차 단위 테스트
```
자동차 테스트
├── 자동차를 생성한다
│   ├── 이름으로 자동차를 생성한다
│   ├── 초기 위치는 0이다
│   ├── 이름이 빈 값이거나 공백이면 예외가 발생한다
│   └── 이름이 5글자 초과하면 예외가 발생한다
├── 자동차를 이동한다
│   ├── 전진 조건이 참이면 위치가 1 증가한다
│   ├── 전진 조건이 거짓이면 위치가 변하지 않는다
│   └── 여러 번 전진할 수 있다
└── 자동차 상태를 표현한다
    ├── 위치가 0이면 빈 문자열을 반환한다
    └── 위치만큼 '-' 문자를 반환한다
```

#### `CarsTest` - 자동차 집합 테스트
```
자동차 집합 테스트
├── 자동차 집합을 생성한다
│   ├── 이름 목록으로 여러 자동차를 생성한다
│   └── 빈 목록으로 생성하면 예외가 발생한다
├── 자동차를 일괄 이동한다
│   ├── 모든 자동차가 이동 조건을 확인한다
│   └── 조건에 따라 각 자동차는 독립적으로 이동한다
└── 우승자를 판정한다
    ├── 가장 많이 전진한 자동차의 최대 위치를 반환한다
    └── 단독 우승자를 찾는다
```

#### `RacingGameTest` - 경주 게임 테스트
```
경주 게임 테스트
├── 게임을 생성한다
│   ├── 게임이 정상적으로 생성된다
│   └── 시도 횟수가 1 미만이면 예외가 발생한다
├── 게임을 진행한다
│   ├── 라운드를 진행하면 모든 자동차가 이동한다
│   └── 지정된 횟수만큼 라운드가 존재한다
└── 게임 결과를 확인한다
    ├── 게임 종료 후 우승자를 반환한다
    └── 현재 자동차 상태를 조회한다
```

#### `RandomMoveConditionTest` - 이동 조건 테스트
```
무작위 이동 조건 테스트
├── 이동 조건은 boolean 값을 반환한다
└── 반복 테스트 시 true와 false가 모두 나타난다
```

---

## 구현 순서
1. [x] 도메인 계층 (Car, Cars, RacingGame, MoveCondition)
2. [x] 뷰 계층 (InputView, OutputView)
3. [x] 컨트롤러 통합 (RacingGameController)
4. [x] 리팩토링 - 도메인 중심 검증으로 전환
5. [x] 리팩토링 - InputParser 분리 (책임 분리)
6. [x] 리팩토링 - 의존성 주입 적용
7. [x] 리팩토링 - Tell, Don't Ask 원칙 적용
8. [x] 테스트 완성

---

## 실행 예시

### 입력
```
경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)
pobi,woni,jun
시도할 횟수는 몇 회인가요?
2
```

### 출력
```
실행 결과
pobi : -
woni : 
jun : -

pobi : --
woni : -
jun : --

최종 우승자 : pobi, jun
```

---

## 참고 자료

### 사용 라이브러리
- `camp.nextstep.edu.missionutils.Randoms`: 무작위 값 생성
- `camp.nextstep.edu.missionutils.Console`: 콘솔 입력

### 테스트 라이브러리
- JUnit 5: 테스트 프레임워크
- AssertJ: 유창한 assertion 라이브러리

### 코딩 컨벤션
- Java Style Guide 준수
- 라인 길이 120자 이하 유지
- Indent depth 2 이하 유지
- 3항 연산자 사용 금지
- 축약 금지, 의미 있는 이름 사용

---

## 주의사항

1. **테스트 실행**: 제출 전 `./gradlew clean test` 실행하여 모든 테스트 통과 확인
2. **출력 형식**: 요구사항의 출력 형식을 정확히 준수
3. **예외 처리**: 잘못된 입력 시 `IllegalArgumentException` 발생 후 종료
4. **라이브러리**: 제공된 `Randoms`, `Console` 라이브러리 사용 필수
5. **커밋**: 기능 단위로 작게 나누어 커밋
6. **코드 품질**: 1주차 피드백 반영 (이름 짓기, 공백, 주석 등)

---

## 핵심 설계 결정

### 1. 도메인 중심 검증
- **Car**: 생성자에서 이름 검증 (빈 값, 5자 초과)
- **Cars**: 생성자에서 자동차 개수 검증 (최소 1대)
- **RacingGame**: 생성자에서 tryCount 검증 (1 이상)
- 도메인 객체가 스스로 유효성을 보장하는 객체지향적 설계

### 2. 전략 패턴
- MoveCondition 인터페이스로 이동 조건 추상화
- RandomMoveCondition 구현체
- 테스트에서 조건을 주입하여 검증 용이
- 다른 이동 조건으로 쉽게 확장 가능

### 3. 일급 컬렉션
- Cars 클래스로 List<Car> 캡슐화
- 자동차 집합 관련 로직 응집
- 우승자 판정, 일괄 이동 등 책임 명확
- 불변 리스트 반환으로 외부 변경 방지

### 4. 의존성 주입 (Dependency Injection)
- Controller가 View 의존성을 생성자로 주입받음
- 제어의 역전(IoC) 원칙 적용
- 테스트 용이성 향상 (Mock 주입 가능)
- View 구현체 변경 용이 (Console → File/GUI)

### 5. 책임 분리
- **InputView**: 입력 받기만
- **InputParser**: 파싱 및 변환만
- **OutputView**: 출력만
- **Controller**: 흐름 제어만
- 단일 책임 원칙(SRP) 준수

### 6. Tell, Don't Ask 원칙
- `Car.isAt(position)`: Car에게 물어봄
- `car.getPosition() == maxPosition` 대신 `car.isAt(maxPosition)` 사용
- 객체의 내부 상태를 직접 꺼내지 않고, 객체에게 판단 요청
- 캡슐화 강화 및 객체 책임 명확화

### 7. 매직 넘버 상수화
- 모든 숫자 리터럴을 의미 있는 상수로 선언
- `DEFAULT_POSITION`, `MIN_CAR_COUNT`, `MOVE_THRESHOLD` 등
- 가독성 향상 및 유지보수 용이

### 8. 리소스 관리
- `Console.close()`를 Application에서 관리
- try-finally로 리소스 안전 해제
- Controller는 흐름 제어에만 집중

---