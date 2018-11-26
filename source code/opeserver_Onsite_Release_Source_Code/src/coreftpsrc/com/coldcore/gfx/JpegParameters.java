/**
 * Created by IntelliJ IDEA.
 * User: sergey
 * Date: Oct 17, 2005
 * Time: 10:43:19 AM
 * Parameters.
 */
package com.coldcore.gfx;

import java.awt.geom.Point2D;

public class JpegParameters {

  private float minQuality, maxQuality;
  private float qualityStep;
  private int maxSize;


  public JpegParameters() {
    maxQuality = 1f;
    minQuality = 0.5f;
  }


  public float getQualityStep() {
    float f = qualityStep;
    if (f < 0f) f = 0f;
    if (f > 1f) f = 1f;
    return f;
  }


  public void setQualityStep(float qualityStep) {
    this.qualityStep = qualityStep;
  }


  public void setQuolity(float min, float max) {
    minQuality = min;
    maxQuality = max;
  }


  public Point2D.Float getQuality() {
    float x = minQuality;
    float y = maxQuality;
    if (x < 0f) x = 0f;
    if (y < 0f) y = 0f;
    if (x > 1f) x = 1f;
    if (y > 1f) y = 1f;
    if (x > y) x = y;
    if (y < x) y = x;
    return new Point2D.Float(x, y);
  }


  public int getMaxSize() {
    int max = maxSize;
    if (max < 0) max = 0;
    return max;
  }


  public void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }
}
