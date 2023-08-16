package com.huylam.realestateserver.controller;

import com.huylam.realestateserver.entity.user.User;
import com.huylam.realestateserver.repository.auth.UserRepository;
import com.huylam.realestateserver.service.PhotoUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/photos")
public class PhotoUploadController {

  @Autowired
  private PhotoUploadService photoUploadService;

  @PostMapping("/upload/{propertyId}")
  public ResponseEntity<String> uploadFiles(
    @RequestParam("file") MultipartFile[] files,
    @PathVariable Long propertyId
  ) {
    String message = "";
    for (MultipartFile file : files) {
      message += photoUploadService.uploadFile(file, propertyId) + " ";
    }
    return ResponseEntity.status(HttpStatus.OK).body(message.trim());
  }

  @PostMapping("/upload-avatar")
  public String uploadAvatar(
    @RequestParam("file") MultipartFile file,
    @RequestParam("email") String email
  ) {
    return photoUploadService.uploadAvatar(file, email);
  }

  @GetMapping("/download/{fileName}")
  public ResponseEntity<ByteArrayResource> downloadFile(
    @PathVariable String fileName
  ) {
    byte[] data = photoUploadService.downloadFile(fileName);
    ByteArrayResource resource = new ByteArrayResource(data);
    return ResponseEntity
      .ok()
      .contentLength(data.length)
      .header("Content-type", "application/octet-stream")
      .header(
        "Content-disposition",
        "attachment; filename=\"" + fileName + "\""
      )
      .body(resource);
  }

  @DeleteMapping("/delete/{fileName}")
  public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
    return new ResponseEntity<>(
      photoUploadService.deleteFile(fileName),
      HttpStatus.OK
    );
  }
}
