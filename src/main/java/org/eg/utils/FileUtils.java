package org.eg.utils;

import org.eg.enums.FileTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;


public class FileUtils {
    @Autowired
    private static Environment env;
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    public static boolean uploadFile(MultipartFile file, String file_name, FileTypeEnum file_type_enum) {
        String root_path = env.getProperty("utils.upload-file-path") + '/';
        String file_type = file_type_enum.getType() + '/';
        String file_path = root_path + file_type + file_name;
        return saveFile(file, file_path);
    }

    public static boolean saveFile(MultipartFile file, String file_path) {
        File dest = new File(file_path);
        if (!dest.getParentFile().exists()) {
            if (!dest.getParentFile().mkdirs()) {
                LOG.info("failed to make parent directory when saving file");
                return false;
            }
        }

        try {
            file.transferTo(dest);
        } catch (Exception e) {
            LOG.info("thrown an exception when transferring file" + Arrays.toString(e.getStackTrace()));
            return false;
        }

        return true;
    }
}
