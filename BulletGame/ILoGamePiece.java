import javalib.funworld.*;

// represents a list of game pieces
interface ILoGamePiece {

  // places all of the game pieces in the list on the given scene
  WorldScene placeAll(WorldScene scene);

  // adds the given ship to this list
  ILoGamePiece appendShip(IGamePiece ship);

  // adds the given bullet to this list
  ILoGamePiece appendBullet(IGamePiece bullet);
  
  //moves the list of game pieces, depending on what type in it
  ILoGamePiece moveLoGamePiece();
  
  //removes any game pieces that are off screen
  ILoGamePiece removeOffscreen();
  
  // returns true if the given game piece overlaps with any in the list
  boolean collisionDetectList(AGamePiece given);
  
  // removes any ship that has collided with a bullet game piece
  //  ILoGamePiece removeAllCollision(ILoGamePiece given);
  
}

// empty list of game pieces
class MtLoGamePiece implements ILoGamePiece {

  // places all of the game pieces in the list on the given scene
  public WorldScene placeAll(WorldScene scene) {
    return scene;
  }

  // adds the given ship to this list
  public ILoGamePiece appendShip(IGamePiece ship) {
    return new ConsLoGamePiece(ship, new MtLoGamePiece());
  }

  // adds the given bullet to this list
  public ILoGamePiece appendBullet(IGamePiece bullet) {
    return new ConsLoGamePiece(bullet, new MtLoGamePiece());
  }
  
  //moves the list of game pieces, depending on what type in it
  public ILoGamePiece moveLoGamePiece() {
    return new MtLoGamePiece();
  }
  
  //removes any game pieces that are off screen
  public ILoGamePiece removeOffscreen() {
    return this;
  }
  
  //returns true if the given game piece overlaps with any in the list
  public boolean collisionDetectList(AGamePiece given) {
    return false;
  }
  
  //removes any ship that has collided with a bullet game piece
  // public ILoGamePiece removeAllCollision(ILoGamePiece given) {
  //    return given;
  //  }
 

}

// non empty list of game pieces
class ConsLoGamePiece implements ILoGamePiece {
  IGamePiece first;
  ILoGamePiece rest;

  ConsLoGamePiece(IGamePiece first, ILoGamePiece rest) {
    this.first = first;
    this.rest = rest;
  }

  // places all of the game pieces in the list on the given scene
  public WorldScene placeAll(WorldScene scene) {
    return this.rest.placeAll(this.first.place(scene));
  }

  // adds the given ship to this list
  public ILoGamePiece appendShip(IGamePiece ship) {
    return new ConsLoGamePiece(ship, this);
  }

  // adds the given bullet to this list
  public ILoGamePiece appendBullet(IGamePiece bullet) {
    return new ConsLoGamePiece(bullet, this);
  }
  
  //moves the list of game pieces, depending on what type in it
  public ILoGamePiece moveLoGamePiece() {
    return new ConsLoGamePiece(this.first.move(), this.rest.moveLoGamePiece());
  }
  
  //removes any game pieces that are off screen
  public ILoGamePiece removeOffscreen() {
    if (this.first.isOffscreen()) {
      return this.rest.removeOffscreen();
    } else {
      return new ConsLoGamePiece(this.first, this.rest.removeOffscreen());
    }
  }
  
  //returns true if the given game piece overlaps with any in the list
  public boolean collisionDetectList(AGamePiece given) {
    return this.first.collisionDetect(given) || this.rest.collisionDetectList(given);
  }
  
  /*
  //removes any ship that has collided with a bullet game piece
  public ILoGamePiece removeAllCollision(ILoGamePiece given) {
    if (given.collisionDetectList(this.first)) {
      return this.rest.removeAllCollision(given);
    }else {
      return new ConsLoGamePiece(this.first, this.rest.removeAllCollision(given));
    }
    */
}
  
  
