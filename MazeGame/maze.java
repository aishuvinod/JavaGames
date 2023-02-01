import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.*;

//represents an edge
class Edge {
  Vertex from;
  Vertex to;
  int weight;
  Random rand;

  Edge(Vertex from, Vertex to) {
    this.from = from;
    this.to = to;
    this.rand = new Random();
    this.weight = this.rand.nextInt(100);

  }

  // draws over the edge so it is not visible and creates a path
  public WorldImage undraw() {
    return new RectangleImage(28, 28, OutlineMode.SOLID, Color.LIGHT_GRAY);
  }

  @Override
  public int hashCode() {
    // return Objects.hash(this.x, this.y, this.outNeighbors);
    return Objects.hash(this.from, this.to, this.weight);
  }

  // overrides the equals method
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof Edge)) {
      return false;
    }

    Edge edge = (Edge) other;

    return this.from.equals(edge.from) && this.to.equals(edge.to) && this.weight == edge.weight;
  }

}

// represents a vertex
class Vertex {
  int x;
  int y;
  ArrayList<Edge> outNeighbors;
  Vertex top;
  Vertex bottom;
  Vertex right;
  Vertex left;
  Color c;

  Vertex(int x, int y) {

    this.x = x;
    this.y = y;
    this.outNeighbors = new ArrayList<Edge>();
    this.top = null;
    this.bottom = null;
    this.right = null;
    this.left = null;
    this.c = Color.LIGHT_GRAY;
  }

  // overrides the equals method
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof Vertex)) {
      return false;
    }

    Vertex vertex = (Vertex) other;

    return this.x == vertex.x && this.y == vertex.y;
  }

  // overrides the hashcode method
  @Override
  public int hashCode() {
    // return Objects.hash(this.x, this.y, this.outNeighbors);
    return Objects.hash(this.x, this.y);
  }

  // draws the vertex as a square
  public WorldImage draw() {
    return new RectangleImage(30, 30, OutlineMode.SOLID, this.c);
  }

  // draws an outline around each vertex
  public WorldImage drawOutline() {
    return new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.black);
  }
}

//compares edges by weight
class WeightCompare<T> implements Comparator<Edge> {
  public int compare(Edge e1, Edge e2) {
    return e2.weight - e1.weight;
  }
}

// represents the graph as a maze game
class Graph extends World {
  int width;
  int height;

  ArrayList<ArrayList<Vertex>> allVertices;
  ArrayList<Edge> allEdges;

  // to sort the edges by weight
  WeightCompare<Vertex> comp;

  // list of edges we removed so we can draw walls there
  ArrayList<Edge> discarded;

  ArrayList<Edge> krus;

  ArrayList<Edge> soFar;

  boolean dfs;
  boolean bfs;
  ArrayList<Vertex> processed;

  Graph(ArrayList<ArrayList<Vertex>> allVertices, ArrayList<Edge> allEdges) {
    this.allVertices = allVertices;
    this.allEdges = allEdges;
    this.comp = new WeightCompare<Vertex>();
    // this.discarded = new ArrayList<Edge>();
    this.krus = this.kruskals();
    this.makeMaze();
    this.makeEdges();
    this.soFar = new ArrayList<Edge>();

  }

  Graph(int width, int height) {
    this.height = height;
    this.width = width;

    this.allVertices = new ArrayList<ArrayList<Vertex>>();
    this.allEdges = new ArrayList<Edge>();
    this.comp = new WeightCompare<Vertex>();
    this.makeMaze();
    this.makeEdges();
    this.krus = this.kruskals();
    this.dfs = false;
    this.bfs = false;
    this.soFar = new ArrayList<Edge>();
    this.processed = new ArrayList<Vertex>();

  }

  // constructor for testing
  Graph(ArrayList<ArrayList<Vertex>> allVertices) {
    this.allVertices = allVertices;

  }

