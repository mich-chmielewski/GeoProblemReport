package pl.mgis.problemreport.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    private long reportId;
    @Lob
    @Column
    @Type(type = "org.hibernate.type.TextType")
    private String content;
    @Embedded
    private Audit audit = new Audit();
    @Column(name = "send_by_email", columnDefinition = "boolean default false")
    private boolean sendByEmail;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public boolean isSendByEmail() {
        return sendByEmail;
    }

    public void setSendByEmail(boolean sendByEmail) {
        this.sendByEmail = sendByEmail;
    }
}
