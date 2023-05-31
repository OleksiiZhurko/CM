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
public class VerticesDetector extends ImageProcessing {

  private final int[][] DIRECTIONS = {
      {-1, 0}, {1, 0}, {0, -1}, {0, 1}
  };
  private final int SIZE = 7;
  private final int HALF_SIZE = 3;

  @Override
  public int[][] process(int[][] img) {
    int h = img.length;
    int w = img[0].length;
    List<Point> points;
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        if (img[y][x] != Color.black.getRGB()) {
          points = findConnected(img, y, x);
          if (points.size() > 8) {
            if (Math.abs(findCorrelation(points)) < 0.8) img[y][x] = Color.red.getRGB();
          }
        }
      }
    }
    return nextProcessing(img);
  }

  private List<Point> findConnected(int[][] img, int y, int x) {
    List<Point> connectedPixels = new ArrayList<>();
    int[][] result = new int[SIZE][SIZE];
    int h = img.length;
    int w = img[0].length;
    int ny, nx;
    for (int yi = -HALF_SIZE; yi <= HALF_SIZE; yi++) {
      for (int xi = -HALF_SIZE; xi <= HALF_SIZE; xi++) {
        ny = y + yi;
        nx = x + xi;
        if (ny >= 0 && ny < h && nx >= 0 && nx < w && img[ny][nx] != Color.black.getRGB()) {
          result[yi + HALF_SIZE][xi + HALF_SIZE] = 1;
        }
      }
    }

    boolean[][] visited = new boolean[SIZE][SIZE];
    dfs(result, visited, SIZE / 2, SIZE / 2);

    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        if (result[i][j] == 1 && !visited[i][j]) result[i][j] = 0;
      }
    }
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        if (result[i][j] == 1) {
          connectedPixels.add(new Point(y + i - HALF_SIZE, x + j - HALF_SIZE));
        }
      }
    }

    return connectedPixels;
  }

  private void dfs(int[][] matrix, boolean[][] visited, int x, int y) {
    visited[x][y] = true;
    int newX, newY;
    for (int[] dir : DIRECTIONS) {
      newX = x + dir[0];
      newY = y + dir[1];
      if (newX >= 0 && newX < SIZE && newY >= 0 && newY < SIZE && matrix[newX][newY] == 1
          && !visited[newX][newY]) {
        dfs(matrix, visited, newX, newY);
      }
    }
  }

  private double findCorrelation(List<Point> points) {
    double sumX = 0, sumY = 0, sumXY = 0;
    double squareSumX = 0, squareSumY = 0;

    for (Point point : shiftData(points)) {
      sumX = sumX + point.x;
      sumY = sumY + point.y;
      sumXY = sumXY + point.x * point.y;
      squareSumX = squareSumX + point.x * point.x;
      squareSumY = squareSumY + point.y * point.y;
    }

    int n = points.size();
    return (n * sumXY - sumX * sumY)
        / Math.sqrt((n * squareSumX - sumX * sumX)
        * (n * squareSumY - sumY * sumY));
  }

  private List<Point> shiftData(List<Point> points) {
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;

    for (Point point : points) {
      if (point.x < minX) minX = point.x;
      if (point.y < minY) minY = point.y;
    }
    for (Point point : points) {
      point.x = point.x - minX;
      point.y = point.y - minY;
    }

    return points;
  }
}
