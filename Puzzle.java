/*
  Course:          CS3462
  Student Name:    Joshua Odeyemi
  Student ID:      000824726
  Assignment #:    #1
  Due Date:        03/05/2021
*/

import java.util.*;

public class Puzzle {
  // Create global set to keep track of visited nodes
  // Also create global list to keep track of moves
  static Set<String> visited = new HashSet<>();
  static List<String> path = new ArrayList<>();

  // Create a "Node" class to store each game state
	static class Node {
    // Initialize instance variables
    /*
      board: holds current state of the game
      emptySpace: stores the location of the empty block in the puzzle
      g: keeps track of how many moves have been taken

    */
		int[][] board = new int[3][3]; 
    int[] emptySpace = new int[2];
    int g = 0;

		public Node(int[][] beginState, int g) {
      this.g = g;

      for (int i = 0; i < beginState.length; i++) {
        System.arraycopy(beginState[i], 0, board[i], 0, beginState[i].length);
      }

      findEmptySpace();
		}

    public boolean isGoalState() {
      if (board[0][0] == 1 && board[0][1] == 2 && board[0][2] == 3 && board[1][0] == 4 && board[1][1] == 5 && board[1][2] == 6 && board[2][0] == 7 && board[2][1] == 8)
        return true;
      else
        return false;
    }

    public void findEmptySpace() {
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
          if (board[i][j] == -1) {
            emptySpace[0] = i;
            emptySpace[1] = j;
            return;
          }
        }
      }
    }

    public int h() {
      int sum = 0;

      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
          if (board[i][j] != -1) {
            sum += displacement(board[i][j], i, j);
          }
        }
      }

      return sum;
    }

    private int displacement(int number, int i, int j) {
      int result = 0;
      switch(number) {
        case 1: result += (Math.abs(i - 0) + Math.abs(j - 0)); break; 
        case 2: result += (Math.abs(i - 0) + Math.abs(j - 1)); break;
        case 3: result += (Math.abs(i - 0) + Math.abs(j - 2)); break;
        case 4: result += (Math.abs(i - 1) + Math.abs(j - 0)); break;
        case 5: result += (Math.abs(i - 1) + Math.abs(j - 1)); break;
        case 6: result += (Math.abs(i - 1) + Math.abs(j - 2)); break;
        case 7: result += (Math.abs(i - 2) + Math.abs(j - 0)); break;
        case 8: result += (Math.abs(i - 2) + Math.abs(j - 1)); break;
        default: result += 0;
      }
      return result;
    }

    public int f() {
      return g + h();
    }

    // Choose next game state using the A* algorithm
    public Node next() {
      // Store state lowest heuristic
      int lowest = Integer.MAX_VALUE;
      String string = "";
      String direction = "";
      Node next = this;
      
      // Check if can swap upward
      if (emptySpace[0] - 1 >= 0) {
        // Create new node
        Node up = new Node(board, g+1);

        // Swap Upward
        up.swapUp();
        
        // Calculate heuristic
        int calc = up.f();

        // Create unique string identifying this state
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < up.board.length; i++) {
          for (int j = 0; j < up.board[i].length; j++) {
            sb.append(up.board[i][j] + " ");
          }
        }
        String s = sb.toString();

        // If this state has the lowest heuristic and has not been visited
        // Choose this state
        if (calc < lowest && !visited.contains(s)) {
          string = s;
          lowest = calc;
          next = up;
          direction = "Move up";
        }
      }

      // Do the same for the space downward
      if (emptySpace[0] + 1 < 3) {
        Node down = new Node(board, g+1);
        down.swapDown();
        int calc = down.f();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < down.board.length; i++) {
          for (int j = 0; j < down.board[i].length; j++) {
            sb.append(down.board[i][j] + " ");
          }
        }
        String s = sb.toString();
        if (calc < lowest && !visited.contains(s)) {
          lowest = calc;
          string = s;
          next = down;
          direction = "Move down";
        }
      }

      // Do the same for the space to the left
      if (emptySpace[1] - 1 >= 0) {
        Node left = new Node(board, g+1);
        left.swapLeft();
        int calc = left.f();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left.board.length; i++) {
          for (int j = 0; j < left.board[i].length; j++) {
            sb.append(left.board[i][j] + " ");
          }
        }
        String s = sb.toString();
        if (calc < lowest && !visited.contains(s)) {
          string = s;
          lowest = calc;
          next = left;
          direction = "Move left";
        }
      }

      // Do the same for the space to the right
      if (emptySpace[1] + 1 < 3) {
        Node right = new Node(board, g+1);
        right.swapRight();
        
        int calc = right.f();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < right.board.length; i++) {
          for (int j = 0; j < right.board[i].length; j++) {
            sb.append(right.board[i][j] + " ");
          }
        }
        String s = sb.toString();
        if (calc < lowest && !visited.contains(s)) {
          string = s;
          lowest = calc;
          next = right;
          direction = "Move right";
        }
      }

      // If no change was made return because unsolvable
      if (next == this) {
        return null;
      }

      // Add new state to the path and the visited set then return
      path.add(direction);
      visited.add(string);
      return next;
    }

    // Swap empty space with space above
    public void swapUp() {
      board[emptySpace[0]][emptySpace[1]] = board[emptySpace[0] - 1][emptySpace[1]];
      board[emptySpace[0] - 1][emptySpace[1]] = -1;
      emptySpace[0]--;
    }

    // Swap empty space with space below
    public void swapDown() {
      board[emptySpace[0]][emptySpace[1]] = board[emptySpace[0] + 1][emptySpace[1]];
      board[emptySpace[0] + 1][emptySpace[1]] = -1;
      emptySpace[0]++;
    }
    
    // Swap empty space with space to the left
    public void swapLeft() {
      board[emptySpace[0]][emptySpace[1]] = board[emptySpace[0]][emptySpace[1] - 1];
      board[emptySpace[0]][emptySpace[1] - 1] = -1;
      emptySpace[1]--;
    }

    // Swap empty space with space to the right
    public void swapRight() {
      board[emptySpace[0]][emptySpace[1]] = board[emptySpace[0]][emptySpace[1] + 1];
      board[emptySpace[0]][emptySpace[1] + 1] = -1;
      emptySpace[1]++;
    }
	}

  // Utility function to print the board
  public static void printBoard(int[][] board) {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (j == 0)
          System.out.print("[");
        System.out.print(board[i][j] + " ");

        if (j == 2)
          System.out.print("]");
      }
      System.out.println();
    }

    System.out.println();
  }

	public static void main(String[] args) {
    // Create a test board
    int[][] board = {
      {5, 6, 4},
      {8, 1, -1},
      {7, 3, 2}
    };

    // Create the initial root node
    Node root = new Node(board, 0);

    // Perform A* algorithm while we have not reached the goal state
   while (root != null && !root.isGoalState()) {
    root = root.next();
   }
   
   // If unsolvable...
   if (root == null) {
     System.out.println("Unable to solve puzzle");
   }
   else {
     // Print solve path
    System.out.println("SOLVED in " + root.g + " moves..");
    for (String s : path) {
      System.out.println(s);
    }
   }
	}
}
