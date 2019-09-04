<%@ page import="java.io.PrintWriter" %><%--
  Created by IntelliJ IDEA.
  User: duanqifeng
  Date: 2019/8/4
  Time: 4:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>checkStatus</title>
</head>
<body>

<%
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");

    String status = (String) session.getAttribute("status");
    if (status !=null){
        PrintWriter printWriter = response.getWriter();
        printWriter.print("<script>window.location = 'write.jsp';</script>");
    }
%>

<form method="POST" action="/CheckStatus">
    <input name="password" type="password">
    <button type="submit" value="提交">提交</button>
</form>
</body>

</html>
