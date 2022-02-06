<template>
  <div class="game" :key="gameString">
    <h2>{{ currentDifficulty }} <span v-if="currentDifficulty != 'solve'"> - {{ formatTime() }}</span></h2>
    <div class="game-wrapper">
      <div class="game-board">
        <template v-if="game.isGameOver">
          <div class="win-overlay">
            <div
              class="win-overlay-inner"
              :class="{ 'hide-overlay': !isOverlayVisible }"
              :key="isOverlayVisible"
            >
              <h2>Game Finished with time {{ formatTime() }}!</h2>
              <button @click="handleControls(['new'])">Start a New Game</button>
              <button @click="onOverlayClick">View Game</button>
            </div>
          </div>
        </template>
        <div class="game-inner">
          <table class="game-table" :class="{ 'game-over': isOverlayVisible }">
            <tbody>
              <tr class="game-row" v-for="row in game.rows" :key="row">
                <td v-for="col in game.cols" :key="col" class="game-col">
                  <Cell
                    :value="game.values[row * 9 + col]"
                    :square="row * 9 + col"
                    :isSelected="selectedSquare === row * 9 + col"
                    :isGameSquare="game.gameSquares[row * 9 + col]"
                    :isSameValue="sameValues && sameValues.has(row * 9 + col)"
                    :isIncorrect="game.incorrectSquares[row * 9 + col]"
                    @click-square="handleSelectSquare"
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <Controls @click="handleControls" />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Cell from "./Cell.vue";
import Controls from "./Controls.vue";

import LeaderboardService from '../services/leaderboard-service'
import Sudoku from "../services/sudoku";

@Options({
  components: {
    Cell,
    Controls,
  },
  props: {
    onControlsClicked: Array,
    currentDifficulty: String,
  },
})
export default class Game extends Vue {
  currentDifficulty!: string;
  game = new Sudoku();
  gameString = "";
  selectedSquare = -1;
  isOverlayVisible = false;
  selectedValues: number[] = [];
  currentTime = 0;
  score = -1;

  formatTime() {
    let sec: any = this.currentTime % 60;
    let min: any = (this.currentTime - sec) / 60;
    if (min < 10) min = "0" + min;
    if (sec < 10) sec = "0" + sec;
    return min + ":" + sec;
  }

  created() {
    this.game.start(this.currentDifficulty);
    this.gameString = this.game.getString();
  }

  mounted() {
    window.addEventListener("keydown", this.handleKeyPress);
    if (this.score == -1 && this.currentDifficulty != 'solve') {
      this.score = setInterval(() => {
        this.currentTime++;
      }, 1000);
      console.log(this.score)
    }
  }

  unmounted() {
    clearInterval(this.score);
  }

  get sameValues(): Set<number> {
    return new Set(this.selectedValues);
  }

  handleKeyPress = (e: KeyboardEvent) => {
    let value = e.key;

    if (value.substr(0, 5) === "Arrow") {
      const direction = value.substr(5);
      this.moveSquare(direction);
      return;
    }

    if (e.code === "Delete" || e.code === "Backspace") {
      value = "Delete";
    }

    this.onInputValue(value);
  };

  handleSelectSquare = (square: number) => {
    this.selectedSquare = square;
    this.selectedValues = this.game.getAllSameValues(square);
  };

  handleControls(args: (string | number)[]) {
    switch (args[0]) {
      case "numpad": {
        this.onInputValue(args[1] as string);
        break;
      }
      case "delete": {
        this.onInputValue("Delete");
        break;
      }
      case "new": {
        this.game.start(this.currentDifficulty);
        this.gameString = this.game.getString();
        this.currentTime = 0;
        if (this.score == -1) {
          this.score = setInterval(() => {
            this.currentTime++;
          }, 1000);
        }
        break;
      }
      default:
        console.log(args);
    }
  }

  onOverlayClick = () => {
    console.log(this.isOverlayVisible);
    this.isOverlayVisible = false;
  };

