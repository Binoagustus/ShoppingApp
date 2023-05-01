package com.intelizign.shoppingapp.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	private final Path fileStorageLocation = Paths.get("Uploads");

	@PostConstruct
	public void init() {
		try {
			Files.createDirectories(fileStorageLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	public void save(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), this.fileStorageLocation.resolve(file.getOriginalFilename()));
		} catch (Exception ex) {
			if (ex instanceof FileAlreadyExistsException) {
				throw new RuntimeException("A file of that name already exists.");
			}

			throw new RuntimeException(ex.getMessage());
		}
	}

	public Resource loadFileAsResource(String filename) {
		try {
			Path file = fileStorageLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	
}
