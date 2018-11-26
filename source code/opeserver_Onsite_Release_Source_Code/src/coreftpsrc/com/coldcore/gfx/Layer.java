/**
 * Created by IntelliJ IDEA.
 * User: sergey
 * Date: Oct 17, 2005
 * Time: 10:40:27 AM
 * Image layer operations.
 */
package com.coldcore.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Layer {

  /** Add a new layer to an image
   * @param img Image
   * @param layer Layer
   * @param lp Parameters
   */
  public static void addLayer(BufferedImage img, Image layer, LayerParameters lp) {
    if (!Gfx.isValid(img) || !Gfx.isValid(layer)) return;

    int x = 0;
    int y = 0;
    int iw = img.getWidth();
    int ih = img.getHeight();
    int lw = layer.getWidth(null);
    int lh = layer.getHeight(null);

    switch (lp.getAlignX()) {
      case TOP:
      case LEFT: x = 0; break;
      case BOTTOM:
      case RIGHT: x = iw-lw; break;
      case CENTER: x = iw/2-lw/2; break;
    }

    switch (lp.getAlignY()) {
      case TOP:
      case LEFT: y = 0; break;
      case BOTTOM:
      case RIGHT: y = ih-lh; break;
      case CENTER: y = ih/2-lh/2; break;
    }

    Point offset = lp.getOffset();
    x += offset.x;
    y += offset.y;

    Graphics2D g2 = img.createGraphics();
    g2.drawImage(layer, x,y, null);
    g2.dispose();
  }
}
