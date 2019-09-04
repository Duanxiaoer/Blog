package xyz.duanwangye.blogs.tools;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class UploadFile extends HttpServlet{

    Database database = new Database();

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB


    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        PrintWriter printWriter = response.getWriter();
        String title  = request.getParameter("title");

        if (title != null){
            String summary  = request.getParameter("summary");
            String tags  = request.getParameter("tags");
            String blog  = request.getParameter("blog");
            int id  = Integer.parseInt(request.getParameter("id"));

            String blog_directory_path = request.getServletContext().getRealPath("./")+File.separator+"blogs";
            String blog_md_file_path = blog_directory_path+File.separator+title+".md";

            System.out.println(blog_md_file_path);

            File blog_md_file = new File(blog_md_file_path);

            if (!blog_md_file.exists()){
                blog_md_file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(blog_md_file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(blog);
            bufferedWriter.flush();

            bufferedWriter.close();
            fileWriter.close();

            database.connect_to_database();
            //根据id判断是新增还是修改
            if (id<0){
                String value_to_insert = "0,'"+title+"','"+tags+"',"+System.currentTimeMillis()+",'"+summary+"'";
                database.insert("blogs_publish_record",value_to_insert);
            }else {
                String value_to_update = " title='"+title+"',summary='"+summary+"',tag='"+tags+"' ";
                database.update("blogs_publish_record",value_to_update,"id="+id);
            }

        }else if (ServletFileUpload.isMultipartContent(request)){


            // 配置上传参数
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
            factory.setSizeThreshold(MEMORY_THRESHOLD);
            // 设置临时存储目录
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

            ServletFileUpload upload = new ServletFileUpload(factory);

            // 设置最大文件上传值
            upload.setFileSizeMax(MAX_FILE_SIZE);

            // 设置最大请求值 (包含文件和表单数据)
            upload.setSizeMax(MAX_REQUEST_SIZE);

            // 中文处理
            upload.setHeaderEncoding("UTF-8");

            // 构造临时路径来存储上传的文件
            // 这个路径相对当前应用的目录
            String uploadPath = request.getServletContext().getRealPath("./") + File.separator + "blogs"+File.separator+"source";

            // 如果目录不存在则创建
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }



            try {
                // 解析请求的内容提取文件数据
                @SuppressWarnings("unchecked")
                List<FileItem> formItems = upload.parseRequest(request);
                if (formItems != null && formItems.size() > 0) {

                    // 迭代表单数据
                    for (FileItem item : formItems) {
                        // 处理不在表单中的字段
                        if (!item.isFormField()) {
                            String fileName = new File(item.getName()).getName();
                            String filePath = uploadPath + File.separator + fileName;
                            File storeFile = new File(filePath);
                            // 在控制台输出文件的上传路径
                            // 保存文件到硬盘
                            item.write(storeFile);
                        }
                    }
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            printWriter.print("<script>alert('Upload source file succeed !');window.location = '/source.jsp';</script>");
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        doPost(req,resp);
    }
}