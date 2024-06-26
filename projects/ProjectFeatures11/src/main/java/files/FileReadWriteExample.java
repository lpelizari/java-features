package files;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReadWriteExample {

    public static void main(String[] args) throws Exception {

        var filePath = Paths.get("example.txt");
        var contentWrite = "Hello, World!";
        Files.writeString(filePath, contentWrite);

        var contentRead = Files.readString(filePath);
        System.out.println(contentRead);
    }
}
