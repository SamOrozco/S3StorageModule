package FileStorage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.directory.model.UnsupportedOperationException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.StringUtils;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Orozco on 2/22/17.
 */
public class S3Storage implements FileStorage {
    private String bucketName;
    AWSCredentials credentials;

    private static final String buckNameAndKeyAreRequired = "Both bucket name and key are required";
    private static final String problemUploadingFileToS3 = "There was a problem uploading file to s3";
    private static final String unableToUploadFileToS3WithInputStream = "Unable to upload file to s3 using an input stream, try java.io.File";
    private static final String problemDownloadingFileFromS3 = "There was a problem downloading the specified file from s3";
    private static final String problemDeletingFileFromS3 = "There was a problem deleting the specified file from s3";
    private static final String bothKeyAndRegionAreRequired = "Both key and region are required";
    private static final String amazonBaseURL = ".amazonaws.com";


    public S3Storage(String accessKeyID, String secretAccessKey, String bucketName) {
        if (StringUtils.isNullOrEmpty(accessKeyID) || StringUtils.isNullOrEmpty(secretAccessKey)) {
            throw new RuntimeException("Access Key and Secret key are required");
        }
        if (StringUtils.isNullOrEmpty(bucketName)) {
            throw new RuntimeException("Bucket name is required");
        }
        credentials = new BasicAWSCredentials(accessKeyID, secretAccessKey);
        this.bucketName = bucketName;
    }

    public void save(InputStream inputStream, String key) {
        throw new UnsupportedOperationException(unableToUploadFileToS3WithInputStream);
    }

    public void save(File file, String key) {
        AmazonS3 amazonS3 = new AmazonS3Client(credentials);
        try {
            amazonS3.putObject(bucketName, key, file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(problemUploadingFileToS3);
        }
    }

    public InputStream get(String key) {
        AmazonS3 amazonS3 = new AmazonS3Client(credentials);
        try {
            S3Object fileObject = amazonS3.getObject(new GetObjectRequest(bucketName, key));
            return fileObject.getObjectContent();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(problemDownloadingFileFromS3);
        }
    }

    public void delete(String key) {
        AmazonS3 amazonS3 = new AmazonS3Client(credentials);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(problemDeletingFileFromS3);
        }
    }


    public String getLocationString(String region, String key) {
        if (StringUtils.isNullOrEmpty(key) || StringUtils.isNullOrEmpty(region)) {
            throw new RuntimeException(bothKeyAndRegionAreRequired);
        }
        StringBuilder fileLocation = new StringBuilder();
        fileLocation.append("https://")
                .append(region)
                .append(amazonBaseURL)
                .append("/")
                .append(bucketName)
                .append("/")
                .append(key);
        return fileLocation.toString();
    }


}
