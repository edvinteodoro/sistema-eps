package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.service.StorageService;
import gt.edu.cunoc.sistemaeps.util.DateUtils;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import java.io.InputStream;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
@Service
public class StorageServiceImp implements StorageService {

    private final MinioClient minioClient;
    private final String bucketName;

    public StorageServiceImp(MinioClient minioClient, @Value("${minio.bucketName}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public String saveFile(MultipartFile file, String path) throws Exception {
        if (!file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream()) {
                String fileName = file.getOriginalFilename();
                String baseObjectName = FilenameUtils.getBaseName(fileName);
                String extension = FilenameUtils.getExtension(fileName);
                String objectName = path + "/" + baseObjectName + "_" + DateUtils.getCurrentLocalDateTimeFormated() + "." + extension;
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .build());
                return objectName;
            }
        } else {
            throw new Exception("Fallo al guardar el archivo vacio.");
        }
    }

    @Override
    public String getFile(String key) throws Exception {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(key)
                .expiry(60)
                .build());
    }

}
