import java.awt.Color;

import javalib.funworld.WorldScene;
import javalib.worldcanvas.WorldCanvas;
import tester.Tester;

// examples and tests for the game and the game pieces in it
class ExamplesGamePieces {
  MyPosn posn1 = new MyPosn(5, 5);
  MyPosn posn2 = new MyPosn(2, 4);
  MyPosn posn3 = new MyPosn(3, 5);

  MyPosn posn4 = new MyPosn(100, 100);
  MyPosn posn4moved = new MyPosn(250, 90);
  MyPosn movedPosn4 = new MyPosn(250, 80);

  AGamePiece bullet1 = new Bullet(Color.pink, 30, this.posn1, this.posn1);
  AGamePiece ship1 = new Ship(Color.cyan, 30, this.posn1, this.posn1);

  AGamePiece sbullet = new Bullet(Color.pink, 2, this.posn1, this.posn4);
  AGamePiece sship = new Ship(Color.CYAN, 10, this.posn1, this.posn4);
  AGamePiece sbulletMoved = new Bullet(Color.pink, 2, this.posn1, this.movedPosn4);
  AGamePiece sshipMoved = new Ship(Color.CYAN, 10, this.posn1, this.movedPosn4);

  ILoGamePiece mtlist = new MtLoGamePiece();
  ILoGamePiece lobullet = new ConsLoGamePiece(this.sbullet,
      new ConsLoGamePiece(this.sbullet, new ConsLoGamePiece(this.sbullet, new MtLoGamePiece())));
  ILoGamePiece lobulletmoved = new ConsLoGamePiece(this.sbulletMoved, new ConsLoGamePiece(
      this.sbulletMoved, new ConsLoGamePiece(this.sbulletMoved, new MtLoGamePiece())));
  ILoGamePiece loship = new ConsLoGamePiece(this.sship,
      new ConsLoGamePiece(this.sship, new ConsLoGamePiece(this.sship, new MtLoGamePiece())));
  ILoGamePiece loshipmoved = new ConsLoGamePiece(this.sshipMoved, new ConsLoGamePiece(
      this.sshipMoved, new ConsLoGamePiece(this.sshipMoved, new MtLoGamePiece())));

  ILoGamePiece shipAppend = new ConsLoGamePiece(this.ship1, new ConsLoGamePiece(this.sship,
      new ConsLoGamePiece(this.sship, new ConsLoGamePiece(this.sship, new MtLoGamePiece()))));
  ILoGamePiece listShip1 = new ConsLoGamePiece(this.ship1, new MtLoGamePiece());
  ILoGamePiece bulletAppend = new ConsLoGamePiece(this.bullet1, new ConsLoGamePiece(this.sbullet,
      new ConsLoGamePiece(this.sbullet, new ConsLoGamePiece(this.sbullet, new MtLoGamePiece()))));
  ILoGamePiece listBullet1 = new ConsLoGamePiece(this.bullet1, new MtLoGamePiece());

  AGamePiece bulletOff = new Bullet(Color.pink, 30, this.posn1, this.offscreenX);
  AGamePiece shipOff = new Ship(Color.cyan, 30, this.posn1, this.offscreenY);
  MyPosn positionMove = new MyPosn(400, 400);
  AGamePiece sshipmoved = new Ship(Color.cyan, 30, this.posn1, this.positionMove);
  MyPosn xaxis = new MyPosn(0, 30);
  MyPosn positionMove2 = new MyPosn(0, 80);
  AGamePiece sship2 = new Ship(Color.cyan, 30, this.posn1, this.xaxis);
  AGamePiece sship2moved = new Ship(Color.cyan, 30, this.posn1, this.positionMove2);

  MyPosn posn1add2 = new MyPosn(7, 9);
  MyPosn posn3add4 = new MyPosn(103, 105);
  MyPosn origin = new MyPosn(0, 0);
  MyPosn offscreenX = new MyPosn(502, 50);
  MyPosn offscreenY = new MyPosn(250, 330);
  MyPosn offscreenMinus = new MyPosn(-3, 400);
  MyPosn offscreenMinus2 = new MyPosn(50, -4);

  ILoGamePiece offlobullet = new ConsLoGamePiece(this.bulletOff, this.lobullet);
  ILoGamePiece offloship = new ConsLoGamePiece(this.shipOff, this.loship);
  ILoGamePiece offlobullet2 = new ConsLoGamePiece(this.sbullet, this.offlobullet);
  ILoGamePiece offlobullet2fix = new ConsLoGamePiece(this.sbullet, this.lobullet);
  ILoGamePiece offloship2 = new ConsLoGamePiece(this.sship, this.offloship);
  ILoGamePiece offloship2fix = new ConsLoGamePiece(this.sship, this.loship);

