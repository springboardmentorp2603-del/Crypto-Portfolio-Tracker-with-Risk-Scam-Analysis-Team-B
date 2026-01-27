package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.dto.AuthRequest;
import com.CryptoProject.CryptoInfosys.dto.ResetPasswordRequest;
import com.CryptoProject.CryptoInfosys.model.PasswordResetToken;
import com.CryptoProject.CryptoInfosys.repository.PasswordResetTokenRepository;
import com.CryptoProject.CryptoInfosys.dto.AuthResponse;
import com.CryptoProject.CryptoInfosys.dto.RegisterRequest;
import com.CryptoProject.CryptoInfosys.model.User;
import com.CryptoProject.CryptoInfosys.repository.UserRepository;
import com.CryptoProject.CryptoInfosys.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.CryptoProject.CryptoInfosys.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.time.LocalDateTime;


@Service
@Transactional
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    

    public AuthService(UserRepository userRepo, 
    PasswordEncoder passwordEncoder, 
    JwtUtils jwtUtils, 
    PasswordResetTokenRepository passwordResetTokenRepository, 
    EmailService emailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }
    // Register -> Authentication
    @Transactional
    public void register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User u = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .build();
        userRepo.save(u);
    }
    // Login -> Authentication
    public AuthResponse login(AuthRequest req) {
        User u = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtUtils.generateToken(u.getEmail());
        return new AuthResponse(token, "Bearer", u.getEmail(), u.getName());
    }

    // Forgot Password
@Transactional
public void forgotPassword(String email) {

    User user = userRepo.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    String token = UUID.randomUUID().toString();
    LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

    PasswordResetToken resetToken =
            passwordResetTokenRepository.findByUser(user)
                    .orElse(null);

    if (resetToken != null) {
        // 🔥 Update existing token
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiry);
    } else {
        // 🔥 Create new token
        resetToken = new PasswordResetToken(token, user, expiry);
    }

    passwordResetTokenRepository.save(resetToken);

    emailService.sendPasswordResetEmail(user.getEmail(), token);
}




// Reset Password
public void resetPassword(ResetPasswordRequest request) {

    PasswordResetToken resetToken = passwordResetTokenRepository
            .findByToken(request.getToken())
            .orElseThrow(() ->
                    new RuntimeException("Invalid reset token"));

    if (resetToken.getExpiryDate()
            .isBefore(java.time.LocalDateTime.now())) {

        passwordResetTokenRepository.delete(resetToken);
        throw new RuntimeException("Reset token expired");
    }

    User user = resetToken.getUser();

    user.setPassword(
            passwordEncoder.encode(request.getNewPassword())
    );

    userRepo.save(user);

    passwordResetTokenRepository.delete(resetToken);
}

}
