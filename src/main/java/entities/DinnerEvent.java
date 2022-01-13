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
@Table(name = "dinnerEvent")
public class DinnerEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dinner_event_id", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "dinnerEvent", cascade = CascadeType.PERSIST)
    private List<Assignment> assignments;

    private String location; //TODO: Object later
    private String dish;
    private double pricePerPerson;

    @Temporal(TemporalType.DATE)
    private Date time;

    public DinnerEvent(String location, String dish, double pricePerPerson, Date time) {
        this.location = location;
        this.dish = dish;
        this.pricePerPerson = pricePerPerson;
        this.assignments = new ArrayList<>();
        this.time = time;
    }

    public DinnerEvent() {
    }



    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
        if (assignment != null){
            assignment.setDinnerEvent(this);
        }
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public Integer getId() {
        return id;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public double getPricePerPerson() {
        return pricePerPerson;
    }

    public Date getTime() {
        return time;
    }

    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }
}
