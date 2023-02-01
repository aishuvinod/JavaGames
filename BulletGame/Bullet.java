import java.awt.Color;

// represents a bullet game piece
class Bullet extends AGamePiece {

  Bullet(Color color, int size, MyPosn velocity, MyPosn position) {
    super(color, size, velocity, position);
    this.color = Color.pink;
    this.size = 4;
    this.velocity = velocity;

  }

  Bullet(MyPosn position) {
    super(Color.pink, 4, new MyPosn(0, 0), position);
  }

  // moves the bullet up the y-axis
  public IGamePiece move() {
    return new Bullet(this.position.moveBullet());
  }

  //returns true if this game piece and the given game piece collide at all
  public boolean collisionDetect(AGamePiece given) {
    return this.position.x + 5 == given.position.x + 5
        && this.position.y + 5 == given.position.y + 5 && given.size == 10;
  }

}
