package dtos;

import entities.DinnerEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }
}
