import java.awt.Color;
import java.util.Random;

import javalib.funworld.WorldScene;

import javalib.worldimages.CircleImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.WorldImage;

// represents a game piece
interface IGamePiece {

  // draws the given image
  WorldImage draw();

  // places an image game piece in the given scene
  WorldScene place(WorldScene scene);

  // returns true if the given game piece is not within the given height and width
  boolean isOffscreen();

  // moves the game piece depending on what type it is
  IGamePiece move();

  // returns true if this game piece and the given game piece collide at all
  boolean collisionDetect(AGamePiece given);

  //
  // WorldScene explode(WorldScene scene);

}

// represents the abstract class of a game piece
abstract class AGamePiece implements IGamePiece {
  Color color;
  int size;
  MyPosn velocity;
  MyPosn position;

  AGamePiece(Color color, int size, MyPosn velocity, MyPosn position) {
    this.color = color;
    this.size = size;
    this.velocity = velocity;
    this.position = position;
  }

  AGamePiece() {
  }

  // draws the given image
  public WorldImage draw() {
    WorldImage circle = new CircleImage(this.size, OutlineMode.SOLID, this.color);
    return circle;
  }

  // places an image gamepiece in the given scene
  public boolean isOffscreen() {
    return this.position.isOffscreen();
  }

  // returns true if the given gamepiece is not within the given height and width
  public WorldScene place(WorldScene scene) {
    scene = scene.placeImageXY(this.draw(), this.position.x, this.position.y);
    return scene;
  }

  // moves the game piece depending on what type it is
  // will be overridden
  public IGamePiece move() {
    return this;
  }

  // returns true if this game piece and the given game piece collide at all
  public boolean collisionDetect(AGamePiece given) {
    return this.position.x + 5 == given.position.x + 5
        && this.position.y + 5 == given.position.y + 5;
  }

}

// represents a ship game piece
class Ship extends AGamePiece {
  Ship(Color color, int size, MyPosn velocity, MyPosn position) {
    super(color, size, velocity, position);

    this.color = Color.CYAN;
    this.size = 10;
    this.velocity = velocity;
  }

  Ship(MyPosn position) {
    super(Color.CYAN, 10, new MyPosn(3, 0), position);

    // this.velocity = new MyPosn (0, 0);
  }

  Ship() {

    this.color = Color.CYAN;
    this.size = 10;

    int rand01 = (new Random()).nextInt(2);
    int randY = (new Random()).nextInt(200);
    if (rand01 == 0) {
      this.velocity = new MyPosn(10, 0);
      this.position = new MyPosn(0, randY + 50);
    }
    else {
      this.velocity = new MyPosn(-10, 0);
      this.position = new MyPosn(500, randY + 50);
    }
  }

  // moves the across the x-axis based on starting position
  public IGamePiece move() {
    return new Ship(this.color, this.size, this.velocity, this.position.add(this.velocity));
  }

  //returns true if this game piece and the given game piece collide at all
  public boolean collisionDetect(AGamePiece given) {
    return this.position.x + 5 == given.position.x + 5
        && this.position.y + 5 == given.position.y + 5 && given.size == 4;
  }

}
