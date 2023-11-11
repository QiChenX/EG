package org.eg.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eg.entity.Video;
import org.eg.service.impl.VideoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private VideoServiceImpl videoService;

    @PostMapping("/add")
    @ResponseBody
    public String addVideo(@RequestPart MultipartFile file) {
        String video_name = request.getParameter("video_name");
        Boolean is_local = Boolean.parseBoolean(request.getParameter("is_local"));
        String url = request.getParameter("url");

        boolean add_result = videoService.addMedia(video_name, is_local, url, file);

        if(add_result) {
            return "add video successfully";
        }
        return "failed to add video";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public String deleteVideo() {
        Integer id = Integer.valueOf(request.getParameter("id"));

        boolean delete_result = videoService.deleteMedia(id);

        if(delete_result) {
            return "delete video successfully";
        }
        return "failed to delete video";
    }

    @PutMapping("/update")
    @ResponseBody
    public String updateVideo() {
        Integer id = Integer.valueOf(request.getParameter("id"));
        String new_name = request.getParameter("/new_name");
        String new_url = request.getParameter("/new_url");

        boolean update_result = videoService.updateMediaInfo(id, new_name, new_url);

        if(update_result) {
            return "update video successfully";
        }
        return "failed to update video";
    }

    @GetMapping("/getAll")
    @ResponseBody
    public List<Video> getAllVideo() {
        return videoService.getAllMedia();
    }

    @GetMapping("/get")
    @ResponseBody
    public Video getVideo() {
        Integer id = Integer.valueOf(request.getParameter("id"));

        return videoService.getMedia(id);
    }
}
