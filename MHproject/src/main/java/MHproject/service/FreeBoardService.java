package MHproject.service;

import java.util.List;
import java.util.Map;

import MHproject.DTO.Criteria;
import MHproject.DTO.FreeBoardDTO;

public interface FreeBoardService {
	
	public List<FreeBoardDTO> selectBoardList() throws Exception;

	void insertBoard(FreeBoardDTO board) throws Exception;

	FreeBoardDTO selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(FreeBoardDTO board) throws Exception;

	List<Map<String, Object>> selectBoardList(Criteria cri) throws Exception;

	public int countBoardListTotal() throws Exception;

	public void selectAndDeleteBoard(List<Integer> idxList) throws Exception;

	public void deleteBoard(int boardIdx) throws Exception;


	public List<FreeBoardDTO> searchTitleBoardList(String keyword) throws Exception;

	public List<FreeBoardDTO> searchContentsBoardList(String keyword);

}
