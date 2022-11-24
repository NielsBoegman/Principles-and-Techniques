import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;


public class Game {
  private Sudoku sudoku;

  Game(Sudoku sudoku) {
    this.sudoku = sudoku;
  }

  public void showSudoku() {
    System.out.println(sudoku);
  }

  // Function to revise the domains as used in AC-3
  public void revise(Field left, Field right){
    List<Integer> to_remove = new ArrayList<>();
    for (int x : left.getDomain()){

      // Right side of the arc is finalized, value should be removed from left domain
      if (right.getDomainSize() == 0) {
        if (x == right.getValue()){
          to_remove.add(x);
        }
        continue;
      }

      // Check if there is a matching value for every value x in left domain.
      boolean matching = false;
      for (int y : right.getDomain()){
        if (x != y){
          matching = true; // Matching value found
        }
      }
      if (!matching){
        to_remove.add(x); // If a matching value is not found, remove x from left domain
      }
    }
    for (int x : to_remove){
      left.removeFromDomain(x); // Remove all values that should be discarded from left domain.
    }
  }
  /**
   * Implementation of the AC-3 algorithm
   * 
   * @return true if the constraints can be satisfied, else false
   */
  public boolean solve(Comparator<Field[]> comp) {
    int arcs_processed = 0; // Complexity measure

    // Create queue for fields to be revised
    PriorityQueue<Field[]> queue = new PriorityQueue<>(comp);
    Field[][] board = sudoku.getBoard();
    for (Field[] row : board){
      for (Field field : row){
        if (field.getDomainSize() == 0) continue; // If field is finalized, no need to put it in the queue
        for (Field neighbor : field.getNeighbours()){
          queue.add(new Field[]{field, neighbor});
        }
      }
    }

    while (!queue.isEmpty()) {
        Field[] arc = queue.remove();
        arcs_processed++;
        int domainsize = arc[0].getDomainSize();
        revise(arc[0], arc[1]);
        if (arc[0].getDomainSize() == 0) { // No possible values left
          return false;
        }
        if (domainsize != arc[0].getDomainSize()){
            for (Field x : arc[0].getOtherNeighbours(arc[1])) {
              if (x.getDomainSize() == 0) continue; // If neighbour is finalized, do not add, nothing to change
              Field[] new_arc = new Field[]{x, arc[0]};
              if (!queue.contains(new_arc)){
                queue.add(new_arc);
              } 
            }
        }
    }
    System.out.println("Solve processed " + arcs_processed + " arcs during solving");
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
        if (board[i][j].getValue() == 0){
          return false;
        }
        if (tester[board[i][j].getValue() - 1] == verify) {
          return false;
        }
        tester[board[i][j].getValue() - 1] = verify;
      }
      verify = !verify;
    }
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if(board[j][i].getValue() == 0) return false;

        if (tester[board[j][i].getValue() - 1] == verify) {
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
            if ( board[i + x][j + y].getValue() == 0) return false;
            if (tester[board[i + x][j + y].getValue() - 1] == verify) {
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
