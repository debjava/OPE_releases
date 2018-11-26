/**
 * Created by IntelliJ IDEA.
 * User: sergey
 * Date: Oct 17, 2005
 * Time: 10:40:27 AM
 * General operations.
 */
package com.coldcore.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Gfx {

  /** Test if the image is valid
   * @param img Image
   * @return TRUE if valid, FALSE otherwise
   */
  public static boolean isValid(Image img) {
    if (img == null) return false;
    return img.getWidth(null) != -1 && img.getHeight(null) != -1;
  }


  /** Create an immutable image
   * @param data Data
   * @return Created image
   */
  public static Image createImage(byte[] data) {
    if (data == null) return null;
    Image img = Toolkit.getDefaultToolkit().createImage(data);
    MediaTracker mc = new MediaTracker(new Panel());
    mc.addImage(img, 0);
    try {
      mc.waitForAll();
    } catch (InterruptedException e) {}
    return isValid(img) ? img : null;
  }


  /** Create a mutable image
   * @param data Data
   * @return Created image
   */
  public static BufferedImage createImage(byte[] data, ImageType imageType) {
    Image img = createImage(data);
    if (!isValid(img)) return null;
    return Scale.scaleImage(img, img.getWidth(null), img.getHeight(null), imageType);
  }


  /** Convenience method (transparemt or white image)
   * @see com.coldcore.gfx.Gfx#createImage(int, int, ImageType, java.awt.Color)
   */
  public static BufferedImage createImage(int width, int height, ImageType imageType) {
    return createImage(width, height, imageType, new Color(255,255,255,0));
  }


  /** Create a mutable image
   * @param width Width
   * @param height Height
   * @param imageType Type
   * @param bg Background
   * @return Image
   */
  public static BufferedImage createImage(int width, int height, ImageType imageType, Color bg) {
    BufferedImage bimg = new BufferedImage(width, height, imageType.value);
    Graphics2D g2 = bimg.createGraphics();

    if (imageType == ImageType.TYPE_4BYTE_ABGR) g2.setComposite(AlphaComposite.Src);
    g2.clipRect(0, 0, width, height);
    g2.setPaint(bg);
    g2.fillRect(0, 0, width, height);
    g2.setClip(null);

    g2.dispose();
    return bimg;
  }
}
