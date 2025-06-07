package MHproject.service;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import MHproject.DTO.PasswordResetDTO;
import MHproject.DTO.UserDTO;

@Service
public class AccountRecoveryService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private LoginService loginService;
    
    // 실제 이메일 발송을 위한 JavaMailSender (주석 해제)
    @Autowired
    private JavaMailSender mailSender;
    
    // 메모리에 비밀번호 재설정 정보 저장
    private Map<String, PasswordResetDTO> resetStorage = new ConcurrentHashMap<>();
    
    /**
     * 임시 비밀번호 생성 (10자리 영문+숫자+특수문자)
     */
    private String generateTempPassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*";
        
        Random random = new Random();
        StringBuilder tempPassword = new StringBuilder();
        
        // 각 종류별로 최소 1개씩 포함
        tempPassword.append(upperCase.charAt(random.nextInt(upperCase.length())));
        tempPassword.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        tempPassword.append(numbers.charAt(random.nextInt(numbers.length())));
        tempPassword.append(specialChars.charAt(random.nextInt(specialChars.length())));
        
        // 나머지 6자리 랜덤
        String allChars = upperCase + lowerCase + numbers + specialChars;
        for (int i = 4; i < 10; i++) {
            tempPassword.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // 문자열 섞기
        for (int i = 0; i < tempPassword.length(); i++) {
            int randomIndex = random.nextInt(tempPassword.length());
            char temp = tempPassword.charAt(i);
            tempPassword.setCharAt(i, tempPassword.charAt(randomIndex));
            tempPassword.setCharAt(randomIndex, temp);
        }
        
        return tempPassword.toString();
    }
    
    /**
     * 재설정 토큰 생성
     */
    private String generateResetToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 이메일로 아이디 찾기 및 실제 이메일 발송
     */
    public String findUserIdByEmail(String email) {
        try {
            // 데이터베이스에서 사용자 아이디 조회
            String userId = userService.findUserIdByEmail(email);
            
            if (userId != null && !userId.isEmpty()) {
                // 실제 이메일 발송
                boolean emailSent = sendUserIdEmail(email, userId);
                
                if (emailSent) {
                    System.out.println("아이디 찾기 이메일 발송 성공: " + email + " -> " + userId);
                    return userId;
                } else {
                    System.err.println("아이디 찾기 이메일 발송 실패: " + email);
                    return null;
                }
            } else {
                System.out.println("해당 이메일로 등록된 계정이 없습니다: " + email);
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("아이디 찾기 중 오류: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 아이디 찾기 이메일 발송
     */
    private boolean sendUserIdEmail(String email, String userId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[MH Community] 아이디 찾기 결과");
            message.setText(
                "안녕하세요. MH Community입니다.\n\n" +
                "요청하신 아이디 찾기 결과를 안내드립니다.\n\n" +
                "📧 이메일: " + email + "\n" +
                "🆔 아이디: " + userId + "\n\n" +
                "로그인 페이지에서 해당 아이디로 로그인하실 수 있습니다.\n" +
                "비밀번호를 잊으셨다면 '비밀번호 찾기'를 이용해주세요.\n\n" +
                "감사합니다.\n" +
                "MH Community 팀"
            );
            message.setFrom("noreply@mhcommunity.com");
            
            mailSender.send(message);
            
            System.out.println("아이디 찾기 이메일 발송 성공: " + email);
            return true;
            
        } catch (Exception e) {
            System.err.println("아이디 찾기 이메일 발송 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 이메일로 임시 비밀번호 발송 및 데이터베이스 업데이트
     */
    public boolean sendTempPassword(String email) {
        try {
            // 이메일로 사용자 확인
            String userId = userService.findUserIdByEmail(email);
            if (userId == null || userId.isEmpty()) {
                System.out.println("해당 이메일로 등록된 계정이 없습니다: " + email);
                return false;
            }
            
            // 기존 재설정 정보 제거
            resetStorage.entrySet().removeIf(entry -> entry.getValue().getEmail().equals(email));
            
            // 임시 비밀번호 생성
            String tempPassword = generateTempPassword();
            String resetToken = generateResetToken();
            
            // 데이터베이스에서 실제 비밀번호 업데이트
            boolean passwordUpdated = userService.updatePassword(userId, tempPassword);
            
            if (!passwordUpdated) {
                System.err.println("비밀번호 업데이트 실패: " + userId);
                return false;
            }
            
            // 재설정 정보 저장 (30분 후 자동 삭제용)
            PasswordResetDTO resetInfo = new PasswordResetDTO(email, resetToken, tempPassword);
            resetStorage.put(resetToken, resetInfo);
            
            // 실제 이메일 발송
            boolean emailSent = sendTempPasswordEmail(email, userId, tempPassword);
            
            if (emailSent) {
                System.out.println("임시 비밀번호 발송 성공: " + email + " -> " + userId);
                return true;
            } else {
                System.err.println("임시 비밀번호 이메일 발송 실패: " + email);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("임시 비밀번호 발송 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 임시 비밀번호 이메일 발송
     */
    private boolean sendTempPasswordEmail(String email, String userId, String tempPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[MH Community] 임시 비밀번호 발송");
            message.setText(
                "안녕하세요. MH Community입니다.\n\n" +
                "요청하신 임시 비밀번호를 발송해드립니다.\n\n" +
                "🆔 아이디: " + userId + "\n" +
                "🔐 임시 비밀번호: " + tempPassword + "\n\n" +
                "⚠️ 보안을 위해 다음 사항을 반드시 준수해주세요:\n" +
                "1. 로그인 후 즉시 비밀번호를 변경해주세요\n" +
                "2. 임시 비밀번호는 타인에게 노출되지 않도록 주의해주세요\n" +
                "3. 이 이메일은 발송 후 삭제하시기 바랍니다\n\n" +
                "로그인 페이지: http://localhost:8080/login/login\n\n" +
                "감사합니다.\n" +
                "MH Community 팀"
            );
            message.setFrom("noreply@mhcommunity.com");
            
            mailSender.send(message);
            
            System.out.println("임시 비밀번호 이메일 발송 성공: " + email);
            return true;
            
        } catch (Exception e) {
            System.err.println("임시 비밀번호 이메일 발송 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 비밀번호 재설정 링크 발송
     */
    public boolean sendPasswordResetLink(String email) {
        try {
            // 이메일로 사용자 확인
            String userId = userService.findUserIdByEmail(email);
            if (userId == null || userId.isEmpty()) {
                System.out.println("해당 이메일로 등록된 계정이 없습니다: " + email);
                return false;
            }
            
            // 기존 재설정 정보 제거
            resetStorage.entrySet().removeIf(entry -> entry.getValue().getEmail().equals(email));
            
            // 재설정 토큰 생성
            String resetToken = generateResetToken();
            String tempPassword = ""; // 링크 방식에서는 임시 비밀번호 불필요
            
            // 재설정 정보 저장
            PasswordResetDTO resetInfo = new PasswordResetDTO(email, resetToken, tempPassword);
            resetStorage.put(resetToken, resetInfo);
            
            // 실제 이메일 발송
            boolean emailSent = sendResetLinkEmail(email, userId, resetToken);
            
            if (emailSent) {
                System.out.println("비밀번호 재설정 링크 발송 성공: " + email + " -> " + userId);
                return true;
            } else {
                System.err.println("재설정 링크 이메일 발송 실패: " + email);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("비밀번호 재설정 링크 발송 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 비밀번호 재설정 링크 이메일 발송
     */
    private boolean sendResetLinkEmail(String email, String userId, String resetToken) {
        try {
            String resetLink = "http://localhost:8080/login/resetPassword?token=" + resetToken;
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[MH Community] 비밀번호 재설정 링크");
            message.setText(
                "안녕하세요. MH Community입니다.\n\n" +
                "비밀번호 재설정을 요청하셨습니다.\n\n" +
                "🆔 아이디: " + userId + "\n" +
                "📧 이메일: " + email + "\n\n" +
                "아래 링크를 클릭하여 새로운 비밀번호를 설정해주세요:\n\n" +
                "🔗 " + resetLink + "\n\n" +
                "⚠️ 보안 안내:\n" +
                "• 이 링크는 30분 후 만료됩니다\n" +
                "• 링크는 1회만 사용 가능합니다\n" +
                "• 본인이 요청하지 않았다면 이 이메일을 무시해주세요\n\n" +
                "감사합니다.\n" +
                "MH Community 팀"
            );
            message.setFrom("noreply@mhcommunity.com");
            
            mailSender.send(message);
            
            System.out.println("비밀번호 재설정 링크 이메일 발송 성공: " + email);
            return true;
            
        } catch (Exception e) {
            System.err.println("재설정 링크 이메일 발송 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 재설정 토큰 검증
     */
    public boolean validateResetToken(String token) {
        PasswordResetDTO resetInfo = resetStorage.get(token);
        
        if (resetInfo == null) {
            System.out.println("유효하지 않은 재설정 토큰: " + token);
            return false;
        }
        
        if (resetInfo.isExpired()) {
            System.out.println("만료된 재설정 토큰: " + token);
            resetStorage.remove(token);
            return false;
        }
        
        if (resetInfo.isUsed()) {
            System.out.println("이미 사용된 재설정 토큰: " + token);
            return false;
        }
        
        return true;
    }
    
    /**
     * 새 비밀번호 설정
     */
    public boolean resetPassword(String token, String newPassword) {
        if (!validateResetToken(token)) {
            return false;
        }
        
        try {
            PasswordResetDTO resetInfo = resetStorage.get(token);
            String email = resetInfo.getEmail();
            String userId = userService.findUserIdByEmail(email);
            
            if (userId == null) {
                return false;
            }
            
            // 데이터베이스에서 비밀번호 업데이트
            boolean updated = userService.updatePassword(userId, newPassword);
            
            if (updated) {
                // 토큰을 사용됨으로 표시
                resetInfo.setUsed(true);
                
                System.out.println("비밀번호 재설정 완료: " + userId);
                return true;
            } else {
                System.err.println("비밀번호 업데이트 실패: " + userId);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("비밀번호 재설정 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 재설정 토큰으로 이메일 정보 조회
     */
    public String getEmailByToken(String token) {
        PasswordResetDTO resetInfo = resetStorage.get(token);
        return resetInfo != null ? resetInfo.getEmail() : null;
    }
    
    /**
     * 만료된 재설정 정보들을 주기적으로 정리
     */
    public void cleanupExpiredResets() {
        resetStorage.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}