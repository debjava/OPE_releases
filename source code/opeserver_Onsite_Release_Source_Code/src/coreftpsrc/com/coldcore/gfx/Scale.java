/**
 * Created by IntelliJ IDEA.
 * User: sergey
 * Date: Oct 17, 2005
 * Time: 10:40:27 AM
 * Image scaling.
 */
package com.coldcore.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Scale {

  /** @see com.coldcore.gfx.Scale#scaleImage(java.awt.Image, int, int, ImageType) */
  public static BufferedImage scaleImage(Image img, ScaleParameters sp) {
    if (!Gfx.isValid(img)) return null;

    Point size = calculateScaledSize(img, sp);
    return scaleImage(img, size.x, size.y, sp.getImageType());
  }


  /** Calculate scaled size of an image
   * @param img Image
   * @param sp Parameters
   * @return Scaled size
   */
  public static Point calculateScaledSize(Image img, ScaleParameters sp) {
    if (!Gfx.isValid(img)) return null;

    int width = img.getWidth(null);
    int height = img.getHeight(null);

    Point minSize = sp.getMinSize();
    Point maxSize = sp.getMaxSize();

    //No scale is needed
    if (minSize.x == 0 && minSize.y == 0 && maxSize.x == 0 && maxSize.y == 0)
      return new Point(width, height);

    //Unproportional scale
    if (!sp.isProportional()) {
      int nw = minSize.x != 0 ? minSize.x : maxSize.y != 0 ? maxSize.y : width;
      int nh = minSize.y != 0 ? minSize.y : maxSize.y != 0 ? maxSize.y : height;
      return new Point(nw, nh);
    }

    //No proportional scale is needed
    if (width >= minSize.x && width <= maxSize.x && height >= minSize.y && height <= maxSize.y)
      return new Point(width, height);

    int nw = width;
    int nh = height;

    double wratio = (double)width/(double)height;
    double hratio = (double)height/(double)width;

    //Scale up to min allowed width
    if (minSize.x != 0 && nw < minSize.x) {
      nw = minSize.x;
      nh = (int)((double)nw/wratio);
    }

    //Scale up to min allowed height
    if (minSize.y != 0 && nh < minSize.y) {
      nh = minSize.y;
      nw = (int)((double)nh/hratio);
    }

    //Scale down to max allowed width
    if (maxSize.x != 0 && nw > maxSize.x) {
      nw = maxSize.x;
      nh = (int)((double)nw/wratio);
    }

    //Scale down to max allowed height
    if (maxSize.y != 0 && nh > maxSize.y) {
      nh = maxSize.y;
      nw = (int)((double)nh/hratio);
    }

    return new Point(nw, nh);
  }


  /** Scale an image
   * @param img Image
   * @param nw New width
   * @param nh New height
   * @param imageType Type of the new image
   * @return Scaled image
   */
  public static BufferedImage scaleImage(Image img, int nw, int nh, ImageType imageType) {
    if (!Gfx.isValid(img)) return null;

    BufferedImage bimg = Gfx.createImage(nw, nh, imageType);
    Graphics2D g2 = bimg.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(img, 0, 0, nw, nh, null);
    g2.dispose();

    return bimg;
  }
}
