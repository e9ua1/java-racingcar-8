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
├── validation
│   └── InputValidator.java
└── view
    ├── InputView.java
    └── OutputView.java

src/test/java/racingcar
├── domain
│   ├── CarTest.java
│   ├── CarsTest.java
│   ├── RacingGameTest.java
│   └── condition
│       └── RandomMoveConditionTest.java
└── validation
    └── InputValidatorTest.java
```

---

### 클래스 다이어그램
```
┌─────────────────────────┐
│  RacingGameController   │
│─────────────────────────│
│ - inputView             │
│ - outputView            │
│ - racingGame            │
│─────────────────────────│
│ + run()                 │
└─────────────────────────┘
         │
         ├──────────────────┐
         │                  │
         ▼                  ▼
┌──────────────┐   ┌──────────────┐
│  InputView   │   │  OutputView  │
│──────────────│   │──────────────│
│              │   │              │
│──────────────│   │──────────────│
│+ readNames() │   │+ printRound()│
│+ readCount() │   │+ printWinner()│
└──────────────┘   └──────────────┘
         │
         ▼
┌──────────────────┐
│ InputValidator   │
│──────────────────│
│                  │
│──────────────────│
│+ validateName()  │
│+ validateCount() │
└──────────────────┘

┌─────────────────┐
│  RacingGame     │
│─────────────────│
│ - cars          │
│ - tryCount      │
│ - condition     │
│─────────────────│
│ + playRound()   │
│ + getWinners()  │
└─────────────────┘
         │
         │ uses
         ▼
┌─────────────────┐       ┌──────────────────┐
│     Cars        │◆─────▶│       Car        │
│─────────────────│       │──────────────────│
│ - carList       │       │ - name           │
│─────────────────│       │ - position       │
│ + moveAll()     │       │──────────────────│
│ + getWinners()  │       │ + move()         │
│ + getMaxPos()   │       │ + getPosition()  │
└─────────────────┘       │ + getName()      │
                          │ + getStatusBar() │
                          └──────────────────┘
                                   │
                                   │ uses
                                   ▼
                          ┌──────────────────────┐
                          │   MoveCondition      │
                          │──────────────────────│
                          │ + isSatisfied()      │
                          └──────────────────────┘
                                   △
                                   │
                          ┌────────┴─────────┐
                          │                  │
                ┌─────────────────────────────────┐
                │  RandomMoveCondition            │
                │─────────────────────────────────│
                │ - MOVE_THRESHOLD = 4            │
                │ - MIN_RANDOM_VALUE = 0          │
                │ - MAX_RANDOM_VALUE = 9          │
                │─────────────────────────────────│
                │ + isSatisfied()                 │
                └─────────────────────────────────┘
