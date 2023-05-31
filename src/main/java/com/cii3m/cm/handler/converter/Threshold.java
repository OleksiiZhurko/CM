package com.cii3m.cm.handler.converter;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.Color;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class Threshold extends ImageProcessing {

  private final int THRESHOLD = 10;

  @Override
  public int[][] process(int[][] img) {
    int h = img.length;
    int w = img[0].length;

    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        if (isColorBlack(img[y][x])) img[y][x] = Color.black.getRGB();
      }
    }

    return nextProcessing(img);
  }

  private boolean isColorBlack(int color) {
    return (color & 0xFF) < THRESHOLD;
  }
}
