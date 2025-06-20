package MHproject.service;

import MHproject.DTO.BoardLikeDTO2;

public interface BoardLikeService2 {
    
    /**
     * 좋아요 토글 (추가/삭제)
     * @param boardIdx 게시글 번호
     * @param userId 사용자 ID
     * @return 좋아요 상태 (true: 좋아요 추가됨, false: 좋아요 취소됨)
     * @throws Exception
     */
    boolean toggleLike(int boardIdx, String userId) throws Exception;
    
    /**
     * 특정 게시글의 좋아요 개수 조회
     * @param boardIdx 게시글 번호
     * @return 좋아요 개수
     * @throws Exception
     */
    int getLikeCount(int boardIdx) throws Exception;
    
    /**
     * 사용자의 특정 게시글 좋아요 여부 확인
     * @param boardIdx 게시글 번호
     * @param userId 사용자 ID
     * @return 좋아요 여부
     * @throws Exception
     */
    boolean isUserLiked(int boardIdx, String userId) throws Exception;
}