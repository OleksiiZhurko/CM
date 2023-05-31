package com.cii3m.cm.handler.converter;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ShapeFromShading {
  private static final double INITIAL_ESTIMATE = 0.0;
  private static final double INITIAL_CONFIDENCE = 1.0;
  private static final double WN = 0.0001 * 0.0001;

  public double[][] process(BufferedImage img) {
    int h = img.getHeight();
    int w = img.getWidth();
    int[][] intImg = new int[h][w];

    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        intImg[y][x] = img.getRGB(x, y);
      }
    }

    return process(intImg);
  }

  public double[][] process(int[][] img) {
    return computeDepthMap(img, 3, 1, 1, 1);
  }

  public double[][] computeDepthMap(int[][] img, int iterations, double lightSourceX, double lightSourceY, double lightSourceZ) {
    int h = img.length;
    int w = img[0].length;
    double[][] depth = new double[h][w];
    double[][] prevDepth = initializeArray(h, w, INITIAL_ESTIMATE);
    double[][] confidence = new double[h][w];
    double[][] prevConfidence = initializeArray(h, w, INITIAL_CONFIDENCE);

    if (lightSourceX == 0.0 && lightSourceY == 0.0) lightSourceX = lightSourceY = 0.01;

    double lightDirectionX = lightSourceX / lightSourceZ;
    double lightDirectionY = lightSourceY / lightSourceZ;
    double[] gradients, fAndDf;
    double pixelIntensity, f, df, y, k;
    for (int iteration = 1; iteration <= iterations; iteration++) {
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          gradients = calculateGradients(prevDepth, i, j);
          pixelIntensity = getPixelIntensity(img[i][j]);
          fAndDf = calculateFAndDf(pixelIntensity, gradients[0], gradients[1], lightDirectionX, lightDirectionY);
          f = fAndDf[0];
          df = fAndDf[1];
          y = f + df * prevDepth[i][j];
          k = calculateK(prevConfidence[i][j], df);
          confidence[i][j] = (1.0 - k * df) * prevConfidence[i][j];
          depth[i][j] = prevDepth[i][j] + k * (y - df * prevDepth[i][j]);
        }
      }
      prevDepth = depth.clone();
      prevConfidence = confidence.clone();
    }
    return depth;
  }

  private double[][] initializeArray(int height, int width, double value) {
    double[][] array = new double[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) array[i][j] = value;
    }
    return array;
  }

  private double[] calculateGradients(double[][] prevDepth, int i, int j) {
    double gradientX = 0.0, gradientY = 0.0;
    if (j - 1 >= 0) gradientX = prevDepth[i][j] - prevDepth[i][j - 1];
    if (i - 1 >= 0) gradientY = prevDepth[i][j] - prevDepth[i - 1][j];
    return new double[]{gradientX, gradientY};
  }

  private double getPixelIntensity(int rgb) {
    int red = (rgb >> 16) & 0xFF;
    int green = (rgb >> 8) & 0xFF;
    int blue = rgb & 0xFF;
    return (red + green + blue) / (3.0 * 255.0);
  }

  private double[] calculateFAndDf(double pixelIntensity, double gradientX, double gradientY, double lightDirectionX, double lightDirectionY) {
    double pq = 1.0 + gradientX * gradientX + gradientY * gradientY;
    double pqs = 1.0 + lightDirectionX * lightDirectionX + lightDirectionY * lightDirectionY;
    double v = Math.sqrt(pq) * Math.sqrt(pqs);
    double f = -1.0 * (pixelIntensity - Math.max(0.0, (1 + gradientX * lightDirectionX + gradientY * lightDirectionY) / v));
    double df = -1.0 * ((lightDirectionX + lightDirectionY) / v - (gradientX + gradientY) * (1.0 + gradientX * lightDirectionX + gradientY * lightDirectionY) / (Math.sqrt(pq * pq * pq) * Math.sqrt(pqs)));
    return new double[]{f, df};
  }

  private double calculateK(double prevConfidence, double df) {
    return prevConfidence * df / (WN + df * prevConfidence * df);
  }
}
