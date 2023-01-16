package pl.mgis.problemreport.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "settings")
public class Setting extends BaseEntity{

    @Column
    @Type(type = "org.hibernate.type.StringType")
    private String key;
    @Column
    @Type(type = "org.hibernate.type.StringType")
    private String displayName;
    @Lob
    @Column
    @Type(type = "org.hibernate.type.TextType")
    private String value;
    @Embedded
    private Audit audit = new Audit();

    public Setting() {
    }

    public Setting(String key, String displayName, String value) {
        this.key = key;
        this.displayName = displayName;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
