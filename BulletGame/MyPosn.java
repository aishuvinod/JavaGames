import java.util.Random;

import javalib.worldimages.Posn;

class MyPosn extends Posn {
  //standard constructor
  MyPosn(int x, int y) {
    super(x, y);
  }

  // constructor to convert from a Posn to a My Posn
  MyPosn(Posn p) {
    this(p.x, p.y);
  }

  // given two numbers representing the width and height of a
  // screen, determines if this position lies outside of it
  public boolean isOffscreen() {
    return 0 > this.x || this.x > 500 || 0 > this.y || this.y > 300;
  }

  // produces a myposn randomly along the y-axis
  public MyPosn randomShip() {
    // int randX = (new Random()).nextInt(400);
    int rand01 = (new Random()).nextInt(2);

    int randY = (new Random()).nextInt(300);

    return new MyPosn(rand01 * 500, randY);

  }

  //given another MyPosn will add its x and y values to this one and output a new MyPosn
  public MyPosn add(MyPosn other) {
    return new MyPosn((this.x + other.x), (other.y + this.y));
  }

  // moves the myposn up
  public MyPosn moveBullet() {
    return new MyPosn(250, this.y - 20);
  }

}
