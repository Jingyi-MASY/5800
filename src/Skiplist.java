import java.util.Random;
import java.util.Stack;

class Skiplist {
  Node head;
  Random rand = new Random();
  //SLL

  class Node {
    int val;
    Node next;
    Node down;

    public Node(int val, Node next, Node down) {
      this.val = val;
      this.next = next;
      this.down = down;
    }
  }




  public Skiplist() {
    this.head = new Node(Integer.MIN_VALUE, null, null);
  }


  //return the Node before value; null if not found
  private boolean lookup(int num) {
    Node iterator = head;

    while (iterator != null) {
      //check iterator.NEXT null pointer
      while (iterator.next != null && iterator.next.val < num) {
        iterator = iterator.next;
      }
      if(iterator.val == num) return true;
      if (iterator.next != null && iterator.next.val == num) return true;
      iterator = iterator.down;
    }
    return false;
  }

  public boolean search(int target) {
    return lookup(target);
  }


  public void add(int num) {
    Stack<Node> stack = new Stack<>();
    Node iterator = head;
    while (iterator != null) {

      while (iterator.next != null && iterator.next.val < num) {
        iterator = iterator.next;
      }
      stack.push(iterator);
      iterator = iterator.down;
    }

    boolean f = true;//float or not
    Node down = null;
    while (f && !stack.isEmpty()) {
      iterator = stack.pop();
      iterator.next = new Node(num, iterator.next, down);
      down = iterator.next;
      if(rand.nextInt(100) < 50) f = false; //[0,99]
    }

    //float to the very top!!!
    if (f) head = new Node(Integer.MIN_VALUE, null, head);
  }

  public boolean erase(int num) {
    Node iterator = head;
    boolean flag = false;

    //Erase down to the end
    while (iterator != null) {
      while (iterator.next != null && iterator.next.val < num) {
        iterator = iterator.next;
      }
      if (iterator.next != null && iterator.next.val == num) {
        flag = true;
        iterator.next = iterator.next.next;
      }
      iterator = iterator.down;
    }
    return flag;
  }

  public static void main(String[] args) {
    Skiplist sll = new Skiplist();
    sll.add(4);
  }

}

