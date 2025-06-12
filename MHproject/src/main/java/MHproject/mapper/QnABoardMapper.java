package MHproject.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import MHproject.DTO.Criteria;
import MHproject.DTO.FreeBoardDTO;
import MHproject.DTO.QnABoardDTO;

@Mapper
public interface QnABoardMapper {

	public List<FreeBoardDTO> selectBoardList();
	
	void insertBoard(QnABoardDTO board) throws Exception;

	void updateHitCount(int boardIdx) throws Exception;

	QnABoardDTO selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(QnABoardDTO board) throws Exception;

	void deleteBoard(int boardIdx) throws Exception;

	List<Map<String, Object>> selectBoardList(Criteria cri) throws Exception;

	public int countBoardList() throws Exception;

	public void selectAndDeleteBoard(List<Integer> idxList);

	public List<QnABoardDTO> searchTitleBoardList(String keyword);

	public List<QnABoardDTO> searchContentsBoardList(String keyword);
	
}

