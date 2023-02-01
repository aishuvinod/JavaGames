import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tester.*;
import javalib.impworld.*;

import java.awt.Color;
import javalib.worldimages.*;

//Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  static int CELL_SIZE = FloodItWorld.BOARD_SIZE;
  Random rand;

  Cell(int x, int y, boolean flooded) {
    this.x = x * CELL_SIZE;
    this.y = y * CELL_SIZE;
    this.flooded = flooded;
    this.left = null;
    this.right = null;
    this.top = null;
    this.bottom = null;
    this.rand = new Random();
    this.color = this.randomColor();
  }

  // constructor for testing random
  Cell(int x, int y, boolean flooded, Random rand) {
    this.x = x * CELL_SIZE;
    this.y = y * CELL_SIZE;
    this.flooded = flooded;
    this.left = null;
    this.right = null;
    this.top = null;
    this.bottom = null;
    this.rand = rand;
    this.color = this.randomColor();
  }

  // returns 1 of 6 random colors
  public Color randomColor() {
    int randcolor = (this.rand.nextInt(6));
    ArrayList<Color> colors = new ArrayList<Color>(
        Arrays.asList(Color.RED, Color.MAGENTA, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE));
    return colors.get(randcolor);
  }

  // draws the cell as a square
  public WorldImage draw() {
    WorldImage square = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, this.color);
    // moves pinhole to top lefthand corner
    WorldImage square2 = square.movePinhole(0, 0);
    return square2;
  }

  // applies the changes neighbor helper so the neighbor's flooded state can be
  // changed
  // as long as neighbors are not null
  public void changeNeighbors(Color newColor) {
    this.color = newColor;
    if (this.left != null) {
      this.changeNeighborsHelper(this.left, newColor);
    }
    if (this.right != null) {
      this.changeNeighborsHelper(this.right, newColor);
    }
    if (this.top != null) {
      this.changeNeighborsHelper(this.top, newColor);
    }
    if (this.bottom != null) {
      this.changeNeighborsHelper(this.bottom, newColor);
    }
  }

  // changes flooded state to true if the cell is the same color but it has
  // a false flooded state
  public void changeNeighborsHelper(Cell c, Color newColor) {
    if (c.color == newColor && (!c.flooded)) {
      c.flooded = true;
    }
  }
}

