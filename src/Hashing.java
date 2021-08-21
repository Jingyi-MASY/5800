import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

public class Hashing {
  private ArrayList<LinkedList> list;
  private static int size = 400;

  private class Pair {
    String key;
    int value;

    Pair(String key, int value) {
      this.key = key;
      this.value = value;
    }
  }

  public Hashing() {
    list = new ArrayList<>();
    for (int i = 0; i < size; i++) list.add(new LinkedList<Pair>());
  }

  //djb2 http://www.cse.yorku.ca/~oz/hash.html
  private int hash(String str) {
    int hash = 0;
    for (int i = 0; i < str.length(); i++) {
      hash = str.charAt(i) + ((hash << 5) - hash);
    }
    hash = hash % size;
    if (hash < 0) hash += size;
    return hash;
  }


  public int insert(String key, int value) {
    if (search(key) == null) {
      list.get(hash(key)).add(new Pair(key, value));
      return 1;
    }
    return -1;
  }

  public int delete(String key) {
    Pair p = search(key);
    if (p == null) return -1;
    list.get(hash(key)).remove(p);
    return 1;
  }


  public void increase(String key) {
    Pair p = search(key);
    if (p == null) {
      list.get(hash(key)).add(new Pair(key, 1));
    } else {
      p.value++;
    }


  }

  private Pair search(String key) {
    LinkedList<Pair> ll = list.get(hash(key));
    for (Pair p : ll) {
      if (p.key.equalsIgnoreCase(key)) return p;
    }
    return null;
  }

  public int find(String key) {
    Pair p = search(key);
    if (p != null) {
      return search(key).value;
    }
    return -1;
  }

  private void check() {
    for (LinkedList<Pair> ll : list) {
      for (Pair p : ll) {
        System.out.print(p.key + ": " + p.value + '\n');
      }
    }
  }

  public int listAllKeys(String filename) {
    try {
      File f = new File(filename);
      if (f.createNewFile()) {
        PrintStream out = new PrintStream(f);
        for (LinkedList<Pair> ll : list) {
          for (Pair p : ll) {
            out.print(p.key + ": " + p.value + '\n');
          }
        }
        out.close();
      } else {
        System.out.println("File already exists.");
        return -1;
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
      return -1;
    }
    PrintStream out;
    return 1;
  }


  public static void main(String[] args) throws Exception {

    Hashing input = new Hashing();

//    No file provided.
    if (args.length < 2) {
      throw new IllegalArgumentException("Provide an input and output file.");
    }

    try {

      String data = new String(Files.readAllBytes(Paths.get(args[0])));
      String str = data.replaceAll("[\\r\\n-]+", " ").toUpperCase();
      String str2 = str.replaceAll("[^A-Za-z\\s']+", "");
      String[] arr = str2.split(" ");

      for (String w : arr) {
        input.increase(w);
      }

    } catch (FileNotFoundException e) {
      System.out.println("404: Not Found.");
      e.printStackTrace();
    }
//    input.check();

    input.listAllKeys(args[1]);
  }
}

