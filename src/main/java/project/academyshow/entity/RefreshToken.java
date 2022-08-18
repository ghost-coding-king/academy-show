package project.academyshow.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id @GeneratedValue
    private Long id;

    @Getter @Setter
    private String token;

    private String username;
}
