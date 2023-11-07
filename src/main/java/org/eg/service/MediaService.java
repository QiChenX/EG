package org.eg.service;

import org.eg.entity.Music;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MediaService<T> {
    List<T> getAllMedia();
    boolean addMedia(String name, Boolean is_local, String url, String local_address, MultipartFile file);
    boolean updateMediaInfo(Integer id, String name, Boolean is_local, String url, String local_address);
    boolean deleteMedia(Integer id);
    T getMedia(Integer id);
}
