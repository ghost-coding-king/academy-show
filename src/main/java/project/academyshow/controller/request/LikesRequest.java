package project.academyshow.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.academyshow.entity.ReferenceType;

@Getter @Setter
@NoArgsConstructor
public class LikesRequest {
    private ReferenceType type;
    private Long referenceId;
}
