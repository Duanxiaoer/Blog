<%@ page import="java.io.PrintWriter" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Contact Me</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">

    <!-- Bootstrap styles -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
  

    <!-- Font-Awesome -->
    <link rel="stylesheet" href="css/font-awesome/css/font-awesome.min.css">

    <!-- Google Webfonts -->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600|PT+Serif:400,400italic' rel='stylesheet' type='text/css'>

    <!-- Styles -->
  <link rel="stylesheet" href="css/style.css" id="theme-styles">



    <%
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        String status = (String) session.getAttribute("status");
        if (status == null){
            PrintWriter printWriter = response.getWriter();
            printWriter.print("<script>alert('It is wrong !');window.location = 'index.html';</script>");
        }
    %>
</head>
<body>
    <header>
    <div class="widewrapper masthead">
        <div class="container" style="width: 95%">
            <a href="index.html" id="logo">
                <img src="img/logo.png" alt="clean Blog">
            </a>

            <div id="mobile-nav-toggle" class="pull-right">
                <a href="#" data-toggle="collapse" data-target=".clean-nav .navbar-collapse">
                    <i class="fa fa-bars"></i>
                </a>
            </div>

            <nav class="pull-right clean-nav" style="float:right;">
                <div class="collapse navbar-collapse">
                    <ul class="nav nav-pills navbar-nav">

                        <li>
                            <a href="index.html">Home</a>
                        </li>
                        <li>
                            <a href="write.jsp">Write</a>
                        </li>
                        <!--<li>-->
                            <!--<a href="source.jsp">Contact</a>-->
                        <!--</li>-->
                    </ul>
                </div>
            </nav>

        </div>
    </div>

    <div class="widewrapper subheader">
        <div class="container" style="width: 95%">
            <div>
                <div class="clean-breadcrumb">
                    <a href="#">Source</a>
                </div>
            </div>

            <div class="clean-searchbox">
                <form action="#" method="get" accept-charset="utf-8">
                    <input class="searchfield" id="searchbox" type="text" placeholder="Search">
                    <button class="searchbutton" type="submit">
                        <i class="fa fa-search"></i>
                    </button>
                </form>
            </div>
        </div>
    </div>
</header>

    <div class="widewrapper main">
        <div class="container" style="width: 95%">
            <div class="row">
                <div class="col-md-3 clean-superblock" id="">
                    <h2>Upload Source</h2>
                    <form id="form" method="post" enctype="multipart/form-data" action="../UploadFile">
                        <input name="source_file" class="form-control input-lg" type="file" required>
                        <button class="btn btn-xlarge btn-clean-one col-md-offset-4" style="margin-top: 10px"  type="submit">Submit</button>
                    </form>
                </div>
                <div class="col-md-9 clean-superblock " id="source-list">
                    <h2>Source List</h2>
                    <ul id="source_list">
                        <li style="margin-bottom: 5px;"><a href="img/1.jpg"><img src="img/1.jpg" style="width: 50px;height: 50px;margin-right: 20px"></a>url：</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

   <footer>
        <div class="widewrapper footer">
            <div class="container"  style="width: 95%">
                <div class="row">
                    <div class="col-md-4 footer-widget">
                        <h3> <i class="fa fa-user"></i>About</h3>

                       <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab animi laboriosam nostrum consequatur fugiat at, labore praesentium modi, quasi dolorum debitis reiciendis facilis, dolor saepe sint nemo, earum molestias quas.</p>
                       <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorum, error aspernatur assumenda quae eveniet.</p>
                    </div>

                    <div class="col-md-4 footer-widget">
                        <h3> <i class="fa fa-pencil"></i> Recent Post</h3>
                        <ul class="clean-list">
                            <li><a href="">Clean - Responsive HTML5 Template</a></li>
                            <li><a href="">Responsive Pricing Table</a></li>
                            <li><a href="">Yellow HTML5 Template</a></li>
                            <li><a href="">Blackor Responsive Theme</a></li>
                            <li><a href="">Portfolio Bootstrap Template</a></li>
                            <li><a href="">Clean Slider Template</a></li>
                        </ul>
                    </div>

                    <div class="col-md-4 footer-widget">
                        <h3> <i class="fa fa-envelope"></i>Contact Me</h3>

                        <p>Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet.</p>
                        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nihil fugiat, cupiditate veritatis excepturi tempore explicabo.</p>
                         <div class="footer-widget-icon">
                            <i class="fa fa-facebook"></i><i class="fa fa-twitter"></i><i class="fa fa-google"></i>
                        </div>
                    </div>
                   
                </div>
            </div>
        </div>
        <div class="widewrapper copyright">
                Copyright 2015
        </div>
    </footer>
    
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/modernizr.js"></script>

    <script>

        $.ajax({
            url:"/DispatcherControl",
            type:"POST",
            dataType:"JSON",
            success(data){
                var ul_html = "";
                for (var tmp in data){
                    ul_html += '<li style="margin-bottom: 5px;"><a href="blogs/source/'+data[tmp]+'"><img src="blogs/source/'+data[tmp]+'" style="width: 50px;height: 50px;margin-right: 20px"></a>url：blogs/source/'+data[tmp]+'</li>\n'
                }

                document.getElementById("source_list").innerHTML = ul_html;
            }
        })
    </script>

</body>
</html>