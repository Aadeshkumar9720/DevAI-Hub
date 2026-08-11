package com.devaihub.backend.service.impl;

import com.devaihub.backend.dto.LoginRequest;
import com.devaihub.backend.dto.RegisterRequest;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.jwt.JwtService;
import com.devaihub.backend.repository.UserRepository;
import com.devaihub.backend.response.LoginResponse;
import com.devaihub.backend.service.interfaces.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.devaihub.backend.service.interfaces.EmailService;
import com.devaihub.backend.entity.PasswordResetToken;
import com.devaihub.backend.repository.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,EmailService emailService,
            PasswordResetTokenRepository passwordResetTokenRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.passwordResetTokenRepository =
                passwordResetTokenRepository;

    }
    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }

    @Override
    public User registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        String html = """
<html>
<body style="font-family:Arial;background:#f4f4f4;padding:30px;">

<div style="
max-width:600px;
margin:auto;
background:white;
padding:30px;
border-radius:10px;
box-shadow:0 0 10px rgba(0,0,0,.1);
">

<h1 style="color:#2563eb;">
🚀 Welcome to DevAI Hub
</h1>

<p>Hello <b>%s</b>,</p>

<p>
Your account has been created successfully.
</p>

<p>
You can now start creating projects,
managing tasks,
uploading attachments
and collaborating with your team.
</p>

<a href="http://localhost:3000"
style="
display:inline-block;
background:#2563eb;
color:white;
padding:12px 20px;
text-decoration:none;
border-radius:6px;
">
Login Now
</a>

<hr>

<p style="color:gray;font-size:12px">
Team DevAI Hub
</p>

</div>

</body>
</html>
""".formatted(savedUser.getFirstName());

        emailService.sendHtmlEmail(
                savedUser.getEmail(),
                "Welcome to DevAI Hub 🚀",
                html
        );

        return savedUser;
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // Remove previous reset tokens for this user
        passwordResetTokenRepository.deleteByUserId(
                user.getId()
        );

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken =
                new PasswordResetToken();

        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(
                LocalDateTime.now().plusMinutes(15)
        );

        passwordResetTokenRepository.save(resetToken);

        String resetLink =
                "http://localhost:5173/reset-password?token="
                        + token;

        String html = """
            <html>
            <body>
                <h2>Reset Your DevAI Hub Password</h2>

                <p>Hello %s,</p>

                <p>
                    We received a request to reset your
                    DevAI Hub password.
                </p>

                <p>
                    Click the button below to reset your password:
                </p>

                <p>
                    <a href="%s"
                       style="
                       display:inline-block;
                       padding:12px 20px;
                       background:#4f46e5;
                       color:white;
                       text-decoration:none;
                       border-radius:6px;">
                       Reset Password
                    </a>
                </p>

                <p>
                    This link will expire in
                    <strong>15 minutes</strong>.
                </p>

                <p>
                    If you did not request a password reset,
                    you can safely ignore this email.
                </p>

                <p>
                    Regards,<br>
                    DevAI Hub Team
                </p>
            </body>
            </html>
            """.formatted(
                user.getFirstName(),
                resetLink
        );

        emailService.sendHtmlEmail(
                user.getEmail(),
                "Reset Your DevAI Hub Password",
                html
        );
    }
    @Override
    @Transactional
    public void resetPassword(
            String token,
            String newPassword
    ) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid or expired reset token"
                                )
                        );

        if (resetToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            passwordResetTokenRepository.delete(resetToken);

            throw new RuntimeException(
                    "Reset token has expired"
            );
        }

        User user = resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        // Token can only be used once
        passwordResetTokenRepository.delete(resetToken);
    }

}
