package MHproject.service;

import java.util.List;
import java.util.Map;

import MHproject.DTO.Criteria;
import MHproject.DTO.FreeBoardDTO;
import MHproject.DTO.QnABoardDTO;

public interface QnABoardService {
	
	public List<FreeBoardDTO> selectBoardList() throws Exception;

	void insertBoard(QnABoardDTO board) throws Exception;

	QnABoardDTO selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(QnABoardDTO board) throws Exception;

	List<Map<String, Object>> selectBoardList(Criteria cri) throws Exception;

	public int countBoardListTotal() throws Exception;

	public void selectAndDeleteBoard(List<Integer> idxList) throws Exception;

	public void deleteBoard(int boardIdx) throws Exception;


	public List<QnABoardDTO> searchTitleBoardList(String keyword) throws Exception;

	public List<QnABoardDTO> searchContentsBoardList(String keyword);

}

