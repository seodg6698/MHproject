package MHproject.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import MHproject.service.CommentService1;
import MHproject.service.FreeBoardService;
import MHproject.DTO.FreeBoardDTO;
import MHproject.DTO.CommentDto1;
import MHproject.DTO.Criteria;
import MHproject.DTO.PageMaker;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/board1")
public class FreeBoardController {
	
	// Logger 선언
    private static final Logger logger = LoggerFactory.getLogger(FreeBoardController.class);

	@Autowired
	private FreeBoardService boardService;
	
	@Autowired
	private CommentService1 commentService;
	
	@PostConstruct
    public void FreeBoardControllerInit() {
        logger.info("FreeBoardController bean 생성 완료");
    }

	//게시글 작성화면
	@GetMapping("/openBoardWrite1")
	public String openBoardWrite() throws Exception{
		logger.debug("게시글 작성 화면 요청");
		return "/board/freeBoard/freeBoardWrite";
	}
	
	//게시글 작성 처리
	@PostMapping("/insertBoard1")
	public String insertBoard(FreeBoardDTO board, HttpSession session, Model model) throws Exception{
		logger.info("게시글 작성 요청 - 제목: {}, 작성자: {}", board.getTitle(), board.getCreatorId());
		logger.debug("게시글 상세 정보: {}", board.toString());
		
		boardService.insertBoard(board);
		
		logger.info("게시글 작성 완료 - 제목: {}", board.getTitle());
		return "redirect:/board1/openBoardList1";
	}
	
	//게시글 상세화면 - 디버깅 강화
	@GetMapping("/openBoardDetail1")
	public String openBoardDetail(@RequestParam int boardIdx, Model model) throws Exception {
		logger.info("게시글 상세 조회 요청 - boardIdx: {}", boardIdx);
		
		try {
			// 게시글 조회
			FreeBoardDTO board = boardService.selectBoardDetail(boardIdx);
			
			// 디버깅을 위한 상세 로그
			logger.debug("서비스에서 반환된 board 객체: {}", board);
			if (board != null) {
				logger.debug("board 상세 정보 - boardIdx: {}, title: {}, creatorId: {}", 
							board.getBoardIdx(), board.getTitle(), board.getCreatorId());
			}
			
			// null 체크 - 하지만 더 상세한 로그와 함께
			if (board == null) {
				logger.error("게시글을 찾을 수 없습니다 - boardIdx: {} (DB에서 null 반환)", boardIdx);
				model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다. (ID: " + boardIdx + ")");
				return "redirect:/board1/openBoardList1";
			}
			
			// 댓글 목록 조회
			List<CommentDto1> comments = commentService.selectCommentList(boardIdx);
			if (comments == null) {
				comments = new ArrayList<>(); // null 방지
				logger.debug("댓글 목록이 null이어서 빈 리스트로 초기화");
			}
			
			model.addAttribute("board", board);
			model.addAttribute("comments", comments);
			
			logger.info("게시글 상세 조회 성공 - boardIdx: {}, 제목: {}, 댓글 수: {}", 
						boardIdx, board.getTitle(), comments.size());
			
		} catch (Exception e) {
			logger.error("게시글 상세 조회 중 예외 발생 - boardIdx: {}, 예외 타입: {}, 메시지: {}", 
						boardIdx, e.getClass().getSimpleName(), e.getMessage(), e);
			model.addAttribute("errorMessage", "게시글 조회 중 오류가 발생했습니다.");
			return "redirect:/board1/openBoardList1";
		}
		
		return "/board/freeBoard/freeBoardDetail";
	}
	
	//게시글 수정처리
	@PostMapping("/updateBoard1")
	public String updateBoard(FreeBoardDTO board) throws Exception{
		logger.info("게시글 수정 요청 - boardIdx: {}, 제목: {}", board.getBoardIdx(), board.getTitle());
		
		try {
			boardService.updateBoard(board);
			logger.info("게시글 수정 완료 - boardIdx: {}", board.getBoardIdx());
		} catch (Exception e) {
			logger.error("게시글 수정 중 오류 발생 - boardIdx: {}, 오류: {}", board.getBoardIdx(), e.getMessage(), e);
			throw e;
		}
		
		return "redirect:/board1/openBoardList1";
	}
	
	//게시글 삭제처리
	@PostMapping("/deleteBoard1")
	public String deleteBoard(int boardIdx) throws Exception{ 
		logger.info("게시글 삭제 요청 - boardIdx: {}", boardIdx);
		
		try {
			boardService.deleteBoard(boardIdx);
			logger.info("게시글 삭제 완료 - boardIdx: {}", boardIdx);
		} catch (Exception e) {
			logger.error("게시글 삭제 중 오류 발생 - boardIdx: {}, 오류: {}", boardIdx, e.getMessage(), e);
			throw e;
		}
		
		return "redirect:/board1/openBoardList1";
	}
	
	//게시글 선택삭제
	@ResponseBody
	@PostMapping("/deleteSelection1")
	public String deleteSelection(HttpServletRequest request,
								@RequestParam(value="boardIdx[]") int[] boardIdx, FreeBoardDTO board) throws Exception {
	
		logger.info("게시글 선택 삭제 요청 - 선택된 게시글: {}", Arrays.toString(boardIdx));
		
		if(boardIdx == null || boardIdx.length == 0) {
			logger.warn("선택삭제 실패 - 선택된 게시글이 없음");
			return "게시글 선택요망";
		}
		
		List<Integer> idxList = new ArrayList<>();
		for(int i=0; i<boardIdx.length; i++) {
			idxList.add(boardIdx[i]);
		}
		
		logger.debug("변환된 게시글 ID 리스트: {}", idxList);
		
		try {
			boardService.selectAndDeleteBoard(idxList);
			logger.info("게시글 선택 삭제 완료 - 삭제된 게시글 수: {}", boardIdx.length);
			return "삭제 성공";
		} catch (Exception e) {
			logger.error("게시글 선택 삭제 중 오류 발생 - 오류: {}", e.getMessage(), e);
			return "삭제 중 오류가 발생했습니다.";
		}
	}
	
