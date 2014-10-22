package com.nx.service;

import com.nx.domain.security.User;
import com.nx.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by Neal on 10/12 012.
 */
@Service
public class UserService {
    private UserRepository userRepository;

    @Cacheable(key = "userCache", value = "#name")
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
