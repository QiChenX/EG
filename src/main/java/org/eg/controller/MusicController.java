package org.eg.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eg.entity.Music;
import org.eg.service.impl.MusicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/music")
public class MusicController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private MusicServiceImpl musicService;

    @PostMapping("/add")
    @ResponseBody
    public String addMusic(@RequestPart MultipartFile file) {
        String music_name = request.getParameter("music_name");
        Boolean is_local = Boolean.parseBoolean(request.getParameter("is_local"));
        String url = request.getParameter("url");

        boolean add_result = musicService.addMedia(music_name, is_local, url, file);

        if(add_result) {
            return "add music successfully";
        }
        return "failed to add music";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public String deleteMusic() {
        Integer id = Integer.valueOf(request.getParameter("id"));

        boolean delete_result = musicService.deleteMedia(id);

        if(delete_result) {
            return "delete music successfully";
        }
        return "failed to delete music";
    }

    @PutMapping("/update")
    @ResponseBody
    public String updateMusic() {
        Integer id = Integer.valueOf(request.getParameter("id"));
        String new_name = request.getParameter("/new_name");
        String new_url = request.getParameter("/new_url");

        boolean update_result = musicService.updateMediaInfo(id, new_name, new_url);

        if(update_result) {
            return "update music successfully";
        }
        return "failed to update music";
    }

    @GetMapping("/getAll")
    @ResponseBody
    public List<Music> getAllMusic() {
        return musicService.getAllMedia();
    }

    @GetMapping("/get")
    @ResponseBody
    public Music getMusic() {
        Integer id = Integer.valueOf(request.getParameter("id"));

        return musicService.getMedia(id);
    }
}
