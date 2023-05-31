package com.cii3m.cm.service;

import com.cii3m.cm.handler.EdgeHandler;
import com.cii3m.cm.handler.HandlerConverter;
import com.cii3m.cm.handler.PointHandler;
import com.cii3m.cm.util.TypeConversion;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class ImageConverter {

  private final HandlerConverter edgeHandler;
  private final HandlerConverter pointHandler;

  public ImageConverter(EdgeHandler edgeHandler, PointHandler pointHandler) {
    this.edgeHandler = edgeHandler;
    this.pointHandler = pointHandler;
  }

  public byte[] processEdges(MultipartFile img) throws IOException {
    BufferedImage image = TypeConversion.fileToImage(img);
    int[][] result = edgeHandler.process(image);
    return TypeConversion.matrixToImage(result);
  }

  public byte[] processPoints(MultipartFile img) throws IOException {
    BufferedImage image = TypeConversion.fileToImage(img);
    int[][] result = pointHandler.process(image);
    return TypeConversion.matrixToImage(result);
  }
}
