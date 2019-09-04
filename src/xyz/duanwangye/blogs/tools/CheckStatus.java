package xyz.duanwangye.blogs.tools;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CheckStatus extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        String pwd = req.getParameter("password");

        System.out.println(pwd);
        if (pwd.equals("YOUR_PASSWORD")){
            HttpSession session = req.getSession();
            session.setAttribute("status","pass");
            resp.getWriter().print("<script>window.location = 'write.jsp'</script>");
        }else {
            resp.getWriter().print("<script>alert('It is wrong !');window.location = 'index.html'</script>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
