package NRow.Players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Tree.Node;

public class MinMaxPlayer extends PlayerController {
    private int depth;

    public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
    }

    /**
     * Function to recursively create the Tree structure for a given depth
     * 
     * @param playerId PlayerID we're creating from
     * @param depth How deep to make the tree
     * @param node What node to start from
     * @return Tree structure containing all possible moves
     */
    public Node makeTree(int playerId, int depth, Node node) {
        Node currentroot = node;
        List<Node> children = new ArrayList<>();
        for (int i = 0; i < node.getBoard().width; i++) {
            if (node.getBoard().isValid(i)) {
                Board newboard = node.getBoard().getNewBoard(i, playerId);
                Node newnode = new Node(newboard);
                newnode.setLastMove(i);
                newnode.setHeuristic(heuristic.evaluateBoard(this.playerId, newboard));
                children.add(newnode);
            }
        }
        if (depth > 1) {
            for (int j = 0; j < children.size(); j++) {
                Node currentchild = children.get(j);
                currentchild = makeTree((playerId % 2) + 1, depth - 1, currentchild);
                children.set(j, currentchild);
            }
        }
        currentroot.addMoves(children);
        return currentroot;
    }


    @Override
    public int makeMove(Board board) {
        // create the tree of certain depth
        Node root = new Node(board);
        root = makeTree(this.playerId, this.depth, root);

        // Either get the move with normal MinMax or A-B pruning.
        // int move = findMove(0, this.playerId, root, this.depth);
        int move = pruningFindMove(0, this.playerId, root, this.depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        for (Node node : root.getMoves()){ // Find node based on heuristic value
            if (node.getHeuristic() == move) return node.getLastMove();
        }
        return -1;
    }

    /**
     * Recursively find the best heuristic value possible for our player using the minimax algorithm
     * 
     * @param cur Current depth we're looking from
     * @param playerId  ID of player we are playing from
     * @param curNode Current node were looking from
     * @param maxDepth Maximum depth to look
     * @return Max heuristic value
     */
    public int findMove(int cur, int playerId, Node curNode, int maxDepth) {
        if (cur == maxDepth) { // Base case
            return heuristic.evaluateBoard(this.playerId, curNode.getBoard());
        }

        if (playerId == 1) { // Maximizing
            List<Integer> values = new ArrayList<>();
            for (Node node : curNode.getMoves()) {
                int next = this.findMove(cur + 1, playerId + 1, node, maxDepth);
                values.add(next);
                node.setHeuristic(next);
            }
            return Collections.max(values);
        } else { // Minimizing
            List<Integer> values = new ArrayList<>();
            for (Node node : curNode.getMoves()) {
                int next = this.findMove(cur + 1, playerId - 1, node, maxDepth);
                values.add(next);
                node.setHeuristic(next);
            }
            return Collections.min(values);
        }

    }

    /**
     * Recursively find the best heuristic value possible for our player using the minimax algorithm using alpha-beta pruning
     * 
     * @param cur Current depth we're looking from
     * @param playerId  ID of player we are playing from
     * @param curNode Current node were looking from
     * @param maxDepth Maximum depth to look
     * @param alpha Alpha value for A-B pruning
     * @param beta Beta value for A-B pruning
     * @return Max heuristic value
     */
    public int pruningFindMove(int cur, int playerId, Node curNode, int maxDepth, int alpha, int beta){
        if (cur == maxDepth) { // Base case
            return heuristic.evaluateBoard(this.playerId, curNode.getBoard());
        }

        if (playerId == 1) { // Maximizing
            int best = Integer.MIN_VALUE;
            for (Node node : curNode.getMoves()) {
                int next = this.pruningFindMove(cur + 1, playerId + 1, node, maxDepth, alpha, beta);
                best = Math.max(best, next);
                alpha = Math.max(alpha, best);
                node.setHeuristic(best);
                if (beta <= alpha) break; // Node is not worth exploring, break out
            }
            return best;
        } else { // Minimizing
            int best = Integer.MAX_VALUE;
            for (Node node : curNode.getMoves()) {
                int next = this.pruningFindMove(cur + 1, playerId - 1, node, maxDepth, alpha, beta);
                best = Math.min(best, next);
                beta = Math.min(best, beta);
                node.setHeuristic(best);
                if (beta <= alpha) break; // Node is not worth exploring, break out
            }
            return best;
        }
    }
}
