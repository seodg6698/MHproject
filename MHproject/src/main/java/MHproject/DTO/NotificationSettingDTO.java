package MHproject.DTO;

import java.time.LocalDateTime;

public class NotificationSettingDTO {
    
    private int settingIdx;
    private String userId;
    private boolean likeNotification;
    private boolean commentNotification;
    private boolean replyNotification;
    private boolean emailNotification;
    private LocalDateTime createdDatetime;
    private LocalDateTime updatedDatetime;
    
    // 생성자
    public NotificationSettingDTO() {}
    
    public NotificationSettingDTO(String userId) {
        this.userId = userId;
        this.likeNotification = true;
        this.commentNotification = true;
        this.replyNotification = true;
        this.emailNotification = false;
    }
    
    // Getter and Setter
    public int getSettingIdx() {
        return settingIdx;
    }
    
    public void setSettingIdx(int settingIdx) {
        this.settingIdx = settingIdx;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public boolean isLikeNotification() {
        return likeNotification;
    }
    
    public void setLikeNotification(boolean likeNotification) {
        this.likeNotification = likeNotification;
    }
    
    public boolean isCommentNotification() {
        return commentNotification;
    }
    
    public void setCommentNotification(boolean commentNotification) {
        this.commentNotification = commentNotification;
    }
    
    public boolean isReplyNotification() {
        return replyNotification;
    }
    
    public void setReplyNotification(boolean replyNotification) {
        this.replyNotification = replyNotification;
    }
    
    public boolean isEmailNotification() {
        return emailNotification;
    }
    
    public void setEmailNotification(boolean emailNotification) {
        this.emailNotification = emailNotification;
    }
    
    public LocalDateTime getCreatedDatetime() {
        return createdDatetime;
    }
    
    public void setCreatedDatetime(LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }
    
    public LocalDateTime getUpdatedDatetime() {
        return updatedDatetime;
    }
    
    public void setUpdatedDatetime(LocalDateTime updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NotificationSettingDTO [settingIdx=");
        builder.append(settingIdx);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", likeNotification=");
        builder.append(likeNotification);
        builder.append(", commentNotification=");
        builder.append(commentNotification);
        builder.append(", replyNotification=");
        builder.append(replyNotification);
        builder.append(", emailNotification=");
        builder.append(emailNotification);
        builder.append(", createdDatetime=");
        builder.append(createdDatetime);
        builder.append(", updatedDatetime=");
        builder.append(updatedDatetime);
        builder.append("]");
        return builder.toString();
    }
}