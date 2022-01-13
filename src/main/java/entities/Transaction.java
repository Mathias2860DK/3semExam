package entities;

import javax.persistence.*;
import java.util.Date;

@Table(name = "transaction")
@Entity
//As a member i would like to see all my prvious transactions and my current account status
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private Integer id;

    private double amount;
    @Temporal(TemporalType.DATE)
    private Date date;

    //En user kan have mange transactions. En transaction kan have en bruger tilknyttet.
    @ManyToOne()
    private User user;

    public Transaction(double amount) {
        this.amount = amount;
        this.date = new Date();
    }

    public Transaction() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}