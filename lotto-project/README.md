# Lotto Project

콘솔에서 동작하는 간단한 로또 시스템입니다. 돈을 입금하고, 티켓을 구매하고, 회차를 종료해 당첨 결과를 확인할 수 있습니다. 회차 이력과 계좌 잔액은 파일로 영속화되어 프로그램을 껐다 켜도 이어서 진행할 수 있습니다.

## 요구 환경

- Kotlin 2.3.10 (JVM)
- JVM Toolchain 19
- Gradle (Wrapper 포함)

## 실행

```bash
./gradlew run # gradlew 실행
./gradlew jar # jar 실행
jar build/libs/lotto-project-1.0-SNAPSHOT.jar
```

종료는 메뉴에서 `0`을 선택합니다.

## 로또 규칙

- 티켓 한 장: **1,000원**
- 번호: **1 ~ 45** 중 **6개**, 중복 없음
- 보너스 번호: **1개**, 당첨 번호와 중복 없음
- 당첨 번호는 해당 회차의 **모든 구매가 끝난 뒤** 랜덤으로 결정됩니다.

### 등수 / 상금

| 등수 | 조건             | 상금             |
|----|----------------|----------------|
| 1등 | 6개 일치          | 2,100,000,000원 |
| 2등 | 5개 일치 + 보너스 일치 | 60,000,000원    |
| 3등 | 5개 일치          | 1,500,000원     |
| 4등 | 4개 일치          | 50,000원        |
| 5등 | 3개 일치          | 5,000원         |

당첨금은 자동으로 계좌에 다시 입금됩니다.

## 메뉴

| 번호 | 기능    | 설명                   |
|----|-------|----------------------|
| 1  | 입금    | 계좌에 금액 입금            |
| 2  | 로또 구매 | 현재 회차에 티켓 구매         |
| 3  | 추첨    | 진행 중인 회차를 종료하고 결과 계산 |
| 4  | 회차 조회 | 회차별/전체 이력 조회         |
| 5  | 계좌 조회 | 현재 잔액 확인             |
| 0  | 종료    | 프로그램 종료              |

## 티켓 구매 방식

한 번의 구매에서 **수동 / 반자동 / 자동**을 혼합할 수 있습니다.

- **수동**: 6개 번호를 모두 사용자가 입력
- **반자동**: 1 ~ 5개 번호만 입력하고, 나머지는 자동으로 채움
- **자동**: 모든 번호를 랜덤으로 생성

예) 총 20장 구매 시 `수동 3장 + 반자동 2장 + 자동 15장` 식으로 조합 가능. 자동만 구매하고 싶다면 수동/반자동 수량에 `0`을 입력합니다.

## 회차 진행

- 구매 시점에 진행 중인 회차가 없으면 새로운 회차가 생성됩니다.
- 진행 중인 회차가 있다면 동일 회차에 티켓이 누적됩니다.
- `추첨` 메뉴를 실행하면 현재 회차가 종료되고 당첨 번호가 결정됩니다.

## 영속화

프로그램을 껐다 켜도 이력이 유지됩니다.

| 데이터   | 기본 경로          | 환경 변수                |
|-------|----------------|----------------------|
| 회차 이력 | `lotto.json`   | `LOTTO_STORE_PATH`   |
| 계좌 잔액 | `account.json` | `ACCOUNT_STORE_PATH` |

파일이 손상된 경우 `*.bak`으로 백업 후 초기 상태로 복구됩니다.

## 프로젝트 구조

```
lotto-project
├── build.gradle.kts
├── settings.gradle.kts
└── src/main/kotlin
    ├── Main.kt
    └── lotto
        ├── App.kt                      # 메뉴 루프
        ├── constant/LottoConstants.kt  # 매직 넘버 상수
        ├── handler                     # 메뉴별 핸들러 (단일 책임)
        │   ├── Handler.kt
        │   ├── AccountHandler.kt
        │   ├── DepositHandler.kt
        │   ├── PurchaseHandler.kt
        │   ├── DrawHandler.kt
        │   └── HistoryHandler.kt
        ├── model
        │   ├── Account.kt
        │   ├── LottoTicket.kt
        │   ├── LottoDraw.kt
        │   ├── LottoRank.kt
        │   └── Menu.kt
        ├── service
        │   ├── LottoService.kt         # 구매 / 추첨 유즈케이스
        │   ├── LottoMachine.kt         # 티켓 생성기
        │   ├── LottoStore.kt           # 회차 영속화
        │   └── AccountStore.kt         # 계좌 영속화
        ├── util/Extensions.kt          # 출력 포맷 유틸
        └── view
            ├── InputView.kt
            └── OutputView.kt
```

## 코드 스타일

[ktlint](https://github.com/JLLeitschuh/ktlint-gradle) 플러그인으로 포맷을 강제합니다.

```bash
./gradlew ktlintCheck   # 검사
./gradlew ktlintFormat  # 자동 포맷
```

## 설계 원칙

- 한 메서드는 한 역할만 담당하며 20줄을 넘기지 않도록 작성합니다.
- 매직 넘버는 `constant/LottoConstants.kt`로 분리합니다.
- 핸들러 / 서비스 / 뷰 / 모델 계층을 분리하여 책임을 명확히 구분합니다.
- 입력 검증은 `InputView`의 `inputPositive` / `inputNonNegative`로 구분해 의미를 명시합니다.
- 계좌에서 돈이 빠지기 전에 티켓을 먼저 생성해 예외 발생 시 금액이 손실되지 않도록 합니다.
