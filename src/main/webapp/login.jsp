<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>登录 - 销售管理系统</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>
<div class="login-container">
    <h1>销售管理系统</h1>

    <form action="LoginController" method="post">
        <label for="username">用户名</label>
        <input type="text" id="username" name="username" required>
        <label for="password">密码</label>
        <input type="password" id="password" name="password" required>
        <button type="submit">登录</button>
    </form>
    <!-- 如果请求中有 error 参数，显示错误信息 -->

    <%
        String ErrorInfo = request.getParameter("error");
        out.println("<div style=\"color: red;\">");
        if(ErrorInfo != null) {
            if (ErrorInfo.equals("invalidRole"))
                out.println("<strong> 职位设置有误，请联系管理员修改。 </strong>");
            else if (ErrorInfo.equals("invalidCredentials"))
                out.println("<strong>无效的用户名或密码，请重试。 </strong>");
            else
                out.println("<strong>出现了未知的错误，请稍后再重试。 </strong>");
        }
        out.println("</div>");

    %>

</div>
</body>
</html>
