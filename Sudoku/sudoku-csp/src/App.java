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
        Comparator<Field[]> min_remaining = new Comparator<Field[]>() {

            @Override
            public int compare(Field[] o1, Field[] o2) {
                return o1[0].getDomainSize() - o2[0].getDomainSize();
            }
            
        };

        Comparator<Field[]> arc_to_finalized = new Comparator<Field[]>() {

            @Override
            public int compare(Field[] o1, Field[] o2) {
                return o1[0].finalizedNeighbours() - o2[0].finalizedNeighbours();
            }
            
        };


        // if (game1.solve(min_remaining) && game1.validSolution()) {
        //     System.out.println("Solved with minimum remaining values as heuristic!");
        // } else {
        //     System.out.println("Could not solve this sudoku :(");
        // }
        if (game1.solve(arc_to_finalized) && game1.validSolution()) {
            System.out.println("Solved with finalized fields as heuristic!");
        } else {
            System.out.println("Could not solve this sudoku :(");
        }
        game1.showSudoku();
    }
}