class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<ArrayList<Cell>> board;
  static int BOARD_SIZE = 25;
  static int CELL_SIZE = FloodItWorld.BOARD_SIZE;

  // how many times the player has clicked
  int movesSoFar;

  // the current color of flooding
  Color floodColor;

  Random rand;

  FloodItWorld(ArrayList<ArrayList<Cell>> board, ArrayList<Color> colors) {
    this.board = board;
    this.makeBoard();
    this.rand = new Random();
    this.connectCells();
  }

  FloodItWorld(ArrayList<ArrayList<Cell>> board, ArrayList<Color> colors, Random rand) {
    this.rand = rand;
    this.board = board;
    this.makeBoard();
    this.connectCells();
  }

  FloodItWorld(ArrayList<ArrayList<Cell>> board, Random rand) {
    this.board = board;
    this.rand = rand;
    this.makeTestBoard();
  }

  FloodItWorld() {
    this.board = new ArrayList<ArrayList<Cell>>();
    this.makeBoard();
    this.connectCells();
    this.movesSoFar = 0;
  }

  // makes a row of cells, that stacks those rows to create the board
  void makeBoard() {

    for (int i = 0; i < FloodItWorld.BOARD_SIZE; i++) {
      ArrayList<Cell> row = new ArrayList<Cell>();
      for (int j = 0; j < FloodItWorld.BOARD_SIZE; j++) {

        row.add(new Cell(i, j, false));
      }

      this.board.add(row);
    }

    // make origin flooded
    this.board.get(0).get(0).flooded = true;

    // set flood color to origin's color
    this.floodColor = this.board.get(0).get(0).color;
  }

  // generates a smaller board for testing
  void makeTestBoard() {
    int x = FloodItWorld.BOARD_SIZE / 2;
    int y = FloodItWorld.BOARD_SIZE / 2;
    for (int z = 0; z < 4; z++) {
      ArrayList<Cell> newBoard = new ArrayList<Cell>();
      for (int j = 0; j < 4; j++) {
        newBoard.add(new Cell(x, y, false, this.rand));
        x += FloodItWorld.BOARD_SIZE;
      }
      this.board.add(newBoard);
      x = FloodItWorld.BOARD_SIZE / 2;
      y += FloodItWorld.BOARD_SIZE;
    }
  }

  // connects the cells by changing their top, bottom, right and left to what it
  // actually is, instead of null
  void connectCells() {
    for (int y = 0; y < BOARD_SIZE; y++) {
      for (int x = 0; x < BOARD_SIZE; x++) {

        // changes top of cell
        if (y - 1 >= 0) {
          board.get(y).get(x).top = board.get(y - 1).get(x);
        }

        // changes bottom of cell
        if (y + 1 < BOARD_SIZE) {
          board.get(y).get(x).bottom = board.get(y + 1).get(x);
        }

        // changes left of cell
        if (x - 1 >= 0) {
          board.get(y).get(x).left = board.get(y).get(x - 1);
        }

        // changes right of cell
        if (x + 1 < BOARD_SIZE) {
          board.get(y).get(x).right = board.get(y).get(x + 1);
        }

      }
    }
  }

  // locates the cell that has been clicked
  @Override
  public void onMouseClicked(Posn place) {

    // for finding the cell that was clicked on
    for (ArrayList<Cell> row : board) {
      for (Cell c : row) {

        // finds the cell that was clicked
        if (c.x <= place.x && place.x <= c.x + 25 && c.y <= place.y && place.y <= c.y + 25) {

          // updates the flood color to the color of the clicked cell
          this.floodColor = c.color;

          // increments the moves so far counter up by one since the player has clicked
          this.movesSoFar = this.movesSoFar + 1;
        }
      }
    }

    for (ArrayList<Cell> row : board) {
      for (Cell c : row) {
        // makes sure that if the cell was flooded, its neighbors will be changed with
        // the color of the clicked cell
        if (c.flooded) {
          c.changeNeighbors(this.floodColor);
        }
      }
    }
  }

  @Override
  public WorldScene makeScene() {
    WorldScene world = new WorldScene(FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE,
        FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE);

    for (ArrayList<Cell> row : board) {
      for (Cell c : row) {
        world.placeImageXY(c.draw(), c.x + Cell.CELL_SIZE / 2, c.y + Cell.CELL_SIZE / 2);
      }

    }
    WorldScene world2 = this.addTitle(world);
    WorldScene world3 = this.addMovesSoFarToScene(world2);
    WorldScene world4 = this.loseGame(world3);
    WorldScene world5 = this.winGame(world4);

    return world5;
  }

  // adds a welcoming title below game board
  WorldScene addTitle(WorldScene scene) {
    scene.placeImageXY(new TextImage("Welcome to FloodIt!", 30, FontStyle.BOLD, Color.pink), 310,
        700);
    return scene;
  }

  // keeps tracks of how many moves the playing has made and displays it
  WorldScene addMovesSoFarToScene(WorldScene scene) {
    scene.placeImageXY(new TextImage("Steps So Far: " + Integer.toString(this.movesSoFar) + " / 80",
        15, FontStyle.BOLD, Color.blue), 310, 730);
    return scene;
  }

  // loses the game if it takes more than 100 moves
  public WorldScene loseGame(WorldScene scene) {
    if (this.movesSoFar > 80) {
      scene.placeImageXY(new TextImage("GAMEOVER :(", 70, FontStyle.BOLD, Color.black), 310, 310);
    }
    return scene;
  }

  // wins the game if entire board is one color
  public WorldScene winGame(WorldScene scene) {
    for (ArrayList<Cell> row : board) {
      for (Cell c : row) {
        if (!(c.color.equals(this.floodColor))) {
          return scene;
        }
      }
    }
    scene.placeImageXY(new TextImage("YOU WIN :)", 70, FontStyle.BOLD, Color.black), 310, 310);
    return scene;
  }

  @Override
  public void onTick() {

    // connects the right cell if it is the same color
    if (this.board.get(0).get(0).right.color == this.board.get(0).get(0).color) {
      this.board.get(0).get(0).right.flooded = true;
    }

    // connects the bottom cell if it is the same color
    if (this.board.get(0).get(0).bottom.color == this.board.get(0).get(0).color) {
      this.board.get(0).get(0).bottom.flooded = true;
    }

    for (ArrayList<Cell> row : board) {
      for (Cell c : row) {

        // if the cell is already flooded and the right color, nothing happens to it
        if (c.color == this.floodColor && (c.flooded)) {
          return;
        }
        // else if it is not the same color, but is flooded then we change the color to
        // the flood color
        // and update its flood state
        else if (c.flooded) {
          c.color = this.floodColor;
          c.flooded = true;

        }
      }
    }
  }

  // resets the board and creates a new board when the 'r' key is pressed
  @Override
  public void onKeyEvent(String key) {
    if (key.equals("r")) {

      // this.makeBoard();

      // resets moves so far to 0 since it is a new game
      this.movesSoFar = 0;

      for (ArrayList<Cell> row : board) {
        for (Cell c : row) {
          c.color = c.randomColor();
          c.flooded = false;
        }
      }

      this.floodColor = this.board.get(0).get(0).color;
      this.board.get(0).get(0).flooded = true;

    }
    return;
  }

  // waterfall effect attempt
  /*
   * public boolean hasNext(){ return this.worklist.size() > 0 }
   * 
   * public T next(){ BTNode<T> currentCell =
   * this.worklist.removeAtHead().asNode(); this.addIfNotSeen(currentCell.right);
   * this.addIfNotSenn(currentCell.left); this.addIfNotSenn(currentCell.top);
   * this.addIfNotSeen(currentCell.bottom);
   * 
   * addToSeen(seen, currentCell); return node.data;
   */

}

