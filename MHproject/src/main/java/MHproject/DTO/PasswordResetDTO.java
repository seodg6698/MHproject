package MHproject.DTO;

import java.time.LocalDateTime;

public class PasswordResetDTO {
    private String email;
    private String resetToken;
    private String tempPassword;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean used;
    
    // 기본 생성자
    public PasswordResetDTO() {}
    
    // 생성자
    public PasswordResetDTO(String email, String resetToken, String tempPassword) {
        this.email = email;
        this.resetToken = resetToken;
        this.tempPassword = tempPassword;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(30); // 30분 후 만료
        this.used = false;
    }
    
    // Getter/Setter
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getResetToken() {
        return resetToken;
    }
    
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    
    public String getTempPassword() {
        return tempPassword;
    }
    
    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public boolean isUsed() {
        return used;
    }
    
    public void setUsed(boolean used) {
        this.used = used;
    }
    
    // 만료 여부 확인
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}