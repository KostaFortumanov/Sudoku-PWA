# Sudoku-PWA

## Опис на апликацијата
Класична 9x9 Судоку PWA изработена со Vue.js, Spring Boot и PostgreSQL.

<div align=center>
  <img width="400" height="795" src="https://github.com/KostaFortumanov/Sudoku-PWA/blob/main/img/home.jpg">
  <img width="400" height="795" src="https://github.com/KostaFortumanov/Sudoku-PWA/blob/main/img/game.jpg">
</div>

Функционалности:
* Играње судоку во три различни тежини лесна, средна и тешка
* Судоку решавач кој решава судоку од било каква тежина
* Локален и Online leaderboard за сите тежини
* Секојдневни предизвици кои се исти за сите корисници со посебен leaderboard
* Push нотификации

## Генерирање на нова игра
Генерирањето на нова игра е иплементирано според: http://zhangroup.aporc.org/images/files/Paper_3485.pdf

### Чекор 1: Креирање терминална шема

Почнуваме со празна табла на која по случаен избор одбираме поле и број од 1-9 и го пополнуваме. Завршуваме кога успешно ќе пополниме 11 полиња.

```ts
  fillGrid = (sudokuString: string, numGivens: number) => {
    if (numGivens >= 11) {
      return sudokuString;
    }

    let index = Math.floor(Math.random() * 81);
    while (sudokuString.charAt(index) != ".") {
      index = Math.floor(Math.random() * 81);
    }
    this.shuffle(this.numbersList);
    for (const value of this.numbersList) {
      const col = index % 9;
      const row = (index - col) / 9;
      if (this.isSafe(sudokuString, row, col, value)) {
        sudokuString = this.addValueAtIndex(sudokuString, index, value);
        sudokuString = this.fillGrid(sudokuString, numGivens + 1);
        if (sudokuString != "") {
          return sudokuString;
        }
      }
    }

    return "";
  }
```

