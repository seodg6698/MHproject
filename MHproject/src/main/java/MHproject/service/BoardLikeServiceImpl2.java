package MHproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import MHproject.DTO.BoardLikeDTO1;
import MHproject.mapper.BoardLikeMapper1;
import MHproject.mapper.BoardLikeMapper2;

@Service
public class BoardLikeServiceImpl2 implements BoardLikeService2 {
    
    private static final Logger logger = LoggerFactory.getLogger(BoardLikeServiceImpl2.class);
    
    @Autowired
    private BoardLikeMapper2 boardLikeMapper;
    
    @Autowired
    @Qualifier("notificationServiceImpl")  // 구체적인 Bean 이름 지정
    private NotificationService notificationService;
    
    @Override
    @Transactional
    public boolean toggleLike(int boardIdx, String userId) throws Exception {
        logger.info("좋아요 토글 요청 - boardIdx: {}, userId: {}", boardIdx, userId);
        
        try {
            // 현재 좋아요 상태 확인
            int currentLikeStatus = boardLikeMapper.checkUserLike(boardIdx, userId);
            boolean isLiked;
            
            if (currentLikeStatus > 0) {
                // 이미 좋아요한 상태 -> 좋아요 취소
                boardLikeMapper.deleteLike(boardIdx, userId);
                isLiked = false;
                logger.info("좋아요 취소 완료 - boardIdx: {}, userId: {}", boardIdx, userId);
            } else {
                // 좋아요하지 않은 상태 -> 좋아요 추가
                BoardLikeDTO1 boardLike = new BoardLikeDTO1(boardIdx, userId);
                boardLikeMapper.insertLike(boardLike);
                isLiked = true;
                logger.info("좋아요 추가 완료 - boardIdx: {}, userId: {}", boardIdx, userId);
                
                // 좋아요 추가 시에만 알림 생성
                try {
                    notificationService.createLikeNotification(boardIdx, userId);
                    logger.debug("좋아요 알림 생성 요청 완료 - boardIdx: {}, senderId: {}", boardIdx, userId);
                } catch (Exception e) {
                    logger.warn("좋아요 알림 생성 실패 - boardIdx: {}, senderId: {}, 오류: {}", 
                               boardIdx, userId, e.getMessage());
                    // 알림 생성 실패해도 좋아요 기능은 정상 동작
                }
            }
            
            // 게시글의 좋아요 개수 업데이트
            int totalLikeCount = boardLikeMapper.getLikeCount(boardIdx);
            boardLikeMapper.updateBoardLikeCount(boardIdx, totalLikeCount);
            
            logger.info("게시글 좋아요 개수 업데이트 완료 - boardIdx: {}, 총 좋아요: {}", 
                       boardIdx, totalLikeCount);
            
            return isLiked;
            
        } catch (Exception e) {
            logger.error("좋아요 토글 중 오류 발생 - boardIdx: {}, userId: {}, 오류: {}", 
                        boardIdx, userId, e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public int getLikeCount(int boardIdx) throws Exception {
        logger.debug("좋아요 개수 조회 - boardIdx: {}", boardIdx);
        
        try {
            int likeCount = boardLikeMapper.getLikeCount(boardIdx);
            logger.debug("좋아요 개수 조회 결과 - boardIdx: {}, 좋아요 수: {}", boardIdx, likeCount);
            return likeCount;
        } catch (Exception e) {
            logger.error("좋아요 개수 조회 중 오류 발생 - boardIdx: {}, 오류: {}", 
                        boardIdx, e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public boolean isUserLiked(int boardIdx, String userId) throws Exception {
        logger.debug("사용자 좋아요 여부 확인 - boardIdx: {}, userId: {}", boardIdx, userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            logger.debug("사용자가 로그인하지 않음 - 좋아요 여부: false");
            return false;
        }
        
        try {
            int likeStatus = boardLikeMapper.checkUserLike(boardIdx, userId);
            boolean isLiked = likeStatus > 0;
            logger.debug("사용자 좋아요 여부 확인 결과 - boardIdx: {}, userId: {}, 좋아요 여부: {}", 
                        boardIdx, userId, isLiked);
            return isLiked;
        } catch (Exception e) {
            logger.error("사용자 좋아요 여부 확인 중 오류 발생 - boardIdx: {}, userId: {}, 오류: {}", 
                        boardIdx, userId, e.getMessage(), e);
            throw e;
        }
    }
}