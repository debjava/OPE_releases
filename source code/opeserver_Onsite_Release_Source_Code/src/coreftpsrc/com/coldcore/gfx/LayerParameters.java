/**
 * Created by IntelliJ IDEA.
 * User: sergey
 * Date: Oct 17, 2005
 * Time: 10:43:19 AM
 * Parameters.
 */
package com.coldcore.gfx;

import java.awt.*;

public class LayerParameters {

  private int offsetX, offsetY;
  private Align alignX, alignY;


  public void setOffset(int x, int y) {
    this.offsetX = x;
    this.offsetY = y;
  }


  public Point getOffset() {
    return new Point(offsetX, offsetY);
  }


  public void setAlign(Align x, Align y) {
    this.alignX = x;
    this.alignY = y;
  }


  public Align getAlignX() {
    return alignX == null ? Align.LEFT : alignX;
  }


  public Align getAlignY() {
    return alignY == null ? Align.TOP : alignY;
  }
}