class ExamplesMyWorldProgram {
  ExamplesMyWorldProgram() {
  }

  // example sample cells
  Cell startcell;
  Cell cell0;
  Cell cell1;

  // example game board
  ArrayList<ArrayList<Cell>> gameBoard;
  ArrayList<ArrayList<Cell>> testGameBoard;
  WorldScene world;

  // example colors
  ArrayList<Color> color;

  // example flood it world
  FloodItWorld flood1;
  FloodItWorld testFlood;

  // examples seeded cells for testing random
  Cell cellSeeded1;
  Cell cellSeeded2;
  Cell cellSeeded3;
  Cell cellSeeded4;

  FloodItWorld floodSeeded;

  void initData() {
    this.gameBoard = new ArrayList<ArrayList<Cell>>();
    this.testGameBoard = new ArrayList<ArrayList<Cell>>();
    this.world = new WorldScene(FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE,
        FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE);
    this.color = new ArrayList<Color>();
    this.flood1 = new FloodItWorld(this.gameBoard, this.color);
    this.testFlood = new FloodItWorld(this.testGameBoard, new Random(5));
    this.startcell = new Cell(5, 5, false);
    this.cell0 = new Cell(7, 7, false);
    this.cell1 = new Cell(10, 10, false);
    this.cellSeeded1 = new Cell(10, 10, false, new Random(6));
    this.cellSeeded2 = new Cell(10, 10, false, new Random(10));
    this.cellSeeded3 = new Cell(10, 10, false, new Random(0));
    this.cellSeeded4 = new Cell(10, 10, false, new Random(-1));
    this.floodSeeded = new FloodItWorld(this.gameBoard, this.color, new Random(1));
  }

  // tests makeScene
  // boolean testScene(Tester t) {
  // this.initData();
  // WorldCanvas canvas = new WorldCanvas(FloodItWorld.BOARD_SIZE *
  // FloodItWorld.BOARD_SIZE,
  // FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE);
  // WorldScene scene = this.floodSeeded.makeScene();
  //
  // return canvas.drawScene(scene) && canvas.show();
  // }

  void testBigBang(Tester t) {
    this.initData();
    FloodItWorld game = new FloodItWorld();
    // width, height, tick rate = 0.5 means every 0.5 seconds the onTick method
    // will get called.
    game.bigBang(FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE, 800, 0.5);
  }

  // void testMakeScene(Tester t) {
  // this.initData();
  //
  // this.world.placeImageXY(testFlood.board.get(0).get(0).draw(), 253, 253);
  // this.world.placeImageXY(testFlood.board.get(0).get(3).draw(), 737, 253);
  // this.world.placeImageXY(testFlood.board.get(1).get(3).draw(), 253, 253);
  // this.world.placeImageXY(testFlood.board.get(3).get(1).draw(), 253, 737);
  // this.world.placeImageXY(testFlood.board.get(2).get(2).draw(), 253, 737);
  // this.world.placeImageXY(testFlood.board.get(1).get(2).draw(), 253, 737);
  //
  // t.checkExpect(this.testFlood.makeScene(), this.world);
  // }

  // tests cell creation
  void testCells() {
    startcell = new Cell(19, 18, false);
    cell0 = new Cell(5, 5, false);
    cell1 = new Cell(7, 9, false);
  }

  // tests randomColor method
  public void testRandomColor(Tester t) {
    initData();
    this.cellSeeded1.randomColor();
    t.checkExpect(this.cellSeeded1.color, Color.MAGENTA);

    this.cellSeeded2.randomColor();
    t.checkExpect(this.cellSeeded2.color, Color.GREEN);

    this.cellSeeded3.randomColor();
    t.checkExpect(this.cellSeeded3.color, Color.RED);

    this.cellSeeded4.randomColor();
    t.checkExpect(this.cellSeeded4.color, Color.BLUE);
  }

