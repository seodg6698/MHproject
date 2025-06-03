package MHproject.DTO;

import java.time.LocalDateTime;

public class RegisterDTO {
	
	private String userId;
	private String password;
	private String email;
	private boolean emailVerified;
	private String verificationCode;
	private LocalDateTime verificationExpiry;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public LocalDateTime getVerificationExpiry() {
		return verificationExpiry;
	}
	public void setVerificationExpiry(LocalDateTime verificationExpiry) {
		this.verificationExpiry = verificationExpiry;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	private Long id;
    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginDTO [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", password=");
		builder.append(password);
		builder.append(", email=");
		builder.append(email);
		builder.append(", emailVerified=");
		builder.append(emailVerified);
		builder.append(", verificationCode=");
		builder.append(verificationCode);
		builder.append(", verificationExpiry=");
		builder.append(verificationExpiry);
		builder.append(", createdAt=");
		builder.append(createdAt);
		builder.append(", updatedAt=");
		builder.append(updatedAt);
		builder.append("]");
		return builder.toString();
	}

}