  // makes rows of vertexes then columns of rows
  void makeMaze() {
    for (int i = 0; i < width; i++) {
      ArrayList<Vertex> row = new ArrayList<Vertex>();
      for (int j = 0; j < height; j++) {
        row.add(new Vertex(i, j));
      }
      this.allVertices.add(row);
    }
  }

  // creates a bottom and right edge for every vertex to connect them as a graph
  void makeEdges() {
    // ArrayList<Edge> el = new ArrayList<Edge>();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {

        // right
        if (i < width - 1) {
          Edge e1 = new Edge(this.allVertices.get(i).get(j), this.allVertices.get(i + 1).get(j));
          allEdges.add(e1);

          // adds the right edge to the vertex's outNeighbors field
          this.allVertices.get(i).get(j).outNeighbors.add(e1);

        }

        // bottom
        if (j < height - 1) {
          Edge e2 = new Edge(this.allVertices.get(i).get(j), this.allVertices.get(i).get(j + 1));
          allEdges.add(e2);

          // adds the bottom edge to the vertex's outNeighbors field
          this.allVertices.get(i).get(j).outNeighbors.add(e2);
        }

      }
    }

    // sorts the edges from smallest to largest
    this.allEdges.sort(comp);

  }

  // makes the scene of the maze game
  @Override
  public WorldScene makeScene() {
    WorldScene world = new WorldScene(500, 500);

    // draws all the vertices and spaces them out correctly based on their size
    for (ArrayList<Vertex> row : allVertices) {
      for (Vertex v : row) {
        // v.c = Color.LIGHT_GRAY;
        world.placeImageXY(v.draw(), v.x * 30 + 30 / 2, v.y * 30 + 30 / 2);
        world.placeImageXY(v.drawOutline(), v.x * 30 + 30 / 2, v.y * 30 + 30 / 2);
      }
    }

    // for all the edges in the kruskal's algorithm it draws out the edge to create
    // a path
    for (Edge e : this.krus) {
      world.placeImageXY(e.undraw(), (((e.from.x + e.to.x) * 30) / 2) + 15,
          ((e.from.y + e.to.y) * 30 / 2) + 15);
    }

    for (ArrayList<Vertex> row : allVertices) {
      for (Vertex v : row) {
        if (v.x == 0 && v.y == 0) {
          v.c = Color.blue;
          world.placeImageXY(v.draw(), v.x * 30 + 30 / 2, v.y * 30 + 30 / 2);
        }
        else if (v.x == this.width - 1 && v.y == this.height - 1) {
          v.c = Color.blue;
          world.placeImageXY(v.draw(), v.x * 30 + 30 / 2, v.y * 30 + 30 / 2);
        }
      }
    }

    return world;
  }

  // represents an event when a key is pressed
  @Override
  public void onKeyEvent(String key) {
    // resets the maze
    if (key.equals("r")) {
      for (Edge e : this.allEdges) {
        e.weight = e.rand.nextInt(100);
      }
      this.makeEdges();
      this.makeMaze();
      this.krus = this.kruskals();
      this.dfs = false;
      this.bfs = false;
    }
    // solves the maze using breadth first search
    if (key.equals("b")) {
      this.bfs = true;
      this.breadthFirst();
    }
    // solves the maze using depth first search
    if (key.equals("d")) {
      this.dfs = true;
      this.depthFirst();
    }
    else {
      return;
    }
  }

  // makes the maze using the kruskals alogrithm
  ArrayList<Edge> kruskals() {
    HashMap<Vertex, Vertex> representatives = new HashMap<Vertex, Vertex>(); // string or vertex
    ArrayList<Edge> edgesInTree = new ArrayList<Edge>(); // meant to store what edges are selected
    ArrayList<Edge> worklist = allEdges; // all edges in graph, sorted by edge weights;

    // initializes every node's (vertex) representative to itself
    for (ArrayList<Vertex> row : allVertices) {
      for (Vertex v : row) {
        representatives.put(v, v);
      }
    }

    // Pick the next cheapest edge of the graph: suppose it connects X and Y.
    while (worklist.size() > 0) {

      // remove from worklist
      Edge currentEdge = worklist.remove(0);

      
      if (find(representatives, currentEdge.from).equals(find(representatives, currentEdge.to))) {
      // do not do anything if they are already connected
      }
      else {
        // else record this edge in edgesInTree
        edgesInTree.add(currentEdge);
        union(representatives, currentEdge.from, currentEdge.to);
      }
    }

    return edgesInTree;
  }

  // links the vertexes to each other in the hash map
  void union(HashMap<Vertex, Vertex> representatives, Vertex v1, Vertex v2) {
    representatives.replace(this.find(representatives, v1), this.find(representatives, v2));
  }

  // find whether two vertexes are in the same hash map
  Vertex find(HashMap<Vertex, Vertex> hm, Vertex key) {
    Vertex value = hm.get(key);

    while (!(hm.get(value).equals(value))) {
      value = hm.get(value);
    }
    return value;

  }

  // assigns the neighbors to a vertex
  void setVertexNeighbors() {
    for (int y = 0; y < this.height; y++) {
      for (int x = 0; x < this.width; x++) {

        // change top
        if (y - 1 >= 0) {
          allVertices.get(y).get(x).top = allVertices.get(y - 1).get(x);
        }

        // change bottom
        if (y + 1 < this.height) {
          allVertices.get(y).get(x).bottom = allVertices.get(y + 1).get(x);
        }

        // change right
        if (x + 1 < this.width) {
          allVertices.get(y).get(x).right = allVertices.get(y).get(x - 1);
        }

        // change left
        if (x - 1 >= 0) {
          allVertices.get(y).get(x).left = allVertices.get(y).get(x + 1);
        }
      }
    }
  }

  // breadth first search
  ArrayList<Edge> breadthFirst() {
    HashMap<Vertex, Edge> cameFromEdge = new HashMap<Vertex, Edge>();
    Stack<Vertex> worklist = new Stack<Vertex>(); // A Queue or a Stack, depending on the algorithm
    // Vertex start = this.allVertices.get(0).get(0);
    Vertex end = this.allVertices.get(width - 1).get(height - 1);

    // initialize the worklist to contain the starting vertex
    worklist.add(this.allVertices.get(0).get(0));

    // While(the worklist is not empty)
    while (worklist.size() > 0) {
      // System.out.println( "while");
      Vertex next = worklist.get(0);

      // if next has already been processed
      if (processed.contains(next)) {
        worklist.remove(next);
        // System.out.println("if");
        // make another list of nodes that have been processed
      }
      else if (next.equals(end)) {
        // System.out.println("else if");
        next.c = Color.blue;
        processed.add(next);

        return this.reconstruct(cameFromEdge, next);
      }

      else {

        next.c = Color.blue;
        processed.add(next);

        for (Edge n : next.outNeighbors) {
          if (n.to.equals(next)) {
            // you have reached target
            // add n to the work list
            this.soFar.add(n);
            worklist.add(n.from);
            cameFromEdge.put(n.from, n);

          }
          else {
            worklist.add(n.to);
            cameFromEdge.put(n.to, n);
            this.soFar.add(n);
          }
        }

      }
    }
    return this.soFar;
  }

  // depth first search
  ArrayList<Edge> depthFirst() {
    HashMap<Vertex, Edge> cameFromEdge = new HashMap<Vertex, Edge>();
    Queue<Vertex> worklist = new ArrayDeque<Vertex>(); 
    Vertex end = this.allVertices.get(width - 1).get(height - 1);

    // initialize the worklist to contain the starting vertex
    worklist.add(this.allVertices.get(0).get(0));

    // While(the worklist is not empty)
    while (worklist.size() > 0) {
      // System.out.println( "while");
      Vertex next = worklist.remove();

      // if next has already been processed
      if (processed.contains(next)) {
        // worklist.remove(next);
        // System.out.println("if");
        // make another list of nodes that have been processed
      }
      else if (next.equals(end)) {
        // System.out.println("else if");
        next.c = Color.blue;
        processed.add(next);

        return this.reconstruct(cameFromEdge, next);
      }

      else {

        next.c = Color.blue;
        processed.add(next);

        for (Edge n : next.outNeighbors) {
          if (n.to.equals(next)) {
            // you have reached target
            // add n to the work list
            this.soFar.add(n);
            worklist.add(n.from);
            cameFromEdge.put(n.from, n);

          }
          else {
            worklist.add(n.to);
            cameFromEdge.put(n.to, n);
            this.soFar.add(n);
          }
        }

      }
    }
    return this.soFar;
  }

  // reconstructs the maze
  ArrayList<Edge> reconstruct(HashMap<Vertex, Edge> cameFromEdge, Vertex next) {
    Vertex start = this.allVertices.get(0).get(0);

    while (!(next.equals(start))) {
      this.soFar.add(cameFromEdge.get(next));
      next = cameFromEdge.get(next).from;

    }
    return this.soFar;
  }

  // represents the events every tick
  @Override
  public void onTick() {
    this.processed.get(0);
    // add field for vertex that represnets whther explored or not
    // set explored to true in ontick
    // make scene: draw out already seen vertices

  }
}

