package com.bawnorton.wildallays.util.file;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtil {
    public static Path createDirectory(Path path) {
        if(!Files.isDirectory(path)){
            try {
                Files.createDirectory(path);
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        return path;
    }

    public static Path getFile(String path) {
        return getFile(Path.of(path));
    }

    public static Path getFile(Path path) {
        if(Files.exists(path)) {
            return path;
        }
        return createFile(path);
    }

    public static Path createFile(Path path) {
        try {
            return Files.writeString(path, "", StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            try {
                return Files.createFile(path);
            } catch(IOException ignored){}
        }
        return null;
    }

    public static Path createFile(String path) {
        return createFile(Path.of(path));
    }

    public static Path toPath(String path) {
        return Paths.get(FilenameUtils.separatorsToSystem(path));
    }
}
