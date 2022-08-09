package project.academyshow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.academyshow.service.FilePath;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private FilePath path;
    private long size;
    private String ext;
}
