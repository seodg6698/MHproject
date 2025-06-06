package MHproject.DTO;

import java.time.LocalDateTime;

public class EmailVerificationDTO {
    private String email;
    private String verificationCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean verified;
    
    // 기본 생성자
    public EmailVerificationDTO() {}
    
    // 생성자
    public EmailVerificationDTO(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(5); // 5분 후 만료
        this.verified = false;
    }
    
    // Getter/Setter
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getVerificationCode() {
        return verificationCode;
    }
    
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
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
    
    public boolean isVerified() {
        return verified;
    }
    
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    
    // 만료 여부 확인
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}