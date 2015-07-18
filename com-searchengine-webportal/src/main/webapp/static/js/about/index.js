/**
 * Created by Frank on 12/18/14.
 */
//var logs=[{
//    "name":"云校排课-阿尔法3.0",
//    "time":"15-01-20",
//    "details":[
//        "1. 开放注册，云校排课正式面向所有学校；",
//        "2. 支持课表导入，灵活使用云校排课进行课程调整；",
//        "3. 优化调课体验，方便用户决策判断；",
//        "4. 算法优化，修复和完善了排课时“未知错误”和“无法找到初始解”的bug,增加了中间解，更加稳定可靠"
//    ]}, {
//    "name":"云校排课-阿尔法2.0",
//    "time":"14-12-26",
//    "details":[
//        "1. 算法优化，缩短排课时间，达到Lv2标准；",
//        "2. 新增规则支持不同教师不同班级合班；",
//        "3. 新增规则支持设置专业教室最大合班数；",
//        "4. 新增课时分布功能，更直观地查看排课效果；",
//        "5. 调整课表下载格式，方便直接打印；"
//    ]}, {
//    "name":"云校排课-阿尔法1.0",
//    "time":"14-12-20",
//    "details":[
//        "1. 新增班级不排课，课程不排课，教师不排课设置；",
//        "2. 新增教案平齐功能；",
//        "3. 新增重要科目，重要时段优先安排功能；",
//        "4. 新增连堂课设置功能；",
//        "5. 优化基础信息导入；"
//    ]}
//];
var logArea = $("#logsArea");
$(function() {
    makeLogs();
    $(".log-name").first().append("<span class='text-red'>(NEW)</span>");
    $(".log-detail-outer").first().css("display","block");
    $(".log-header").first().removeClass("border-bottom-toggle");
    logArea.delegate("div.log-header","click",logHeaderClick);

});
function logHeaderClick(){
    $(this).toggleClass("border-bottom-toggle");
    var index = $(this).parent().attr("id");
    $("artical#"+index).find(".log-detail-outer").slideToggle();
}
/**
 * create the logs
 */
function makeLogs(){
    var html=new Array();
    for(var i=0;i<logs.length;i++){
        html.push("<artical id='"+i+"'>");
        html.push("<div class='log-header border-bottom-toggle' id='name_"+i+"'>");
        html.push("<span class='log-name'>"+logs[i].name+"</span>");
        html.push("<span class='log-time'>"+logs[i].time+"</span>");
        html.push("</div>");
        html.push("<div class='log-detail-outer'>");
        html.push("<ol class='log-detail nolist'>");
        for(var j=0;j<logs[i].details.length;j++){
            html.push("<li>"+logs[i].details[j]+"</li>");
        }
        html.push("</ol>");
        html.push("</div>");
        html.push("</artical>");
    }
    logArea.html(html.join(""));
}


