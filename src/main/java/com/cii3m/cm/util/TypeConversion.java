package com.cii3m.cm.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TypeConversion {

  public static byte[] matrixToImage(int[][] img) throws IOException {
    int h = img.length;
    int w = img[0].length;
    BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    int color;
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        color = img[y][x];
        if (color == 0) continue;
        image.setRGB(x, y, color);
      }
    }
    var out = new ByteArrayOutputStream();
    ImageIO.write(image, "png", out);
    return out.toByteArray();
  }

  public static byte[] matrixToObj(double[][] depthMap) {
    int h = depthMap.length;
    int w = depthMap[0].length;

    float xScale = 1f / w;
    float yScale = 1f / h;
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        sb.append(String.format("v %.6f %.6f %.6f\n", x * xScale, depthMap[y][x], y * yScale));
      }
    }

    int v1, v2, v3, v4;
    for (int y = 0; y < h - 1; y++) {
      for (int x = 0; x < w - 1; x++) {
        v1 = y * w + x + 1;
        v2 = y * w + x + 2;
        v3 = (y + 1) * w + x + 1;
        v4 = (y + 1) * w + x + 2;
        sb.append(String.format("f %d %d %d\n", v1, v2, v3));
        sb.append(String.format("f %d %d %d\n", v2, v4, v3));
      }
    }
    return sb.toString().getBytes();
  }

  public static BufferedImage fileToImage(MultipartFile img) throws IOException {
    BufferedImage image;
    try (var in = img.getInputStream()) {
      image = ImageIO.read(in);
    }
    return image;
  }
}
