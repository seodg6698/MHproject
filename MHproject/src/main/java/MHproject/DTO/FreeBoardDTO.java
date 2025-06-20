package MHproject.DTO;

import java.time.LocalDateTime;

public class FreeBoardDTO {
	
	
	private int boardIdx;
	private String title;
	private String contents;
	private int hitCnt;
	private String creatorId;
	private LocalDateTime createdDatetime;
	private String updaterId;
	private LocalDateTime updatedDatetime;
	private String type;
	private String keyword;
	
	
	
	// 좋아요 관련 필드 추가
	private int likeCnt;        // 게시글의 총 좋아요 개수
	private boolean isLiked;    // 현재 사용자의 좋아요 여부
	
	

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
	public int getBoardIdx() {
		return boardIdx;
	}
	public void setBoardIdx(int boardIdx) {
		this.boardIdx = boardIdx;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getHitCnt() {
		return hitCnt;
	}
	public void setHitCnt(int hitCnt) {
		this.hitCnt = hitCnt;
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
	public String getUpdaterId() {
		return updaterId;
	}
	public void setUpdaterId(String updaterId) {
		this.updaterId = updaterId;
	}
	public LocalDateTime getUpdatedDatetime() {
		return updatedDatetime;
	}
	public void setUpdatedDatetime(LocalDateTime updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}
	
	
	// 좋아요 관련 getter/setter 추가
		public int getLikeCnt() {
			return likeCnt;
		}
		public void setLikeCnt(int likeCnt) {
			this.likeCnt = likeCnt;
		}
		
		public boolean isLiked() {
			return isLiked;
		}
		public void setLiked(boolean isLiked) {
			this.isLiked = isLiked;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("FreeBoardDTO [boardIdx=");
			builder.append(boardIdx);
			builder.append(", title=");
			builder.append(title);
			builder.append(", contents=");
			builder.append(contents);
			builder.append(", hitCnt=");
			builder.append(hitCnt);
			builder.append(", creatorId=");
			builder.append(creatorId);
			builder.append(", createdDatetime=");
			builder.append(createdDatetime);
			builder.append(", updaterId=");
			builder.append(updaterId);
			builder.append(", updatedDatetime=");
			builder.append(updatedDatetime);
			builder.append(", type=");
			builder.append(type);
			builder.append(", keyword=");
			builder.append(keyword);
			builder.append(", likeCnt=");
			builder.append(likeCnt);
			builder.append(", isLiked=");
			builder.append(isLiked);
			builder.append("]");
			return builder.toString();
		}
	
	
	

}
