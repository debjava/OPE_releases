/**
 * Created by IntelliJ IDEA.
 * User: sergey
 * Date: Oct 17, 2005
 * Time: 12:38:39 PM
 * Image type.
 */
package com.coldcore.gfx;

import java.awt.image.BufferedImage;

public enum ImageType {

  TYPE_INT_RGB(BufferedImage.TYPE_INT_RGB),
  TYPE_4BYTE_ABGR(BufferedImage.TYPE_4BYTE_ABGR);

  public int value;

  ImageType(int value) {
   this.value = value;
 }
}
