package pl.mgis.problemreport.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CommentDTO {

    private long id;
    private LocalDateTime created;
    private LocalDateTime updated;
    @NotNull(message = "This field cannot be null")
    @Min(value = 1, message = "Only positive numbers")
    private long reportId;
    @NotBlank(message = "This field cannot be blank")
    private String content;
    private boolean sendByEmail = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSendByEmail() {
        return sendByEmail;
    }

    public void setSendByEmail(boolean sendByEmail) {
        this.sendByEmail = sendByEmail;
    }
}
