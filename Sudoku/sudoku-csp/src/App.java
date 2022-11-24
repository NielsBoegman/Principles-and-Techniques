import java.util.Comparator;

public class App {
    public static void main(String[] args) throws Exception {
        start("C:\\Users\\Niels\\Documents\\AI\\FourInARow\\Sudoku\\sudoku-csp\\Sudoku1.txt");
    }

    /**
     * Start AC-3 using the sudoku from the given filepath, and reports whether the
     * sudoku could be solved or not, and how many steps the algorithm performed
     * 
     * @param filePath
     */
    public static void start(String filePath) {
        Game game1 = new Game(new Sudoku(filePath));
        game1.showSudoku();

        // Heuristic thats based on the minimum amount of elements in the domain left
        Comparator<Field[]> min_remaining = new Comparator<Field[]>() {

            @Override
            public int compare(Field[] o1, Field[] o2) {
                return o1[0].getDomainSize() - o2[0].getDomainSize();
            }
            
        };

        // Heuristic thats based on the amount of finalized neighbours
        Comparator<Field[]> arc_to_finalized = new Comparator<Field[]>() {

            @Override
            public int compare(Field[] o1, Field[] o2) {
                return o1[0].finalizedNeighbours() - o2[0].finalizedNeighbours();
            }
            
        };

        // Solving based on minimum amount of elements in the domain left
        // if (game1.solve(min_remaining) && game1.validSolution()) {
        //     System.out.println("Solved!");
        // } else {
        //     System.out.println("Could not solve this sudoku :(");
        // }
        // game1.showSudoku();

        // We reverse the arc heuristic as otherwise it will prioritize fields with less finalized neighbours
        if (game1.solve(arc_to_finalized.reversed()) && game1.validSolution()) {
            System.out.println("Solved!");
        } else {
            System.out.println("Could not solve this sudoku :(");
        }
        game1.showSudoku();
    }
}