// examples and tests
class ExamplesGraph {
  Vertex a;
  Vertex b;
  Vertex c;
  Vertex d;
  Vertex a2;

  Edge aToB;
  Edge bToC;
  Edge cToD;
  Edge dToA;

  Graph maze1;

  ArrayList<Edge> edge1;
  ArrayList<Edge> edge2;

  // for testing the weight
  WeightCompare<Integer> wc;

  WorldScene world;

  // for the testMaze
  ArrayList<ArrayList<Vertex>> testVertices;
  Graph test1;

  void initData() {
    this.a = new Vertex(0, 0);
    this.b = new Vertex(0, 1);
    this.c = new Vertex(0, 2);
    this.d = new Vertex(1, 1);
    this.a2 = new Vertex(0, 0);

    this.aToB = new Edge(a, b);
    this.bToC = new Edge(b, c);
    this.cToD = new Edge(c, d);
    this.dToA = new Edge(d, a);


    this.maze1 = new Graph(20, 15);

    this.wc = new WeightCompare<Integer>();

    this.world = new WorldScene(500, 500);

    this.testVertices = new ArrayList<ArrayList<Vertex>>();
    this.test1 = new Graph(testVertices);

  }

  // tests the game
  void testBigBang(Tester t) {
    this.initData();
    Graph game = new Graph(20, 15);
    // width, height, tick rate = 0.5 means every 0.5 seconds the onTick method
    // will get called.
    game.bigBang(game.width * 30 + 1, game.height * 30 + 1, 0.5);
  }

