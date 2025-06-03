package MHproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import MHproject.DTO.LoginDTO;
import MHproject.DTO.UserDTO;
import MHproject.service.LoginService;
import MHproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping(value = "/login")
public class LoginController {
	
	 @Autowired
	    private LoginService loginService;
	 
	 @Autowired
	 	private UserService userService;
	
	 @Autowired
	 public void LoginControll(LoginService loginService) {
		 this.loginService = loginService;
	 }
	
	 
	@GetMapping(value = "/login")
	public String loginPage() {
		System.out.println("loginPage load OK.");
		
		return "/login/loginPage";
	}
	
	
	 
	
	// 회원가입 처리
    @PostMapping(value = "/register")
    @ResponseBody
    public Map<String, String> register(@RequestParam(value="userId") String userId,
    		@RequestParam(value="password") String password,
    		@RequestParam(value="email") String email) {
    	
    	System.out.println(userId);
    	System.out.println(password);
    	System.out.println(email);
    	
    	loginService.getRegister(userId,password,email);
    	
    	 Map<String, String> response = new HashMap<>();
    	    response.put("message", "회원가입이 완료되었습니다.");
    	    response.put("status", "success");
    	    
    	    return response;
    }
    
    
    //로그인 처리
    @PostMapping("/api/login")
    @ResponseBody
    public Map<String, Object> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        UserDTO user = userService.login(loginDTO.getUserid(), loginDTO.getPassword());
        
        if (user != null) {
            // 세션에 사용자 정보 저장
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(120 * 60); 
            
            result.put("success", true);
            result.put("message", user.getUserid() + "님 환영합니다!");
            result.put("redirectUrl", "/user/main");
        } else {
            result.put("success", false);
            result.put("message", "아이디 또는 비밀번호가 잘못되었습니다.");
        }
        
        return result;
    }
    
    
    
    //로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/main";
    }
    
    
  //아이디 중복체크
  	@PostMapping("/idCheck")
  	@ResponseBody
  	public String idCheck(HttpServletRequest request,
  			@RequestParam(name="userId") String userId) {
  		
  		System.out.println(userId);
  		
  		int cnt = loginService.idCheck(userId);
  		
  		System.out.println(cnt);
  		
  		String msg = null;
  		
  		if(cnt == 0) {
  			msg = "사용가능한 아이디 입니다.";
  		}else{
  			msg = "아이디 중복 다른아이디를 사용해주세요.";
  		}
  		return msg;
    
   }
  	
}
