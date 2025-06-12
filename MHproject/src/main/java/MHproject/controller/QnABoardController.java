
package MHproject.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
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
import MHproject.service.QnABoardService;
import MHproject.DTO.QnABoardDTO;
import MHproject.DTO.CommentDto1;
import MHproject.DTO.Criteria;
import MHproject.DTO.PageMaker;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/board2")
public class QnABoardController {
	
	// Logger 선언
    private static final Logger logger = LoggerFactory.getLogger(FreeBoardController.class);


	@Autowired
	private QnABoardService boardService;
	
	@Autowired
	private CommentService1 commentService;

	
	
	@Autowired
	public void QnABoardService(QnABoardService boardService) {
		
		this.boardService = boardService;
	}
	
	@PostConstruct
    public void QnABoardControllerInit() {
        logger.info("FreeBoardControllerInit bean 생성");
    }

	
	/*
	//게시글 리스트 불러오기
	@GetMapping("/openBoardList.do")
	public ModelAndView openBoardList() throws Exception{
		ModelAndView mv = new ModelAndView("/boardList");
		
		List<BoardDto> list = boardService.selectBoardList();
		mv.addObject("list", list);
		
		
		System.out.println(list.toString());
		
		
		return mv;
	}
	*/

	//게시글 작성화면
	@GetMapping("/openBoardWrite2")
	public String openBoardWrite() throws Exception{
	
		return "/board/QnABoard/QnABoardWrite";
	}
	
	//게시글 작성 처리
	@PostMapping("/insertBoard2")
	public String insertBoard(QnABoardDTO board,HttpSession session, Model model) throws Exception{
		System.out.println(board.toString());
		boardService.insertBoard(board);
	
		return "redirect:/board2/openBoardList2";
	}
	
	//게시글 상세화면
	@GetMapping("/openBoardDetail2")
	public String openBoardDetail(@RequestParam int boardIdx, Model model) throws Exception {
	    // 기존 게시글 조회 로직
		QnABoardDTO board = boardService.selectBoardDetail(boardIdx);
	    
	    // 댓글 목록 조회 추가
	    List<CommentDto1> comments = commentService.selectCommentList(boardIdx);
	    
	    model.addAttribute("board", board);
	    model.addAttribute("comments", comments);
	    
	    return "/board/QnABoard/QnABoardDetail";
	}
	
	
	//게시글 수정처리
	@PostMapping("/updateBoard2")
	public String updateBoard(QnABoardDTO board) throws Exception{
		
		boardService.updateBoard(board);
		return "redirect:/board2/openBoardList2";
	}
	
	
	//게시글 삭제처리
	@PostMapping("/deleteBoard2")
	public String deleteBoard(int boardIdx) throws Exception{ 
		
		boardService.deleteBoard(boardIdx);
		return "redirect:/board2/openBoardList2";
  }
	
	
	
	//게시글 선택삭제
	@ResponseBody
	@PostMapping("/deleteSelection2")
	public String deleteSelection(HttpServletRequest request,
								@RequestParam(value="boardIdx[]") int[] boardIdx, QnABoardDTO board) throws Exception {
	
		System.out.println(Arrays.toString(boardIdx) + "::controller boardIdx");
		
		List<Integer> idxList = new ArrayList<>();
		for(int i=0; i<boardIdx.length; i++) {
			idxList.add(boardIdx[i]);
		}
		System.out.println(idxList  + "::controller idxList");
		
		boardService.selectAndDeleteBoard(idxList);
			
		
		String result = null;
		if(boardIdx.length == 0) {
			result = "게시글 선택요망";
		}else {
			result = "삭제 성공";
		}
		
	return result;
	}
	
	/*
		
			String[] ajaxMsg = request.getParameterValues("boardIdx");
			
		System.out.println(Arrays.toString(ajaxMsg) + ":: controller array");
			
			
			for(int i=0; i<ajaxMsg.length; i++) {
				boardService.selectAndDeleteBoard(ajaxMsg[i]);
			}
	 */
	//Map<Integer,Integer> map = new HashMap<Integer,Integer>();
	
