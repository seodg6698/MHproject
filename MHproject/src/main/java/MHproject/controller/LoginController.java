package MHproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import MHproject.service.EmailVerificationService;
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
	private EmailVerificationService emailVerificationService;

	@Autowired
	public void LoginControll(LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping(value = "/login")
	public String loginPage() {
		System.out.println("loginPage load OK.");
		return "/login/loginPage";
	}

	/**
	 * 이메일 인증번호 발송
	 */
	@PostMapping(value = "/sendVerification")
	@ResponseBody
	public Map<String, Object> sendVerificationEmail(@RequestParam("email") String email) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			// 이메일 형식 검증
			if (email == null || email.trim().isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				response.put("success", false);
				response.put("message", "올바른 이메일 주소를 입력해주세요.");
				return response;
			}
			
			boolean sent = emailVerificationService.sendVerificationEmail(email);
			
			if (sent) {
				response.put("success", true);
				response.put("message", "인증번호가 발송되었습니다. 이메일을 확인해주세요.");
			} else {
				response.put("success", false);
				response.put("message", "이메일 발송에 실패했습니다. 다시 시도해주세요.");
			}
			
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "오류가 발생했습니다: " + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 이메일 인증번호 확인
	 */
	@PostMapping(value = "/verifyEmail")
	@ResponseBody
	public Map<String, Object> verifyEmail(
			@RequestParam("email") String email,
			@RequestParam("verificationCode") String verificationCode) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			boolean verified = emailVerificationService.verifyCode(email, verificationCode);
			
			if (verified) {
				response.put("success", true);
				response.put("message", "이메일 인증이 완료되었습니다.");
			} else {
				response.put("success", false);
				response.put("message", "인증번호가 일치하지 않거나 만료되었습니다.");
			}
			
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "인증 중 오류가 발생했습니다: " + e.getMessage());
		}
		
		return response;
	}
	
	// 회원가입 처리 (이메일 인증 확인 추가)
    @PostMapping(value = "/register")
    @ResponseBody
    public Map<String, String> register(
    		@RequestParam(value="userId") String userId,
    		@RequestParam(value="password") String password,
    		@RequestParam(value="email") String email,
    		@RequestParam(value="verificationCode", required = false) String verificationCode) {
    	
    	Map<String, String> response = new HashMap<>();
    	
    	try {
    		// 입력값 검증
    		if (userId == null || userId.trim().isEmpty()) {
    			response.put("message", "아이디를 입력해주세요.");
    			response.put("status", "error");
    			return response;
    		}
    		
    		if (password == null || password.trim().isEmpty()) {
    			response.put("message", "비밀번호를 입력해주세요.");
    			response.put("status", "error");
    			return response;
    		}
    		
    		if (email == null || email.trim().isEmpty()) {
    			response.put("message", "이메일을 입력해주세요.");
    			response.put("status", "error");
    			return response;
    		}
    		
    		// 이메일 인증 완료 여부 확인
    		if (!emailVerificationService.isEmailVerified(email)) {
    			response.put("message", "이메일 인증을 완료해주세요.");
    			response.put("status", "error");
    			return response;
    		}
    		
    		System.out.println("회원가입 요청 - ID: " + userId + ", Email: " + email + ", 인증번호: " + verificationCode);
    		
    		// 회원가입 처리
    		loginService.getRegister(userId, password, email);
    		
    		// 회원가입 완료 후 인증 정보 정리
    		emailVerificationService.clearVerification(email);
    		
    		response.put("message", "회원가입이 완료되었습니다.");
    		response.put("status", "success");
    		
    	} catch (Exception e) {
    		System.err.println("회원가입 처리 중 오류: " + e.getMessage());
    		e.printStackTrace();
    		response.put("message", "회원가입 중 오류가 발생했습니다: " + e.getMessage());
    		response.put("status", "error");
    	}
    	
    	return response;
    }

	// 로그인 처리
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
			result.put("message", "아이디 또는 비밀번로가 잘못되었습니다.");
		}

		return result;
	}

	// 로그아웃
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/user/main";
	}

	// 아이디 중복체크
	@PostMapping("/idCheck")
	@ResponseBody
	public String idCheck(HttpServletRequest request, @RequestParam(name = "userId") String userId) {

		System.out.println("아이디 중복체크: " + userId);

		int cnt = loginService.idCheck(userId);

		System.out.println("중복체크 결과: " + cnt);

		String msg = null;

		if (cnt == 0) {
			msg = "사용가능한 아이디 입니다.";
		} else {
			msg = "아이디 중복 다른아이디를 사용해주세요.";
		}
		
		return msg;
	}
}