package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.model.UserDAO;
import com.dgut.salesmanagementsystem.pojo.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {

    private UserDAO userDAO;
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 假设这里通过 DAO 从数据库校验用户信息
        User user = userDAO.validateUser(username, password);

        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user); // 保存用户信息到 Session

            // 根据角色重定向到不同的页面
            switch (user.getRole()) {
                case SALES_MANAGER:
                    resp.sendRedirect("dashboard/dashboard_sales_manager.jsp");
                    break;
                case WAREHOUSE_MANAGER:
                    resp.sendRedirect("dashboard/dashboard_warehouse_manager.jsp");
                    break;
                case SALES_MAN:
                    resp.sendRedirect("dashboard/dashboard_sales_man.jsp");
                    break;
                default:
                    resp.sendRedirect("login.jsp?error=invalidRole");
            }
        } else {
            resp.sendRedirect("login.jsp?error=invalidCredentials");
        }
    }


}
