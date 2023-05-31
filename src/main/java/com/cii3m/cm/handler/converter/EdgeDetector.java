package com.cii3m.cm.handler.converter;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class EdgeDetector extends ImageProcessing {

  @Override
  int[][] process(int[][] img) {
    int h = img.length;
    int w = img[0].length;
    int[][] resImg = new int[h][w];

    int p1, p2, p3, p4;
    for (int y = 1; y < h - 1; y++) {
      for (int x = 1; x < w - 1; x++) {
        p1 = img[y - 1][x - 1];
        p2 = img[y - 1][x + 1];
        p3 = img[y + 1][x - 1];
        p4 = img[y + 1][x + 1];
        resImg[y][x] = Math.abs(p1 - p4) + Math.abs(p2 - p3);
      }
    }

    return nextProcessing(resImg);
  }
}
