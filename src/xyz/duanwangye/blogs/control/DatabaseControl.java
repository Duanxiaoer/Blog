package xyz.duanwangye.blogs.control;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.util.calendar.BaseCalendar;
import xyz.duanwangye.blogs.tools.Database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseControl extends javax.servlet.http.HttpServlet {

    private Database db = new Database();
    private JSONObject jsions = new JSONObject();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        response.setCharacterEncoding("utf-8");

        PrintWriter printWriter = response.getWriter();

        db.connect_to_database();
        //返回的json数组

        //who request the source
        String who_request_url = request.getHeader("REFERER");

        if (who_request_url.contains("index.html") || who_request_url.equals("http://localhost:8080/")){



                ResultSet type_resultSet = db.select("type","1=1","*");
                ResultSet blogs_record_resultSet = db.select("blogs_publish_record","1=1","*");
                try {
                    //types
                    JSONObject type_jsion = new JSONObject();
                    //all bolgs records
                    JSONArray blogs_jsion = new JSONArray();

                    while(type_resultSet.next()){
                        type_jsion.put(type_resultSet.getString("type"),type_resultSet.getString("type"));
                    }

                    while(blogs_record_resultSet.next()){
                        //single blog record
                        JSONObject blog_jsion = new JSONObject();

                        blog_jsion.put("id",blogs_record_resultSet.getString("id"));
                        blog_jsion.put("title",blogs_record_resultSet.getString("title"));
                        blog_jsion.put("tag",blogs_record_resultSet.getString("tag"));
                        blog_jsion.put("time",blogs_record_resultSet.getString("time"));
                        blog_jsion.put("summary",blogs_record_resultSet.getString("summary"));

                        //put all blogs in a json array
                        blogs_jsion.add(blog_jsion);

                    }

                    //prepare the json array need to return
                    jsions.put("kindtypes",type_jsion);
                    jsions.put("blogsrecords",blogs_jsion);

                }catch (Exception e){
                    e.printStackTrace();
                }


        }else if (who_request_url.contains("single.html")){

            String blog_id = request.getParameter("id");
            String relpy_to_name = request.getParameter("reply_to_name");


            DateFormat dateFormat = new SimpleDateFormat("yyyy-yy-yy");
            int time_stamp = (int) (System.currentTimeMillis()%100000000);
            String time = dateFormat.format(System.currentTimeMillis());
            System.out.println(time+"："+time_stamp);

            if (relpy_to_name != null){//submit comment and refresh comments

                int relpy_to_id = 0;

                String comment_name = request.getParameter("comment_name");
                String comment_body = request.getParameter("comment_body");

                String value_to_insert = "";

                if (relpy_to_name.equals("")){
                    //timestamp is id,if the comment is not a reply ,it's id should be equal with it's reply
                    relpy_to_id = time_stamp;
                    value_to_insert = ""+time_stamp+",'"+comment_name+"','"+time+"','"+comment_body+"',"+blog_id+","+relpy_to_id+",'"+relpy_to_name+"'";
                }else {
                    relpy_to_id = Integer.parseInt(request.getParameter("reply_to_id"));
                    value_to_insert = ""+time_stamp+",'"+comment_name+"','"+time+"','"+comment_body+"',"+blog_id+","+relpy_to_id+",'"+relpy_to_name+"'";
                }
                db.insert("comments",value_to_insert);

                put_comment_in_json_back(blog_id);

            }else {//request comments
                put_comment_in_json_back(blog_id);
            }
        }

        //return json array
        printWriter.print(jsions);

    }



    private void put_comment_in_json_back(String blog_id){
        ResultSet commnents_resultSet = db.select("comments","blog="+blog_id+" group by reply,id,author,blog,content","*");

        try{

            //all comments
            JSONObject comments_jsion = new JSONObject();

            //the comment need to be sorted so add a variable to keep sorted
            int position = 0;
            while(commnents_resultSet.next()){
                //single ccomment record
                JSONObject comment_jsion = new JSONObject();

                comment_jsion.put("id",commnents_resultSet.getString("id"));
                comment_jsion.put("author",commnents_resultSet.getString("author"));
                comment_jsion.put("time",commnents_resultSet.getString("time"));
                comment_jsion.put("content",commnents_resultSet.getString("content"));
                comment_jsion.put("reply",commnents_resultSet.getString("reply"));
                comment_jsion.put("reply_to_name",commnents_resultSet.getString("reply_to_name"));

                //put all comments in a json array
                comments_jsion.put("\""+position+"\"",comment_jsion);
                ++position;
            }

            jsions.put("comments",comments_jsion);


        }catch (Exception e){
            e.printStackTrace();
        }
    }




    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request,response);
    }
}
