package org.eg.service.impl;

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
    public boolean addMedia(String name, Boolean is_local, String url, String local_address, MultipartFile file) {
        Music music = new Music();
        music.setMusic_name(name);
        music.setIs_local(is_local);
        music.setUrl(url);
        music.setLocal_address(local_address);

        boolean flag = true;
        if(is_local & file != null) {
            flag = FileUtils.uploadFile(file, name, FileTypeEnum.Music);
        }

        if(flag) {
            musicRepository.saveAndFlush(music);
            return true;
        }

        return false;
    }

    @Override
    public boolean updateMediaInfo(Integer id, String name, Boolean is_local, String url, String local_address) {
        Music music = musicRepository.findById(id).orElse(null);
        if(music == null) {
            LOG.info("failed to update music info: cannot find the record in database");
            return false;
        }
        music.setMusic_name(name);
        music.setIs_local(is_local);
        music.setUrl(url);
        music.setLocal_address(local_address);
        musicRepository.saveAndFlush(music);
        return true;
    }

    @Override
    public boolean deleteMedia(Integer id) {
        if(musicRepository.existsById(id)) {
            musicRepository.deleteById(id);
        }
        return true;
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
