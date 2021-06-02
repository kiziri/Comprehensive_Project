console.log("js result");


window.onload = function () {
//json 처리
    var json = JSON.parse(result_json)
//좌표 설정
    for (i = 0; i < json.length; i++) {
        name = "face" + i
        x = json[i].face_local.local_x;
        y = json[i].face_local.local_y;
        w = json[i].face_local.local_w;
        h = json[i].face_local.local_h;
        var divDemo = document.createElement('div');
        divDemo.setAttribute("id", name)
        $(divDemo).css("position", "absolute");
        $(divDemo).css("top", y + "px");
        $(divDemo).css("left", x + "px");
        $(divDemo).css("width", w + "px");
        $(divDemo).css("height", h + "px");
        $(divDemo).css("border", "2px solid black");
        $(divDemo).css("opacity", "0.5");
        $(".imagePreview").append(divDemo)
    }
//결과 삽입
    const message_span = document.querySelector("#message");
    message_span.innerHTML = JSON.stringify(json, null, 2);
}





