package campus_care.service.storage;

import campus_care.enums.FileType;
import campus_care.exception.StorageException;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.Map;

@Service
@Primary
@RequiredArgsConstructor
public class CloudinaryStorageService implements FileStorageService {

    private final Cloudinary cloudinary;

    @Override
    public String store(
            MultipartFile file,
            FileType fileType
    ) {

        try {

            Map<?, ?> result = cloudinary.uploader().upload(

                    file.getBytes(),

                    ObjectUtils.asMap(

                            "folder", fileType.getFolderName(),

                            "resource_type", "auto",

                            "use_filename", false,

                            "unique_filename", true

                    )

            );

            return result.get("public_id").toString();

        }

        catch (IOException exception) {

            throw new StorageException(
                    "Failed to upload file to Cloudinary.",
                    exception
            );

        }

    }

    @Override
    public String getFileUrl(
            String fileName,
            FileType fileType
    ) {

        String resourceType =
                fileType == FileType.NOTICE
                        ? "raw"
                        : "image";

        return cloudinary.url()
                .secure(true)
                .resourceType(resourceType)
                .generate(fileName);
    }
    @Override
    public void delete(
            String fileName,
            FileType fileType
    ) {

        try {

            String resourceType =
                    fileType == FileType.NOTICE
                            ? "raw"
                            : "image";

            cloudinary.uploader().destroy(

                    fileName,

                    ObjectUtils.asMap(
                            "resource_type",
                            resourceType
                    )

            );

        }

        catch (Exception exception) {

            throw new StorageException(
                    "Failed to delete file from Cloudinary.",
                    exception
            );

        }

    }
}