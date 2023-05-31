package com.cii3m.cm.service;

import com.cii3m.cm.handler.converter.ShapeFromShading;
import com.cii3m.cm.util.TypeConversion;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class ModelConverter {

  private final ShapeFromShading shading;

  public ModelConverter(ShapeFromShading shading) {
    this.shading = shading;
  }

  public byte[] processModel(MultipartFile img) throws IOException {
    BufferedImage image = TypeConversion.fileToImage(img);
    double[][] result = shading.process(image);
    return TypeConversion.matrixToObj(result);
  }
}
