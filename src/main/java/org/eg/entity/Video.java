package org.eg.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Data
@Table(name = "video_details")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String video_name;
    @Column
    private Boolean is_local;
    @Column
    private String url;
    @Column
    private String local_address;

}
