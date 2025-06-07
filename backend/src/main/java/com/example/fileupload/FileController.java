package com.example.fileupload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:4200")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !isSupportedContentType(contentType)) {
            String message = "Invalid file type. Only TXT, JPG, PNG, and JSON files are allowed.";
            logger.error(message);
            return ResponseEntity.badRequest().body(new ResponseMessage(message));
        }
        try {
            fileStorageService.store(file);
            String message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.ok().body(new ResponseMessage(message));
        } catch (Exception e) {
            String message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            logger.error(message, e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("text/plain")
                || contentType.equals("image/jpeg")
                || contentType.equals("image/png")
                || contentType.equals("application/json");
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileResponse>> getListFiles() {
        try {
            List<FileResponse> files = fileStorageService.getAllFiles().map(dbFile -> {
                long size = 0;
                try {
                    size = Files.size(Paths.get(dbFile.getFilePath()));
                } catch (IOException e) {
                    logger.error("Could not get file size for " + dbFile.getName(), e);
                }
                return new FileResponse(
                        dbFile.getId(),
                        dbFile.getName(),
                        dbFile.getType(),
                        size);
            }).collect(Collectors.toList());

            return ResponseEntity.ok().body(files);
        } catch (Exception e) {
            logger.error("Could not get list of files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id,
                                            @RequestParam(required = false) String disposition,
                                            HttpServletRequest request) {
        try {
            File file = fileStorageService.getFile(id);
            Resource resource = fileStorageService.loadFileAsResource(file.getName());
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                logger.info("Could not determine file type.");
            }
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            String dispositionType = "attachment".equals(disposition) ? "attachment" : "inline";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, dispositionType + "; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Could not get file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}