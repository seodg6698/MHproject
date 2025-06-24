package MHproject.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import MHproject.DTO.Criteria;
import MHproject.DTO.FreeBoardDTO;
import MHproject.mapper.FreeBoardMapper;

@Service
public class FreeBoardServiceImpl implements FreeBoardService {
	
	private static final Logger logger = LoggerFactory.getLogger(FreeBoardServiceImpl.class);
	
	@Autowired
	private FreeBoardMapper boardMapper;
	
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
	public FreeBoardDTO selectBoardDetailWithLike(int boardIdx, String userId) throws Exception {
		logger.info("좋아요 정보 포함 게시글 상세 조회 - boardIdx: {}, userId: {}", boardIdx, userId);
		
		boardMapper.updateHitCount(boardIdx);
		
		FreeBoardDTO board = boardMapper.selectBoardDetailWithLike(boardIdx, userId);
		
		if (board != null) {
			logger.debug("게시글 조회 완료 - 제목: {}, 좋아요 수: {}, 사용자 좋아요 여부: {}", 
						board.getTitle(), board.getLikeCnt(), board.isLiked());
		}
		
		return board;
	}
	
	
	@Override
	public void deleteBoard(int boardIdx) throws Exception{
		boardMapper.deleteBoard(boardIdx);
	}
	
	@Override
	public void selectAndDeleteBoard(List<Integer> idxList) throws Exception{
		logger.info("선택 삭제할 게시글 목록: {}", idxList);
		boardMapper.selectAndDeleteBoard(idxList);
	}
	
	@Override
	@Transactional
	public void moveBoardsToAnotherBoard(List<Integer> boardIdxList, String targetBoard) throws Exception {
	    logger.info("=== 게시글 이동 시작 ===");
	    logger.info("이동할 게시글 목록: {}", boardIdxList);
	    logger.info("대상 게시판: {}", targetBoard);
	    
	    int successCount = 0;
	    
	    for (Integer boardIdx : boardIdxList) {
	        try {
	            FreeBoardDTO originalBoard = boardMapper.selectBoardDetail(boardIdx);
	            
	            if (originalBoard != null) {
	                boardMapper.insertBoardToTargetBoard(originalBoard, targetBoard);
	                boardMapper.deleteBoard(boardIdx);
	                
	                successCount++;
	                logger.info("게시글 이동 완료 - ID: {}, 제목: '{}' -> {}", 
	                           boardIdx, originalBoard.getTitle(), targetBoard);
	            } else {
	                logger.warn("게시글을 찾을 수 없습니다 - ID: {}", boardIdx);
	            }
	        } catch (Exception e) {
	            logger.error("게시글 이동 실패 - ID: {}, 오류: {}", boardIdx, e.getMessage());
	            throw e;
	        }
	    }
	    
	    logger.info("=== 게시글 이동 완료 ===");
	    logger.info("총 {}개 중 {}개 이동 성공", boardIdxList.size(), successCount);
	}
	
	@Override
	public List<Map<String, Object>> selectBoardList(Criteria cri) throws Exception {
		 return boardMapper.selectBoardList(cri);
	}
	
	@Override
	public List<Map<String, Object>> selectBoardListWithLike(Criteria cri, String userId) throws Exception {
		logger.debug("좋아요 정보 포함 게시글 목록 조회 - 페이지: {}, userId: {}", cri.getPage(), userId);
		return boardMapper.selectBoardListWithLike(cri, userId);
	}
	 
	@Override
	public int countBoardListTotal() throws Exception {
	    return boardMapper.countBoardList();
	}

	@Override
	public List<FreeBoardDTO> searchTitleBoardList(String keyword) throws Exception {
		logger.debug("제목 검색 키워드: {}", keyword);
		List<FreeBoardDTO> boardList = boardMapper.searchTitleBoardList(keyword);
		logger.debug("검색 결과 개수: {}", boardList.size());
		return boardList;
	}
	
	@Override
	public List<FreeBoardDTO> searchTitleBoardListWithLike(String keyword, String userId) throws Exception {
		logger.debug("좋아요 정보 포함 제목 검색 - 키워드: {}, userId: {}", keyword, userId);
		List<FreeBoardDTO> boardList = boardMapper.searchTitleBoardListWithLike(keyword, userId);
		logger.debug("검색 결과 개수: {}", boardList.size());
		return boardList;
	}

	@Override
	public List<FreeBoardDTO> searchContentsBoardList(String keyword) {
		logger.debug("내용 검색 키워드: {}", keyword);
		List<FreeBoardDTO> boardList = boardMapper.searchContentsBoardList(keyword);
		logger.debug("검색 결과 개수: {}", boardList.size());
		return boardList;
	}
	
	@Override
	public List<FreeBoardDTO> searchContentsBoardListWithLike(String keyword, String userId) throws Exception {
		logger.debug("좋아요 정보 포함 내용 검색 - 키워드: {}, userId: {}", keyword, userId);
		List<FreeBoardDTO> boardList = boardMapper.searchContentsBoardListWithLike(keyword, userId);
		logger.debug("검색 결과 개수: {}", boardList.size());
		return boardList;
	}



	@Override
	public void updateBoard(String title, String contents, int boardIdx, String creatorId) {
		 boardMapper.updateBoard(title,contents,boardIdx,creatorId);
		
	}

	@Override
	public int updateBoard(FreeBoardDTO board) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}