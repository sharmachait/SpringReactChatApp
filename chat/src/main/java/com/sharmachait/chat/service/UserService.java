package com.sharmachait.chat.service;

import com.sharmachait.chat.model.Status;
import com.sharmachait.chat.model.User;
import com.sharmachait.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public User saveUser(User user){
        user.setStatus(Status.ONLINE);
        return userRepository.save(user);
    }

    public User disconnect(User user) throws IllegalArgumentException{
        User storedUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if(storedUser != null){
            storedUser.setStatus(Status.OFFLINE);
            return userRepository.save(storedUser);
        }else{
            throw new IllegalArgumentException("User not found");
        }
    }

    public List<User> getOnlineUsers(){
        return  userRepository.findAllByStatus(Status.ONLINE);
    }
}
