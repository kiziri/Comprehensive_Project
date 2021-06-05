window.onload = function () {

    const json = JSON.parse(result_json)

//좌표 설정
    var baseX = $(".imagePreview").width()
    var baseY = $(".imagePreview").height()
    var imageX = $("#image_view").width()
    var imageY = $("#image_view").height()
//이미지 div 공백 사이즈 계산
    var blankX = (baseX - imageX) / 2
    var blankY = (baseY - imageY) / 2

    for (i = 0; i < json.length; i++) {
        name = "face" + i
        x = json[i].face_local.local_x;
        y = json[i].face_local.local_y;
        w = json[i].face_local.local_w;
        h = json[i].face_local.local_h;

        //영역 div 생성
        const divDemo = document.createElement('div');
        divDemo.setAttribute("class", name)
        $(divDemo).css("position", "absolute");
        $(divDemo).css("left", x + blankX + "px");
        $(divDemo).css("top", y + blankY + "px");
        $(divDemo).css("width", w + "px");
        $(divDemo).css("height", h + "px");
        $(divDemo).css("border", "10px solid blue");
        $(divDemo).css("opacity", "0.5");

        //호버 div 생성
        const hoverdiv = document.createElement('div');
        const hname = "hover" + i;
        hoverdiv.setAttribute("class", hname)
        $(hoverdiv).css("position", "absolute")
        $(hoverdiv).css("left", x + blankX + w + 5 + "px");
        $(hoverdiv).css("top", y + blankY + h / 2 + "px");
        $(hoverdiv).css("display", "none")
        $(hoverdiv).css("padding", "5px")
        $(hoverdiv).css("background-color", "white")//연회색
        $(hoverdiv).append(
            "happy:" + json[i].face_emotion.happy + "<br>" +
            "sad: " + json[i].face_emotion.sad + "<br>" +
            "neutral: " + json[i].face_emotion.neutral + "<br>" +
            "angry: " + json[i].face_emotion.angry + "<br>" +
            "surprise: " + json[i].face_emotion.surprise + "<br>" +
            "fear: " + json[i].face_emotion.fear
        )
        //hover 설정
        $(divDemo).hover(
            function () {
                $(hoverdiv).css("display", "")
            },
            function () {
                $(hoverdiv).css("display", "none")
            }
        )
        $(".imagePreview").append(divDemo)
        $(".imagePreview").append(hoverdiv)
    }
    //결과 삽입
    const message_span = document.querySelector("#message");
    message_span.innerHTML = JSON.stringify(json, null, 2);
}

