package com.cii3m.cm.handler.converter;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.Color;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class GrayscaleConverter extends ImageProcessing {

  @Override
  public int[][] process(int[][] img) {
    int h = img.length;
    int w = img[0].length;
    var resImg = new int[h][w];

    Color c;
    int gray;
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        c = new Color(img[y][x], true);
        gray = (int) (0.2989 * c.getRed() + 0.5870 * c.getGreen() + 0.1140 * c.getBlue());
        resImg[y][x] = new Color(gray, gray, gray).getRGB();
      }
    }

    return nextProcessing(resImg);
  }
}
