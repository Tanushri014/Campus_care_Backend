package campus_care.service.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import campus_care.enums.FileType;
import campus_care.exception.StorageException;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.*;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        name = "storage.provider",
        havingValue = "local",
        matchIfMissing = true
)
public class LocalFileStorageService implements FileStorageService {

    private final StorageProperties storageProperties;
    private Path getUploadDirectory(FileType fileType) {

        Path uploadPath = Paths.get(
                storageProperties.getUploadDir(),
                fileType.getFolderName()
        );

        try {

            Files.createDirectories(uploadPath);

        } catch (IOException e) {

            throw new StorageException(
                    "Could not create upload directory",
                    e
            );
        }

        return uploadPath;
    }


    @Override
    public String store(
            MultipartFile file,
            FileType fileType
    ) {

        try {

            Path uploadDirectory = getUploadDirectory(fileType);

            String originalFilename =
                    StringUtils.cleanPath(file.getOriginalFilename());
            if (originalFilename.isBlank()) {
                throw new StorageException("Invalid file name");
            }
            String filename =
                    UUID.randomUUID()
                            + "_"
                            + originalFilename;

            Path destination =
                    uploadDirectory.resolve(filename);

            Files.copy(
                    file.getInputStream(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            log.info(
                    "Stored {} file '{}' successfully.",
                    fileType,
                    filename
            );

            return filename;

        } catch (IOException e) {

            throw new StorageException(
                    "Failed to store file",
                    e
            );
        }

    }
    @Override
    public String getFileUrl(
            String fileName,
            FileType type
    ) {

        return "http://localhost:8080/files/"
                + type.getFolderName()
                + "/"
                + fileName;

    }
    @Override
    public void delete(
            String fileName,
            FileType fileType
    ) {

        try {

            Path file =
                    getUploadDirectory(fileType)
                            .resolve(fileName);

            Files.deleteIfExists(file);

            log.info(
                    "Deleted file {}",
                    fileName
            );

        } catch (IOException e) {

            throw new StorageException(
                    "Unable to delete file",
                    e
            );
        }

    }
}