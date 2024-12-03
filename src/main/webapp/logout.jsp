<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 获取当前会话对象并使其失效
    session.invalidate();

    // 重定向到登录页面
    response.sendRedirect("login.jsp");
%>