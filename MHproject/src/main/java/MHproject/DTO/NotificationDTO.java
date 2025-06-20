package MHproject.DTO;

import java.time.LocalDateTime;

public class NotificationDTO {
    
    private int notificationIdx;
    private String receiverId;
    private String senderId;
    private int boardIdx;
    private Integer commentIdx; // 댓글 알림이 아닌 경우 null
    private NotificationType notificationType;
    private String title;
    private String content;
    private boolean isRead;
    private LocalDateTime createdDatetime;
    private LocalDateTime readDatetime;
    private String deletedYn;
    
    // 조인된 추가 정보
    private String boardTitle; // 게시글 제목
    private String senderName; // 발신자 이름 (실제 구현시 User 테이블과 조인)
    
    // 알림 타입 열거형
    public enum NotificationType {
        LIKE("좋아요"),
        COMMENT("댓글"), 
        REPLY("답글");
        
        private final String description;
        
        NotificationType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // 생성자
    public NotificationDTO() {}
    
    public NotificationDTO(String receiverId, String senderId, int boardIdx, 
                          NotificationType notificationType, String title, String content) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.boardIdx = boardIdx;
        this.notificationType = notificationType;
        this.title = title;
        this.content = content;
    }
    
    // 댓글 알림용 생성자
    public NotificationDTO(String receiverId, String senderId, int boardIdx, int commentIdx,
                          NotificationType notificationType, String title, String content) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.boardIdx = boardIdx;
        this.commentIdx = commentIdx;
        this.notificationType = notificationType;
        this.title = title;
        this.content = content;
    }
    
    // Getter and Setter
    public int getNotificationIdx() {
        return notificationIdx;
    }
    
    public void setNotificationIdx(int notificationIdx) {
        this.notificationIdx = notificationIdx;
    }
    
    public String getReceiverId() {
        return receiverId;
    }
    
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    
    public String getSenderId() {
        return senderId;
    }
    
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    
    public int getBoardIdx() {
        return boardIdx;
    }
    
    public void setBoardIdx(int boardIdx) {
        this.boardIdx = boardIdx;
    }
    
    public Integer getCommentIdx() {
        return commentIdx;
    }
    
    public void setCommentIdx(Integer commentIdx) {
        this.commentIdx = commentIdx;
    }
    
    public NotificationType getNotificationType() {
        return notificationType;
    }
    
    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
    
    public LocalDateTime getCreatedDatetime() {
        return createdDatetime;
    }
    
    public void setCreatedDatetime(LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }
    
    public LocalDateTime getReadDatetime() {
        return readDatetime;
    }
    
    public void setReadDatetime(LocalDateTime readDatetime) {
        this.readDatetime = readDatetime;
    }
    
    public String getDeletedYn() {
        return deletedYn;
    }
    
    public void setDeletedYn(String deletedYn) {
        this.deletedYn = deletedYn;
    }
    
    public String getBoardTitle() {
        return boardTitle;
    }
    
    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    /**
     * 알림 메시지 생성 헬퍼 메소드
     */
    public String getFormattedMessage() {
        switch (notificationType) {
            case LIKE:
                return senderName + "님이 회원님의 게시글을 좋아합니다.";
            case COMMENT:
                return senderName + "님이 회원님의 게시글에 댓글을 남겼습니다.";
            case REPLY:
                return senderName + "님이 회원님의 댓글에 답글을 남겼습니다.";
            default:
                return content;
        }
    }
    
    /**
     * 시간 경과 표시 헬퍼 메소드
     */
    public String getTimeAgo() {
        if (createdDatetime == null) return "";
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(createdDatetime, now).toMinutes();
        
        if (minutes < 1) return "방금 전";
        else if (minutes < 60) return minutes + "분 전";
        else if (minutes < 1440) return (minutes / 60) + "시간 전";
        else return (minutes / 1440) + "일 전";
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NotificationDTO [notificationIdx=");
        builder.append(notificationIdx);
        builder.append(", receiverId=");
        builder.append(receiverId);
        builder.append(", senderId=");
        builder.append(senderId);
        builder.append(", boardIdx=");
        builder.append(boardIdx);
        builder.append(", commentIdx=");
        builder.append(commentIdx);
        builder.append(", notificationType=");
        builder.append(notificationType);
        builder.append(", title=");
        builder.append(title);
        builder.append(", content=");
        builder.append(content);
        builder.append(", isRead=");
        builder.append(isRead);
        builder.append(", createdDatetime=");
        builder.append(createdDatetime);
        builder.append(", readDatetime=");
        builder.append(readDatetime);
        builder.append(", deletedYn=");
        builder.append(deletedYn);
        builder.append(", boardTitle=");
        builder.append(boardTitle);
        builder.append(", senderName=");
        builder.append(senderName);
        builder.append("]");
        return builder.toString();
    }
}

