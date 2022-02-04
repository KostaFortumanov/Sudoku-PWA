export default class Sudoku {
  static DIGITS = "123456789";
  private sudokuString = "";
  private solvedString = "";
  private numbersList = ["1", "2", "3", "4", "5", "6", "7", "8", "9"];
  public gameSquares: boolean[] = [];
  public values: string[] = [];
  public _incorrectSquares: boolean[] = [];

  public start(difficulty: string) {
    this.sudokuString = this.getEmptyGrid();
    if (difficulty != "solve") {
      this.sudokuString = this.fillGrid(this.sudokuString, 0);
      console.log("filled " + this.sudokuString);
      this.solveGrid();
      this.solvedString = this.sudokuString;
      console.log("solved " + this.sudokuString);
      if (difficulty == "easy") {
        this.limit = 45;
        this.maxEmptyCells = 5;
        this.sudokuString = this.digCells(this.generateRandomCells());
        console.log(this.sudokuString);
      } else if (difficulty == "medium") {
        this.limit = 49;
        this.maxEmptyCells = 6;
        this.sudokuString = this.digCells(this.generateJumpingOnceCells());
        console.log(this.sudokuString);
      } else if (difficulty == "hard") {
        this.limit = 53;
        this.maxEmptyCells = 7;
        this.sudokuString = this.digCells(this.generateWanderingAlongSCells());
        console.log(this.sudokuString);
      } else if (difficulty == "extreme") {
        this.limit = 59;
        this.maxEmptyCells = 9;
        this.sudokuString = this.digCells(this.generateOrderedCells());
        console.log(this.sudokuString);
      }
    }

    for (let i = 0; i < 81; i++) {
      this.gameSquares[i] = this.sudokuString.charAt(i) != ".";
      this.values[i] = this.sudokuString.charAt(i);
      this._incorrectSquares[i] = false;
    }
  }

  public setSolvedValues() {
    this.sudokuString = this.getCurrentGameString();
    let canSolve = true;
    for (let i = 0; i < 81; i++) {
      if (this._incorrectSquares[i]) {
        canSolve = false;
      }
    }
    if (canSolve) {
      this.solveGrid();
      for (let i = 0; i < 81; i++) {
        this.values[i] = this.sudokuString.charAt(i);
      }
    }
  }

  public setSquareValue(value: string, index: number) {
    if (!this.gameSquares[index]) {
      if (value === "Delete") {
        this.values[index] = ".";
        this._incorrectSquares[index] = false;
      } else if (Sudoku.DIGITS.includes(value)) {
        this.values[index] = value;
      }

      const currentGameString = this.getCurrentGameString();
      for (let i = 0; i < 81; i++) {
        if (this.values[i] != ".") {
          const col = i % 9;
          const row = (i - col) / 9;

          let rowCount = 0;
          for (let j = 0; j < 9; j++) {
            if (currentGameString.charAt(row * 9 + j) == this.values[i]) {
              rowCount++;
            }
          }

          let colCount = 0;
          for (let j = 0; j < 9; j++) {
            if (currentGameString.charAt(j * 9 + col) == this.values[i]) {
              colCount++;
            }
          }

          const startRow = row - (row % 3);
          const startCol = col - (col % 3);
          let boxCount = 0;
          for (let j = 0; j < 3; j++) {
            for (let k = 0; k < 3; k++) {
              if (
                currentGameString.charAt((j + startRow) * 9 + k + startCol) ==
                this.values[i]
              ) {
                boxCount++;
              }
            }
          }

          this._incorrectSquares[i] =
            rowCount != 1 || colCount != 1 || boxCount != 1;
        }
      }
    }
  }

  public getAllSameValues(index: number) {
    const value = this.values[index];
    const sameValues = [];
    if (value != ".") {
      for (let i = 0; i < 81; i++) {
        if (this.values[i] == value) {
          sameValues.push(i);
        }
      }
    }
    return sameValues;
  }

  public get incorrectSquares() {
    return this._incorrectSquares;
  }

  public get isGameOver() {
    return this.values.join("") == this.solvedString;
  }

  //generation
  public fillGrid(sudokuString: string, numGivens: number): string {
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

  private solveGrid() {
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

  private canSolveGrid(sudokuString: string) {
    let index = 0;
    for (let i = 0; i < 81; i++) {
      if (sudokuString.charAt(i) == ".") {
        for (let value = 1; value < 10; value++) {
          const col = i % 9;
          const row = (i - col) / 9;
          if (this.isSafe(sudokuString, row, col, value + "")) {
            sudokuString = this.addValueAtIndex(sudokuString, i, value + "");
            if (this.checkFullGrid(sudokuString)) {
              return true;
            } else {
              if (this.canSolveGrid(sudokuString)) {
                return true;
              }
            }
          }
        }
        index = i;
        break;
      }
    }
    sudokuString = this.removeValueAtIndex(sudokuString, index);
  }

  public checkFullGrid(grid: string) {
    return !grid.includes(".");
  }

  //cellRemoval

  private maxEmptyCells = 0;
  private limit = 0;

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
            console.log("another solution");
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

  private canDig(sudokuString: string, index: number) {
    const col = index % 9;
    const row = (index - col) / 9;

    let numEmptyCells = 0;
    for (let i = 0; i < 9; i++) {
      if (sudokuString.charAt(row * 9 + i) == ".") {
        numEmptyCells++;
      }
    }

    if (numEmptyCells >= this.maxEmptyCells) {
      return false;
    }

    numEmptyCells = 0;
    for (let i = 0; i < 9; i++) {
      if (sudokuString.charAt(i * 9 + col) == ".") {
        numEmptyCells++;
      }
    }

    if (numEmptyCells >= this.maxEmptyCells) {
      return false;
    }

    return true;
  }

  private generateOrderedCells() {
    const cells = [];
    for (let i = 0; i < 81; i++) {
      cells.push(i);
    }
    return cells;
  }

  private generateRandomCells() {
    const cells = this.generateOrderedCells();
    this.shuffle(cells);
    return cells;
  }

  private generateWanderingAlongSCells() {
    const cells = [];
    for (let i = 0; i < 9; i++) {
      for (let j = 0; j < 9; j++) {
        if (i % 2 == 0) {
          cells.push(i * 9 + j);
        } else {
          cells.push(i * 9 + (8 - j));
        }
      }
    }
    return cells;
  }

  private generateJumpingOnceCells() {
    const cells = this.generateWanderingAlongSCells();
    const temp = [];
    for (let i = 0; i < cells.length; i += 2) {
      temp.push(cells[i]);
    }
    for (let i = 1; i < cells.length; i += 2) {
      temp.push(cells[i]);
    }
    return temp;
  }

  private isSafe(
    sudokuString: string,
    row: number,
    col: number,
    value: string
  ) {
    return (
      !this.valueInRow(sudokuString, row, value) &&
      !this.valueInColumn(sudokuString, col, value) &&
      !this.valueInBox(sudokuString, row, col, value)
    );
  }

  private valueInRow(sudokuString: string, row: number, value: string) {
    for (let i = 0; i < 9; i++) {
      if (sudokuString.charAt(row * 9 + i) == value) {
        return true;
      }
    }
    return false;
  }

  private valueInColumn(sudokuString: string, col: number, value: string) {
    for (let i = 0; i < 9; i++) {
      if (sudokuString.charAt(i * 9 + col) == value) {
        return true;
      }
    }
    return false;
  }

  private valueInBox(
    sudokuString: string,
    row: number,
    col: number,
    value: string
  ) {
    const startRow = row - (row % 3);
    const startCol = col - (col % 3);
    for (let i = 0; i < 3; i++) {
      for (let j = 0; j < 3; j++) {
        if (sudokuString.charAt((i + startRow) * 9 + j + startCol) == value) {
          return true;
        }
      }
    }
    return false;
  }

  //utill
  public getString() {
    return this.sudokuString;
  }

  public getCurrentGameString() {
    return this.values.join("");
  }

  private getEmptyGrid() {
    let emptyGrid = "";
    for (let i = 0; i < 81; i++) {
      emptyGrid = emptyGrid.concat(".");
    }
    return emptyGrid;
  }

  private shuffle(array: string[] | number[]) {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
  }

  private addValueAtIndex(sudokuString: string, index: number, value: string) {
    return (
      sudokuString.substring(0, index) +
      value +
      sudokuString.substring(index + 1)
    );
  }

  private removeValueAtIndex(sudokuString: string, index: number) {
    return (
      sudokuString.substring(0, index) + "." + sudokuString.substring(index + 1)
    );
  }

  public get rows() {
    return [0, 1, 2, 3, 4, 5, 6, 7, 8];
  }

  public get cols() {
    return [0, 1, 2, 3, 4, 5, 6, 7, 8];
  }
}
