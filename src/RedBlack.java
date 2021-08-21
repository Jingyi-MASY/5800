import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class RedBlack {
  //sort search min max successor predecessor
  //rotation insert
  //TODO: rotation insert

  private static final boolean RED   = false;
  private static final boolean BLACK = true;

  private Node root;
  private Node NIL;

  private class Node {
    private int key;
    private Node p;
    private Node left;
    private Node right;
    private boolean color;

    private Node(int key, Node parent, Node left, Node right, boolean color) {
      this.key = key;
      this.p = parent;
      this.color = color;
      this.left = left;
      this.right = right;
    }

    private Node() {
    }
  }

  public RedBlack() {
    NIL = new Node();
    NIL.p = NIL;
    NIL.left = NIL;
    NIL.right = NIL;
    NIL.color = BLACK;
    root = NIL;
  }

  public Node search(Node x, int k) {
    while(x != NIL && k != x.key) {
      if(k < x.key) x = x.left;
      else x = x.right;
    }
    if(x != NIL) System.out.println("Found " + k);
    else System.out.println("Not found");
    return x;
  }

  public Node min(Node x) {
    while(x != NIL) {
      x = x.left;
    }
    return x;
  }

  public Node max(Node x) {
    while(x != NIL) {
      x = x.right;
    }
    return x;
  }

  public Node successor(Node x) {
    if(x.right != NIL) {
      return min(x.right);
    }
    Node y = x.p;
    while (y != NIL && x == y.right) {
      x = y;
      y = y.p;
    }
    return y;
  }

  public Node predecessor(Node x) {
    if(x.left != NIL) {
      return max(x.left);
    }
    Node y = x.p;
    while (y != NIL && x == y.left) {
      x = y;
      y = y.p;
    }
    return y;
  }


  private void sort(Node x) {
    if(x != NIL) {
      sort(x.left);
      System.out.print(x.key + " ");
      sort(x.right);
    }
  }

  public void blackHeight() {
    int i = 0;
    Node x = root;
    while (x != NIL){
      if (x.color){
        i++;

      }
      x = x.left;
    }
    System.out.println("Height: " + i);
  }

  public void sort() {
    sort(root);
    System.out.println();
  }

  public void insert(int k) {
    Node y = NIL;
    Node x = root;
    while (x != NIL) {
      y = x;
      if(k < x.key) {
        x = x.left;
      } else x = x.right;
    }
    Node z = new Node(k, y, NIL, NIL, RED);
    if(y == NIL) {
      root = z;
    } else if(k < y.key) {
      y.left = z;
    } else {
      y.right = z;
    }
    fixup(z);
  }

  private void fixup(Node z) {
    while (z.p.color == RED) {
      if (z.p == z.p.p.left) {
        Node y = z.p.p.right;
        if (y.color == RED) {
          z.p.color = BLACK;
          y.color = BLACK;
          z.p.p.color = RED;
          z = z.p.p;
        } else if (z == z.p.right) {
          z = z.p;
          leftRotate(z);
        } else {
          z.p.color = BLACK;
          z.p.p.color = RED;
          rightRotate(z.p.p);
        }
      } else {
        Node y = z.p.p.left;
        if (y.color == RED) {
          z.p.color = BLACK;
          y.color = BLACK;
          z.p.p.color = RED;
          z = z.p.p;
        } else if (z == z.p.left) {
          z = z.p;
          rightRotate(z);
        } else {
          z.p.color = BLACK;
          z.p.p.color = RED;
          leftRotate(z.p.p);
        }
      }
    }
    root.color = BLACK;
  }

  private void leftRotate(Node x) {
    Node y = x.right;
    x.right = y.left;
    if (y.left != NIL) {
      y.left.p = x;
    }
    y.p = x.p;
    if(x.p == NIL) {
      root = y;
    } else if (x == x .p.left) {
      x.p.left = y;
    } else {
      x.p.right = y;
    }
    y.left = x;
    x.p = y;
  }

  private void rightRotate(Node x) {
    Node y = x.left;
    x.left = y.right;
    if (y.right != NIL) {
      y.right.p = x;
    }
    y.p = x.p;
    if(x.p == NIL) {
      root = y;
    } else if (x == x .p.right) {
      x.p.right = y;
    } else {
      x.p.left = y;
    }
    y.right = x;
    x.p = y;
  }

  public void print(Node x) {
    if(x == NIL) return;
    String left, right, color;
    if(x.left == NIL){
      left = "null";
    } else{
      left = String.valueOf(x.left.key);
    }
    if(x.right == NIL){
      right = "null";
    } else{
      right = String.valueOf(x.right.key);
    }
    if(x.color){
      color = "BLACK";
    } else{
      color = "RED";
    }
    System.out.println("Parent: " + x.key +
            " Left: " + left + " Right: "+ right + " Color: " + color);
    print(x.left);
    print(x.right);
  }



  public static void main(String[] args) throws IOException {

    RedBlack tree = new RedBlack();

    //    No file provided.
    if (args.length < 1) {
      throw new IllegalArgumentException("Provide an input file.");
    }

    try {
      Scanner scanner = new Scanner(new File(args[0]));
      while(scanner.hasNextInt())
      {
        tree.insert(scanner.nextInt());
      }

    } catch (FileNotFoundException e) {
      System.out.println("404: Not Found.");
      e.printStackTrace();
    }

    Scanner in = new Scanner(System.in);
    String inString;

    while (true) {
      System.out.println("Do you want to insert, sort, search, print or quit?");
      inString = in.next();
      if(inString.equalsIgnoreCase("quit")) {
        return;
      } else if(inString.equalsIgnoreCase("sort")){
        tree.sort();
      } else if(inString.equalsIgnoreCase("print")){
        tree.print(tree.root);
      }else{
        String ins = inString.substring(0,5);
        int k = in.nextInt();
        if(ins.equalsIgnoreCase("search".substring(0,5))){
          tree.search(tree.root, k);
        } else if(ins.equalsIgnoreCase("insert".substring(0,5))) {
          tree.insert(k);
        }
      }
      tree.blackHeight();
    }
  }

}

