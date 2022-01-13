package dtos;

import entities.Assignment;
import entities.User;

public class UserDTO {
    private String userName;
    private String address;
    private double balance;
    private int birthYear;
    private Assignment assignment;

   /* public UserDTO(String userName, String address, double balance, int birthYear) {
        this.userName = userName;
        this.address = address;
        this.balance = balance;
        this.birthYear = birthYear;
    }*/
    public UserDTO(String userName, Assignment assignment){
        this.userName = userName;
        this.assignment = assignment;
    }

    public UserDTO(User user){
        this.userName = user.getUserName();
        this.balance = user.getBalance();
        this.address = user.getAddress();
        this.birthYear = user.getBirthYear();
    }

    public String getUserName() {
        return userName;
    }

    public String getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public Assignment getAssignment() {
        return assignment;
    }
}
