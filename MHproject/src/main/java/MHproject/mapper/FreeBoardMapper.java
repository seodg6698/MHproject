package MHproject.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import MHproject.DTO.Criteria;
import MHproject.DTO.FreeBoardDTO;

@Mapper
public interface FreeBoardMapper {

	public List<FreeBoardDTO> selectBoardList();
	
	void insertBoard(FreeBoardDTO board) throws Exception;

	void updateHitCount(int boardIdx) throws Exception;

	FreeBoardDTO selectBoardDetail(int boardIdx) throws Exception;
	
	/**
	 * 사용자의 좋아요 여부를 포함한 게시글 상세 조회
	 * @param boardIdx 게시글 번호
	 * @param userId 사용자 ID (로그인하지 않은 경우 null)
	 * @return 게시글 상세 정보
	 */
	FreeBoardDTO selectBoardDetailWithLike(@Param("boardIdx") int boardIdx, @Param("userId") String userId) throws Exception;

	/**
     * 게시글 수정
     * @param boardDto 수정할 게시글 정보
     * @return 수정된 행의 개수
     * @throws Exception
     */
    int updateBoard(FreeBoardDTO boardDto) throws Exception;

	void deleteBoard(int boardIdx) throws Exception;

	List<Map<String, Object>> selectBoardList(Criteria cri) throws Exception;
	
	/**
	 * 사용자의 좋아요 여부를 포함한 게시글 목록 조회
	 * @param cri 페이징 정보
	 * @param userId 사용자 ID (로그인하지 않은 경우 null)
	 * @return 게시글 목록
	 */
	List<Map<String, Object>> selectBoardListWithLike(@Param("cri") Criteria cri, @Param("userId") String userId) throws Exception;

	public int countBoardList() throws Exception;

	public void selectAndDeleteBoard(List<Integer> idxList);

	public List<FreeBoardDTO> searchTitleBoardList(String keyword);

	public List<FreeBoardDTO> searchContentsBoardList(String keyword);
	
	/**
	 * 제목 검색 시 사용자의 좋아요 여부 포함
	 */
	public List<FreeBoardDTO> searchTitleBoardListWithLike(@Param("keyword") String keyword, @Param("userId") String userId);
	
	/**
	 * 내용 검색 시 사용자의 좋아요 여부 포함
	 */
	public List<FreeBoardDTO> searchContentsBoardListWithLike(@Param("keyword") String keyword, @Param("userId") String userId);
	
	public void insertBoardToTargetBoard(@Param("board") FreeBoardDTO board, @Param("targetBoard") String targetBoard) throws Exception;

	public void updateBoard(@Param("title") String title, @Param("contents") String contents, @Param("boardIdx") int boardIdx,@Param("creatorId") String creatorId);
}