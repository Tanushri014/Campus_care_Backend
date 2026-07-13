package campus_care.service.storage;

import campus_care.enums.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String store(
            MultipartFile file,
            FileType fileType
    );

    String getFileUrl(
            String fileName,
            FileType fileType
    );

    void delete(
            String fileName,
            FileType fileType
    );
}