package com.cii3m.cm.controller;

import com.cii3m.cm.service.ImageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController extends ObjectController {

  private final Logger logger = LoggerFactory.getLogger(ImageController.class);
  private final ImageConverter imageConverter;

  public ImageController(ImageConverter imageConverter) {
    this.imageConverter = imageConverter;
  }

  @PostMapping("/edges")
  public ResponseEntity<Resource> processEdges(@RequestPart("img") MultipartFile img) throws IOException {
    logger.info(String.format("Request for 'image/edges/' with '%s' type and '%s' size", img.getContentType(), img.getSize()));
    checkFileFormat(img);
    byte[] bytes = imageConverter.processEdges(img);
    logger.info("Processed request for 'image/edges/'");
    return prepareResponse(bytes, MediaType.IMAGE_PNG, "edges");
  }

  @PostMapping("/vertices")
  public ResponseEntity<Resource> processVertices(@RequestPart("img") MultipartFile img) throws IOException {
    logger.info(String.format("Request for 'image/vertices/' with '%s' type and '%s' size", img.getContentType(), img.getSize()));
    checkFileFormat(img);
    byte[] bytes = imageConverter.processPoints(img);
    logger.info("Processed request for 'image/vertices/'");
    return prepareResponse(bytes, MediaType.IMAGE_PNG, "vertices");
  }
}
