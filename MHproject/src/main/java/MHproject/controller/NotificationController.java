package MHproject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import MHproject.DTO.NotificationDTO;
import MHproject.DTO.NotificationSettingDTO;
import MHproject.service.NotificationService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/notification")
public class NotificationController {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * 세션에서 사용자 ID 추출하는 헬퍼 메소드
     */
    private String getUserIdFromSession(HttpSession session) {
        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return null;
        }
        
        try {
            // User 객체의 실제 구조에 따라 수정 필요
            if (userObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> userMap = (Map<String, Object>) userObj;
                return (String) userMap.get("userid");
            }
            
            // User 클래스가 있는 경우 reflection 사용
            java.lang.reflect.Method getUserIdMethod = userObj.getClass().getMethod("getUserid");
            return (String) getUserIdMethod.invoke(userObj);
            
        } catch (Exception e) {
            logger.error("사용자 ID 추출 중 오류 발생: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 알림 목록 조회 (AJAX)
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param session HTTP 세션
     * @return JSON 응답 (알림 목록)
     */
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getNotificationList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            HttpSession session) {
        
        logger.debug("알림 목록 조회 요청 - page: {}, size: {}", page, size);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 세션에서 사용자 ID 추출
            String userId = getUserIdFromSession(session);
            if (userId == null) {
                logger.warn("알림 목록 조회 실패 - 로그인되지 않은 사용자");
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                response.put("needLogin", true);
                return ResponseEntity.ok(response);
            }
            
            // 알림 목록 조회
            List<NotificationDTO> notifications = notificationService.getNotificationList(userId, page, size);
            
            // 읽지 않은 알림 개수 조회
            int unreadCount = notificationService.getUnreadNotificationCount(userId);
            
            response.put("success", true);
            response.put("notifications", notifications);
            response.put("unreadCount", unreadCount);
            response.put("currentPage", page);
            response.put("pageSize", size);
            
            logger.debug("알림 목록 조회 성공 - userId: {}, 알림 수: {}, 읽지 않은 알림: {}", 
                        userId, notifications.size(), unreadCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("알림 목록 조회 중 오류 발생 - 오류: {}", e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "알림 목록을 불러오는 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 읽지 않은 알림 개수 조회 (AJAX)
     * @param session HTTP 세션
     * @return JSON 응답 (읽지 않은 알림 개수)
     */
    @GetMapping("/unread-count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUnreadCount(HttpSession session) {
        
        logger.debug("읽지 않은 알림 개수 조회 요청");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String userId = getUserIdFromSession(session);
            if (userId == null) {
                response.put("success", true);
                response.put("unreadCount", 0);
                return ResponseEntity.ok(response);
            }
            
            int unreadCount = notificationService.getUnreadNotificationCount(userId);
            
            response.put("success", true);
            response.put("unreadCount", unreadCount);
            
            logger.debug("읽지 않은 알림 개수 조회 성공 - userId: {}, count: {}", userId, unreadCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("읽지 않은 알림 개수 조회 중 오류 발생 - 오류: {}", e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "알림 개수 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 알림 읽음 처리 (AJAX)
     * @param notificationIdx 알림 번호
     * @param session HTTP 세션
     * @return JSON 응답 (성공 여부)
     */
    @PostMapping("/mark-read")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> markAsRead(
            @RequestParam("notificationIdx") int notificationIdx,
            HttpSession session) {
        
        logger.debug("알림 읽음 처리 요청 - notificationIdx: {}", notificationIdx);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String userId = getUserIdFromSession(session);
            if (userId == null) {
                logger.warn("알림 읽음 처리 실패 - 로그인되지 않은 사용자");
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                response.put("needLogin", true);
                return ResponseEntity.ok(response);
            }
            
            boolean success = notificationService.markAsRead(notificationIdx, userId);
            
            if (success) {
                // 업데이트된 읽지 않은 알림 개수 조회
                int unreadCount = notificationService.getUnreadNotificationCount(userId);
                
                response.put("success", true);
                response.put("message", "알림을 읽음으로 처리했습니다.");
                response.put("unreadCount", unreadCount);
                
                logger.debug("알림 읽음 처리 성공 - notificationIdx: {}, userId: {}, 남은 읽지 않은 알림: {}", 
                            notificationIdx, userId, unreadCount);
            } else {
                response.put("success", false);
                response.put("message", "알림을 찾을 수 없습니다.");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("알림 읽음 처리 중 오류 발생 - notificationIdx: {}, 오류: {}", 
                        notificationIdx, e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "알림 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 모든 알림 읽음 처리 (AJAX)
     * @param session HTTP 세션
     * @return JSON 응답 (처리된 알림 개수)
     */
    @PostMapping("/mark-all-read")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> markAllAsRead(HttpSession session) {
        
        logger.debug("모든 알림 읽음 처리 요청");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String userId = getUserIdFromSession(session);
            if (userId == null) {
                logger.warn("모든 알림 읽음 처리 실패 - 로그인되지 않은 사용자");
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                response.put("needLogin", true);
                return ResponseEntity.ok(response);
            }
            
            int processedCount = notificationService.markAllAsRead(userId);
            
            response.put("success", true);
            response.put("message", processedCount + "개의 알림을 읽음으로 처리했습니다.");
            response.put("processedCount", processedCount);
            response.put("unreadCount", 0);
            
            logger.info("모든 알림 읽음 처리 성공 - userId: {}, 처리된 알림 수: {}", userId, processedCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("모든 알림 읽음 처리 중 오류 발생 - 오류: {}", e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "알림 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 알림 삭제 (AJAX)
     * @param notificationIdx 알림 번호
     * @param session HTTP 세션
     * @return JSON 응답 (성공 여부)
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteNotification(
            @RequestParam("notificationIdx") int notificationIdx,
            HttpSession session) {
        
        logger.debug("알림 삭제 요청 - notificationIdx: {}", notificationIdx);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String userId = getUserIdFromSession(session);
            if (userId == null) {
                logger.warn("알림 삭제 실패 - 로그인되지 않은 사용자");
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                response.put("needLogin", true);
                return ResponseEntity.ok(response);
            }
            
            boolean success = notificationService.deleteNotification(notificationIdx, userId);
            
            if (success) {
                // 업데이트된 읽지 않은 알림 개수 조회
                int unreadCount = notificationService.getUnreadNotificationCount(userId);
                
                response.put("success", true);
                response.put("message", "알림이 삭제되었습니다.");
                response.put("unreadCount", unreadCount);
                
                logger.debug("알림 삭제 성공 - notificationIdx: {}, userId: {}, 남은 읽지 않은 알림: {}", 
                            notificationIdx, userId, unreadCount);
            } else {
                response.put("success", false);
                response.put("message", "알림을 찾을 수 없습니다.");
                
                logger.warn("알림 삭제 실패 - 알림을 찾을 수 없음: notificationIdx: {}, userId: {}", 
                           notificationIdx, userId);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("알림 삭제 중 오류 발생 - notificationIdx: {}, userId: {}, 오류: {}", 
                        notificationIdx, getUserIdFromSession(session), e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "알림 삭제 중 오류가 발생했습니다.");
            response.put("errorCode", "DELETE_ERROR");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 알림 클릭 시 게시글로 이동 및 읽음 처리
     * @param notificationIdx 알림 번호
     * @param session HTTP 세션
     * @return 게시글 상세 페이지로 리다이렉트
     */
    @GetMapping("/goto")
    public String gotoNotificationTarget(
            @RequestParam("notificationIdx") int notificationIdx,
            HttpSession session) {
        
        logger.debug("알림 클릭하여 게시글 이동 - notificationIdx: {}", notificationIdx);
        
        try {
            String userId = getUserIdFromSession(session);
            if (userId == null) {
                logger.warn("알림 이동 실패 - 로그인되지 않은 사용자");
                return "redirect:/login/login";
            }
            
            // 알림 목록에서 해당 알림 정보 찾기
            List<NotificationDTO> notifications = notificationService.getNotificationList(userId, 0, 100);
            NotificationDTO targetNotification = null;
            
            for (NotificationDTO notification : notifications) {
                if (notification.getNotificationIdx() == notificationIdx) {
                    targetNotification = notification;
                    break;
                }
            }
            
            if (targetNotification == null) {
                logger.warn("알림을 찾을 수 없음 - notificationIdx: {}", notificationIdx);
                return "redirect:/board1/openBoardList1";
            }
            
            // 알림 읽음 처리
            notificationService.markAsRead(notificationIdx, userId);
            
            // 게시글 상세 페이지로 이동
            int boardIdx = targetNotification.getBoardIdx();
            String redirectUrl = "redirect:/board1/openBoardDetail1?boardIdx=" + boardIdx;
            
            logger.info("알림 클릭 처리 완료 - notificationIdx: {}, userId: {}, boardIdx: {}", 
                       notificationIdx, userId, boardIdx);
            
            return redirectUrl;
            
        } catch (Exception e) {
            logger.error("알림 클릭 처리 중 오류 발생 - notificationIdx: {}, 오류: {}", 
                        notificationIdx, e.getMessage(), e);
            return "redirect:/user/main";
        }
    }
    
    /**
     * 알림 설정 페이지
     * @param session HTTP 세션
     * @param model Spring Model
     * @return 알림 설정 페이지
     */
    @GetMapping("/settings")
    public String notificationSettings(HttpSession session, 
                                     org.springframework.ui.Model model) {
        
        logger.debug("알림 설정 페이지 요청");
        
        try {
            String userId = getUserIdFromSession(session);
            if (userId == null) {
                logger.warn("알림 설정 페이지 접근 실패 - 로그인되지 않은 사용자");
                return "redirect:/login/login";
            }
            
            // 현재 알림 설정 조회
            NotificationSettingDTO setting = notificationService.getNotificationSetting(userId);
            model.addAttribute("setting", setting);
            
            logger.debug("알림 설정 페이지 로드 - userId: {}", userId);
            
            return "/user/NotificationSettings";
            
        } catch (Exception e) {
            logger.error("알림 설정 페이지 로드 중 오류 발생 - 오류: {}", e.getMessage(), e);
            return "redirect:/user/main";
        }
    }
    
    /**
     * 알림 설정 업데이트 (AJAX)
     * @param likeNotification 좋아요 알림 수신 여부
     * @param commentNotification 댓글 알림 수신 여부
     * @param replyNotification 답글 알림 수신 여부
     * @param emailNotification 이메일 알림 수신 여부
     * @param session HTTP 세션
     * @return JSON 응답 (성공 여부)
     */
    @PostMapping("/settings/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateNotificationSettings(
            @RequestParam(value = "likeNotification", defaultValue = "false") boolean likeNotification,
            @RequestParam(value = "commentNotification", defaultValue = "false") boolean commentNotification,
            @RequestParam(value = "replyNotification", defaultValue = "false") boolean replyNotification,
            @RequestParam(value = "emailNotification", defaultValue = "false") boolean emailNotification,
            HttpSession session) {
        
        logger.debug("알림 설정 업데이트 요청 - like: {}, comment: {}, reply: {}, email: {}", 
                    likeNotification, commentNotification, replyNotification, emailNotification);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String userId = getUserIdFromSession(session);
            if (userId == null) {
                logger.warn("알림 설정 업데이트 실패 - 로그인되지 않은 사용자");
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                response.put("needLogin", true);
                return ResponseEntity.ok(response);
            }
            
            // 알림 설정 업데이트
            NotificationSettingDTO setting = new NotificationSettingDTO();
            setting.setUserId(userId);
            setting.setLikeNotification(likeNotification);
            setting.setCommentNotification(commentNotification);
            setting.setReplyNotification(replyNotification);
            setting.setEmailNotification(emailNotification);
            
            boolean success = notificationService.updateNotificationSetting(setting);
            
            if (success) {
                response.put("success", true);
                response.put("message", "알림 설정이 저장되었습니다.");
                
                logger.info("알림 설정 업데이트 성공 - userId: {}", userId);
            } else {
                response.put("success", false);
                response.put("message", "알림 설정 저장에 실패했습니다.");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("알림 설정 업데이트 중 오류 발생 - 오류: {}", e.getMessage(), e);
            
            response.put("success", false);
            response.put("message", "알림 설정 저장 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
}