	//System.out.println(Arrays.toString(boardIdx) + ":: controller array");
	/*
	List<Integer> idx = new ArrayList<>();
	
	for(int i=0; i<boardIdx.length; i++) {
	    idx.add(boardIdx[i]);
	}
	System.out.println(idx.size() + " :: listSize");
	
	System.out.println(idx.get(j) + ":: controller idxlist" + j);
	
	*/
	
		
		/*
		for(int i=0; i<boardIdx.length; i++) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
				
	
		paramMap.put("boardIdx", boardIdx[i]);
		
		
		System.out.println(paramMap + ":: controller map");
		
		
		
		}
		
		/*
		
		for(int i=0; i<boardIdx.length; i++) {
			paramMap.put("boardIdx", boardIdx[i]);
		}
		
		
		 for (Entry<String,Object> entrySet : paramMap.entrySet()) {
	            System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
	        }
	        */
	
		
		
		/*
		int [] checkidx;
		
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			System.out.println("[key]:" + entry.getKey() + ", [value]:" + entry.getValue());
			
			int checkNum = entry.getValue();
			System.out.println(checkNum + ":: controller");
			
			checkidx = {entry.getValue()};
			boardService.selectAndDeleteBoard(checkidx);
					
		}
		
		*/
		
	
		//boardService.selectAndDeleteBoard(map);
		
		
			/*
			for(int i=0; i<boardIdx.length; i++) {
				boardService.selectAndDeleteBoard(boardIdx[i]);
			}
			*/
        
	
	//페이징처리
	
	@GetMapping("/openBoardList2")
	public ModelAndView openBoardList(Criteria cri) throws Exception {
	        
	    ModelAndView mav = new ModelAndView("/board/QnABoard/QnABoardList");
	        
	    PageMaker pageMaker = new PageMaker();
	    pageMaker.setCri(cri);
	    pageMaker.setTotalCount(100);
	   
	    pageMaker.setEndPage(225);
	    
	    pageMaker.setTotalCount(boardService.countBoardListTotal());
	    
	   System.out.println(pageMaker.getStartPage()); 
	   System.out.println(pageMaker.getEndPage());
	        
	    List<Map<String,Object>> list = boardService.selectBoardList(cri);
	    mav.addObject("list", list);
	    mav.addObject("pageMaker", pageMaker);
	        
	    return mav;
	        
	}
	
	//검색처리
	@GetMapping("/boardList2")
	@ResponseBody
	public List<QnABoardDTO> searchView(HttpServletRequest request, 
									@RequestParam(value="type") String type,
									@RequestParam(value="keyword") String keyword) throws Exception {
		System.out.println(keyword + " ::controller");
		System.out.println(type + " ::controller");
		
		QnABoardDTO serchKey = new QnABoardDTO();
		serchKey.setKeyword(keyword);
		serchKey.setType(type);
		
		String serchKey1 = serchKey.getKeyword();
		String serchKey2 = serchKey.getType();
		
		System.out.println(serchKey1 + " ::keyword controller");
		System.out.println(serchKey2 + " ::type controller");
		
		
		List<QnABoardDTO> boardList = null;
		
		
	if(serchKey2.equals("title") ) {
		boardList = boardService.searchTitleBoardList(serchKey1);
			System.out.println(boardList.toString());
			if (boardList == null || boardList.isEmpty()) {
	            System.out.println("list is empty");
	        }
			 
		}else if(serchKey2.equals("contents") ){
			boardList = boardService.searchContentsBoardList(serchKey1);
			
			System.out.println(boardList.toString());
			if (boardList == null || boardList.isEmpty()) {
	            System.out.println("list is empty");
	        }
			
		}
		
	return boardList;
	}
	




}

	
	
	
	