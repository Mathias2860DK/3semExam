package dtos;

import entities.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionDTO {
    private Integer id;
private double amount;
private Date date;

    public TransactionDTO(Integer id, double amount, Date date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
    }

    public static List<TransactionDTO> getDtos(List<Transaction> transactions){
        List<TransactionDTO> transactionDTOS = new ArrayList();
        transactions.forEach(transaction->transactionDTOS.add(new TransactionDTO(transaction)));
        return transactionDTOS;
    }


    public TransactionDTO(Transaction transaction) {
        if(transaction.getId() != null)
            this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
    }

}
