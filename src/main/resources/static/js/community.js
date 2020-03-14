$("#comment_submit").click(function () {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    if (!content){
        alert("评论内容不能为空");
        return;
    }
    $.ajax({
        url:"/comment",
        type:"post",
        contentType:"application/json",
        data:JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        dataType:"json",
        success:function(response){
            if (response.code==200){
                $("#comment_section").hide();
                location.reload();
            }else{
                if (response.code==2007){
                    alert(response.message);
                }
                if (response.code==2003){
                    //确认是否登录
                    var isAccepted = confirm(response.message);
                    if (isAccepted==true){
                        window.open("https://github.com/login/oauth/authorize?client_id=d3c547072be67a1eb3ba&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                }
            }
        }
    });
});