```

---

### 클래스별 책임

#### **도메인 계층 (domain)**

##### `Car` - 자동차
**책임:**
- 자동차의 이름과 위치를 관리한다.
- 주어진 조건에 따라 전진한다.
- 현재 위치를 시각적으로 표현한다.

**주요 메서드:**
- `Car(String name)`: 이름으로 자동차 생성
- `void move(boolean canMove)`: 조건이 참이면 전진
- `int getPosition()`: 현재 위치 반환
- `String getName()`: 자동차 이름 반환
- `String getStatusBar()`: 위치만큼 `-` 문자 반환

---

##### `Cars` - 자동차 집합
**책임:**
- 여러 자동차를 관리한다.
- 모든 자동차를 일괄 이동시킨다.
- 우승자를 판정한다.

**주요 메서드:**
- `Cars(List<String> names)`: 이름 목록으로 자동차들 생성
- `void moveAll(MoveCondition condition)`: 모든 자동차 이동
- `List<String> getWinners()`: 우승자 이름 목록 반환
- `int getMaxPosition()`: 최대 전진 거리 반환
- `List<Car> getCars()`: 자동차 목록 반환

---

##### `MoveCondition` - 이동 조건 인터페이스
**책임:**
- 전진 가능 여부를 판단하는 계약을 정의한다.

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

**주요 메서드:**
- `RacingGame(Cars cars, int tryCount, MoveCondition condition)`: 게임 생성
- `void playRound()`: 한 라운드 진행
- `boolean hasNextRound()`: 다음 라운드 존재 여부
- `Cars getCars()`: 현재 자동차 상태 반환
- `List<String> getWinners()`: 최종 우승자 반환

---

#### **검증 계층 (validation)**

##### `InputValidator` - 입력 검증
**책임:**
- 자동차 이름의 유효성을 검증한다.
- 시도 횟수의 유효성을 검증한다.
- 잘못된 입력에 대해 예외를 발생시킨다.

**주요 메서드:**
- `void validateCarName(String name)`: 이름 검증 (5자 이하, 공백 불가)
- `void validateCarNames(List<String> names)`: 이름 목록 검증
- `void validateTryCount(int count)`: 시도 횟수 검증 (양수)

**검증 규칙:**
- 이름은 1자 이상 5자 이하
- 이름은 공백만으로 구성될 수 없음
- 시도 횟수는 1 이상의 정수

---

#### **뷰 계층 (view)**

##### `InputView` - 입력 뷰
**책임:**
- 사용자로부터 입력을 받는다.
- 입력값을 적절한 형태로 파싱한다.
- 입력값의 유효성을 검증한다.

**주요 메서드:**
- `List<String> readCarNames()`: 자동차 이름 목록 입력
- `int readTryCount()`: 시도 횟수 입력

**입력 형식:**
- 자동차 이름: `pobi,crong,honux`
- 시도 횟수: `5`

---

##### `OutputView` - 출력 뷰
**책임:**
- 게임 진행 상황을 출력한다.
- 최종 우승자를 출력한다.
- 사용자에게 안내 메시지를 출력한다.

**주요 메서드:**
- `void printRoundResult(Cars cars)`: 라운드 결과 출력
- `void printWinners(List<String> winners)`: 우승자 출력
- `void printResultHeader()`: "실행 결과" 헤더 출력

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
- 예외를 처리한다.

**주요 메서드:**
- `void run()`: 게임 실행

**실행 흐름:**
1. 자동차 이름 입력
2. 시도 횟수 입력
3. 게임 진행 및 결과 출력
4. 우승자 발표

---

## 객체 간 협력 흐름

### 전체 흐름도
```
Application
    ↓
RacingGameController ←→ InputView ←→ InputValidator
    ↓                      ↓
    ↓                 OutputView
    ↓
RacingGame
    ↓
Cars (일급 컬렉션)
    ↓
Car[] + MoveCondition
```

### 상세 협력 과정

#### 1️⃣ 게임 초기화
```
Controller → InputView → InputValidator
         ↓
    Cars 생성 → Car 객체들 생성
         ↓
    RacingGame 생성 (Cars, tryCount, MoveCondition)
```

#### 2️⃣ 라운드 진행 (반복)
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

#### 3️⃣ 우승자 판정
```
Controller → RacingGame.getWinners()
         ↓
    Cars.getWinners()
         ↓
    Cars.getMaxPosition() → 최대 위치 계산
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
| **Controller** | RacingGameController | 전체 흐름 제어, View와 Domain 연결 |
| **View** | InputView, OutputView | 사용자 입출력 |
| **Validation** | InputValidator | 입력값 검증 |
| **Domain** | RacingGame | 게임 진행 관리 |
| **Domain** | Cars | 자동차 집합 관리 (일급 컬렉션) |
| **Domain** | Car | 개별 자동차 상태 및 이동 |
| **Domain** | MoveCondition | 전진 조건 판단 |

---

## 테스트 전략

### 테스트 계층 구조

#### `CarTest` - 자동차 단위 테스트
```
자동차 테스트
├── 생성 테스트
│   ├── 이름으로 자동차를 생성한다
│   ├── 이름이 빈 값이면 예외가 발생한다
│   ├── 이름이 5자를 초과하면 예외가 발생한다
│   └── 초기 위치는 0이다
├── 이동 테스트
│   ├── 전진 조건이 참이면 위치가 1 증가한다
│   ├── 전진 조건이 거짓이면 위치가 변하지 않는다
│   └── 여러 번 전진할 수 있다
└── 상태 표현 테스트
    ├── 위치가 0이면 빈 문자열을 반환한다
    ├── 위치만큼 '-' 문자를 반환한다
    └── 이름과 상태를 형식에 맞게 표현한다
```

