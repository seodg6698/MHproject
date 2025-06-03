package MHproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import MHproject.DTO.UserDTO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@GetMapping(value = "/main")
	public String mainPage(HttpSession session, Model model) {
		System.out.println("main page load OK.");
		UserDTO userid = (UserDTO) session.getAttribute("userid");
		model.addAttribute("userid", userid);
		return "/user/main";
	}
	
	@GetMapping(value = "/disorders")
	public String disordersPage() {
		System.out.println("disorders page load OK.");
		
		return "/user/disorders";
	}
	
	@GetMapping(value = "/medications")
	public String medicationsPage() {
		System.out.println("medications page load OK.");
		
		return "/user/medications";
	}
	
	
	@GetMapping(value = "/treatments")
	public String treatmentsPage() {
		System.out.println("treatments page load OK.");
		
		return "/user/treatments";
	}
	
	@GetMapping(value = "/signUp")
	public String signUpPage() {
		System.out.println("signUp page load OK.");
		
		return "/user/signUp";
	}
	
	

	
	
}