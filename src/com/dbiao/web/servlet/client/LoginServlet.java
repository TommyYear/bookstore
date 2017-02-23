package com.dbiao.web.servlet.client;

import java.io.IOException;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbiao.model.User;
import com.dbiao.service.UserService;

/**
 * Notes:
 * 		该类完成用户登陆操作
 * @author TommyYear
 *
 */
public class LoginServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1、获取登陆页面输入的用户名和密码
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		//2、调用service完成登陆操作
		UserService service=new UserService();
		try {
			User user = service.login(username, password);
			//3、登陆成功，将用户存储到session中
			req.getSession().setAttribute("user", user);
			//获取用户的角色，其中用户的角色分为普通和超级用户
			String role = user.getRole();
			//如果用超级用户，就进入网上书城的后台管理系统，否则账户页面
			if ("超级用户".equals(role)) {
				//重定向到后台
				resp.sendRedirect(req.getContextPath() + "/admin/login/home.jsp");
			}else{
				resp.sendRedirect(req.getContextPath()+"/client/myAccount.jsp");
				
			}
		} catch (LoginException e) {
			//如果出现问题，将错误信息存储到request范围，并跳转会登陆页面
			e.printStackTrace();
			req.setAttribute("register_message", e.getMessage());
			req.getRequestDispatcher("/client/login.jsp").forward(req, resp);
		}
	}
}