#### `CarsTest` - 자동차 집합 테스트
```
자동차 집합 테스트
├── 생성 테스트
│   ├── 이름 목록으로 여러 자동차를 생성한다
│   └── 빈 목록으로 생성하면 예외가 발생한다
├── 이동 테스트
│   ├── 모든 자동차가 동시에 이동한다
│   └── 각 자동차는 독립적으로 이동 여부를 판단한다
└── 우승자 판정 테스트
    ├── 가장 많이 전진한 자동차를 찾는다
    ├── 우승자가 여러 명이면 모두 반환한다
    └── 최대 위치를 계산한다
```

#### `RandomMoveConditionTest` - 이동 조건 테스트
```
무작위 이동 조건 테스트
├── 전진 조건 테스트
│   ├── 무작위 값을 생성한다
│   └── 반복 테스트로 무작위성을 확인한다
└── 경계값 테스트
    └── 항상 boolean 값을 반환한다
```

#### `InputValidatorTest` - 입력 검증 테스트
```
입력 검증 테스트
├── 이름 검증
│   ├── 정상적인 이름은 통과한다
│   ├── 빈 이름은 예외가 발생한다
│   ├── 5자 초과 이름은 예외가 발생한다
│   └── 공백만 있는 이름은 예외가 발생한다
└── 시도 횟수 검증
    ├── 양수는 통과한다
    ├── 0은 예외가 발생한다
    └── 음수는 예외가 발생한다
```

---

### 1단계: 프로젝트 설정
- [x] 패키지 구조 생성
- [x] README.md 작성

### 2단계: 도메인 모델 (TDD)
- [x] `Car` 클래스
  - [x] 테스트: 생성 및 이름 반환
  - [x] 구현: 생성자, getName()
  - [x] 테스트: 전진 기능
  - [x] 구현: move(), getPosition()
  - [x] 테스트: 상태 표현
  - [x] 구현: getStatusBar()

- [x] `MoveCondition` 인터페이스 & `RandomMoveCondition`
  - [x] 테스트: 무작위 조건 판단
  - [x] 구현: isSatisfied()

- [x] `Cars` 클래스
  - [x] 테스트: 자동차 집합 생성
  - [x] 구현: 생성자
  - [x] 테스트: 일괄 이동
  - [x] 구현: moveAll()
  - [x] 테스트: 우승자 판정
  - [x] 구현: getWinners(), getMaxPosition()

- [x] `RacingGame` 클래스
  - [x] 테스트: 게임 진행
  - [x] 구현: playRound(), hasNextRound()
  - [x] 테스트: 우승자 조회
  - [x] 구현: getWinners()

### 3단계: 검증 계층
- [x] `InputValidator` 클래스
  - [x] 테스트: 이름 검증
  - [x] 구현: validateCarName()
  - [x] 테스트: 시도 횟수 검증
  - [x] 구현: validateTryCount()

### 4단계: 뷰 계층
- [x] `InputView` 클래스
  - [x] 구현: readCarNames()
  - [x] 구현: readTryCount()

- [x] `OutputView` 클래스
  - [x] 구현: printRoundResult()
  - [x] 구현: printWinners()

### 5단계: 컨트롤러 통합
- [x] `RacingGameController` 클래스
  - [x] 구현: run()
  - [x] 예외 처리 통합

### 6단계: 리팩토링 및 최종 점검
- [x] 매직 넘버 상수화
- [x] 메서드 분리 (indent depth 2 이하)
- [x] 코드 포매팅
- [x] 전체 테스트 실행 확인

---

## 실행 예시

### 입력
```
경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)
pobi,woni,jun
시도할 횟수는 몇 회인가요?
5
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

pobi : ---
woni : --
jun : ---

pobi : ----
woni : ---
jun : ----

pobi : -----
woni : ----
jun : -----

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