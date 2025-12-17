package com.example.demo.service;


import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // autowired nhờ Bean
    private final JwtService jwtService;


    public RegisterResponse register(User user) {
        checkUserIsValid(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new RegisterResponse(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getImgUrl(),
                user.getGender(),
                user.getDateOfBirth()
        );
    }

    public Optional<UserDto> getUserDtoByToken(String token) {
        String username = jwtService.extractUsername(token);
        return userRepository.findUserDtoByUsername(username);
    }

    public Long getUserIdByToken(String token) {
        String username = jwtService.extractUsername(token);
        return userRepository.findUserDtoByUsername(username).get().getId();
    }

    public LoginResponse login(LoginRequest loginRequest) {

        // 1. Tìm user theo username
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // 2. Kiểm tra password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // 3. Tạo token
//        String token = JwtUtil.generateToken(user.getUsername());
        String token = jwtService.generateToken(user.getUsername());

        // 4. Trả về response
        return new LoginResponse(
                200,
                "Login successful",
                token
        );
    }

    public Optional<UserDto> getUserInfoById(Long id) {
        return userRepository.findUserDtoById(id);
    }

    private void checkUserIsValid(User user) {

        // Kiểm tra null cơ bản (mặc dù @NotBlank sẽ kiểm rồi nếu dùng @Valid)
        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new RuntimeException("Fullname is required");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new RuntimeException("Username is required");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            throw new RuntimeException("Phone number is required");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }
        if (user.getGender() == null || user.getGender().isBlank()) {
            throw new RuntimeException("Gender is required");
        }
        if (user.getDateOfBirth() == null) {
            throw new RuntimeException("Date of birth is required");
        }

        // Kiểm tra duplicate trong DB
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }
    }

    public int updateUserAvatar(String fileImg, String token) throws IOException {
        String username = jwtService.extractUsername(token);
        return userRepository.updateUserAvatar(username, fileImg);
    }

}
