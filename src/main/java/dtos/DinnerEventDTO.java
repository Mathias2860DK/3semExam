package dtos;

import entities.DinnerEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class DinnerEventDTO {
    private Integer id;
    private String location; //TODO: Object later
    private String dish;
    private double pricePerPerson;
    private Date time;

    public DinnerEventDTO(String location, String dish, double pricePerPerson) {
        this.location = location;
        this.dish = dish;
        this.pricePerPerson = pricePerPerson;
    }

    public DinnerEventDTO(DinnerEvent dinnerEvent) {
        if(dinnerEvent.getId() != null)
            this.id = dinnerEvent.getId();
        this.location = dinnerEvent.getLocation();
        this.dish = dinnerEvent.getDish();
        this.pricePerPerson = dinnerEvent.getPricePerPerson();
        this.time = dinnerEvent.getTime();
    }

    public static List<DinnerEventDTO> getDtos(List<DinnerEvent> dinnerEvents){
        List<DinnerEventDTO> dinnerEventDTOS = new ArrayList();
        dinnerEvents.forEach(dinnerEvent->dinnerEventDTOS.add(new DinnerEventDTO(dinnerEvent)));
        return dinnerEventDTOS;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DinnerEventDTO that = (DinnerEventDTO) o;
        return Double.compare(that.pricePerPerson, pricePerPerson) == 0 && Objects.equals(id, that.id) && Objects.equals(location, that.location) && Objects.equals(dish, that.dish) && Objects.equals(time, that.time);
    }

    @Override
    public String toString() {
        return "DinnerEventDTO{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", dish='" + dish + '\'' +
                ", pricePerPerson=" + pricePerPerson +
                ", time=" + time +
                '}';
    }

    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }
}
