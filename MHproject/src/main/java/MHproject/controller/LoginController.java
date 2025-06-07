package MHproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import MHproject.DTO.LoginDTO;
import MHproject.DTO.UserDTO;
import MHproject.service.AccountRecoveryService;
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
	private AccountRecoveryService accountRecoveryService;

	/**
	 * 로그인 페이지
	 */
	@GetMapping(value = "/login")
	public String loginPage() {
		System.out.println("loginPage load OK.");
		return "/login/loginPage";
	}

	/**
	 * 이메일 인증번호 발송 (회원가입용)
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
	 * 이메일 인증번호 확인 (회원가입용)
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
	
	/**
	 * 아이디 중복체크
	 */
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

	/**
	 * 회원가입 처리
	 */
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

	/**
	 * 로그인 처리
	 * @throws Exception 
	 */
	@PostMapping("/api/login")
	@ResponseBody
	public Map<String, Object> login(@RequestBody LoginDTO loginDTO, HttpSession session) throws Exception {
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

	/**
	 * 로그아웃
	 */
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/user/main";
	}

	/**
	 * 아이디 찾기
	 */
	@PostMapping(value = "/findUserId")
	@ResponseBody
	public Map<String, Object> findUserId(@RequestParam("email") String email) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			// 이메일 형식 검증
			if (email == null || email.trim().isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				response.put("success", false);
				response.put("message", "올바른 이메일 주소를 입력해주세요.");
				return response;
			}
			
			String userId = accountRecoveryService.findUserIdByEmail(email);
			
			if (userId != null && !userId.isEmpty()) {
				response.put("success", true);
				response.put("message", "아이디를 이메일로 발송했습니다.");
				response.put("userId", userId); // 실제 운영에서는 보안상 제거 권장
			} else {
				response.put("success", false);
				response.put("message", "해당 이메일로 등록된 계정을 찾을 수 없습니다.");
			}
			
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "아이디 찾기 중 오류가 발생했습니다: " + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 임시 비밀번호 발송
	 */
	@PostMapping(value = "/sendTempPassword")
	@ResponseBody
	public Map<String, Object> sendTempPassword(@RequestParam("email") String email) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			// 이메일 형식 검증
			if (email == null || email.trim().isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				response.put("success", false);
				response.put("message", "올바른 이메일 주소를 입력해주세요.");
				return response;
			}
			
			boolean sent = accountRecoveryService.sendTempPassword(email);
			
			if (sent) {
				response.put("success", true);
				response.put("message", "임시 비밀번호를 이메일로 발송했습니다.");
			} else {
				response.put("success", false);
				response.put("message", "해당 이메일로 등록된 계정을 찾을 수 없습니다.");
			}
			
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "임시 비밀번호 발송 중 오류가 발생했습니다: " + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 비밀번호 재설정 링크 발송
	 */
	@PostMapping(value = "/sendResetLink")
	@ResponseBody
	public Map<String, Object> sendResetLink(@RequestParam("email") String email) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			// 이메일 형식 검증
			if (email == null || email.trim().isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				response.put("success", false);
				response.put("message", "올바른 이메일 주소를 입력해주세요.");
				return response;
			}
			
			boolean sent = accountRecoveryService.sendPasswordResetLink(email);
			
			if (sent) {
				response.put("success", true);
				response.put("message", "비밀번호 재설정 링크를 이메일로 발송했습니다.");
			} else {
				response.put("success", false);
				response.put("message", "해당 이메일로 등록된 계정을 찾을 수 없습니다.");
			}
			
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "재설정 링크 발송 중 오류가 발생했습니다: " + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 비밀번호 재설정 페이지
	 */
	@GetMapping(value = "/resetPassword")
	public String resetPasswordPage(@RequestParam("token") String token, Model model) {
		boolean validToken = accountRecoveryService.validateResetToken(token);
		
		if (validToken) {
			String email = accountRecoveryService.getEmailByToken(token);
			model.addAttribute("token", token);
			model.addAttribute("email", email);
			return "/login/resetPasswordPage";
		} else {
			model.addAttribute("error", "유효하지 않거나 만료된 링크입니다.");
			return "/login/resetPasswordError";
		}
	}
	
	/**
	 * 새 비밀번호 설정
	 */
	@PostMapping(value = "/resetPassword")
	@ResponseBody
	public Map<String, Object> resetPassword(
			@RequestParam("token") String token,
			@RequestParam("newPassword") String newPassword) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			boolean success = accountRecoveryService.resetPassword(token, newPassword);
			
			if (success) {
				response.put("success", true);
				response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
			} else {
				response.put("success", false);
				response.put("message", "비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
			}
			
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "비밀번호 변경 중 오류가 발생했습니다: " + e.getMessage());
		}
		
		return response;
	}

	@GetMapping("/findAccount")
	public String findAccount() {
		System.out.println("findAccount page load OK.");
		
		return "/login/find";
	}
}