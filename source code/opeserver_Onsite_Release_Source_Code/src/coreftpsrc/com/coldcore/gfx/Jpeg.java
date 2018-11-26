/**
 * Created by IntelliJ IDEA.
 * User: sergey
 * Date: Oct 17, 2005
 * Time: 10:40:27 AM
 * JPEG format.
 */
package com.coldcore.gfx;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Jpeg {

  /** Convert image data into JPEG format
   * @param img Image
   * @param out Output
   * @param quality Quality
   */
  public static void write(BufferedImage img, OutputStream out, float quality) throws IOException {
    if (!Gfx.isValid(img)) return;

    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img);
    param.setQuality(quality, false);
    encoder.setJPEGEncodeParam(param);
    encoder.encode(img);
    out.flush();
  }


  /** @see com.coldcore.gfx.Jpeg#write(java.awt.image.BufferedImage, java.io.OutputStream, float) */
  public static void write(BufferedImage img, OutputStream out, JpegParameters jp) throws IOException {
    if (!Gfx.isValid(img)) return;

    //No max size specified
    if (jp.getMaxSize() == 0) {
      float quality = Math.max(jp.getQuality().x, jp.getQuality().y);
      write(img, out, quality);
      return;
    }

    //Match max size
    int maxSize = jp.getMaxSize();
    float quality = jp.getQuality().y;
    byte[] b;
    while (true) {
      b = write(img, quality);
      if (b.length <= maxSize) break;
      if (quality <= jp.getQuality().x) break;
      if (jp.getQualityStep() == 0f) quality = jp.getQuality().x;
      else quality -= jp.getQualityStep();
      if (quality < jp.getQuality().x) break;
    }

    out.write(b);
    out.flush();
  }


  /** Convenience method
   * @see com.coldcore.gfx.Jpeg#write(java.awt.image.BufferedImage, java.io.OutputStream, float)
   */
  public static byte[] write(BufferedImage img, JpegParameters jp) throws IOException {
    if (!Gfx.isValid(img)) return null;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    write(img, out, jp);
    return out.toByteArray();
  }


  /** Convenience method
   * @see com.coldcore.gfx.Jpeg#write(java.awt.image.BufferedImage, java.io.OutputStream, float)
   */
  public static byte[] write(BufferedImage img, float quality) throws IOException {
    if (!Gfx.isValid(img)) return null;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    write(img, out, quality);
    return out.toByteArray();
  }
}