  // tests draw cell method
  boolean testDraw(Tester t) {
    initData();
    return t.checkExpect(this.cellSeeded1.draw(),
        new RectangleImage(25, 25, OutlineMode.SOLID, Color.MAGENTA))
        && t.checkExpect(this.cellSeeded2.draw(),
            new RectangleImage(25, 25, OutlineMode.SOLID, Color.GREEN))
        && t.checkExpect(this.cellSeeded3.draw(),
            new RectangleImage(25, 25, OutlineMode.SOLID, Color.RED))
        && t.checkExpect(this.cellSeeded4.draw(),
            new RectangleImage(25, 25, OutlineMode.SOLID, Color.BLUE));

  }

  // tests makeBoard method
  void testMakeBoard(Tester t) {
    initData();
    t.checkExpect(this.floodSeeded.board.size(), 50);
    t.checkExpect(this.floodSeeded.board.get(0).size(), 25);
    t.checkExpect(this.floodSeeded.board.get(1).size(), 25);
  }

  // tests ConnectCells method
  void testConnectCells(Tester t) {
    initData();
    t.checkExpect(this.floodSeeded.board.get(1).get(0).top, this.floodSeeded.board.get(0).get(0));
    t.checkExpect(this.floodSeeded.board.get(1).get(0).bottom,
        this.floodSeeded.board.get(2).get(0));
    t.checkExpect(this.floodSeeded.board.get(1).get(0).right, this.floodSeeded.board.get(1).get(1));
    t.checkExpect(this.floodSeeded.board.get(0).get(1).left, this.floodSeeded.board.get(0).get(0));

    t.checkExpect(this.floodSeeded.board.get(0).get(0).top, null);
    t.checkExpect(this.floodSeeded.board.get(24).get(24).bottom, null);
    t.checkExpect(this.floodSeeded.board.get(24).get(24).right, null);
    t.checkExpect(this.floodSeeded.board.get(0).get(0).left, null);
  }

  // tests changeNeighbors method
  void testChangeNeighbors(Tester t) {
    initData();
    this.floodSeeded.board.get(0).get(0).changeNeighbors(Color.black);
    t.checkExpect(this.floodSeeded.board.get(0).get(0).color, Color.black);
    t.checkExpect(this.floodSeeded.board.get(0).get(0).top, null);
    t.checkExpect(this.floodSeeded.board.get(0).get(0).left, null);
    t.checkFail(this.floodSeeded.board.get(0).get(0).bottom, null);
    t.checkFail(this.floodSeeded.board.get(0).get(0).right, null);

  }

  // tests changeNeighborsHelper method
  void testChangeNeighborsHelper(Tester t) {
    initData();
    t.checkExpect(this.cellSeeded2.flooded, false);
    t.checkExpect(this.cellSeeded2.color, Color.green);
    this.cellSeeded2.changeNeighborsHelper(cellSeeded2, Color.green);
    t.checkExpect(this.cellSeeded2.flooded, true);
    t.checkExpect(this.cellSeeded2.color, Color.green);
  }

  // tests onTick method
  void testOnTick(Tester t) {
    initData();
    this.floodSeeded.board.get(1).get(1).flooded = true;
    this.floodSeeded.onTick();
    this.floodSeeded.floodColor = Color.pink;
    this.floodSeeded.board.get(1).get(1).changeNeighbors(this.floodSeeded.floodColor);
    t.checkExpect(this.floodSeeded.board.get(1).get(1).color, Color.pink);
  }

  // tests onMouseClicked method
  void testOnMouseClicked(Tester t) {
    initData();
    t.checkExpect(this.floodSeeded.movesSoFar, 0);
    Posn p = new Posn(2, 2);
    this.floodSeeded.onMouseClicked(p);
    t.checkExpect(this.floodSeeded.movesSoFar, 2);
    t.checkExpect(this.floodSeeded.board.get(0).get(0).color, this.floodSeeded.floodColor);
  }

  // tests onKeyEvent method
  void testOnKeyEvent(Tester t) {
    initData();
    this.floodSeeded.board.get(1).get(1).flooded = true;
    t.checkExpect(this.floodSeeded.board.get(1).get(1).flooded, true);

    this.floodSeeded.onKeyEvent("r");
    t.checkExpect(this.floodSeeded.board.get(1).get(1).flooded, false);
  }

  // tests loseGame method
  void testLoseGame(Tester t) {
    initData();
    this.floodSeeded.movesSoFar = 101;
    // t.checkExpect(this.floodSeeded.board, new ArrayList<ArrayList<Cell>>());
  }

  // tests winGame method
  void testWinGame(Tester t) {
    initData();
  }

}
