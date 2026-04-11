package com.pt.personal_trainer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pt.personal_trainer.domain.dto.AuthResponseDto;
import com.pt.personal_trainer.domain.dto.UserResponseDto;
import com.pt.personal_trainer.domain.input.ForgotPasswordInput;
import com.pt.personal_trainer.domain.input.LoginInput;
import com.pt.personal_trainer.domain.input.ResetPasswordInput;
import com.pt.personal_trainer.domain.input.UserInput;
import com.pt.personal_trainer.service.AuthService;
import com.pt.personal_trainer.service.EmailConfirmationService;
import com.pt.personal_trainer.service.PasswordResetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailConfirmationService emailConfirmationService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginInput input) {
        return authService.login(input);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid @RequestBody UserInput input) {
        return authService.register(input);
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forgotPassword(@Valid @RequestBody ForgotPasswordInput input) {
        passwordResetService.sendResetEmail(input.getEmail());
    }

    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPasswordForm(@RequestParam String token) {
        String html = """
            <!DOCTYPE html>
            <html><body style="font-family: Arial, sans-serif; text-align: center; padding: 50px;">
            <h2 style="color: #4CAF50;">Reset your password</h2>
            <p>Enter your new password below.</p>
            <form id="resetForm" style="display:inline-block; text-align:left;">
                <input type="password" id="password" name="password" placeholder="New password" minlength="8" required
                    style="padding:10px; width:260px; font-size:14px; border:1px solid #ccc; border-radius:6px;" />
                <br/><br/>
                <button type="submit"
                    style="padding: 12px 28px; background-color: #4CAF50; color: #ffffff;
                           text-decoration: none; border:none; border-radius: 6px; font-weight: bold; cursor:pointer;">
                    Reset Password
                </button>
            </form>
            <p id="msg" style="margin-top:20px;"></p>
            <script>
              const token = %s;
              document.getElementById('resetForm').addEventListener('submit', async (e) => {
                e.preventDefault();
                const password = document.getElementById('password').value;
                const res = await fetch('/api/auth/reset-password', {
                  method: 'PUT',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify({ token, password })
                });
                const msg = document.getElementById('msg');
                if (res.ok) {
                  msg.style.color = '#4CAF50';
                  msg.textContent = 'Password updated! You can close this page and log in.';
                } else {
                  const data = await res.json().catch(() => ({}));
                  msg.style.color = '#c00';
                  msg.textContent = data.detail || 'Failed to reset password.';
                }
              });
            </script>
            </body></html>
            """.formatted("\"" + token.replace("\"", "\\\"") + "\"");
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }

    @PutMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordInput input) {
        passwordResetService.resetPassword(input.getToken(), input.getPassword());
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestParam String token) {
        emailConfirmationService.confirmEmail(token);
        String html = """
            <!DOCTYPE html>
            <html><body style="font-family: Arial, sans-serif; text-align: center; padding: 50px;">
            <h2 style="color: #4CAF50;">Email Confirmed!</h2>
            <p>Your email has been verified successfully. You can close this page and return to the app.</p>
            </body></html>
            """;
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }

}
