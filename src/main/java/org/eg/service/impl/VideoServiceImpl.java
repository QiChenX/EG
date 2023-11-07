package org.eg.service.impl;

import org.eg.entity.Music;
import org.eg.entity.Video;
import org.eg.enums.FileTypeEnum;
import org.eg.repository.VideoRepository;
import org.eg.service.MediaService;
import org.eg.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class VideoServiceImpl implements MediaService<Video> {
    @Autowired
    private VideoRepository videoRepository;
    private static final Logger LOG = LoggerFactory.getLogger(VideoServiceImpl.class);

    @Override
    public List<Video> getAllMedia() {
        return videoRepository.findAll();
    }

    @Override
    public boolean addMedia(String name, Boolean is_local, String url, String local_address, MultipartFile file) {
        Video video = new Video();
        video.setVideo_name(name);
        video.setIs_local(is_local);
        video.setUrl(url);
        video.setLocal_address(local_address);

        boolean flag = true;
        if(is_local & file != null) {
            flag = FileUtils.uploadFile(file, name, FileTypeEnum.Video);
        }

        if(flag) {
            videoRepository.saveAndFlush(video);
            return true;
        }

        return false;
    }

    @Override
    public boolean updateMediaInfo(Integer id, String name, Boolean is_local, String url, String local_address) {
        Video video = videoRepository.findById(id).orElse(null);
        if(video == null) {
            LOG.info("failed to update video info: cannot find the record in database");
            return false;
        }
        video.setVideo_name(name);
        video.setIs_local(is_local);
        video.setUrl(url);
        video.setLocal_address(local_address);
        videoRepository.saveAndFlush(video);
        return true;
    }

    @Override
    public boolean deleteMedia(Integer id) {
        if(videoRepository.existsById(id)) {
            videoRepository.deleteById(id);
        }
        return true;
    }

    @Override
    public Video getMedia(Integer id) {
        Video video = videoRepository.findById(id).orElse(null);
        if(video == null) {
            LOG.info("failed to get video: cannot find the record in database");
        }
        return video;
    }
}
