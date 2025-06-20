package MHproject.service;

import java.util.List;
import java.util.Map;

import MHproject.DTO.Criteria;
import MHproject.DTO.ExperienceBoardDTO;

public interface ExperienceBoardService {
	
	public List<ExperienceBoardDTO> selectBoardList() throws Exception;

	void insertBoard(ExperienceBoardDTO board) throws Exception;

	ExperienceBoardDTO selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(ExperienceBoardDTO board) throws Exception;

	List<Map<String, Object>> selectBoardList(Criteria cri) throws Exception;

	public int countBoardListTotal() throws Exception;

	public void selectAndDeleteBoard(List<Integer> idxList) throws Exception;

	public void deleteBoard(int boardIdx) throws Exception;


	public List<ExperienceBoardDTO> searchTitleBoardList(String keyword) throws Exception;

	public List<ExperienceBoardDTO> searchContentsBoardList(String keyword);

	public void moveBoardsToAnotherBoard(List<Integer> boardIdxList, String targetBoard) throws Exception;
	
}
