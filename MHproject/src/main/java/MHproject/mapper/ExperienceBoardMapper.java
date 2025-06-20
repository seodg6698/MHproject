package MHproject.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import MHproject.DTO.Criteria;
import MHproject.DTO.ExperienceBoardDTO;

@Mapper
public interface ExperienceBoardMapper {

	public List<ExperienceBoardDTO> selectBoardList();
	
	void insertBoard(ExperienceBoardDTO board) throws Exception;

	void updateHitCount(int boardIdx) throws Exception;

	ExperienceBoardDTO selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(ExperienceBoardDTO board) throws Exception;

	void deleteBoard(int boardIdx) throws Exception;

	List<Map<String, Object>> selectBoardList(Criteria cri) throws Exception;

	public int countBoardList() throws Exception;

	public void selectAndDeleteBoard(List<Integer> idxList);

	public List<ExperienceBoardDTO> searchTitleBoardList(String keyword);

	public List<ExperienceBoardDTO> searchContentsBoardList(String keyword);
	
	public void insertBoardToTargetBoard(@Param("board") ExperienceBoardDTO board, @Param("targetBoard") String targetBoard) throws Exception;
	
}

