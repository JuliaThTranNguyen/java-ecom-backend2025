package com.example.fakestore.service.impl;

import com.example.fakestore.dto.request.ChangePassword;
import com.example.fakestore.dto.request.MailBody;
import com.example.fakestore.dto.request.VerifyEmailRequest;
import com.example.fakestore.dto.request.VerifyOtpRequest;
import com.example.fakestore.entity.ForgotPassword;
import com.example.fakestore.entity.User;
import com.example.fakestore.exception.ApiException;
import com.example.fakestore.exception.ErrorCode;
import com.example.fakestore.repository.ForgotPasswordRepository;
import com.example.fakestore.repository.UserRepository;
import com.example.fakestore.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private Random random = new Random();

    @Override
    public String verifyEmail(VerifyEmailRequest emailRequest) {
        User user = userRepository
                .findByEmail(emailRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

        forgotPasswordRepository.deleteByUser(user);

        int otp = random.nextInt(100_000, 999_999);
        MailBody mailBody = MailBody.builder()
                .to(emailRequest.getEmail())
                .subject("OTP for Forgot Password request")
                .text("This is the otp for your Forgot Password request: " + otp)
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 2 * 60 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return "Email sent for verification!";
    }

    @Override
    public String verifyOtp(VerifyOtpRequest otpRequest) {
        User user = userRepository
                .findByEmail(otpRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

        ForgotPassword fp = forgotPasswordRepository
                .findByOtpAndUser(otpRequest.getOtp(), user)
                .orElseThrow(() ->
                        new ApiException(ErrorCode.BAD_REQUEST, "Invalid OTP for email: " + otpRequest.getEmail()));

        if (Boolean.TRUE.equals(fp.getExpirationTime().before(Date.from(Instant.now())))) {
            forgotPasswordRepository.deleteById(fp.getFpid());
            return "OTP has expired!";
        }

        return "OTP verified!";
    }

    @Override
    public String changePassword(ChangePassword changePassword, String email) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return "Please enter the password again";
        }

        userRepository.updatePassword(email, passwordEncoder.encode(changePassword.password()));

        return "Password has been changed!";
    }
}
