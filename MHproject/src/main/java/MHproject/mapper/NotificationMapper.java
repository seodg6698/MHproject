package MHproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import MHproject.DTO.NotificationDTO;
import MHproject.DTO.NotificationSettingDTO;

@Mapper
public interface NotificationMapper {
    
    /**
     * 알림 생성
     * @param notification 알림 정보
     * @return 삽입된 행 수
     */
    int insertNotification(NotificationDTO notification) throws Exception;
    
    /**
     * 사용자별 알림 목록 조회 (최신순, 페이징)
     * @param userId 사용자 ID
     * @param limit 조회 개수 제한
     * @param offset 시작 위치
     * @return 알림 목록
     */
    List<NotificationDTO> selectNotificationList(@Param("userId") String userId, 
                                               @Param("limit") int limit, 
                                               @Param("offset") int offset) throws Exception;
    
    /**
     * 사용자의 읽지 않은 알림 개수 조회
     * @param userId 사용자 ID
     * @return 읽지 않은 알림 개수
     */
    int countUnreadNotifications(@Param("userId") String userId) throws Exception;
    
    /**
     * 알림 읽음 처리
     * @param notificationIdx 알림 번호
     * @param userId 사용자 ID (권한 확인용)
     * @return 업데이트된 행 수
     */
    int markAsRead(@Param("notificationIdx") int notificationIdx, 
                   @Param("userId") String userId) throws Exception;
    
    /**
     * 모든 알림 읽음 처리
     * @param userId 사용자 ID
     * @return 업데이트된 행 수
     */
    int markAllAsRead(@Param("userId") String userId) throws Exception;
    
    /**
     * 알림 삭제 (soft delete)
     * @param notificationIdx 알림 번호
     * @param userId 사용자 ID (권한 확인용)
     * @return 업데이트된 행 수
     */
    int deleteNotification(@Param("notificationIdx") int notificationIdx, 
                          @Param("userId") String userId) throws Exception;
    
    /**
     * 특정 기간 이전의 읽은 알림 정리
     * @param days 일수 (예: 30일 이전)
     * @return 삭제된 행 수
     */
    int cleanupOldReadNotifications(@Param("days") int days) throws Exception;
    
    /**
     * 중복 알림 방지 - 동일한 게시글에 대한 최근 알림 확인
     * @param receiverId 수신자 ID
     * @param senderId 발신자 ID
     * @param boardIdx 게시글 번호
     * @param notificationType 알림 타입
     * @param minutes 분 단위 (예: 5분 이내)
     * @return 중복 알림 개수
     */
    int checkDuplicateNotification(@Param("receiverId") String receiverId,
                                 @Param("senderId") String senderId,
                                 @Param("boardIdx") int boardIdx,
                                 @Param("notificationType") String notificationType,
                                 @Param("minutes") int minutes) throws Exception;
    
    // ============== 알림 설정 관련 ==============
    
    /**
     * 사용자별 알림 설정 조회
     * @param userId 사용자 ID
     * @return 알림 설정 정보
     */
    NotificationSettingDTO selectNotificationSetting(@Param("userId") String userId) throws Exception;
    
    /**
     * 알림 설정 생성 (신규 사용자)
     * @param setting 알림 설정 정보
     * @return 삽입된 행 수
     */
    int insertNotificationSetting(NotificationSettingDTO setting) throws Exception;
    
    /**
     * 알림 설정 업데이트
     * @param setting 알림 설정 정보
     * @return 업데이트된 행 수
     */
    int updateNotificationSetting(NotificationSettingDTO setting) throws Exception;
    
    /**
     * 알림 설정 확인 (존재 여부)
     * @param userId 사용자 ID
     * @return 설정 존재 개수
     */
    int countNotificationSetting(@Param("userId") String userId) throws Exception;
    
    // ============== 통계 및 관리 ==============
    
    /**
     * 사용자별 총 알림 개수 조회
     * @param userId 사용자 ID
     * @return 총 알림 개수
     */
    int countTotalNotifications(@Param("userId") String userId) throws Exception;
    
    /**
     * 알림 타입별 통계
     * @param userId 사용자 ID
     * @return 타입별 알림 개수 맵
     */
    List<java.util.Map<String, Object>> getNotificationStats(@Param("userId") String userId) throws Exception;
}