  // tests randomization
  void testHashCode(Tester t) {
    this.initData();
    t.checkExpect(a.hashCode(), 961);
    t.checkExpect(b.hashCode(), 962);
    t.checkExpect(c.hashCode(), 963);
    t.checkExpect(d.hashCode(), 993);
  }

  // tests the equals method
  void testEquals(Tester t) {
    this.initData();
    t.checkExpect(a.equals(a), true);
    t.checkExpect(a.equals(a2), true); // false
    t.checkExpect(a.equals(b), false);
    t.checkExpect(b.equals(a2), false);
    t.checkExpect(a.equals(aToB), false);
  }

  // tests the draw method
  void testDraw(Tester t) {
    this.initData();
    t.checkExpect(a.draw(), new RectangleImage(30, 30, OutlineMode.SOLID, Color.LIGHT_GRAY));
    t.checkExpect(b.draw(), new RectangleImage(30, 30, OutlineMode.SOLID, Color.LIGHT_GRAY));
    t.checkExpect(a2.draw(), new RectangleImage(30, 30, OutlineMode.SOLID, Color.LIGHT_GRAY));
  }

  // tests the drawOutline method
  void testDrawOutline(Tester t) {
    this.initData();
    t.checkExpect(a.drawOutline(), new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.black));
    t.checkExpect(b.drawOutline(), new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.black));
    t.checkExpect(a2.drawOutline(), new RectangleImage(30, 30, OutlineMode.OUTLINE, Color.black));
  }

  // tests the MakeMaze method
  void testMakeMaze(Tester t) {
    this.initData();
    t.checkExpect(this.maze1.allVertices.size(), 20);
    t.checkExpect(this.maze1.allVertices.get(0).size(), 15);
    t.checkExpect(this.maze1.allVertices.get(1).size(), 15);
  }

  // tests the makeEdges method
  //  void testMakeEdges(Tester t) {
  //    this.initData();
  //    t.checkExpect(this.maze1.allEdges.size(), 565);
  //  }

  // // tests the makeScene method
  // void testMakeScene(Tester t) {
  // this.initData();
  // this.world.placeImageXY(test1.allVertices.get(0).get(0).draw(), 0, 0);
  // this.world.placeImageXY(test1.allVertices.get(0).get(1).draw(), 0, 0);
  // this.world.placeImageXY(test1.allVertices.get(1).get(0).draw(), 0, 0);
  // this.world.placeImageXY(test1.allVertices.get(1).get(1).draw(), 0, 0);
  //
  // t.checkExpect(this.test1.makeScene(), this.world);
  // }
  //

  // tests the undraw method
  //  void testUndraw(Tester t) {
  //    this.initData();
  //    t.checkExpect(this.aToB.undraw(),
  //        new RectangleImage(28, 28, OutlineMode.SOLID, Color.LIGHT_GRAY));
  //    t.checkExpect(this.bToC.undraw(),
  //        new RectangleImage(28, 28, OutlineMode.SOLID, Color.LIGHT_GRAY));
  //    t.checkExpect(this.cToD.undraw(),
  //        new RectangleImage(28, 28, OutlineMode.SOLID, Color.LIGHT_GRAY));
  //    t.checkExpect(this.dToA.undraw(),
  //        new RectangleImage(28, 28, OutlineMode.SOLID, Color.LIGHT_GRAY));
  //  }

  //// tests the compare method
  // void testCompare(Tester t) {
  // this.initData();
  // t.checkExpect(this.wc.compare(bToC, aToB) > 0, false);
  // t.checkExpect(this.wc.compare(dToA, aToB) > 0, false);
  // t.checkExpect(this.wc.compare(bToC, cToD) > 0, false);
  // }

  // tests the find method
  void testUnion(Tester t) {
    this.initData();
    HashMap<Vertex, Vertex> hm = new HashMap<Vertex, Vertex>();
    hm.put(a, a);
    t.checkExpect(hm.containsValue(a), true);
    hm.put(b, b);
    t.checkExpect(hm.containsValue(b), true);
    hm.put(c, d);
    t.checkExpect(hm.containsValue(c), false);
  }

  // test the krusal's method
  //  void testKrusals(Tester t) {
  //    this.initData();
  //    t.checkExpect(this.maze1.allEdges.size() > this.maze1.kruskals().size(), true);
  //    // test union find
  //  }

  //  // tests the find method
  //  void testFind(Tester t) {
  //    this.initData();
  //    HashMap<Vertex, Vertex> hm = new HashMap<Vertex, Vertex>();
  //    hm.put(a, a);
  //    t.checkExpect(maze1.find(hm, a), a);
  //    hm.put(b, c);
  //    t.checkExpect(maze1.find(hm, b), b);
  //    hm.put(d, a);
  //    t.checkExpect(maze1.find(hm, a), a);
  //  }

  void testBreadthFirst() {
    this.initData();

  }

  void testDepthFirst() {
    this.initData();

  }
}
