package org.eg.service.impl;

import io.micrometer.common.util.StringUtils;
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
    public boolean addMedia(String name, Boolean is_local, String url, MultipartFile file) {
        Video video = new Video();
        video.setVideo_name(name);
        video.setIs_local(is_local);
        video.setUrl(url);

        if(is_local) {
            if(file != null) {
                String file_path = null;
                file_path = FileUtils.uploadFile(file, name, FileTypeEnum.Video);
                if(StringUtils.isNotBlank(file_path)) {
                    video.setLocal_address(file_path);
                    videoRepository.saveAndFlush(video);
                    return true;
                }
            }
            else {
                LOG.info("file is null");
            }
        }
        else {
            video.setLocal_address("");
            videoRepository.saveAndFlush(video);
            return true;
        }

        return false;
    }

    @Override
    public boolean updateMediaInfo(Integer id, String name, String url) {
        Video video = videoRepository.findById(id).orElse(null);

        if(video == null) {
            LOG.info("failed to update video info: cannot find the record in database");
            return false;
        }

        if(!video.getIs_local() & !url.isEmpty()) {
            video.setUrl(url);
        }
        if(!video.getVideo_name().equals(name) & !name.isEmpty()) {
            String new_address = FileUtils.renameFile(video.getLocal_address(), name);
            if(new_address.isEmpty()) {
                LOG.info("failed to rename file");
                return false;
            }
            video.setVideo_name(name);
            video.setLocal_address(new_address);
        }

        videoRepository.saveAndFlush(video);
        return true;
    }

    @Override
    public boolean deleteMedia(Integer id) {
        if(videoRepository.existsById(id)) {
            Video target = videoRepository.findById(id).orElse(null);
            assert target != null;
            if(target.getIs_local()) {
                boolean delete_result = FileUtils.deleteFile(target.getLocal_address());
                if(!delete_result) {
                    LOG.info("failed to delete local file");
                    return false;
                }
            }
            videoRepository.deleteById(id);
            return true;
        }
        LOG.info("cannot find the record");
        return false;
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
