import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

/**
 * @author : Joey Huang
 * @since : 12/30/21, Thu
 **/

public class BoggleSolver {

    private static final int R = 26;
    private Node root;
    private Cube[] adjMatrix;
    private char[] flatBoard;
    private boolean[] visited;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();
        this.constructTrie(dictionary);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    private static int calculateValue(int length) {
        switch (length) {
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    private void computeAdjMatrix(BoggleBoard b) {
        int row = b.rows();
        int col = b.cols();
        this.adjMatrix = new Cube[row * col];
        this.flatBoard = new char[row * col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int idx = i * col + j;
                this.adjMatrix[idx] = new Cube();
                this.flatBoard[idx] = b.getLetter(i, j);
                if (i > 0) {
                    this.adjMatrix[idx].neighbors[this.adjMatrix[idx].n++] = (i - 1) * col + j; // up
                    if (j < col - 1) {
                        this.adjMatrix[idx].neighbors[this.adjMatrix[idx].n++] = (i - 1) * col + (j + 1); // top-right
                    }
                }
                if (i < row - 1) {
                    this.adjMatrix[idx].neighbors[this.adjMatrix[idx].n++] = (i + 1) * col + j; // down
                    if (j > 0) {
                        this.adjMatrix[idx].neighbors[this.adjMatrix[idx].n++] = (i + 1) * col + (j - 1); // down-left
                    }
                }
                if (j > 0) {
                    this.adjMatrix[idx].neighbors[this.adjMatrix[idx].n++] = i * col + (j - 1); // left
                    if (i > 0) {
                        this.adjMatrix[idx].neighbors[this.adjMatrix[idx].n++] = (i - 1) * col + (j - 1); // top-left
                    }
                }
                if (j < col - 1) {
                    this.adjMatrix[idx].neighbors[this.adjMatrix[idx].n++] = i * col + (j + 1); // right
                    if (i < row - 1) {
                        this.adjMatrix[idx].neighbors[this.adjMatrix[idx].n++] = (i + 1) * col + (j + 1); // down-right
                    }
                }
            }
        }
    }

    private SET<String> DFS(int num) {
        this.visited = new boolean[num];
        SET<String> words = new SET<>();
        for (int i = 0; i < num; i++) {
            DFS(i, new StringBuilder(), words, this.root);
        }
        return words;
    }

    private void DFS(int i, StringBuilder sb, SET<String> words, Node n) {
        char c = this.flatBoard[i];
        Node next = n.subNodes[c - 'A'];
        if (c == 'Q' && next != null) {
            next = next.subNodes['U' - 'A'];
        }
        if (next == null) return;

        if (c == 'Q') {
            sb.append("QU");
        }
        else sb.append(this.flatBoard[i]);

        if (sb.length() > 2 && next.isWord) {
            words.add(sb.toString());
        }

        this.visited[i] = true;
        for (int j = 0; j < this.adjMatrix[i].n; j++) {
            int nextIdx = this.adjMatrix[i].neighbors[j];
            if (!this.visited[nextIdx]) {
                this.DFS(nextIdx, new StringBuilder(sb), words, next);
            }
        }
        this.visited[i] = false;
    }

    private void constructTrie(String[] dictionary) {
        this.root = new Node();
        for (String s : dictionary) {
            this.addWord(s);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();
        this.computeAdjMatrix(board);
        return this.DFS(board.rows() * board.cols());
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException();
        Integer i = this.searchWord(word);
        return i == null ? 0 : i;
    }

    private void addWord(String word) {
        Node currentNode = this.root;
        int length = word.length();
        for (int i = 0; i < length; i++) {
            char charAt = word.charAt(i);
            int index = charAt - 'A';
            if (i == length - 1) {
                currentNode.subNodes[index] = new Node(calculateValue(length));
            } else {
                if (currentNode.subNodes[index] == null) currentNode.subNodes[index] = new Node();
                currentNode = currentNode.subNodes[index];
            }
        }
    }

    private Integer searchWord(String word) {
        Node currentNode = this.root;
        int length = word.length();
        for (int i = 0; i < word.length(); i++) {
            char charAt = word.charAt(i);
            int index = charAt - 'A';
            if (currentNode.subNodes[index] != null) {
                currentNode = currentNode.subNodes[index];
                if (i == length - 1) {
                    return currentNode.value;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    private static class Cube {
        private int n = 0;
        private final int[] neighbors = new int[8];

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.n; i++) {
                sb.append(neighbors[i]);
                sb.append(" ");
            }
            return sb.toString();
        }
    }

    private static class Node {
        private final Node[] subNodes;
        private Integer value;
        private boolean isWord = false;

        private Node() {
            this.subNodes = new Node[R];
        }

        private Node(int value) {
            this.value = value;
            this.isWord = true;
            this.subNodes = new Node[R];
        }
    }
}
