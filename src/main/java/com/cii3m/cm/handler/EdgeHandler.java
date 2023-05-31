package com.cii3m.cm.handler;

import com.cii3m.cm.handler.converter.Denoiser;
import com.cii3m.cm.handler.converter.EdgeDetector;
import com.cii3m.cm.handler.converter.Erosion;
import com.cii3m.cm.handler.converter.GrayscaleConverter;
import com.cii3m.cm.handler.converter.HardErosion;
import com.cii3m.cm.handler.converter.ImageProcessing;
import com.cii3m.cm.handler.converter.Threshold;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class EdgeHandler implements HandlerConverter {

  private final ImageProcessing imageConverter;

  public EdgeHandler(
      Denoiser denoiser,
      GrayscaleConverter grayscaleConverter,
      EdgeDetector edgeDetector,
      Threshold threshold,
      Erosion erosion,
      HardErosion commonErosion
  ) {
    denoiser.setNextImageProcessing(grayscaleConverter)
        .setNextImageProcessing(edgeDetector)
        .setNextImageProcessing(threshold)
        .setNextImageProcessing(erosion)
        .setNextImageProcessing(commonErosion);
    this.imageConverter = denoiser;
  }

  public int[][] process(BufferedImage img) {
    return imageConverter.process(img);
  }
}
