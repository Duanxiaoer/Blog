<%@ page import="java.io.PrintWriter" %>
<%@ page import="xyz.duanwangye.blogs.tools.Database" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>

<%
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");

    String status = (String) session.getAttribute("status");
    if (status == null){
        PrintWriter printWriter = response.getWriter();
        printWriter.print("<script>alert('It is wrong !');window.location = 'index.html';</script>");
    }
%>

<head>
    <meta charset="UTF-8">
    <title>write</title>
    <script src="js/jquery.min.js"></script>
    <script src="js/ace/ace.js"></script>
    <script src="js/marked.min.js"></script>
    <link href="css/markdown.css" rel="stylesheet" />
    <!-- Styles -->
    <link rel="stylesheet" href="css/style.css" id="theme-styles">

    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
            outline: none;
            border-radius: 0;
        }

        html,
        body {
            width: 100%;
            height: 100%;
            font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
            background-color: #ebebeb;
        }

        #bar {
            height: 130px;
            width: 100%;
            color: #fff;
        }

        #bar #topbar {
            height: 40px;
            background-color: #444;
            width: 100%;
            color: #fff;
            line-height: 50px;
        }

        #bar #czbar {
            height: 50px;
            background-color: #FFFFFF;
            width: 100%;
            color: #fff;
            line-height: 50px;
        }

        #bar #toolbar {
            height: 40px;
            background-color: #AAAAAA;
            width: 100%;
            color: #fff;
            line-height: 50px;
        }

        #container {
            overflow: auto;
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            top: 130px;
        }

        #editor-column,
        #preview-column {
            width: 49.5%;
            height: 100%;
            overflow: auto;
            position: relative;
            background-color: #F6F6F6;
        }

        .pull-left {
            float: left;
        }

        .pull-right {
            float: right;
        }

        #toolbar img {
            width: 25px;
            height: 25px;
            padding-top: 8px;
            padding-bottom: 7px;
            margin-left: 10px;
        }

        #toolbar img:hover {
            background: #EBEBEB;
            cursor: pointer;
        }

        #commit {
            color: #FFFFFF;
            padding: 5px 15px;
            background: #BE1A21;
            border: 0px none #FFECEC;
        }

        #commit:hover {
            cursor: pointer;
            background: #CB474D;
        }
    </style>

</head>

<body>
<div id='bar'>
    <div id="topbar">

    </div>
    <div id="czbar">
        <input style="font-size: 26px;width: 80%;padding-left: 10px;border: 0px none #FFFFFF;padding-top: 5px;" type="text"  id="title" placeholder="此处写文章标题" />

        <select id="tags">
            <option value="opel">java思想</option>
        </select>

        <button id="commit" onclick="publish_blog()">PUBLISH</button>
        <a href="index.html">HOME</a>
        <a href="source.jsp">SOURCE</a>

    </div>
    <div id="toolbar">
        <p style="font-size: larger;margin-left: 20px;margin-bottom: 10px">to the moon</p>
    </div>
</div>
<div id='container'>
    <div style="width: 100%;height: 100px">
        <textarea style="width: 100%;height: 100%"  id="summary" placeholder="此处写文章概要" ></textarea>
    </div>
    <div id='editor-column' class='pull-left'>
        <div id='panel-editor' style="height: 100%;">
            <!--编辑区-->
            <div class="editor-content" id="mdeditor" style="height: 100%;margin-top: 5px"></div>
        </div>
    </div>
    <div id='preview-column' class='pull-right'>
        <div id='panel-preview'>
            <!--显示区-->
            <div id="preview" class="markdown-body"></div>
        </div>
    </div>
</div>
</body>



<script>
    //新写时为-1，修改时为对应id
    var blog_id = -1;

    var acen_edit = ace.edit('mdeditor');//左侧编辑框
    acen_edit.setTheme('ace/theme/chrome');
    acen_edit.getSession().setMode('ace/mode/markdown');
    acen_edit.renderer.setShowPrintMargin(false);
    $("#mdeditor").keyup(function() {//给左侧编辑框添加事件，，当键盘抬起时，右侧实时显示左侧的md内容
        $("#preview").html(marked(acen_edit.getValue()));
    });


    function getTags() {
        $.ajax({
            url:"/DatabaseControl",
            type:"POST",
            dataType: "JSON",
            success : function(data){
                var tags = document.getElementById("tags");
                //tags
                var html = "";
                for (var key in data.kindtypes) {
                    html += "<option value='"+data.kindtypes[key]+"'>"+data.kindtypes[key]+"</option>\n"
                }
                tags.innerHTML=html;
            }
        })
    }

    function publish_blog() {

        var title = document.getElementById("title").value;
        var tags = document.getElementById("tags").value;
        var summary = document.getElementById("summary").value;
        var blog = acen_edit.getValue();
        var id = blog_id;

        if (title !== "" && blog !== "" && summary !== ""){

            $.ajax({
                url:"/UploadFile",
                type:"POST",
                data:{title:title,blog:blog,tags:tags,summary:summary,id:id},
                success(data){
                    alert("success")
                }
            })
        }
    }

    function get_blog(title,id){

        $.ajax({
            url:"/DispatcherControl",
            type:"POST",
            data:{title:title,id:id},
            success(data){
                //show the blog
                acen_edit.setValue(data);
            }
        })
    }


    <%
        String blog_id = request.getParameter("id");

        if (blog_id != null){
            Database db = new Database();
            db.connect_to_database();
            ResultSet blog_info = db.select("blogs_publish_record","id="+blog_id,"*");
            try {
                blog_info.next();
                String title = blog_info.getString("title");
                String tag = blog_info.getString("tag");
                String summary = blog_info.getString("summary");
                System.out.println(title+tag+summary);
    %>
    document.getElementById("title").value = "<%=title%>";
    document.getElementById("tags").innerHTML = '<option value="<%=tag%>"><%=tag%></option>';
    document.getElementById("summary").value = "<%=summary%>";
    blog_id = <%=blog_id%>;
    get_blog('<%=title%>',<%=blog_id%>);
    <%
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
    %>
    getTags();
    <%
        }
    %>

</script>

</html>