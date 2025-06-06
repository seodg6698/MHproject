package MHproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import MHproject.service.EmailVerificationService;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    
    @Autowired
    private EmailVerificationService emailVerificationService;
    
    /**
     * 매 10분마다 만료된 인증 정보들을 정리
     */
    @Scheduled(fixedRate = 600000) // 10분 = 600,000ms
    public void cleanupExpiredVerifications() {
        emailVerificationService.cleanupExpiredVerifications();
        System.out.println("만료된 이메일 인증 정보 정리 완료");
    }
}