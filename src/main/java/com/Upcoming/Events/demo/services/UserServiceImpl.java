package com.Upcoming.Events.demo.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.Upcoming.Events.demo.Exception.UserAlreadyExistsException;
import com.Upcoming.Events.demo.Exception.UserServiceException;
import com.Upcoming.Events.demo.models.User;
import com.Upcoming.Events.demo.repositories.UserRepository;

@Service
public class UserServiceImpl implements BaseService<User>{
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User save(User User) {
        return userRepository.save(User);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User store(User user) { 

        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

        if (userOptional.isPresent()) {
            throw new UserAlreadyExistsException("User already exist.");
        }
        try {          
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                return userRepository.save(user);
            
        } catch (DataAccessException e) {
            throw new UserServiceException("Dont save user", e);
        }
    }

    @Override
    public void deleteById(Long id) {
         userRepository.deleteById(id);
    }
}
