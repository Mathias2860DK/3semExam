package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
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

    @ManyToMany
    private List<User> users;

    private String familyName;
    @Temporal(TemporalType.DATE)
    private Date createDate;
    private String contactInfo; //TODO: Object


    public Assignment(DinnerEvent dinnerEvent, String familyName, String contactInfo) {
        this.dinnerEvent = dinnerEvent;
        this.familyName = familyName;
        this.createDate = new Date();
        this.contactInfo = contactInfo;
        this.users = new ArrayList<>();
    }

    public Assignment() {
    }

    public void addUser(User user) {
        this.users.add(user);
        if (user != null){
           // user.add(this);
        }
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

    public List<User> getUsers() {
        return users;
    }


    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "dinnerEvent=" + dinnerEvent +
                ", users=" + users +
                ", familyName='" + familyName + '\'' +
                ", createDate=" + createDate +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