  onInputValue = (value: string) => {
    if (!this.game.isGameOver) {
      this.game.setSquareValue(value, this.selectedSquare);
      this.selectedValues = this.game.getAllSameValues(this.selectedSquare);
      if (this.game.isGameOver && this.currentDifficulty != 'solve') {
        this.handleGameFinished();
      }
    } else {
      if(this.currentDifficulty != 'solve') {
        this.handleGameFinished();
      }
    }
  };

  moveSquare = (direction: string) => {
    let col = this.selectedSquare % 9;
    let row = (this.selectedSquare - col) / 9;
    switch (direction.toLowerCase()) {
      case "up":
        row = row - 1;
        break;
      case "down":
        row = row + 1;
        break;
      case "left":
        col = col - 1;
        break;
      case "right":
        col = col + 1;
        break;
      default:
        return;
    }

    if (row >= 0 && row < 9 && col >= 0 && col < 9) {
      const square = row * 9 + col;
      this.handleSelectSquare(square);
    }
  };

  handleGameFinished = () => {
    this.selectedSquare = -1;
    this.selectedValues = [];
    console.log(this.gameString)
    this.gameString = this.game.getCurrentGameString();
    console.log(this.score)
    if (this.score != -1) {
      clearInterval(this.score);
      this.score = -1;
      LeaderboardService.saveTime(this.currentDifficulty, this.currentTime)
    }
    this.isOverlayVisible = true;
  };

  solve() {
    this.game.setSolvedValues();
  }
}
</script>

<style lang="scss">
.game-wrapper {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  h2 {
    display: flex;
  }
}

.win-overlay {
  position: absolute;
  z-index: 11;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;

  h2 {
    justify-content: center;
  }

  .win-overlay-inner {
    display: flex;
    background: #ffffff;
    padding: 18px;
    flex-direction: column;

    &.hide-overlay {
      display: none;
    }

    button {
      border: solid 1px #1489ff;
      background: #1489ff;
      color: #ffffff;
      font-size: 20px;
      cursor: pointer;
      padding: 8px;
      margin-bottom: 1em;

      &:hover {
        background: #ffffff;
        color: #1489ff;
      }
    }
  }
}

.game-board {
  position: relative;
  width: 100%;
  max-width: 500px;
  min-width: 250px;
  background-color: #f3f6fa;

  &::after {
    content: "";
    display: block;
    padding-bottom: 100%;
  }

  .game-inner {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
  }
}

.game-table {
  width: 100%;
  margin: 0 auto;
  padding: 0;
  background: #ffffff;
  user-select: none;
  border: 2px solid #344861;

  &,
  &::after {
    display: block;
    box-sizing: border-box;
    height: 100%;
  }

  &::after {
    content: "";
    position: absolute;
    left: calc(100% / 3);
    width: calc(100% / 3);
    top: 0;
    border-left: 2px solid #344861;
    border-right: 2px solid #344861;
    pointer-events: none;
  }

  &.game-over {
    border-color: #1489ff;
  }

  tbody {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
    overflow: hidden;

    &::after {
      display: block;
      content: "";
      position: absolute;
      left: 0;
      top: calc(100% / 3);
      height: calc(100% / 3);
      border-top: 2px solid #344861;
      border-bottom: 2px solid #344861;
      pointer-events: none;
    }
  }
}

.game-row,
.game-table tbody:after {
  box-sizing: border-box;
  width: 100%;
}

.game-row {
  height: 11.11111%;
}

.game-col,
.game-row {
  display: flex;
  padding: 0;
  margin: 0;
}

.game-col {
  flex-basis: 11.1111%;
  box-sizing: border-box;
  position: relative;
  border-right: 1px solid #bec6d4;
  border-bottom: 1px solid #bec6d4;
  cursor: pointer;
  transform: translateZ(0);
  color: #080808;
}

.paused {
  .game-col {
    &.highlight-table {
      background-color: transparent;
    }

    &.highlight-table {
      background-color: transparent;
    }

    &.highlight-number {
      background-color: transparent;
    }

    &.incorrect {
      background-color: transparent;
    }

    &.cell-selected {
      background-color: transparent;
    }
  }
}
</style>
