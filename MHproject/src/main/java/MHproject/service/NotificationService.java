package MHproject.service;

import java.util.List;

import MHproject.DTO.NotificationDTO;
import MHproject.DTO.NotificationSettingDTO;

public interface NotificationService {
    
    /**
     * 좋아요 알림 생성
     * @param boardIdx 게시글 번호
     * @param senderId 좋아요를 누른 사용자 ID
     * @throws Exception
     */
    void createLikeNotification(int boardIdx, String senderId) throws Exception;
    
    /**
     * 댓글 알림 생성
     * @param boardIdx 게시글 번호
     * @param commentIdx 댓글 번호
     * @param senderId 댓글 작성자 ID
     * @throws Exception
     */
    void createCommentNotification(int boardIdx, int commentIdx, String senderId) throws Exception;
    
    /**
     * 답글 알림 생성
     * @param boardIdx 게시글 번호
     * @param commentIdx 댓글 번호
     * @param parentCommentIdx 부모 댓글 번호
     * @param senderId 답글 작성자 ID
     * @throws Exception
     */
    void createReplyNotification(int boardIdx, int commentIdx, int parentCommentIdx, String senderId) throws Exception;
    
    /**
     * 사용자별 알림 목록 조회
     * @param userId 사용자 ID
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @return 알림 목록
     * @throws Exception
     */
    List<NotificationDTO> getNotificationList(String userId, int page, int size) throws Exception;
    
    /**
     * 읽지 않은 알림 개수 조회
     * @param userId 사용자 ID
     * @return 읽지 않은 알림 개수
     * @throws Exception
     */
    int getUnreadNotificationCount(String userId) throws Exception;
    
    /**
     * 알림 읽음 처리
     * @param notificationIdx 알림 번호
     * @param userId 사용자 ID
     * @return 성공 여부
     * @throws Exception
     */
    boolean markAsRead(int notificationIdx, String userId) throws Exception;
    
    /**
     * 모든 알림 읽음 처리
     * @param userId 사용자 ID
     * @return 처리된 알림 개수
     * @throws Exception
     */
    int markAllAsRead(String userId) throws Exception;
    
    /**
     * 알림 삭제
     * @param notificationIdx 알림 번호
     * @param userId 사용자 ID
     * @return 성공 여부
     * @throws Exception
     */
    boolean deleteNotification(int notificationIdx, String userId) throws Exception;
    
    /**
     * 사용자 알림 설정 조회
     * @param userId 사용자 ID
     * @return 알림 설정 정보
     * @throws Exception
     */
    NotificationSettingDTO getNotificationSetting(String userId) throws Exception;
    
    /**
     * 사용자 알림 설정 업데이트
     * @param setting 알림 설정 정보
     * @return 성공 여부
     * @throws Exception
     */
    boolean updateNotificationSetting(NotificationSettingDTO setting) throws Exception;
}
