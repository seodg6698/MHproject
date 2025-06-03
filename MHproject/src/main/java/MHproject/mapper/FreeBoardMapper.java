package MHproject.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import MHproject.DTO.Criteria;
import MHproject.DTO.FreeBoardDTO;

@Mapper
public interface FreeBoardMapper {

	public List<FreeBoardDTO> selectBoardList();
	
	void insertBoard(FreeBoardDTO board) throws Exception;

	void updateHitCount(int boardIdx) throws Exception;

	FreeBoardDTO selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(FreeBoardDTO board) throws Exception;

	void deleteBoard(int boardIdx) throws Exception;

	List<Map<String, Object>> selectBoardList(Criteria cri) throws Exception;

	public int countBoardList() throws Exception;

	public void selectAndDeleteBoard(List<Integer> idxList);

	public List<FreeBoardDTO> searchTitleBoardList(String keyword);

	public List<FreeBoardDTO> searchContentsBoardList(String keyword);
	
}
