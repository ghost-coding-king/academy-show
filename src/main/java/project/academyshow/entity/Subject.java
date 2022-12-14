package project.academyshow.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Subject {

    @Id @GeneratedValue
    private Long id;
    private String name;

    public Subject(String name) {
        this.name = name;
    }
}
