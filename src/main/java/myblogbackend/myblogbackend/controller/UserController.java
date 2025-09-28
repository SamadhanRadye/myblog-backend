package myblogbackend.myblogbackend.controller;

import myblogbackend.myblogbackend.entity.User;
import myblogbackend.myblogbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

   @PostMapping("/signup")
public ResponseEntity<Map<String, Object>> signUp(@Valid @RequestBody User user) {
    // Debugging: print received data
    System.out.println("\n\nUsername: " + user.getUsername());
    System.out.println("Password: " + user.getPassword() + "\n\n");

    Map<String, Object> response = new HashMap<>();

    try {
        User savedUser = userService.registerUser(user);
        response.put("success", true);
        response.put("message", "User registered successfully");
        response.put("user", Map.of(
                "id", savedUser.getId(),
                "username", savedUser.getUsername(),
                "email", savedUser.getEmail(),
                "createdAt", savedUser.getCreatedAt()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    } catch (Exception e) {
        response.put("success", false);
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            User user = userService.loginUser(email, password);
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "createdAt", user.getCreatedAt()));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = userService.findById(id).orElseThrow(() -> new Exception("User not found"));
            response.put("success", true);
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "createdAt", user.getCreatedAt()));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
