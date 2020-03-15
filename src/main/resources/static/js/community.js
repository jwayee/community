//回复评论
$("#comment_submit").click(function () {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    var type = 1;
    comment(questionId, content, type);
});

//回复二级评论
function submitComment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $(".second-comment-inp").val();
    var type = 2;
    comment(commentId, content, type);
};

function comment(parentId, content, type) {
    if (!content) {
        alert("评论内容不能为空");
        return;
    }
    $.ajax({
        url: "/comment",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": parentId,
            "content": content,
            "type": type
        }),
        dataType: "json",
        success: function (response) {
            if (response.code == 200) {
                $("#comment_section").hide();
                location.reload();
                console.log(response);
            } else {
                if (response.code == 2007) {
                    alert(response.message);
                }
                if (response.code == 2003) {
                    //确认是否登录
                    var isAccepted = confirm(response.message);
                    if (isAccepted == true) {
                        window.open("https://github.com/login/oauth/authorize?client_id=d3c547072be67a1eb3ba&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                }
            }
        }
    });
}

//展开评论
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comment = $("#comment-" + id);
    comment.fadeToggle("in");
    $("div[parentId="+id+"]").remove();
//  记录点击状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠
        e.removeAttribute("data-collapse");
        e.classList.remove("aw-comment-wrap");
    } else {
        //展开
        e.setAttribute("data-collapse", "in");
        e.classList.add("aw-comment-wrap");
        $.ajax({
            url: "/comment/" + id,
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.code == 200) {
                    var comments = result.data;
                    console.log(comments);
                    //清空内容
                    $.each(comments, function (index, comment) {
                        var commentWrap = $("<div/>").addClass("aw-comment-list-wrap").attr("parentId",comment.parentId)
                            .prependTo("#comment-"+comment.parentId);
                        var awCommentList = $("<div></div>").addClass("aw-comment-list clearfix").appendTo(commentWrap);
                        var awCommentImg = $("<div></div>").addClass("aw-comment-img").appendTo(awCommentList);
                            $("<img>").attr("src",comment.user.avatarUrl).appendTo(awCommentImg);
                        var awCommentRight = $("<div></div>").addClass("aw-comment-right").appendTo(awCommentList);
                        var awCommentRightWrap = $("<div></div>").addClass("aw-comment-right-wrap").appendTo(awCommentRight);
                        var nameAndTime = $("<span></span>").addClass("nameandTime").appendTo(awCommentRightWrap);
                            $("<span></span>").addClass("username").html(comment.user.name+" · ")
                                .appendTo(nameAndTime);
                            $("<span></span>").addClass("time").html(moment(comment.gmtCreate).format("YYYY-MM-DD hh:mm:ss"))
                                .appendTo(nameAndTime);
                        var reply= $("<div></div>").addClass("reply").appendTo(awCommentRightWrap);
                            $("<a></a>").attr("href","").html("回复").appendTo(reply);
                        var deleteComment = $("<div></div>").addClass("reply").appendTo(awCommentRightWrap);
                            $("<a></a>").attr("href","").html("删除").appendTo(deleteComment);
                        var p = $("<p></p>").addClass("comment-content").html(comment.content)
                            .appendTo(awCommentRight);

                    })
                }
            }
        })

    }

}

$(".second-comment-inp").focus(function () {
    $(".comment2-submit").css("display", "block");
});
$(".cancel").click(function () {
    $(".comment2-submit").css({"display": "none"});
});
