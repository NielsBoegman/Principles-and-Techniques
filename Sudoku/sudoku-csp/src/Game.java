public class Game {
  private Sudoku sudoku;

  Game(Sudoku sudoku) {
    this.sudoku = sudoku;
  }

  public void showSudoku() {
    System.out.println(sudoku);
  }

  /**
   * Implementation of the AC-3 algorithm
   * 
   * @return true if the constraints can be satisfied, else false
   */
  public boolean solve() {
    // TODO: implement AC-3
    return true;
  }

  /**
   * Checks the validity of a sudoku solution
   * 
   * @return true if the sudoku solution is correct
   */
  public boolean validSolution() {
    Field[][] board = sudoku.getBoard();
    boolean[] tester = new boolean[9];
    for (int i = 0; i < 9; i++) {
      tester[i] = false;
    }
    boolean verify = true;
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (tester[board[i][j].getValue() - 1] == verify || board[i][j].getValue() == 0) {
          return false;
        }
        tester[board[i][j].getValue() - 1] = verify;
      }
      verify = !verify;
    }
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (tester[board[j][i].getValue() - 1] == verify || board[i][j].getValue() == 0) {
          return false;
        }
        tester[board[j][i].getValue() - 1] = verify;
      }
      verify = !verify;
    }
    for (int i = 0; i < 8; i += 3) {
      for (int j = 0; j < 8; j += 3) {
        for (int x = 0; x < 3; x++) {
          for (int y = 0; y < 3; y++) {
            if (tester[board[i + x][j + y].getValue() - 1] == verify || board[i + x][j + y].getValue() == 0) {
              return false;
            }
            tester[board[i + x][j + y].getValue() - 1] = verify;
          }
        }
        verify = !verify;
      }
    }
    return true;
  }
}
