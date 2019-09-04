package xyz.duanwangye.blogs.control;

import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class DispatcherControl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");

        PrintWriter printWriter = response.getWriter();


        String who_request = request.getHeader("REFERER");

        if (who_request.contains("single.html") || who_request.contains("write.jsp")){//request the blog
            String title = request.getParameter("title");
            String filepath = request.getServletContext().getRealPath("./")+File.separator+"blogs"+File.separator+title+".md";
            System.out.println(filepath);
            File blog_md_file = new File(filepath);

            //when the requested page do not exists show the error page
            if (!blog_md_file.exists()){
                blog_md_file = new File(request.getServletContext().getRealPath("./")+File.separator+"blogs"+File.separator+"error"+".md");
            }

            FileReader reader = new FileReader(blog_md_file);
            BufferedReader bufferedReader = new BufferedReader(reader);



            String blog_text = "";
            String line = "";
            while ( (line = bufferedReader.readLine()) != null){
                blog_text += line+"\n";
            }

            bufferedReader.close();
            reader.close();

            //return the markdown file
            printWriter.print(blog_text);
        }else if (who_request.contains("source.jsp")){//request the source files

            String uploadPath = request.getServletContext().getRealPath("./") + File.separator + "blogs"+File.separator+"source";

            File fileDir = new File(uploadPath);
            if (!fileDir.exists()){
                fileDir.mkdirs();
            }

            String[] filelist = fileDir.list();

            JSONObject source_list = new JSONObject();

            for (int i=0;i<filelist.length;++i){
                source_list.put(String.valueOf(i),filelist[i]);
            }

            //return the source files
            printWriter.print(source_list);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
