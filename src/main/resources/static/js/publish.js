var tag = document.querySelector("#tag");
var tabNav = document.querySelector("#nav-tabs");
//querySelector选取类选择器的时候默认选取第一个，querySelectorAll则会选取全部
var tagFirst = document.querySelector(".tab-pane");
var categorys = document.querySelectorAll(".category");

tag.addEventListener('focus',function () {
    tabNav.style.display = "block";
    //默认选中第一个标签页
    tagFirst.className = "tab-pane active";
    categorys[0].className = "category active";
});
function selectTag(e){
    var value = e.getAttribute("data-tag");
    var preview = $("#tag").val();
    if (preview.indexOf(value)==-1){
        if (preview){
           $("#tag").val(preview+","+value);
        } else{
           $("#tag").val(value);
        }
    }
}
