package MHproject.DTO;

import java.time.LocalDateTime;

public class BoardLikeDTO1 {
    
    private int likeIdx;
    private int boardIdx;
    private String userId;
    private LocalDateTime createdDatetime;
    
    // 생성자
    public BoardLikeDTO1() {}
    
    public BoardLikeDTO1(int boardIdx, String userId) {
        this.boardIdx = boardIdx;
        this.userId = userId;
    }
    
    // Getter and Setter
    public int getLikeIdx() {
        return likeIdx;
    }
    
    public void setLikeIdx(int likeIdx) {
        this.likeIdx = likeIdx;
    }
    
    public int getBoardIdx() {
        return boardIdx;
    }
    
    public void setBoardIdx(int boardIdx) {
        this.boardIdx = boardIdx;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getCreatedDatetime() {
        return createdDatetime;
    }
    
    public void setCreatedDatetime(LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BoardLikeDTO [likeIdx=");
        builder.append(likeIdx);
        builder.append(", boardIdx=");
        builder.append(boardIdx);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", createdDatetime=");
        builder.append(createdDatetime);
        builder.append("]");
        return builder.toString();
    }
}