package com.huylam.realestateserver.service;

import com.amazonaws.services.account.model.ResourceNotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.huylam.realestateserver.entity.Property;
import com.huylam.realestateserver.entity.user.User;
import com.huylam.realestateserver.repository.PropertyRepository;
import com.huylam.realestateserver.repository.auth.UserRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class PhotoUploadService {

  @Value("${application.bucket.name}")
  private String bucketName;

  @Autowired
  PropertyRepository propertyRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  private AmazonS3 s3Client;

  public String uploadFile(MultipartFile file, Long propertyId) {
    Property property = propertyRepository
      .findById(propertyId)
      .orElseThrow(() ->
        new ResourceNotFoundException(
          "Property not found with id " + propertyId
        )
      );

    File fileObj = convertMultiPartFileToFile(file);
    String fileName =
      UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
    fileObj.delete();

    List<String> coverPaths = property.getPropertyCoverPaths();
    coverPaths.add(fileName); // Add the generated filename to the list of cover paths
    property.setPropertyCoverPaths(coverPaths); // Set the updated list to the property
    propertyRepository.save(property); // Save the updated property entity to the database
    return "File uploaded : " + fileName;
  }

  public String uploadAvatar(MultipartFile file, String userEmail) {
    User user = userRepository
      .findByEmail(userEmail)
      .orElseThrow(() ->
        new ResourceNotFoundException("User not found: " + userEmail)
      );

    File fileObj = convertMultiPartFileToFile(file);
    String fileName =
      UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
    fileObj.delete();

    user.setAvatar_url(fileName); // Set the updated avatar URL to the new file name
    userRepository.save(user); // Save the updated user entity to the database
    return "File uploaded: " + fileName;
  }

  public byte[] downloadFile(String fileName) {
    S3Object s3Object = s3Client.getObject(bucketName, fileName);
    S3ObjectInputStream inputStream = s3Object.getObjectContent();
    try {
      byte[] content = IOUtils.toByteArray(inputStream);
      return content;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String deleteFile(String fileName) {
    s3Client.deleteObject(bucketName, fileName);
    return fileName + " removed ...";
  }

  private File convertMultiPartFileToFile(MultipartFile file) {
    File convertedFile = new File(file.getOriginalFilename());
    try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
      fos.write(file.getBytes());
    } catch (IOException e) {
      log.error("Error converting multipartFile to file", e);
    }
    return convertedFile;
  }
}
