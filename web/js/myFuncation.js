//parse the query params
var query = window.location.search.substring(1);
var vars = query.split("&");
var title,id;//传递的参数
for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
    if(pair[0] === "title"){
        title =  pair[1];
    }
    if(pair[0] === "id"){
        id =  pair[1];
    }
}


function get_blog(){

    alert(title);
    $.ajax({
        url:"/DispatcherControl",
        type:"POST",
        data:{title:title,id:id},
        success(data){
            //show the blog
            document.getElementById("blog").innerHTML = marked(data);
        }
    })
}


var reply_to_id = "";
var reply_to_name = "";
function set_reply_comment_params(comment_id,comment_author) {
    reply_to_id = comment_id;
    reply_to_name = comment_author;
    window.location.href = "#create-comment";
}

function submit_comment() {

    var tell = document.getElementsByName("tell");
    var which_operation = "";

    for (var i = 0; i < tell.length; i++) { //遍历Radio
        if (tell[i].checked) {
            which_operation = tell[i].value;
        }
    }


    if (which_operation === "refresh"){
        var comment_name = document.getElementById("comment-name").value;
        // var comment_email = document.getElementById("comment-email").value;
        var comment_body = document.getElementById("comment-body").value;

        $.ajax({
            url:"/DatabaseControl",
            type:"POST",
            dataType:"JSON",
            data:{id:id,reply_to_id:reply_to_id,reply_to_name:reply_to_name,comment_body:comment_body,comment_name:comment_name},
            success(data){
                show_comment(data);
                document.getElementById("comment-body").value = "";
                alert("评论成功 :) ")
            }
        })
    }else {
        window.location = "https://www.baidu.com/s?wd=%E5%A6%82%E4%BD%95%E6%89%BE%E5%A5%B3%E6%9C%8B%E5%8F%8B&rsv_spt=1&rsv_iqid=0xd17a0a06002f47d1&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_dl=tb&rsv_sug3=23&rsv_sug1=20&rsv_sug7=101&rsv_t=0d19Wn5db1%2F8OgIZxFJQd7dhXKy3A6muqE5WDWxRL1j7ZV1hs4zeI5vFy5HEZrKoxl7u&rsv_sug2=0&inputT=8418&rsv_sug4=10781"
    }
}


function show_comment(data) {

    var comments = data.comments;

    var comments_html = "";

    var comments_count = 0;

    //显示评论
    for (var comment_position in comments){
        //评论数
        comments_count += 1;

        //恢复的缩进样式
        var  reply = "";
        //回复谁
        var  reply_to_name = "";
        //是否是回复
        if (comments[comment_position].reply !== comments[comment_position].id){
            reply = "reply";
            reply_to_name = '<span style="color: #2a6496;margin-right: 5px">@'+comments[comment_position].reply_to_name+'</span>'
        }

        comments_html += '' +
            '<article class="comment '+reply+'">\n' +
            '                                <header class="clearfix">\n' +
            '                                    <img src="img/avatar.png" alt="A Smart Guy" class="avatar">\n' +
            '                                    <div class="meta" onclick="set_reply_comment_params(\''+comments[comment_position].id+'\',\''+comments[comment_position].author+'\')" >\n' +
            '                                        <h3><a href="#">'+comments[comment_position].author+'</a></h3>\n' +
            '                                        <span class="date">\n' +
            '                                        '+comments[comment_position].time+'\n' +
            '                                    </span>\n' +
            '                                        <span class="separator">\n' +
            '                                        -\n' +
            '                                    </span>\n' +
            '\n' +
            '                                        <a href="#creat-comment" class="reply-link">Reply</a>\n' +
            '                                    </div>\n' +
            '                                </header>\n' +
            '                                <div class="body">\n' +
            '                                    '+reply_to_name+comments[comment_position].content+'\n' +
            '                                </div>\n' +
            '                            </article>\n' +
            '';

    }

    //评论数
    document.getElementById("comments_number").innerHTML = '<i class="fa fa-comments"></i> '+ comments_count + " Comments";
    //评论
    document.getElementById("comments").innerHTML = comments_html;
}



function get_comments(){
    $.ajax({
        url:"/DatabaseControl",
        type:"POST",
        dataType:"JSON",
        data:{id:id},
        success(data){
            show_comment(data);
        }
    })
}

get_blog();
get_comments();


