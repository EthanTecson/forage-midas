package com.jpmc.midascore.entity;

import com.jpmc.midascore.foundation.Transaction;
import jakarta.persistence.*;
import org.springframework.data.repository.Repository;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue()
    private Long id; // This is the unique ID for this specific record

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserRecord recipient;

    private float amount;

    public TransactionRecord() {}

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount){
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }
}

