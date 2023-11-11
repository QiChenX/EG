package org.eg.service.impl;


import io.micrometer.common.util.StringUtils;
import org.eg.entity.Music;
import org.eg.enums.FileTypeEnum;
import org.eg.repository.MusicRepository;
import org.eg.service.MediaService;
import org.eg.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MusicServiceImpl implements MediaService<Music> {
    @Autowired
    private MusicRepository musicRepository;
    private static final Logger LOG = LoggerFactory.getLogger(MusicServiceImpl.class);

    @Override
    public List<Music> getAllMedia() {
        return musicRepository.findAll();
    }

    @Override
    public boolean addMedia(String name, Boolean is_local, String url, MultipartFile file) {
        Music music = new Music();
        music.setMusic_name(name);
        music.setIs_local(is_local);
        music.setUrl(url);

        if(is_local) {
            if(file != null) {
                String file_path = null;
                file_path = FileUtils.uploadFile(file, name, FileTypeEnum.Music);
                if(StringUtils.isNotBlank(file_path)) {
                    music.setLocal_address(file_path);
                    musicRepository.saveAndFlush(music);
                    return true;
                }
            }
            else {
                LOG.info("file is null");
            }
        }
        else {
            music.setLocal_address("");
            musicRepository.saveAndFlush(music);
            return true;
        }

        return false;
    }

    @Override
    public boolean updateMediaInfo(Integer id, String name, String url) {
        Music music = musicRepository.findById(id).orElse(null);

        if(music == null) {
            LOG.info("failed to update music info: cannot find the record in database");
            return false;
        }

        if(!music.getIs_local() & !url.isEmpty()) {
            music.setUrl(url);
        }
        if(!music.getMusic_name().equals(name) & !name.isEmpty()) {
            String new_address = FileUtils.renameFile(music.getLocal_address(), name);
            if(new_address.isEmpty()) {
                LOG.info("failed to rename file");
                return false;
            }
            music.setMusic_name(name);
            music.setLocal_address(new_address);
        }

        musicRepository.saveAndFlush(music);
        return true;
    }

    @Override
    public boolean deleteMedia(Integer id) {
        if(musicRepository.existsById(id)) {
            Music target = musicRepository.findById(id).orElse(null);
            assert target != null;
            if(target.getIs_local()) {
               boolean delete_result = FileUtils.deleteFile(target.getLocal_address());
               if(!delete_result) {
                   LOG.info("failed to delete local file");
                   return false;
               }
            }
            musicRepository.deleteById(id);
            return true;
        }
        LOG.info("cannot find the record");
        return false;
    }

    @Override
    public Music getMedia(Integer id) {
        Music music = musicRepository.findById(id).orElse(null);
        if(music == null) {
            LOG.info("failed to get music: cannot find the record in database");
        }
        return music;
    }
}
