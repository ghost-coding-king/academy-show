package project.academyshow.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Post {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    @Lob
    private String content;

    @OneToOne
    @JoinColumn(name = "id")
    private Academy academy;

    private int star;

    @OneToMany(mappedBy = "post")
    private List<FileInfo> files;
}
