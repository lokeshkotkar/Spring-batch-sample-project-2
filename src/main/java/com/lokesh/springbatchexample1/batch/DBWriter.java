package com.lokesh.springbatchexample1.batch;

import com.lokesh.springbatchexample1.model.User;
import com.lokesh.springbatchexample1.repository.UserRepository;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * this class writes a data back into database
 *
 */
@Component
public class DBWriter implements ItemWriter<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void write(List<? extends User> users) throws Exception {

        System.out.println("Data Saved for Users: " + users);
        userRepository.save(users);
    }
}