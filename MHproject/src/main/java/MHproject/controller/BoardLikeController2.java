package MHproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import MHproject.service.BoardLikeService1;
import MHproject.service.BoardLikeService2;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/like2")
public class BoardLikeController2 {
    
    private static final Logger logger = LoggerFactory.getLogger(BoardLikeController2.class);
    
    @Autowired
    private BoardLikeService2 boardLikeService;
    
    /**
     * 좋아요 토글 (추가/취소)
     * @param boardIdx 게시글 번호
     * @param session HTTP 세션
     * @return JSON 응답 (성공 여부, 좋아요 상태, 총 좋아요 수)
     */
    @PostMapping("/toggle2")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleLike(
            @RequestParam("boardIdx") int boardIdx,
            HttpSession session) {
        
        logger.info("좋아요 토글 요청 - boardIdx: {}", boardIdx);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 세션에서 사용자 정보 확인
            Object userObj = session.getAttribute("user");
            if (userObj == null) {
                logger.warn("좋아요 토글 실패 - 로그인되지 않은 사용자");
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                response.put("needLogin", true);
                return ResponseEntity.ok(response);
            }
            
            // 사용자 ID 추출 (User 객체의 구조에 따라 수정 필요)
            String userId = getUserIdFromSession(userObj);
            if (userId == null || userId.trim().isEmpty()) {
                logger.warn("좋아요 토글 실패 - 사용자 ID를 찾을 수 없음");
                response.put("success", false);
                response.put("message", "사용자 정보를 확인할 수 없습니다.");
                return ResponseEntity.ok(response);
            }
            
            logger.debug("좋아요 토글 처리 - boardIdx: {}, userId: {}", boardIdx, userId);
            
            // 좋아요 토글 실행
            boolean isLiked = boardLikeService.toggleLike(boardIdx, userId);
            
            // 업데이트된 좋아요 개수 조회
            int likeCount = boardLikeService.getLikeCount(boardIdx);
            
            response.put("success", true);
            response.put("isLiked", isLiked);
            response.put("likeCount", likeCount);
            response.put("message", isLiked ? "좋아요를 눌렀습니다." : "좋아요를 취소했습니다.");
            
            logger.info("좋아요 토글 성공 - boardIdx: {}, userId: {}, 상태: {}, 총 좋아요: {}", 
                       boardIdx, userId, isLiked ? "추가" : "취소", likeCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("좋아요 토글 중 오류 발생 - boardIdx: {}, 오류: {}", 
                        boardIdx, e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "좋아요 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 세션 객체에서 사용자 ID 추출
     * User 객체의 실제 구조에 맞게 수정 필요
     * @param userObj 세션의 user 객체
     * @return 사용자 ID
     */
    private String getUserIdFromSession(Object userObj) {
        try {
            // User 객체의 실제 구조에 따라 수정 필요
            // 예시: user 객체에 userid 필드가 있는 경우
            if (userObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> userMap = (Map<String, Object>) userObj;
                return (String) userMap.get("userid");
            }
            
            // User 클래스가 있는 경우 reflection 사용 예시
            java.lang.reflect.Method getUserIdMethod = userObj.getClass().getMethod("getUserid");
            return (String) getUserIdMethod.invoke(userObj);
            
        } catch (Exception e) {
            logger.error("사용자 ID 추출 중 오류 발생: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 게시글의 좋아요 개수 조회
     * @param boardIdx 게시글 번호
     * @return JSON 응답 (좋아요 개수)
     */
    @PostMapping("/count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getLikeCount(@RequestParam("boardIdx") int boardIdx) {
        
        logger.debug("좋아요 개수 조회 요청 - boardIdx: {}", boardIdx);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int likeCount = boardLikeService.getLikeCount(boardIdx);
            
            response.put("success", true);
            response.put("likeCount", likeCount);
            
            logger.debug("좋아요 개수 조회 성공 - boardIdx: {}, 개수: {}", boardIdx, likeCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("좋아요 개수 조회 중 오류 발생 - boardIdx: {}, 오류: {}", 
                        boardIdx, e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "좋아요 개수 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 사용자의 좋아요 여부 확인
     * @param boardIdx 게시글 번호
     * @param session HTTP 세션
     * @return JSON 응답 (좋아요 여부)
     */
    @PostMapping("/check")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkUserLike(
            @RequestParam("boardIdx") int boardIdx,
            HttpSession session) {
        
        logger.debug("사용자 좋아요 여부 확인 요청 - boardIdx: {}", boardIdx);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Object userObj = session.getAttribute("user");
            boolean isLiked = false;
            
            if (userObj != null) {
                String userId = getUserIdFromSession(userObj);
                if (userId != null && !userId.trim().isEmpty()) {
                    isLiked = boardLikeService.isUserLiked(boardIdx, userId);
                }
            }
            
            response.put("success", true);
            response.put("isLiked", isLiked);
            
            logger.debug("사용자 좋아요 여부 확인 완료 - boardIdx: {}, 좋아요 여부: {}", 
                        boardIdx, isLiked);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("사용자 좋아요 여부 확인 중 오류 발생 - boardIdx: {}, 오류: {}", 
                        boardIdx, e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "좋아요 상태 확인 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
}