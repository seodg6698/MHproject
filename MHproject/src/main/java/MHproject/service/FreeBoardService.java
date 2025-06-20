package MHproject.service;

import java.util.List;
import java.util.Map;

import MHproject.DTO.Criteria;
import MHproject.DTO.FreeBoardDTO;

public interface FreeBoardService {
	
	public List<FreeBoardDTO> selectBoardList() throws Exception;

	void insertBoard(FreeBoardDTO board) throws Exception;

	FreeBoardDTO selectBoardDetail(int boardIdx) throws Exception;
	
	/**
	 * 사용자의 좋아요 여부를 포함한 게시글 상세 조회
	 * @param boardIdx 게시글 번호
	 * @param userId 사용자 ID (로그인하지 않은 경우 null)
	 * @return 게시글 상세 정보
	 * @throws Exception
	 */
	FreeBoardDTO selectBoardDetailWithLike(int boardIdx, String userId) throws Exception;

	void updateBoard(FreeBoardDTO board) throws Exception;

	List<Map<String, Object>> selectBoardList(Criteria cri) throws Exception;
	
	/**
	 * 사용자의 좋아요 여부를 포함한 게시글 목록 조회
	 * @param cri 페이징 정보
	 * @param userId 사용자 ID (로그인하지 않은 경우 null)
	 * @return 게시글 목록
	 * @throws Exception
	 */
	List<Map<String, Object>> selectBoardListWithLike(Criteria cri, String userId) throws Exception;

	public int countBoardListTotal() throws Exception;

	public void selectAndDeleteBoard(List<Integer> idxList) throws Exception;

	public void deleteBoard(int boardIdx) throws Exception;

	public List<FreeBoardDTO> searchTitleBoardList(String keyword) throws Exception;
	
	/**
	 * 제목 검색 시 사용자의 좋아요 여부 포함
	 */
	public List<FreeBoardDTO> searchTitleBoardListWithLike(String keyword, String userId) throws Exception;

	public List<FreeBoardDTO> searchContentsBoardList(String keyword);
	
	/**
	 * 내용 검색 시 사용자의 좋아요 여부 포함
	 */
	public List<FreeBoardDTO> searchContentsBoardListWithLike(String keyword, String userId) throws Exception;

	public void moveBoardsToAnotherBoard(List<Integer> boardIdxList, String targetBoard) throws Exception;
}