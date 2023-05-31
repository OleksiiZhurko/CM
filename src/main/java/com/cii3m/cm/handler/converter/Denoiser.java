package com.cii3m.cm.handler.converter;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.Color;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class Denoiser extends ImageProcessing {

  private int radius;

  public Denoiser() {
    setRadius(3);
  }

  public Denoiser(int radius) {
    this.radius = radius;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  @Override
  public int[][] process(int[][] img) {
    float[] kernel = createGaussianKernel();
    int h = img.length;
    int w = img[0].length;
    var tmpImg = new int[h][w];
    var resImg = new int[h][w];

    // Horizontal pass
    float r, g, b, kernelValue;
    int pixelX;
    Color color;
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        r = g = b = 0;
        for (int kx = -radius; kx <= radius; kx++) {
          pixelX = Math.min(Math.max(x + kx, 0), w - 1);
          kernelValue = kernel[kx + radius];
          color = new Color(img[y][pixelX]);
          r += kernelValue * color.getRed();
          g += kernelValue * color.getGreen();
          b += kernelValue * color.getBlue();
        }
        tmpImg[y][x] = new Color(clamp(r), clamp(g), clamp(b)).getRGB();
      }
    }

    // Vertical pass
    int pixelY;
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        r = g = b = 0;
        for (int ky = -radius; ky <= radius; ky++) {
          pixelY = Math.min(Math.max(y + ky, 0), h - 1);
          kernelValue = kernel[ky + radius];
          color = new Color(tmpImg[pixelY][x]);
          r += kernelValue * color.getRed();
          g += kernelValue * color.getGreen();
          b += kernelValue * color.getBlue();
        }
        resImg[y][x] = new Color(clamp(r), clamp(g), clamp(b)).getRGB();
      }
    }

    return nextProcessing(resImg);
  }

  private float[] createGaussianKernel() {
    float[] kernel = new float[radius * 2 + 1];
    float sigma = radius / 3.0f;
    float twoSigmaSquare = 2.0f * sigma * sigma;
    float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
    float total = 0;

    for (int i = -radius; i <= radius; i++) {
      kernel[i + radius] = (float) Math.exp(-(i * i) / twoSigmaSquare) / sigmaRoot;
      total += kernel[i + radius];
    }

    for (int i = 0; i < kernel.length; i++) kernel[i] /= total;

    return kernel;
  }

  private int clamp(float value) {
    return Math.max(0, Math.min((int) value, 255));
  }
}
