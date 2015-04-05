/**
 * @Title: LoginController.java
 * @Package com.ezcloud.framework.controller
 * @Description: TODO
 * @author ez-cloud work group
 * @date 2014-7-14 下午06:48:33
 * @version V1.0
 */
package com.ezcloud.framework.controller;

import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.login.Login;
import com.ezcloud.framework.vo.Row;

/**
 * 系统用户登陆控制器
 * 
 * @ClassName: LoginController
 * @Description: TODO
 * @author ez-cloud work group
 * @date 2014-7-14 下午06:48:33
 */
@Controller("frameworkLoginController")
@RequestMapping("/login")
public class LoginController extends BaseController {

	@Resource(name = "frameworkLoginService")
	private Login loginService;

	@RequestMapping(value = "/login")
	public String login(String username, String password, String captcha, String isRememberUsername, String token ,RedirectAttributes redirectAttributes, HttpSession session, ModelMap model) {
		System.out.println("username:" + username);
		System.out.println("password:" + password);
		System.out.println("captcha:" + captcha);
		System.out.println("isRememberUsername:" + isRememberUsername);
		Assert.notNull(username, "username can not be null");
		Assert.notNull(password, "password can not be null");
		Assert.notNull(captcha, "captcha can not be null");
		String sessionCaptcha = (String) session.getAttribute("validateCode");
		boolean boolCode = captcha.toUpperCase().equals(sessionCaptcha);
		if (!boolCode) {
			model.addAttribute("error", message("framework.validatecode.error"));
			return "Login";
		}
		try {
			loginService.getRow().put("username", username);
			loginService.getRow().put("password", password);
			if(token == null || token.replace(" ", "").length() == 0){
				token ="";
			}
			token =URLDecoder.decode(token);
			loginService.getRow().put("token", token);
			loginService.login();
		} catch (JException e) {
			e.printStackTrace();
			model.addAttribute("error", e.getMsg());
			return "Login";
		}
		Row staff = loginService.getRow();
		session.setAttribute("staff", staff);
		// permissionService.getStaffPermission();
		// model.addAttribute("permission_list",
		// permissionService.getDataSet());
		// model.addAttribute("permission", "123");
		// return "/main/menu/main";
		return "redirect:/main/menu/main.do";
	}
}