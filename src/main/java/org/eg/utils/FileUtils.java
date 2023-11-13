package org.eg.utils;

import org.eg.enums.FileTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

@Component
public class FileUtils {

    private static Environment env;
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    @Autowired
    private void setEnv(Environment env) {
        FileUtils.env = env;
    }

    public static String uploadFile(MultipartFile file, String file_name, FileTypeEnum file_type_enum) {
        String original_name = file.getOriginalFilename();
        assert original_name != null;
        String ext = original_name.substring(original_name.lastIndexOf("."));

        String root_path = env.getProperty("utils.upload-file-path") + '/';
        String file_type = file_type_enum.getType() + '/';
        String file_path = root_path + file_type + file_name + ext;

        if(saveFile(file, file_path)) {
            return file_path;
        }
        else {
            return "";
        }
    }

    public static boolean saveFile(MultipartFile file, String file_path) {
        File dest = new File(file_path);

        if(dest.exists()) {
            LOG.info("file already exists");
            return false;
        }

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

    public static boolean deleteFile(String file_path) {
        File file = new File(file_path);
        if(!file.exists()) {
            LOG.info("file does not exist");
            return false;
        }
        if(!file.delete()) {
            LOG.info("failed to delete the file");
            return false;
        }
        return true;
    }

    public static String renameFile(String file_path, String new_name) {
        File old = new File(file_path);
        if(!old.exists()) {
            LOG.info("file does not exist");
            return "";
        }

        String new_path = file_path.substring(0, file_path.lastIndexOf('/')) + "/" + new_name + file_path.substring(file_path.lastIndexOf('.'));
        File new_file = new File(new_path);
        if(new_file.exists()) {
            LOG.info("file already exists");
            return "";
        }
        boolean rename_result = old.renameTo(new_file);
        if(!rename_result) {
            LOG.info("failed to rename file");
            return "";
        }

        return new_path;

    }
}
