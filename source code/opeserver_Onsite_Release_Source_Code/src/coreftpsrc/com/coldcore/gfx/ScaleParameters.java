/**
 * Created by IntelliJ IDEA.
 * User: sergey
 * Date: Oct 17, 2005
 * Time: 10:43:19 AM
 * Parameters.
 */
package com.coldcore.gfx;

import java.awt.*;

public class ScaleParameters {

  private int minW, minH, maxW, maxH;
  private boolean proportional;
  private ImageType imageType;


  public ScaleParameters() {
    proportional = true;
  }


  public void setMinSize(int width, int height) {
    this.minW = width;
    this.minH = height;
  }


  public void setMaxSize(int width, int height) {
    this.maxW = width;
    this.maxH = height;
  }


  public Point getMinSize() {
    int x = minW;
    int y = minH;
    if (x < 0) x = 0;
    if (y < 0) y = 0;
    if (x != 0 && maxW != 0 && x > maxW) x = maxW;
    if (y != 0 && maxH != 0 && y > maxH) y = maxH;
    return new Point(x, y);
  }


  public Point getMaxSize() {
    int x = maxW;
    int y = maxH;
    if (x < 0) x = 0;
    if (y < 0) y = 0;
    if (x != 0 && minW != 0 && x < minW) x = minW;
    if (y != 0 && minH != 0 && y < minH) y = minH;
    return new Point(x, y);
  }


  public boolean isProportional() {
    return proportional;
  }


  public ImageType getImageType() {
    return imageType == null ? ImageType.TYPE_INT_RGB : imageType;
  }


  public void setImageType(ImageType imageType) {
    this.imageType = imageType;
  }
}
