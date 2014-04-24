package forwe.hexj.learn.mybatis.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import forwe.hexj.learn.mybatis.model.User;
import forwe.hexj.learn.mybatis.servcie.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {
	private IUserService userService;

	public IUserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@RequestMapping("/{id}")
	public @ResponseBody User showUser(@PathVariable int id, HttpServletRequest request) {
		User u = userService.getUserById(id);
		request.setAttribute("user", u);
		return u;
	}
}
