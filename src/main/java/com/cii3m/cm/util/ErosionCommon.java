package com.cii3m.cm.util;

import java.awt.Color;

public class ErosionCommon {

  public static int countNotEmpty(int[][] matrix) {
    int count = 0;
    for (var arr : matrix) {
      for (var in : arr) {
        if (in != Color.black.getRGB()) ++count;
      }
    }
    return count;
  }
}
