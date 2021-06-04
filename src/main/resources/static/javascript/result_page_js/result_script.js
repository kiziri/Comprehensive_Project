
window.onload = function () {
//json 처리
    var json = JSON.parse(result_json)

//좌표 설정
    var baseX= $(".imagePreview").width()
    var baseY= $(".imagePreview").height()
    var imageX = $("#image_view").width()
    var imageY = $("#image_view").height()
//이미지 div 공백 사이즈 계산
    var blankX = (baseX-imageX)/2
    var blankY = (baseY-imageY)/2


    for (i = 0; i < json.length; i++) {
        name = "face" + i
        x = json[i].face_local.local_x;
        y = json[i].face_local.local_y;
        w = json[i].face_local.local_w;
        h = json[i].face_local.local_h;
        var divDemo = document.createElement('div');
        divDemo.setAttribute("id", name)
        $(divDemo).css("position", "absolute");
        $(divDemo).css("left", x+blankX + "px");
        $(divDemo).css("top", y+blankY + "px");
        $(divDemo).css("width", w + "px");
        $(divDemo).css("height", h + "px");
        $(divDemo).css("border", "10px solid blue");
        $(divDemo).css("opacity", "0.5");
        $(".imagePreview").append(divDemo)
    }
//결과 삽입
    const message_span = document.querySelector("#message");
    message_span.innerHTML = JSON.stringify(json, null, 2);
}





