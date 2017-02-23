import FileStorage.S3Storage;

import java.io.File;

/**
 * Created by Orozco on 2/23/17.
 */
    public class main {
    public static void main(String[] args) {
        S3Storage s3Storage = new S3Storage("AKIAIFS5OMWOJVBN3NUQ","WdWCWkdsNkxXz8MTPcYHj+s2PIlINVbzJPP20LcC","taylor-freezer-storage");
        File file = new File("/Users/Orozco/images/images_2.jpg");
        s3Storage.save(file, "fourth.jpg");
        String fileURL = s3Storage.getLocationString("s3-us-west-2", "fourth.jpg");
        System.out.println(fileURL);
    }
}
