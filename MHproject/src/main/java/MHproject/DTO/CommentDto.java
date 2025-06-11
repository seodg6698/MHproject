package MHproject.DTO;

import java.time.LocalDateTime;

public class CommentDto {
    private int commentIdx;
    private int boardIdx;
    private int parentIdx;
    private String content;
    private String creatorId;
    private LocalDateTime createdDatetime;
    private LocalDateTime updatedDatetime;
    private String deletedYn;
    private int depth;
    private int sortOrder;
    
    // 기본 생성자
    public CommentDto() {}
    
    // 생성자
    public CommentDto(int boardIdx, int parentIdx, String content, String creatorId) {
        this.boardIdx = boardIdx;
        this.parentIdx = parentIdx;
        this.content = content;
        this.creatorId = creatorId;
        this.depth = (parentIdx > 0) ? 1 : 0;
    }
    
    // Getter와 Setter
    public int getCommentIdx() {
        return commentIdx;
    }
    
    public void setCommentIdx(int commentIdx) {
        this.commentIdx = commentIdx;
    }
    
    public int getBoardIdx() {
        return boardIdx;
    }
    
    public void setBoardIdx(int boardIdx) {
        this.boardIdx = boardIdx;
    }
    
    public int getParentIdx() {
        return parentIdx;
    }
    
    public void setParentIdx(int parentIdx) {
        this.parentIdx = parentIdx;
        this.depth = (parentIdx > 0) ? 1 : 0; // parentIdx에 따라 depth 자동 설정
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getCreatorId() {
        return creatorId;
    }
    
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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
    
    public String getDeletedYn() {
        return deletedYn;
    }
    
    public void setDeletedYn(String deletedYn) {
        this.deletedYn = deletedYn;
    }
    
    public int getDepth() {
        return depth;
    }
    
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    public int getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