Потоа ги пополнуваме останатите броеви додека не добиеме валидна целосно пополнета табла. [Судоку решавач](#судоку-решавач)

### Чекор 2: "Копање дупки"

Откако ја имаме целосно пополнета табла бришеме полиња.

Според редоследот на бришење добиваме игри од различни тежини
* По случаен избор - лесна
* Со прескокнување на едно поле - средна (c)
* Во форма на S - тешка (b)

<div align=center>
  <img src="https://github.com/KostaFortumanov/Sudoku-PWA/blob/main/img/pattern.jpg">
</div>

Полето го бришеме доколку по бришењето:
* Играта има едно решение
* Во редот/колоната остануваат повеќе од дефинираниот минимален број на пополнети полиња за редот/колоната
* Во целата табла остануваат повеќе од дефинираниот минимален број на пополнети полиња за целата табла

```ts
  private digCells(cells: number[]) {
    let copySudokuString = this.sudokuString;
    let digCount = 0;
    for (const index of cells) {
      if (!this.canDig(copySudokuString, index)) {
        continue;
      }

      const prevValue = copySudokuString.charAt(index);
      copySudokuString = this.removeValueAtIndex(copySudokuString, index);

      let hasAnotherSolution = false;
      const col = index % 9;
      const row = (index - col) / 9;
      for (const value of this.numbersList) {
        if (
          this.isSafe(copySudokuString, row, col, value) &&
          value != prevValue
        ) {
          copySudokuString = this.addValueAtIndex(
            copySudokuString,
            index,
            value
          );
          const copy = copySudokuString;
          if (this.canSolveGrid(copy)) {
            hasAnotherSolution = true;
            break;
          }
        }
      }

      if (hasAnotherSolution) {
        copySudokuString = this.addValueAtIndex(
          copySudokuString,
          index,
          prevValue
        );
      } else {
        copySudokuString = this.removeValueAtIndex(copySudokuString, index);
        digCount++;
      }

      if (digCount >= this.limit) {
        return copySudokuString;
      }
    }

    return copySudokuString;
  }
```

## Судоку решавач

Алгоритмот е exhaustive search и backtracking, што значи дека ги пробува сите можни комбинации ја проверува таблата и се враќа назад чим нема веќе валидни потези.

```ts
  solveGrid = () => {
    let index = 0;
    for (let i = 0; i < 81; i++) {
      if (this.sudokuString.charAt(i) == ".") {
        for (let value = 1; value < 10; value++) {
          const col = i % 9;
          const row = (i - col) / 9;
          if (this.isSafe(this.sudokuString, row, col, value + "")) {
            this.sudokuString = this.addValueAtIndex(
              this.sudokuString,
              i,
              value + ""
            );
            if (this.checkFullGrid(this.sudokuString)) {
              return true;
            } else {
              if (this.solveGrid()) {
                return true;
              }
            }
          }
        }
        index = i;
        break;
      }
    }
    this.sudokuString = this.removeValueAtIndex(this.sudokuString, index);
  }
```

## Локален и Online leaderboard

По завршување на игра времето се сочувува локално, доколку корисникот е најавен и има пристап до интернет конекција времето се зачувува и на сервер.

<div align=center>
  <img width="400" height="795" src="https://github.com/KostaFortumanov/Sudoku-PWA/blob/main/img/leaderboard.jpg">
</div>

При добивање на интернет конекција сите времиња кој не се зачувани се испраќаат на сервер.

```ts
  window.addEventListener("online", this.handleConnection);
  window.addEventListener("offline", this.handleConnection);

  handleConnection = () => {
    if (navigator.onLine) {
      this.isReachable(window.location.origin).then((online) => {
        if (online) {
          this.store.state.onlineStatus = true;
          console.log("online");
          this.backupTimes("easy");
          this.backupTimes("medium");
          this.backupTimes("hard");
        } else {
          console.log("no connectivity");
          this.store.state.onlineStatus = false;
        }
      });
    } else {
      console.log("offline");
      this.store.state.onlineStatus = false;
    }
  };
```

## Дневни предизвици

За играње на дневниот предизвик корисникот мора да е најавен и да има интернет конекција. Секој корисник има дозволен еден обид за решавање на дневниот предизвик.

Секој ден во 00:00 часот корисниците имаат пристап до нов дневен предизвик, времињата од претходниот ден се бришат и на сите корисници им се додава нов обид за решавање на дневниот предизивк.

```java
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearDailyChallenge() {
        List<LeaderboardTime> dailyTimes = leaderboardService.findAllByDifficulty("DAILY");
        leaderboardService.deleteAll(dailyTimes);

        List<AppUser> users = userService.findAll()
                .stream()
                .filter(AppUser::getFinishedDaily)
                .collect(Collectors.toList());

        users.forEach(user -> user.setFinishedDaily(false));

        userService.saveAll(users);
    }
```

## Push нотификации

Нотификациите се имплементирани со [Firebase cloud messaging](https://firebase.google.com/products/cloud-messaging). Доколку корисниот дозволи нотификации повремено добива потсетнк за да игра и потсетник за нов дневен предизвик.

На секој 6 саати доколку корисникот го нема завршено дневниот предизвик и е регистриран за нотификации добива потсетник за да игра.

```java
    @Scheduled(cron = "0 0 */6 * * ?")
    public void sendDailyChallengeReminder() {
        userService.findAll()
                .stream()
                .filter(user -> !user.getFinishedDaily() && user.getNotificationToken() != null)
                .forEach(user -> {
                    try {
                        firebaseMessagingService.sendNotificationToUser(
                                "Sudoku",
                                "New daily challenge is available",
                                user.getNotificationToken());
                    } catch (FirebaseMessagingException e) {
                        System.out.println("Error sending notification to user " + user.getUsername());
                    }
                });
    }
```

<div align=center>
  <img width="400" height="795" src="https://github.com/KostaFortumanov/Sudoku-PWA/blob/main/img/notification.jpg">
</div>
