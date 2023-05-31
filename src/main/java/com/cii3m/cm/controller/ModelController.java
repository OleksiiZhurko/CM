package com.cii3m.cm.controller;

import com.cii3m.cm.service.ModelConverter;
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
@RequestMapping("/object")
public class ModelController extends ObjectController {

  private final Logger logger = LoggerFactory.getLogger(ImageController.class);
  private final ModelConverter modelConverter;

  public ModelController(ModelConverter modelConverter) {
    this.modelConverter = modelConverter;
  }

  @PostMapping("/convert")
  public ResponseEntity<Resource> upload(@RequestPart("img") MultipartFile img) throws IOException {
    logger.info(String.format("Request for 'object/convert/' with '%s' type and '%s' size", img.getContentType(), img.getSize()));
    checkFileFormat(img);
    byte[] bytes = modelConverter.processModel(img);
    logger.info("Processed request for 'object/convert/'");
    return prepareResponse(bytes, MediaType.APPLICATION_OCTET_STREAM, "model");
  }
}
