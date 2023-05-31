package com.cii3m.cm.handler.converter;

import com.cii3m.cm.dto.ErosionPoint;
import com.cii3m.cm.util.ErosionCommon;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.Color;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class Erosion extends ImageProcessing {

  private final int[][] DIRECTIONS = {
      {-1, -1}, {0, -1}, {1, -1}, {1, 0},
      {1, 1}, {0, 1}, {-1, 1}, {-1, 0},
  };

  @Override
  public int[][] process(int[][] img) {
    int h = img.length;
    int w = img[0].length;

    int[] erodes;
    int nonEmptyPixels = ErosionCommon.countNotEmpty(img);
    int currentNonEmptyPixels = nonEmptyPixels - 1;
    while (nonEmptyPixels > currentNonEmptyPixels) {
      nonEmptyPixels = currentNonEmptyPixels;
      for (int y = 0; y < h; y++) {
        for (int x = 0; x < w; x++) {
          if (Color.black.getRGB() == img[y][x]) continue;
          erodes = new int[DIRECTIONS.length];
          for (int i = 0; i < DIRECTIONS.length; i++) {
            int ni = y + DIRECTIONS[i][0];
            int nii = x + DIRECTIONS[i][1];

            erodes[i] = ni >= 0 && ni < h && nii >= 0
                && nii < w ? isColorStronger(img[y][x], img[ni][nii]) : -1;
          }

          var points = countSequences(erodes);
          if (isEmpty(erodes, points)) img[y][x] = Color.black.getRGB();
        }
      }
      currentNonEmptyPixels = ErosionCommon.countNotEmpty(img);
    }

    return nextProcessing(img);
  }

  private boolean isEmpty(int[] erodes, ErosionPoint erosionPoint) {
    int count = 0;
    for (int i = 1; i < erodes.length; i += 2) {
      if (erodes[i] == -1) count++;
    }
    if (count > 2) return true;
    if (erosionPoint.getMaxSequenceMinusOne() == 5) {
      for (int i = 0; i < erodes.length; i += 2) {
        if (erodes[i] == 0 || erodes[i] == 1) return true;
      }
      return false;
    }
    if (erosionPoint.getMaxSequenceMinusOne() > 5
        || erosionPoint.getMaxSequenceZero() > 6
        || erosionPoint.getMaxSequenceOne() > 6
        || ((erosionPoint.getMaxSequenceMinusOne() == 3 || erosionPoint.getMaxSequenceMinusOne() == 2) && erosionPoint.getCountSequenceMinusOne() == 1 && erosionPoint.getCountSequenceTwo() == 0)
    ) {
      return true;
    }
    int corner = -1;
    if (erosionPoint.getMaxSequenceMinusOne() == 4 || erosionPoint.getMaxSequenceMinusOne() == 3) {
      for (int i = 0; i < erodes.length; i += 2) {
        if (erodes[i] == -1) {
          if (i == 0) {
            if (erodes[i + 1] == -1 && erodes[erodes.length - 1] == -1) {
              corner = i;
              break;
            }
          } else {
            if (erodes[i + 1] == -1 && erodes[i - 1] == -1) {
              corner = i;
              break;
            }
          }
        }
      }
      if (corner != -1) {
        return erodes[(corner + 4) % 8] == 0 || erodes[(corner + 4) % 8] == 1;
      }
    }
    return false;
  }

  private ErosionPoint countSequences(int[] erodes) {
    ErosionPoint erosionPoint = new ErosionPoint();

    int maxSequenceMinusOne = 0;
    int maxSequenceZero = 0;
    int maxSequenceOne = 0;
    int maxSequenceTwo = 0;
    int current = -2;
    for (int erode : erodes) {
      if (erode == -1) {
        if (current != -1) {
          erosionPoint.setCountSequenceMinusOne(erosionPoint.getCountSequenceMinusOne() + 1);
        }
        if (maxSequenceZero > erosionPoint.getMaxSequenceZero()) {
          erosionPoint.setMaxSequenceZero(maxSequenceZero);
        } else if (maxSequenceOne > erosionPoint.getMaxSequenceOne()) {
          erosionPoint.setMaxSequenceOne(maxSequenceOne);
        } else if (maxSequenceTwo > erosionPoint.getMaxSequenceTwo()) {
          erosionPoint.setMaxSequenceTwo(maxSequenceTwo);
        }
        maxSequenceMinusOne++;
        maxSequenceZero = 0;
        maxSequenceOne = 0;
        maxSequenceTwo = 0;
      } else if (erode == 0) {
        if (current != 0) {
          erosionPoint.setCountSequenceZero(erosionPoint.getCountSequenceZero() + 1);
        }
        if (maxSequenceMinusOne > erosionPoint.getMaxSequenceMinusOne()) {
          erosionPoint.setMaxSequenceMinusOne(maxSequenceMinusOne);
        } else if (maxSequenceOne > erosionPoint.getMaxSequenceOne()) {
          erosionPoint.setMaxSequenceOne(maxSequenceOne);
        } else if (maxSequenceTwo > erosionPoint.getMaxSequenceTwo()) {
          erosionPoint.setMaxSequenceTwo(maxSequenceTwo);
        }
        maxSequenceZero++;
        maxSequenceMinusOne = 0;
        maxSequenceOne = 0;
        maxSequenceTwo = 0;
      } else if (erode == 1) {
        if (current != 1) {
          erosionPoint.setCountSequenceOne(erosionPoint.getCountSequenceOne() + 1);
        }
        if (maxSequenceMinusOne > erosionPoint.getMaxSequenceMinusOne()) {
          erosionPoint.setMaxSequenceMinusOne(maxSequenceMinusOne);
        } else if (maxSequenceZero > erosionPoint.getMaxSequenceZero()) {
          erosionPoint.setMaxSequenceZero(maxSequenceZero);
        } else if (maxSequenceTwo > erosionPoint.getMaxSequenceTwo()) {
          erosionPoint.setMaxSequenceTwo(maxSequenceTwo);
        }
        maxSequenceOne++;
        maxSequenceMinusOne = 0;
        maxSequenceZero = 0;
        maxSequenceTwo = 0;
      } else if (erode == 2) {
        if (current != 2) {
          erosionPoint.setCountSequenceTwo(erosionPoint.getCountSequenceTwo() + 1);
        }
        if (maxSequenceMinusOne > erosionPoint.getMaxSequenceMinusOne()) {
          erosionPoint.setMaxSequenceMinusOne(maxSequenceMinusOne);
        } else if (maxSequenceZero > erosionPoint.getMaxSequenceZero()) {
          erosionPoint.setMaxSequenceZero(maxSequenceZero);
        } else if (maxSequenceOne > erosionPoint.getMaxSequenceOne()) {
          erosionPoint.setMaxSequenceOne(maxSequenceOne);
        }
        maxSequenceTwo++;
        maxSequenceMinusOne = 0;
        maxSequenceZero = 0;
        maxSequenceOne = 0;
      }
      current = erode;
    }
    if (maxSequenceMinusOne > erosionPoint.getMaxSequenceMinusOne()) {
      erosionPoint.setMaxSequenceMinusOne(maxSequenceMinusOne);
    } else if (maxSequenceZero > erosionPoint.getMaxSequenceZero()) {
      erosionPoint.setMaxSequenceZero(maxSequenceZero);
    } else if (maxSequenceOne > erosionPoint.getMaxSequenceOne()) {
      erosionPoint.setMaxSequenceOne(maxSequenceOne);
    } else if (maxSequenceTwo > erosionPoint.getMaxSequenceTwo()) {
      erosionPoint.setMaxSequenceTwo(maxSequenceTwo);
    }

    int start = erodes[0];
    int end = erodes[erodes.length - 1];
    if (start == end) {
      if (start == -1) {
        erosionPoint.setCountSequenceMinusOne(erosionPoint.getCountSequenceMinusOne() - 1);
        int maxSequence = countSequence(erodes, -1);
        if (maxSequence > erosionPoint.getMaxSequenceMinusOne()) {
          erosionPoint.setMaxSequenceMinusOne(maxSequence);
        }
      } else if (start == 0) {
        erosionPoint.setCountSequenceZero(erosionPoint.getCountSequenceZero() - 1);
        int maxSequence = countSequence(erodes, 0);
        if (maxSequence > erosionPoint.getMaxSequenceZero()) {
          erosionPoint.setMaxSequenceZero(maxSequence);
        }
      } else if (start == 1) {
        erosionPoint.setCountSequenceOne(erosionPoint.getCountSequenceOne() - 1);
        int maxSequence = countSequence(erodes, 1);
        if (maxSequence > erosionPoint.getMaxSequenceOne()) {
          erosionPoint.setMaxSequenceOne(maxSequence);
        }
      } else if (start == 2) {
        erosionPoint.setCountSequenceTwo(erosionPoint.getCountSequenceTwo() - 1);
        int maxSequence = countSequence(erodes, 2);
        if (maxSequence > erosionPoint.getMaxSequenceTwo()) {
          erosionPoint.setMaxSequenceTwo(maxSequence);
        }
      }
    }

    return erosionPoint;
  }

  private int countSequence(int[] arr, int n) {
    int startCount = 1;
    int i = 1;
    while (i < arr.length - 1) {
      if (arr[i++] != n) break;
      startCount++;
    }
    if (i == arr.length - 1) return startCount;

    int endCount = 1;
    i = arr.length - 2;
    while (i > 0) {
      if (arr[i--] != n) break;
      endCount++;
    }
    return startCount + endCount;
  }

  private int isColorStronger(int color1, int color2) {
    if (Color.black.getRGB() == color2) return -1;
    if (color1 < color2) return 0;
    if (color1 == color2) return 1;
    return 2;
  }
}
