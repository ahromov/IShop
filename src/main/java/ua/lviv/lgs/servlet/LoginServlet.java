package ua.lviv.lgs.servlet;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.lviv.lgs.domain.User;
import ua.lviv.lgs.dto.UserLogin;
import ua.lviv.lgs.service.impl.UserServiceImpl;
import ua.lviv.lgs.shared.MailSender;

@WebServlet(value = "/login", loadOnStartup = 1)
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1115455152301778383L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = UserServiceImpl.getUserService().getUserByEmail(email);

		if (user != null) {
			if (user.getPassword().equals(password)) {
				HttpSession session = request.getSession(true);
				session.setAttribute("userId", user.getId());
				session.setAttribute("userName", user.getFirstName());
				session.setAttribute("role", user.getRole().toString());

				UserLogin userLogin = new UserLogin();
				userLogin.userEmail = user.getEmail();
				userLogin.destinationUrl = "cabinet.jsp";

				ObjectMapper objectMapper = new ObjectMapper();
				String json = objectMapper.writeValueAsString(userLogin);

				response.setContentType("application/json");
				response.getWriter().write(json);
			} else {
				response.setContentType("text/html");
				response.getWriter().write("InvalidPassword");
			}
		} else {
			response.setContentType("text/html");
			response.getWriter().write("NotExists");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		String email = request.getParameter("email");
		User user = UserServiceImpl.getUserService().getUserByEmail(email);

		if (user != null) {
			try {
				MailSender.getMailSender().sendMail(email, "Hello " + user.getFirstName(),
						"Your password " + user.getPassword());

				response.getWriter().write("PasswordSended");
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		} else {
			response.getWriter().write("NotExists");
		}
	}

}