	// 게시글 이동 처리
	@ResponseBody
	@PostMapping("/moveBoards")
	public String moveBoards(@RequestParam(value="boardIdx[]") int[] boardIdx,
	                        @RequestParam("targetBoard") String targetBoard) throws Exception {
	    
	    logger.info("게시글 이동 요청 - 선택된 게시글: {}, 대상 게시판: {}", 
	               Arrays.toString(boardIdx), targetBoard);
	    
	    if(boardIdx == null || boardIdx.length == 0) {
	        logger.warn("게시글 이동 실패 - 선택된 게시글이 없음");
	        return "이동할 게시글을 선택해주세요.";
	    }
	    
	    List<Integer> idxList = new ArrayList<>();
	    for(int i=0; i<boardIdx.length; i++) {
	        idxList.add(boardIdx[i]);
	    }
	    
	    try {
	        boardService.moveBoardsToAnotherBoard(idxList, targetBoard);
	        logger.info("게시글 이동 성공 - 이동된 게시글 수: {}, 대상: {}", 
	                   boardIdx.length, targetBoard);
	        return "게시글이 성공적으로 이동되었습니다.";
	    } catch (Exception e) {
	        logger.error("게시글 이동 중 오류 발생 - 대상: {}, 오류: {}", targetBoard, e.getMessage(), e);
	        return "게시글 이동 중 오류가 발생했습니다.";
	    }
	}
	
	//페이징처리
	@GetMapping("/openBoardList1")
	public ModelAndView openBoardList(Criteria cri) throws Exception {
		logger.debug("게시글 목록 조회 요청 - 페이지: {}, 페이지당 개수: {}", 
		           cri.getPage(), cri.getPerPageNum());
	        
	    ModelAndView mav = new ModelAndView("/board/freeBoard/freeBoardList");
	        
	    try {
	    	PageMaker pageMaker = new PageMaker();
	    	pageMaker.setCri(cri);
	    	
	    	// 총 게시글 수 조회
	    	int totalCount = boardService.countBoardListTotal();
	    	pageMaker.setTotalCount(totalCount);
	    	
	    	logger.debug("페이징 정보 - 시작 페이지: {}, 끝 페이지: {}, 총 개수: {}", 
	    	           pageMaker.getStartPage(), pageMaker.getEndPage(), pageMaker.getTotalCount());
	    	        
	    	List<Map<String,Object>> list = boardService.selectBoardList(cri);
	    	if (list == null) {
	    		list = new ArrayList<>(); // null 방지
	    	}
	    	
	    	mav.addObject("list", list);
	    	mav.addObject("pageMaker", pageMaker);
	    	
	    	logger.info("게시글 목록 조회 완료 - 조회된 게시글 수: {}", list.size());
	    	
	    } catch (Exception e) {
	    	logger.error("게시글 목록 조회 중 오류 발생 - 오류: {}", e.getMessage(), e);
	    	mav.addObject("errorMessage", "게시글 목록 조회 중 오류가 발생했습니다.");
	    	mav.addObject("list", new ArrayList<>());
	    }
	        
	    return mav;
	}
	
	//검색처리
	@GetMapping("/boardList1")
	@ResponseBody
	public List<FreeBoardDTO> searchView(HttpServletRequest request, 
									@RequestParam(value="type") String type,
									@RequestParam(value="keyword") String keyword) throws Exception {
		
		logger.info("게시글 검색 요청 - 검색 타입: {}, 키워드: '{}'", type, keyword);
		
		// 입력값 검증
		if (keyword == null || keyword.trim().isEmpty()) {
			logger.warn("검색 키워드가 비어있음");
			return new ArrayList<>();
		}
		
		if (type == null || type.trim().isEmpty()) {
			logger.warn("검색 타입이 비어있음");
			return new ArrayList<>();
		}
		
		FreeBoardDTO searchKey = new FreeBoardDTO();
		searchKey.setKeyword(keyword.trim());
		searchKey.setType(type.trim());
		
		String searchKeyword = searchKey.getKeyword();
		String searchType = searchKey.getType();
		
		logger.debug("검색 파라미터 확인 - 키워드: '{}', 타입: '{}'", searchKeyword, searchType);
		
		List<FreeBoardDTO> boardList = new ArrayList<>();
		
		try {
			if("title".equals(searchType)) {
				logger.debug("제목 검색 실행");
				boardList = boardService.searchTitleBoardList(searchKeyword);
				
			} else if("contents".equals(searchType)) {
				logger.debug("내용 검색 실행");
				boardList = boardService.searchContentsBoardList(searchKeyword);
				
			} else {
				logger.warn("잘못된 검색 타입 - 타입: '{}'", searchType);
				return new ArrayList<>();
			}
			
			// null 체크
			if (boardList == null) {
				boardList = new ArrayList<>();
			}
			
			if (boardList.isEmpty()) {
				logger.info("검색 결과 없음 - 타입: {}, 키워드: '{}'", searchType, searchKeyword);
			} else {
				logger.info("검색 완료 - 타입: {}, 키워드: '{}', 결과 수: {}", 
						   searchType, searchKeyword, boardList.size());
			}
			
		} catch (Exception e) {
			logger.error("검색 중 오류 발생 - 타입: {}, 키워드: '{}', 오류: {}", 
					    searchType, searchKeyword, e.getMessage(), e);
			boardList = new ArrayList<>();
		}
		
		return boardList;
	}
}