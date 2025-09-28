package myblogbackend.myblogbackend.service;

import myblogbackend.myblogbackend.entity.User;
import myblogbackend.myblogbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register user without encoding
    public User registerUser(User user) throws Exception {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already exists");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Username already exists");
        }

        // Save password as plain text (for learning purposes only)
        return userRepository.save(user);
    }

    // Login user without password encoder
    public User loginUser(String email, String password) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = userOptional.get();

        if (!password.equals(user.getPassword())) {
            throw new Exception("Invalid password");
        }

        return user;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
