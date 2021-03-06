package ua.lviv.lgs.servlet;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.lviv.lgs.domain.product.Product;
import ua.lviv.lgs.domain.user.User;
import ua.lviv.lgs.dto.BucketDto;
import ua.lviv.lgs.service.dao.UserService;

@WebServlet("/buckets")
public class BucketsServlet extends HttpServlet {

	private static final long serialVersionUID = -7884991345661641441L;

	private static final UserService userServiceImpl = UserService.getUserService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = userServiceImpl.getById(request.getSession().getAttribute("userId").toString());

		String json = new ObjectMapper().writeValueAsString(toDto(user, user.getBucket().getProducts()));

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	private Set<BucketDto> toDto(User user, List<Product> allProducts) {
		Set<BucketDto> bucketsDtos = new HashSet<>();

		for (Product p : allProducts) {
			bucketsDtos.add(new BucketDto(user.getId().toString(), p.getPhoto().getContent(), p.getId(), p.getName(),
					p.getPrice(), user.getBucket().findQttyByProdId(p.getId()).getQtty()));
		}

		return bucketsDtos;
	}

}
