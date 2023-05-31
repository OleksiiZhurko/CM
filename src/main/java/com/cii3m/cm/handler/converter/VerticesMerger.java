package com.cii3m.cm.handler.converter;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class VerticesMerger extends ImageProcessing {
  private static final int RED = Color.red.getRGB();
  private static final int WHITE = Color.white.getRGB();
  private static final int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
  private static final int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

  @Override
  public int[][] process(int[][] img) {
    int h = img.length;
    int w = img[0].length;
    boolean[][] visited = new boolean[img.length][img[0].length];

    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        if (img[i][j] == RED && !visited[i][j]) {
          List<Point> redPixels = new ArrayList<>();
          dfs(img, visited, i, j, redPixels);

          Point centroid = getCentroid(redPixels);
          for (Point p : redPixels) {
            if (!isCentroidOrHasNonAdjacentRedPixels(img, p.x, p.y, centroid)) {
              img[p.x][p.y] = WHITE;
            }
          }
        }
      }
    }
    return nextProcessing(img);
  }

  private void dfs(int[][] img, boolean[][] visited, int i, int j, List<Point> redPixels) {
    visited[i][j] = true;
    redPixels.add(new Point(i, j));
    int x, y;
    for (int k = 0; k < 8; k++) {
      x = i + dx[k];
      y = j + dy[k];
      if (isValid(img, visited, x, y)) dfs(img, visited, x, y, redPixels);
    }
  }

  private boolean isValid(int[][] img, boolean[][] visited, int i, int j) {
    return i >= 0 && i < img.length && j >= 0 && j < img[i].length && !visited[i][j] && img[i][j] == RED;
  }

  private Point getCentroid(List<Point> redPixels) {
    int xSum = 0;
    int ySum = 0;
    for (Point p : redPixels) {
      xSum += p.x;
      ySum += p.y;
    }
    return new Point(xSum / redPixels.size(), ySum / redPixels.size());
  }

  private boolean isCentroidOrHasNonAdjacentRedPixels(int[][] img, int i, int j, Point centroid) {
    return (i == centroid.x && j == centroid.y) || hasThreeNonAdjacentRedPixels(img, i, j);
  }

  private boolean hasThreeNonAdjacentRedPixels(int[][] img, int i, int j) {
    int h = img.length;
    int w = img[0].length;
    int count = 0;
    boolean[] isReds = new boolean[4];
    for (int bi = 0, k = 0; k < 8; bi++, k += 2) {
      int x = i + dx[k];
      int y = j + dy[k];
      if (x >= 0 && x < h && y >= 0 && y < w && img[x][y] == RED) {
        count++;
        isReds[bi] = true;
      }
    }
    if (count > 2) return true;
    return isReds[0] && isReds[1] || isReds[1] && isReds[2]
        || isReds[2] && isReds[3] || isReds[3] && isReds[0];
  }
}

