import tester.*;

import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;

// represents the fields involved in the game
class MyGame extends World {
  int width = 500;
  int height = 300;
  int currentTick;
  int frequency;

  boolean displayBulletCount;

  ILoGamePiece bullets;
  ILoGamePiece ships;

  int bulletCount;
  int shipsDestroyed;

  //for user
  MyGame(int width, int height, int bulletCount) {
    this.width = width;
    this.height = height;
    this.currentTick = 1;
    this.frequency = frequency;

    this.displayBulletCount = false;

    this.bullets = new MtLoGamePiece();
    this.ships = new MtLoGamePiece();

    this.bulletCount = bulletCount;
    this.shipsDestroyed = shipsDestroyed;
  }

  //for me
  MyGame(int width, int height, int currentTick, int frequency, boolean displayBulletCount,
      ILoGamePiece bullets, ILoGamePiece ships, int bulletCount, int shipsDestroyed) {
    if (width < 0 || height < 0 || currentTick < 0) {
      throw new IllegalArgumentException("Illegal arguments.");
    }

    this.width = width;
    this.height = height;
    this.currentTick = currentTick;
    this.frequency = frequency;
    this.displayBulletCount = displayBulletCount;
    this.bullets = bullets;
    this.ships = ships;
    this.bulletCount = bulletCount;

  }

  @Override
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(this.width, this.height);
    scene = this.bullets.placeAll(this.ships.placeAll(scene));
    WorldScene scene2 = this.addBulletDisplayToScene(scene);
    return scene2;

  }

  // displays the number of bullets left for the user to use on the screen
  WorldScene addBulletDisplayToScene(WorldScene scene) {
    return scene.placeImageXY(
        new TextImage("Bullets Left: " + Integer.toString(this.bulletCount), Color.magenta), 50, 20)
        .placeImageXY(new TextImage("Ships Destroyed: 0 ", Color.GREEN), 250, 20);
  }

  // what the scene does at every tick
  @Override
  public World onTick() {

    return new MyGame(this.width, this.height, this.currentTick + 1, this.frequency, true,
        this.bullets.moveLoGamePiece().removeOffscreen(),
        this.ships.appendShip(this.spawnShip()).moveLoGamePiece().removeOffscreen(),
        this.bulletCount, this.shipsDestroyed);

  }
  // We tried to do the new additions to onTick in steps like said in the assignment. 
  //  It would not let us, so we had to put it all in the same step
  // Though process of steps;
  // step 1: add ship
  // step 2: append to list
  // step 3: move list of ships
  // step 4: move bullets
  // step 5: remove off screen
  // step 6: remove collisions
  // step 7; explode bullets
  // step 8: divide bullets
  // step 9: grow bullets
  // Step 10: add to current tick

  // spawns a new ship randomly along the y-axis
  public Ship spawnShip() {
    return new Ship();
  }

  public MyGame addBulletDisplay() {
    return new MyGame(this.width, this.height, this.currentTick, this.frequency, true, this.bullets,
        this.ships, this.bulletCount, this.shipsDestroyed);
  }

  @Override
  public World onKeyEvent(String key) {
    if (key.equals(" ")) {
      // System.out.println("Space got Pressed");
      return new MyGame(this.width, this.height, this.currentTick, this.frequency,
          this.displayBulletCount, this.bullets.appendBullet(this.spawnBullet(new MyPosn(0, 250))),
          this.ships, this.bulletCount - 1, this.shipsDestroyed);
    }
    return this;
  }

  // spawns a new bullet 
  public Bullet spawnBullet(MyPosn newPosition) {
    return new Bullet(newPosition.moveBullet());
  }

  // moves a game piece
  public World moveGamePiece() {
    return new MyGame(this.width, this.height, this.currentTick, this.frequency,
        this.displayBulletCount, this.bullets.moveLoGamePiece(), this.ships.moveLoGamePiece(),
        this.bulletCount, this.shipsDestroyed);
  }

  // displays a scene showing the end of the game
  WorldScene makeEndScene() {
    WorldScene scene = new WorldScene(this.width, this.height);
    return scene.placeImageXY(new TextImage("gameover.", Color.red), (int) (this.width / 2.0),
        (int) (this.height / 2));

  }

  // causes the game to end
  @Override
  public WorldEnd worldEnds() {
    if (this.bulletCount < 1) {
      return new WorldEnd(true, makeEndScene());
    }
    else {
      return new WorldEnd(false, makeEndScene());
    }
  }

}

// tests the entire game
class ExamplesMyWorldProgram {
  boolean testBigBang(Tester t) {
    MyGame game = new MyGame(500, 300, 10);
    // width, height, tick rate = 0.5 means every 0.5 seconds the onTick method will
    // get called.
    return game.bigBang(500, 300, 0.5);
  }

}
