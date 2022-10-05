package NRow.Tree;

//imports here
import NRow.Board;
import java.util.List;

public class Node {
    private Board board;
    private List<Node> moves;
    private int LastMove;

    public Node(Board board, List<Node> moves) {
        this.board = board;
        this.moves = moves;
        this.LastMove = -1;
    }

    public Node(Board board) {
        this.board = board;
        this.LastMove = -1;
    }

    public void addMoves(List<Node> moves) {
        this.moves = moves;
    }

    public Board getBoard() {
        return this.board;
    }

    public List<Node> getMoves() {
        return this.moves;
    }

    public int getLastMove() {
        return this.LastMove;
    }

    public void setLastMove(int i) {
        this.LastMove = i;
    }
}