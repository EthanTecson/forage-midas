package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Kafka {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public Kafka(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    @KafkaListener(id = "midas-core", topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {
        //System.out.println("Here is the transaction: " + transaction);
        preformTransaction(transaction);
    }

    @Transactional
    public void preformTransaction(Transaction transaction){
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        // 1. do both user exist?
        if (sender == null || recipient == null){
            return;
        }

        // 2. Does sender have enough money?
        if (sender.getBalance() < transaction.getAmount()){
            return;
        }

        // 3. Execution
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());

        // 4. Persistence: Save changes to database
        userRepository.save(sender);
        userRepository.save(recipient);

        // 5. Recording: Create the history record?
        TransactionRecord transactionRecord = new TransactionRecord(sender,recipient,transaction.getAmount());
        transactionRecordRepository.save(transactionRecord);

    }

}

