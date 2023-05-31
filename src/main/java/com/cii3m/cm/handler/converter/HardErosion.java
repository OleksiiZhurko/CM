package com.cii3m.cm.handler.converter;

import com.cii3m.cm.dto.CommonErosionPoint;
import com.cii3m.cm.util.ErosionCommon;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.Color;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class HardErosion extends ImageProcessing {

  private final int[][] DIRECTIONS = {
      {-1, -1}, {0, -1}, {1, -1}, {1, 0},
      {1, 1}, {0, 1}, {-1, 1}, {-1, 0},
  };

  @Override
  public int[][] process(int[][] img) {
    int h = img.length;
    int w = img[0].length;

    boolean[] erodes;
    int nonEmptyPixels = ErosionCommon.countNotEmpty(img);
    int currentNonEmptyPixels = nonEmptyPixels - 1;
    while (nonEmptyPixels > currentNonEmptyPixels) {
      nonEmptyPixels = currentNonEmptyPixels;
      for (int y = 0; y < h; y++) {
        for (int x = 0; x < w; x++) {
          if (Color.black.getRGB() == img[y][x]) continue;
          erodes = new boolean[DIRECTIONS.length];
          for (int i = 0; i < DIRECTIONS.length; i++) {
            int ni = y + DIRECTIONS[i][0];
            int nii = x + DIRECTIONS[i][1];

            erodes[i] = ni >= 0 && ni < h && nii >= 0 && nii < w
                && Color.black.getRGB() == img[ni][nii];
          }

          var res = countTrueSequences(erodes);
          img[y][x] = res.isB() && res.getN() > 2 ? img[y][x] : Color.black.getRGB();
        }
      }
      currentNonEmptyPixels = ErosionCommon.countNotEmpty(img);
    }

    return nextProcessing(img);
  }

  private CommonErosionPoint countTrueSequences(boolean[] booleans) {
    int count = 0;
    boolean inSequence = false;
    boolean wrapAround = booleans[0] && booleans[booleans.length - 1];

    for (boolean b : booleans) {
      if (b) {
        if (!inSequence) {
          count++;
          inSequence = true;
        }
      } else {
        inSequence = false;
      }
    }

    if (wrapAround && count > 1) count--;
    if (count == 1) return new CommonErosionPoint(false, 0);

    count = 0;
    for (boolean b : booleans) if (b) count++;

    return new CommonErosionPoint(true, count);
  }
}
