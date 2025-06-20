package MHproject.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import MHproject.DTO.FreeBoardDTO;
import MHproject.DTO.NotificationDTO;
import MHproject.DTO.NotificationDTO.NotificationType;
import MHproject.DTO.NotificationSettingDTO;
import MHproject.DTO.QnABoardDTO;
import MHproject.controller.FreeBoardController;
import MHproject.controller.QnABoardController;
import MHproject.mapper.FreeBoardMapper;
import MHproject.mapper.NotificationMapper;
import MHproject.mapper.QnABoardMapper;


@Service
class NotificationServiceImpl implements NotificationService{
	
	@Autowired
	private NotificationMapper notificationMapper;

	@Autowired
	private FreeBoardMapper freeBoardMapper;
	
	@Autowired
	private QnABoardMapper qnaBoardMapper;
	
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

@Override
@Async
public void createLikeNotification(int boardIdx, String senderId) throws Exception {
    logger.info("좋아요 알림 생성 시작 - boardIdx: {}, senderId: {}", boardIdx, senderId);
    
    try {
        // 게시글 정보 조회 - 게시글 타입에 따라 적절한 mapper 사용
        String receiverId = getBoardCreatorId(boardIdx);
        if (receiverId == null) {
            logger.warn("게시글을 찾을 수 없음 - boardIdx: {}", boardIdx);
            return;
        }
        
        String boardTitle = getBoardTitle(boardIdx);
        
        // 자기 자신에게는 알림을 보내지 않음
        if (senderId.equals(receiverId)) {
            logger.debug("자기 자신의 게시글 좋아요 - 알림 생성 건너뜀");
            return;
        }
        
        // 알림 설정 확인
        NotificationSettingDTO setting = getNotificationSetting(receiverId);
        if (!setting.isLikeNotification()) {
            logger.debug("사용자가 좋아요 알림을 비활성화함 - receiverId: {}", receiverId);
            return;
        }
        
        // 중복 알림 방지 (5분 이내 동일한 좋아요 알림)
        int duplicateCount = notificationMapper.checkDuplicateNotification(
            receiverId, senderId, boardIdx, NotificationType.LIKE.name(), 5);
        
        if (duplicateCount > 0) {
            logger.debug("중복 좋아요 알림 방지 - 5분 이내 동일한 알림 존재");
            return;
        }
        
        // 알림 생성
        String title = "게시글에 좋아요를 받았습니다";
        String content = senderId + "님이 회원님의 게시글 '" + 
                       truncateTitle(boardTitle) + "'을 좋아합니다.";
        
        NotificationDTO notification = new NotificationDTO(
            receiverId, senderId, boardIdx, NotificationType.LIKE, title, content);
        
        notificationMapper.insertNotification(notification);
        
        logger.info("좋아요 알림 생성 완료 - receiverId: {}, senderId: {}, boardIdx: {}", 
                   receiverId, senderId, boardIdx);
        
    } catch (Exception e) {
        logger.error("좋아요 알림 생성 중 오류 발생 - boardIdx: {}, senderId: {}, 오류: {}", 
                    boardIdx, senderId, e.getMessage(), e);
        throw e;
    }
}

/**
 * 게시글 제목 길이 제한 (헬퍼 메소드)
 */
private String truncateTitle(String title) {
    if (title == null) return "";
    return title.length() > 20 ? title.substring(0, 20) + "..." : title;
}

@Override
@Async
public void createCommentNotification(int boardIdx, int commentIdx, String senderId) throws Exception {
    logger.info("댓글 알림 생성 시작 - boardIdx: {}, commentIdx: {}, senderId: {}", 
               boardIdx, commentIdx, senderId);
    
    try {
        // 게시글 정보 조회
        String receiverId = getBoardCreatorId(boardIdx);
        if (receiverId == null) {
            logger.warn("게시글을 찾을 수 없음 - boardIdx: {}", boardIdx);
            return;
        }
        
        String boardTitle = getBoardTitle(boardIdx);
        
        // 자기 자신에게는 알림을 보내지 않음
        if (senderId.equals(receiverId)) {
            logger.debug("자기 자신의 게시글 댓글 - 알림 생성 건너뜀");
            return;
        }
        
        // 알림 설정 확인
        NotificationSettingDTO setting = getNotificationSetting(receiverId);
        if (!setting.isCommentNotification()) {
            logger.debug("사용자가 댓글 알림을 비활성화함 - receiverId: {}", receiverId);
            return;
        }
        
        // 중복 알림 방지 (3분 이내 동일한 댓글 알림)
        int duplicateCount = notificationMapper.checkDuplicateNotification(
            receiverId, senderId, boardIdx, NotificationType.COMMENT.name(), 3);
        
        if (duplicateCount > 0) {
            logger.debug("중복 댓글 알림 방지 - 3분 이내 동일한 알림 존재");
            return;
        }
        
        // 알림 생성
        String title = "게시글에 새 댓글이 달렸습니다";
        String content = senderId + "님이 회원님의 게시글 '" + 
                       truncateTitle(boardTitle) + "'에 댓글을 남겼습니다.";
        
        NotificationDTO notification = new NotificationDTO(
            receiverId, senderId, boardIdx, commentIdx, NotificationType.COMMENT, title, content);
        
        notificationMapper.insertNotification(notification);
        
        logger.info("댓글 알림 생성 완료 - receiverId: {}, senderId: {}, boardIdx: {}, commentIdx: {}", 
                   receiverId, senderId, boardIdx, commentIdx);
        
    } catch (Exception e) {
        logger.error("댓글 알림 생성 중 오류 발생 - boardIdx: {}, commentIdx: {}, senderId: {}, 오류: {}", 
                    boardIdx, commentIdx, senderId, e.getMessage(), e);
        throw e;
    }
}

@Override
@Async
public void createReplyNotification(int boardIdx, int commentIdx, int parentCommentIdx, String senderId) throws Exception {
    logger.info("답글 알림 생성 시작 - boardIdx: {}, commentIdx: {}, parentCommentIdx: {}, senderId: {}", 
               boardIdx, commentIdx, parentCommentIdx, senderId);
    
    try {
        // TODO: 부모 댓글 작성자 ID 조회 로직 구현 필요
        // 현재는 임시로 게시글 작성자에게 알림
        String receiverId = getBoardCreatorId(boardIdx);
        if (receiverId == null) {
            logger.warn("게시글을 찾을 수 없음 - boardIdx: {}", boardIdx);
            return;
        }
        
        // 자기 자신에게는 알림을 보내지 않음
        if (senderId.equals(receiverId)) {
            logger.debug("자기 자신의 댓글 답글 - 알림 생성 건너뜀");
            return;
        }
        
        // 알림 설정 확인
        NotificationSettingDTO setting = getOrCreateNotificationSetting(receiverId);
        if (!setting.isReplyNotification()) {
            logger.debug("사용자가 답글 알림을 비활성화함 - receiverId: {}", receiverId);
            return;
        }
        
        // 중복 알림 방지 (3분 이내 동일한 답글 알림)
        int duplicateCount = notificationMapper.checkDuplicateNotification(
            receiverId, senderId, boardIdx, NotificationType.REPLY.name(), 3);
        
        if (duplicateCount > 0) {
            logger.debug("중복 답글 알림 방지 - 3분 이내 동일한 알림 존재");
            return;
        }
        
        // 알림 생성
        String title = "댓글에 새 답글이 달렸습니다";
        String content = senderId + "님이 회원님의 댓글에 답글을 남겼습니다.";
        
        NotificationDTO notification = new NotificationDTO(
            receiverId, senderId, boardIdx, commentIdx, NotificationType.REPLY, title, content);
        
        notificationMapper.insertNotification(notification);
        
        logger.info("답글 알림 생성 완료 - receiverId: {}, senderId: {}, boardIdx: {}, commentIdx: {}", 
                   receiverId, senderId, boardIdx, commentIdx);
        
    } catch (Exception e) {
        logger.error("답글 알림 생성 중 오류 발생 - boardIdx: {}, commentIdx: {}, senderId: {}, 오류: {}", 
                    boardIdx, commentIdx, senderId, e.getMessage(), e);
        throw e;
    }
}

/**
 * 게시글 작성자 ID 조회 (헬퍼 메소드)
 */
private String getBoardCreatorId(int boardIdx) throws Exception {
    // FreeBoardDTO 먼저 시도
    FreeBoardDTO freeBoard = freeBoardMapper.selectBoardDetail(boardIdx);
    if (freeBoard != null) {
        return freeBoard.getCreatorId();
    }
    
    // QnABoardDTO 시도
    QnABoardDTO qnaBoard = qnaBoardMapper.selectBoardDetail(boardIdx);
    if (qnaBoard != null) {
        return qnaBoard.getCreatorId();
    }
    
    return null;
}

/**
 * 게시글 제목 조회 (헬퍼 메소드)
 */
private String getBoardTitle(int boardIdx) throws Exception {
    // FreeBoardDTO 먼저 시도
    FreeBoardDTO freeBoard = freeBoardMapper.selectBoardDetail(boardIdx);
    if (freeBoard != null) {
        return freeBoard.getTitle();
    }
    
    // QnABoardDTO 시도
    QnABoardDTO qnaBoard = qnaBoardMapper.selectBoardDetail(boardIdx);
    if (qnaBoard != null) {
        return qnaBoard.getTitle();
    }
    
    return "제목 없음";
}

@Override
public List<NotificationDTO> getNotificationList(String userId, int page, int size) throws Exception {
    logger.debug("알림 목록 조회 - userId: {}, page: {}, size: {}", userId, page, size);
    
    int offset = page * size;
    List<NotificationDTO> notifications = notificationMapper.selectNotificationList(userId, size, offset);
    
    logger.debug("알림 목록 조회 완료 - userId: {}, 조회된 알림 수: {}", userId, notifications.size());
    
    return notifications;
}

@Override
public int getUnreadNotificationCount(String userId) throws Exception {
    logger.debug("읽지 않은 알림 개수 조회 - userId: {}", userId);
    
    int count = notificationMapper.countUnreadNotifications(userId);
    
    logger.debug("읽지 않은 알림 개수 - userId: {}, count: {}", userId, count);
    
    return count;
}

@Override
@Transactional
public boolean markAsRead(int notificationIdx, String userId) throws Exception {
    logger.debug("알림 읽음 처리 - notificationIdx: {}, userId: {}", notificationIdx, userId);
    
    int result = notificationMapper.markAsRead(notificationIdx, userId);
    boolean success = result > 0;
    
    logger.debug("알림 읽음 처리 결과 - notificationIdx: {}, userId: {}, success: {}", 
                notificationIdx, userId, success);
    
    return success;
}

@Override
@Transactional
public int markAllAsRead(String userId) throws Exception {
    logger.info("모든 알림 읽음 처리 - userId: {}", userId);
    
    int count = notificationMapper.markAllAsRead(userId);
    
    logger.info("모든 알림 읽음 처리 완료 - userId: {}, 처리된 알림 수: {}", userId, count);
    
    return count;
}

@Override
@Transactional
public boolean deleteNotification(int notificationIdx, String userId) throws Exception {
    logger.debug("알림 삭제 - notificationIdx: {}, userId: {}", notificationIdx, userId);
    
    int result = notificationMapper.deleteNotification(notificationIdx, userId);
    boolean success = result > 0;
    
    logger.debug("알림 삭제 결과 - notificationIdx: {}, userId: {}, success: {}", 
                notificationIdx, userId, success);
    
    return success;
}

@Override
public NotificationSettingDTO getNotificationSetting(String userId) throws Exception {
    logger.debug("알림 설정 조회 - userId: {}", userId);
    
    NotificationSettingDTO setting = notificationMapper.selectNotificationSetting(userId);
    
    // 설정이 없으면 기본 설정 생성
    if (setting == null) {
        setting = getOrCreateNotificationSetting(userId);
    }
    
    logger.debug("알림 설정 조회 완료 - userId: {}", userId);
    
    return setting;
}

@Override
@Transactional
public boolean updateNotificationSetting(NotificationSettingDTO setting) throws Exception {
    logger.info("알림 설정 업데이트 - userId: {}", setting.getUserId());
    
    // 기존 설정 존재 여부 확인
    int existingCount = notificationMapper.countNotificationSetting(setting.getUserId());
    
    int result;
    if (existingCount > 0) {
        result = notificationMapper.updateNotificationSetting(setting);
    } else {
        result = notificationMapper.insertNotificationSetting(setting);
    }
    
    boolean success = result > 0;
    
    logger.info("알림 설정 업데이트 완료 - userId: {}, success: {}", setting.getUserId(), success);
    
    return success;
}

/**
 * 알림 설정 조회 또는 생성 (헬퍼 메소드)
 */
private NotificationSettingDTO getOrCreateNotificationSetting(String userId) throws Exception {
    NotificationSettingDTO setting = notificationMapper.selectNotificationSetting(userId);
    
    if (setting == null) {
        // 기본 설정으로 생성
        setting = new NotificationSettingDTO(userId);
        notificationMapper.insertNotificationSetting(setting);
        logger.info("새로운 알림 설정 생성 - userId: {}", userId);
    }
    
    return setting;
}

}