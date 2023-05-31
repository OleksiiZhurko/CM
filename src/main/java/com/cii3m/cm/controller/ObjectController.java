package com.cii3m.cm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public abstract class ObjectController {

  private final Logger logger = LoggerFactory.getLogger(ImageController.class);
  private static final List<String> ACCEPTED_FILE_TYPES = List.of("image/jpeg", "image/png");

  protected void checkFileFormat(MultipartFile img) {
    if (!ACCEPTED_FILE_TYPES.contains(img.getContentType())) {
      logger.info(String.format("Unsupportable '%s' type", img.getContentType()));
      throw new ResponseStatusException(
          HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only PNG and JPEG files are supported"
      );
    }
  }

  protected ResponseEntity<Resource> prepareResponse(byte[] bytes, MediaType type, String filename) {
    var resource = new ByteArrayResource(bytes);
    String header = "attachment; filename=\"" + filename + "-" + System.currentTimeMillis() + "\"";
    return ResponseEntity.ok()
        .contentType(type)
        .header("Content-Disporsition", header)
        .body(resource);
  }
}
