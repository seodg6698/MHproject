package MHproject.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import MHproject.DTO.EmailVerificationDTO;

@Service
public class EmailVerificationService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    // 메모리에 인증 정보를 저장 (실제 운영에서는 Redis 등 사용 권장)
    private Map<String, EmailVerificationDTO> verificationStorage = new ConcurrentHashMap<>();
    
    /**
     * 인증번호 생성 (6자리 숫자)
     */
    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
    
    /**
     * 이메일 인증번호 발송
     */
    public boolean sendVerificationEmail(String email) {
        try {
            // 기존 인증 정보가 있다면 제거
            verificationStorage.remove(email);
            
            // 새로운 인증번호 생성
            String verificationCode = generateVerificationCode();
            
            // 인증 정보 저장
            EmailVerificationDTO verification = new EmailVerificationDTO(email, verificationCode);
            verificationStorage.put(email, verification);
            
            // 이메일 발송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[MH Community] 이메일 인증번호");
            message.setText(
                "안녕하세요. MH Community입니다.\n\n" +
                "회원가입을 위한 이메일 인증번호는 다음과 같습니다.\n\n" +
                "인증번호: " + verificationCode + "\n\n" +
                "인증번호는 5분 후 만료됩니다.\n" +
                "감사합니다."
            );
            message.setFrom("noreply@mhcommunity.com");
            
            mailSender.send(message);
            
            System.out.println("이메일 발송 성공: " + email + ", 인증번호: " + verificationCode);
            return true;
            
        } catch (Exception e) {
            System.err.println("이메일 발송 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 인증번호 확인
     */
    public boolean verifyCode(String email, String inputCode) {
        EmailVerificationDTO verification = verificationStorage.get(email);
        
        if (verification == null) {
            System.out.println("인증 정보가 없습니다: " + email);
            return false;
        }
        
        if (verification.isExpired()) {
            System.out.println("인증번호가 만료되었습니다: " + email);
            verificationStorage.remove(email);
            return false;
        }
        
        if (verification.getVerificationCode().equals(inputCode)) {
            verification.setVerified(true);
            System.out.println("인증 성공: " + email);
            return true;
        }
        
        System.out.println("인증번호가 일치하지 않습니다: " + email);
        return false;
    }
    
    /**
     * 이메일 인증 완료 여부 확인
     */
    public boolean isEmailVerified(String email) {
        EmailVerificationDTO verification = verificationStorage.get(email);
        return verification != null && verification.isVerified() && !verification.isExpired();
    }
    
    /**
     * 회원가입 완료 후 인증 정보 정리
     */
    public void clearVerification(String email) {
        verificationStorage.remove(email);
        System.out.println("인증 정보 정리 완료: " + email);
    }
    
    /**
     * 만료된 인증 정보들을 주기적으로 정리하는 메서드
     */
    public void cleanupExpiredVerifications() {
        verificationStorage.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}