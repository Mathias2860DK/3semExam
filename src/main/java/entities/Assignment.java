package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Plaul
 */
@Entity
@Table(name = "assignment")
public class Assignment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id", nullable = false)
    private Integer id;

    @ManyToOne()
    private DinnerEvent dinnerEvent;

    private String familyName;
    @Temporal(TemporalType.DATE)
    private Date createDate;
    private String contactInfo; //TODO: Object


    public Assignment(DinnerEvent dinnerEvent, String familyName, String contactInfo) {
        this.dinnerEvent = dinnerEvent;
        this.familyName = familyName;
        this.createDate = new Date();
        this.contactInfo = contactInfo;
    }

    public Assignment() {
    }

    public DinnerEvent getDinnerEvent() {
        return dinnerEvent;
    }

    public void setDinnerEvent(DinnerEvent dinnerEvent) {
        this.dinnerEvent = dinnerEvent;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
