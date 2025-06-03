package MHproject.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MHproject.DTO.Criteria;
import MHproject.DTO.FreeBoardDTO;
import MHproject.mapper.FreeBoardMapper;

@Service
public class FreeBoardServiceImpl implements FreeBoardService {
	
	@Autowired
	private FreeBoardMapper boardMapper;
	
	@Autowired
	public void FreeBoardMapper(FreeBoardMapper boardMapper) {
		
		this.boardMapper = boardMapper;
	}
	
	@Override
	public List<FreeBoardDTO> selectBoardList() throws Exception{
		
		return boardMapper.selectBoardList();
	
	}

	@Override
	public void insertBoard(FreeBoardDTO board) throws Exception {
		boardMapper.insertBoard(board);
		
		
		
	}

	@Override
	public FreeBoardDTO selectBoardDetail(int boardIdx) throws Exception {
		boardMapper.updateHitCount(boardIdx);
		
		FreeBoardDTO board = boardMapper.selectBoardDetail(boardIdx);
		return board;
	}
	
	@Override
	public void updateBoard(FreeBoardDTO board) throws Exception{
		boardMapper.updateBoard(board);
	}
	
	@Override
	public void deleteBoard(int boardIdx) throws Exception{
		boardMapper.deleteBoard(boardIdx);
	}
	
	//게시글 선택삭제
	@Override
	public void selectAndDeleteBoard(List<Integer> idxList) throws Exception{
		System.out.println(idxList + ":: service idxList");
		boardMapper.selectAndDeleteBoard(idxList);
		
		/*
		Map<String, Object> idxMap = new HashMap<String, Object>();
		
		idxMap=paramMap;
		System.out.println(idxMap + ":: service map");
		
		
		List<Integer> bIdx = new ArrayList<>();
		bIdx = idx;
		for(int i=0; i<bIdx.size(); i++) {
			System.out.println(bIdx.get(i) + " :: sevice bidxList" + i);
		}
		
		*/
		
		
	}
	
	
	@Override
	public List<Map<String, Object>> selectBoardList(Criteria cri) throws Exception {
		 return boardMapper.selectBoardList(cri);
	}
	 
	
	@Override
	public int countBoardListTotal() throws Exception {
	    return boardMapper.countBoardList();
	}

	

	@Override
	public List<FreeBoardDTO> searchTitleBoardList(String keyword) throws Exception {
		System.out.println(keyword +" ::service");
		List<FreeBoardDTO> boardList = boardMapper.searchTitleBoardList(keyword);
		System.out.println(boardList +" ::service");
		return boardList;
	}

	@Override
	public List<FreeBoardDTO> searchContentsBoardList(String keyword) {
		List<FreeBoardDTO> boardList = boardMapper.searchContentsBoardList(keyword);
		System.out.println(keyword +" ::service");
		System.out.println(boardList +" ::service");
		return boardList;
	}

	


}