  // tests the drawBullet method
  boolean testdrawBullet(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(this.bullet1.draw(), 250, 250)) && c.show();
  }

  // tests the drawShip method
  boolean testdrawShip(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(this.ship1.draw(), 250, 250)) && c.show();
  }

  // tests the testPlace method
  boolean testPlace(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(this.bullet1.draw(), 5, 5));
  }

  // tests the moveBullet method
  boolean testMoveBullet(Tester t) {
    return t.checkExpect(this.posn4.moveBullet(), this.posn4moved);
  }

  // tests the placeAllBullet method
  boolean testplaceAllBullet(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(this.sbullet.draw(), 100, 100)
        .placeImageXY(this.sbullet.draw(), 100, 100).placeImageXY(this.sbullet.draw(), 100, 100))
        && c.show();
  }

  // tests the placeAllShip method
  boolean testplaceAllShip(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c
        .drawScene(s.placeImageXY(this.sship.draw(), 100, 100)
            .placeImageXY(this.sship.draw(), 100, 100).placeImageXY(this.sship.draw(), 100, 100))
        && c.show();
  }

  // tests the appendShip method
  boolean testAppendShip(Tester t) {
    return t.checkExpect(this.loship.appendShip(this.ship1), this.shipAppend)
        && t.checkExpect(this.mtlist.appendShip(this.ship1), this.listShip1);
  }

  // tests the appendBullet method
  boolean testAppendBullet(Tester t) {
    return t.checkExpect(this.lobullet.appendBullet(this.bullet1), this.bulletAppend)
        && t.checkExpect(this.mtlist.appendBullet(this.bullet1), this.listBullet1);
  }

  // tests the collisionDetect method
  boolean testCollisionDetect(Tester t) {
    return t.checkExpect(this.bullet1.collisionDetect(this.ship1), true)
        && t.checkExpect(this.sship.collisionDetect(this.bullet1), false);
  }

  // tests the add method
  boolean testAdd(Tester t) {
    return t.checkExpect(this.posn1.add(this.posn2), this.posn1add2)
        && t.checkExpect(this.posn3.add(this.posn4), this.posn3add4)
        && t.checkExpect(this.origin.add(this.posn1), this.posn1)
        && t.checkExpect(this.posn2.add(this.origin), this.origin);
  }

  // tests the isOffScreenPosn method
  boolean testIsOffscreenPosn(Tester t) {
    return t.checkExpect(this.posn1.isOffscreen(), false)
        && t.checkExpect(this.offscreenX.isOffscreen(), true)
        && t.checkExpect(this.offscreenY.isOffscreen(), true)
        && t.checkExpect(this.offscreenMinus.isOffscreen(), true)
        && t.checkExpect(this.offscreenMinus2.isOffscreen(), true);
  }

  // tests the moveLoGamePiece method
  boolean testMoveLoGamePiece(Tester t) {
    return t.checkExpect(this.mtlist.moveLoGamePiece(), new MtLoGamePiece())
        && t.checkExpect(this.lobullet.moveLoGamePiece(), this.lobulletmoved)
        && t.checkExpect(this.loship.moveLoGamePiece(), this.loshipmoved);
  }

  // tests the isOffscreenGamePiece method
  boolean testIsOffscreenGamePiece(Tester t) {
    return t.checkExpect(this.bullet1.isOffscreen(), false)
        && t.checkExpect(this.sship.isOffscreen(), false);
    // && t.checkExpect(this.bulletOff.isOffscreen(), true)
    // && t.checkExpect(this.shipOff.isOffscreen(), true);
  }

  // tests the moveShip method
  boolean testMoveShip(Tester t) {
    return t.checkExpect(this.sship.move(), this.sshipmoved)
        && t.checkExpect(this.sship2.move(), this.sship2moved);
  }

  /*
   * boolean testRemoveAllCollision (Tester t) { return
   * t.checkExpect(this.lobullet.RemoveAllCollision(loship), ) &&
   * t.checkExpect(this.loshipmoved.RemoveAllCollision(lobulletmoved), );
   * 
   */

  // tests the removeOffScreen method
  boolean testRemoveOffScreen(Tester t) {
    return t.checkExpect(this.loship.removeOffscreen(), this.loship)
        && t.checkExpect(this.lobullet.removeOffscreen(), this.lobullet);
    //        && t.checkExpect(this.offlobullet.removeOffscreen(), this.lobullet)
    // && t.checkExpect(this.offloship.removeOffscreen(), this.loship);
    // && t.checkExpect(this.mtlist.removeOffscreen(), this.mtlist)
    // && t.checkExpect(this.offlobullet2.removeOffscreen(), this.offlobullet2fix)
    // && t.checkExpect(this.offloship2.removeOffscreen(), this.offloship2fix);
  }

  // tests the moveLoGamePiece method
  boolean testmoveLoGamePiece(Tester t) {
    return t.checkExpect(this.mtlist.collisionDetectList(this.bullet1), false)
        && t.checkExpect(this.lobullet.collisionDetectList(this.sbullet), false)
        && t.checkExpect(this.lobullet.collisionDetectList(this.sship), true)
        && t.checkExpect(this.lobullet.collisionDetectList(this.sship2), false)

        && t.checkExpect(this.loship.collisionDetectList(this.sbullet), true)
        && t.checkExpect(this.loship.collisionDetectList(this.sship), false)
        && t.checkExpect(this.loship.collisionDetectList(this.bullet1), false);
  }

}
