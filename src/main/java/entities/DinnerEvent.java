package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Plaul
 */
@Entity
@Table(name = "dinnerEvent")
public class gDinnerEvent implements Serializable {
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

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DinnerEvent that = (DinnerEvent) o;
        return Double.compare(that.pricePerPerson, pricePerPerson) == 0 && Objects.equals(id, that.id) && Objects.equals(assignments, that.assignments) && Objects.equals(location, that.location) && Objects.equals(dish, that.dish) && Objects.equals(time, that.time);
    }


    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }
}
