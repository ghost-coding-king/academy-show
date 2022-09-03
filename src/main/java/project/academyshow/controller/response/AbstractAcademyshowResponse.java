package project.academyshow.controller.response;

import project.academyshow.entity.AbstractTimestampEntity;

import java.time.LocalDateTime;

public abstract class AbstractAcademyshowResponse {
    /**
     * timestamps
     */
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected AbstractAcademyshowResponse(AbstractTimestampEntity entity) {
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
