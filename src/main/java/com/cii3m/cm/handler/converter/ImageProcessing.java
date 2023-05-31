package com.cii3m.cm.handler.converter;

import java.awt.image.BufferedImage;

public abstract class ImageProcessing {

  private ImageProcessing nextImageProcessing;

  public int[][] process(BufferedImage img) {
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

  abstract int[][] process(int[][] img);

  protected int[][] nextProcessing(int[][] img) {
    if (nextImageProcessing == null) return img;
    return nextImageProcessing.process(img);
  }

  public ImageProcessing getNextImageProcessing() {
    return nextImageProcessing;
  }

  public ImageProcessing setNextImageProcessing(ImageProcessing nextImageProcessing) {
    this.nextImageProcessing = nextImageProcessing;
    return this.nextImageProcessing;
  }
}
