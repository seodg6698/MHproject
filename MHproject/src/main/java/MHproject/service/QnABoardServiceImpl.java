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
import MHproject.DTO.QnABoardDTO;
import MHproject.mapper.FreeBoardMapper;
import MHproject.mapper.QnABoardMapper;

@Service
public class QnABoardServiceImpl implements QnABoardService {
	
	private static final Logger logger = LoggerFactory.getLogger(QnABoardServiceImpl.class);
	
	
	  @Autowired
	    private NotificationService notificationService; // 기존 서비스 재사용!
	
	@Autowired
	private QnABoardMapper boardMapper;
	
	@Autowired
	public void QnABoardMapper(QnABoardMapper boardMapper) {
		
		this.boardMapper = boardMapper;
	}
	
	@Override
	public List<FreeBoardDTO> selectBoardList() throws Exception{
		
		return boardMapper.selectBoardList();
	
	}

	@Override
	public void insertBoard(QnABoardDTO board) throws Exception {
		boardMapper.insertBoard(board);
		
		
		
	}

	@Override
	public QnABoardDTO selectBoardDetail(int boardIdx) throws Exception {
		boardMapper.updateHitCount(boardIdx);
		
		QnABoardDTO board = boardMapper.selectBoardDetail(boardIdx);
		return board;
	}
	
	
	@Override
	public QnABoardDTO selectBoardDetailWithLike(int boardIdx, String userId) throws Exception {
		logger.info("좋아요 정보 포함 게시글 상세 조회 - boardIdx: {}, userId: {}", boardIdx, userId);
		
		boardMapper.updateHitCount(boardIdx);
		
		QnABoardDTO board = boardMapper.selectBoardDetailWithLike(boardIdx, userId);
		
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
	public List<Map<String, Object>> selectBoardListWithLike(Criteria cri, String userId) throws Exception {
		logger.debug("좋아요 정보 포함 게시글 목록 조회 - 페이지: {}, userId: {}", cri.getPage(), userId);
		return boardMapper.selectBoardListWithLike(cri, userId);
	}
	 
	
	@Override
	public int countBoardListTotal() throws Exception {
	    return boardMapper.countBoardList();
	}
	
	
	
	// 게시글 이동 구현 - 로깅 개선
		@Override
		@Transactional
		public void moveBoardsToAnotherBoard(List<Integer> boardIdxList, String targetBoard) throws Exception {
		    logger.info("=== 게시글 이동 시작 ===");
		    logger.info("이동할 게시글 목록: {}", boardIdxList);
		    logger.info("대상 게시판: {}", targetBoard);
		    
		    int successCount = 0;
		    
		    for (Integer boardIdx : boardIdxList) {
		        try {
		            // 1. 원본 게시글 정보 조회
		           QnABoardDTO originalBoard = boardMapper.selectBoardDetail(boardIdx);
		            
		            if (originalBoard != null) {
		                // 2. 대상 게시판에 게시글 복사
		                boardMapper.insertBoardToTargetBoard(originalBoard, targetBoard);
		                
		                // 3. 원본 게시글 삭제 (soft delete)
		                boardMapper.deleteBoard(boardIdx);
		                
		                successCount++;
		                logger.info("게시글 이동 완료 - ID: {}, 제목: '{}' -> {}", 
		                           boardIdx, originalBoard.getTitle(), targetBoard);
		            } else {
		                logger.warn("게시글을 찾을 수 없습니다 - ID: {}", boardIdx);
		            }
		        } catch (Exception e) {
		            logger.error("게시글 이동 실패 - ID: {}, 오류: {}", boardIdx, e.getMessage());
		            throw e; // 트랜잭션 롤백을 위해 예외 재발생
		        }
		    }
		    
		    logger.info("=== 게시글 이동 완료 ===");
		    logger.info("총 {}개 중 {}개 이동 성공", boardIdxList.size(), successCount);
		}

	

	@Override
	public List<QnABoardDTO> searchTitleBoardList(String keyword) throws Exception {
		System.out.println(keyword +" ::service");
		List<QnABoardDTO> boardList = boardMapper.searchTitleBoardList(keyword);
		System.out.println(boardList +" ::service");
		return boardList;
	}
	
	
	@Override
	public List<QnABoardDTO> searchTitleBoardListWithLike(String keyword, String userId) throws Exception {
		logger.debug("좋아요 정보 포함 제목 검색 - 키워드: {}, userId: {}", keyword, userId);
		List<QnABoardDTO> boardList = boardMapper.searchTitleBoardListWithLike(keyword, userId);
		logger.debug("검색 결과 개수: {}", boardList.size());
		return boardList;
	}
	

	@Override
	public List<QnABoardDTO> searchContentsBoardList(String keyword) {
		List<QnABoardDTO> boardList = boardMapper.searchContentsBoardList(keyword);
		System.out.println(keyword +" ::service");
		System.out.println(boardList +" ::service");
		return boardList;
	}

	

	@Override
	public List<QnABoardDTO> searchContentsBoardListWithLike(String keyword, String userId) throws Exception {
		logger.debug("좋아요 정보 포함 내용 검색 - 키워드: {}, userId: {}", keyword, userId);
		List<QnABoardDTO> boardList = boardMapper.searchContentsBoardListWithLike(keyword, userId);
		logger.debug("검색 결과 개수: {}", boardList.size());
		return boardList;
	}

	@Override
	public void updateBoard(String title, String contents, int boardIdx, String creatorId) {
		 boardMapper.updateBoard(title,contents,boardIdx,creatorId);
		
	}

	@Override
	public void updateBoard(QnABoardDTO board) throws Exception {
		// TODO Auto-generated method stub
		
	}

	


}
