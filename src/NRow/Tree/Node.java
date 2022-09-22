package NRow.Tree;

//imports here
import NRow.Board;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private Board board;
    private List<Board> moves;

    public Node(Board board, List<Board> moves) {
        this.board = board;
        this.moves = moves;
    }

    public Node(Board board) {
        this.board = board;
    }
}