package com.example.demo.domain.Service;

import com.example.demo.domain.exception.ResourceNotFoundException;
import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection (recommended over @Autowired on fields)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET: Retrieve all users
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET: Retrieve user by ID
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // CREATE: Save a new user
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
