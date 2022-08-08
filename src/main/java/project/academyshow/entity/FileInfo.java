package project.academyshow.entity;

import javax.persistence.*;

@Entity
public class FileInfo {

    @Id @GeneratedValue
    private Long id;

    private String OriginalFileName;
    private String savedFileName;

    private int size;
    private String ext;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
