package com.film;



import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminLoginController {

	@GetMapping("/login")
    public String login() {
        return "adminlogin";
    }
    
	@GetMapping("/admin/logout")
	public String adminLogout(HttpServletRequest request) {
	    request.getSession().invalidate();
	    return "redirect:/";
	}

    
}
