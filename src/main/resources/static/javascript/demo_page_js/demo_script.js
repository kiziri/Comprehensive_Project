var sel_file;

$(document).ready(function() {
    $("#formFileLg").on("change", handleImgFileSelect);
});

function handleImgFileSelect(e) {
    var files = e.target.files;
    var filesArr = Array.prototype.slice.call(files);

    var reg = /(.*?)\/(jpg|jpeg|png|bmp)$/;

    filesArr.forEach(function(f) {
        if (!f.type.match(reg)) {
            alert("확장자는 이미지 확장자만 가능합니다.");
            return;
        }

        sel_file = f;

        var reader = new FileReader();
        reader.onload = function(e) {
            $("#image_view").attr("src", e.target.result);
            image_resize();
        }
        reader.readAsDataURL(f);
    });
}
function image_resize(){
    const imageX = $("#image_view").width();
    const imageY = $("#image_view").height();
    const maxX = $(".imagePreview").width();
    const maxY = $(".imagePreview").height();


    if(imageX>maxX) {
        const resultX=imageX-maxX
        const finalX= imageX-(resultX+20)
        const finalY= (finalX/16)*9
        $("#image_view").css("width", finalX+"px");
        $(".imagePreview").height(finalY+"px");
        $("#image_view").css("height", finalY+"px");
        $("#image_view").css("object-fit", "contain");
    }else if(imageY>maxY){
        const resultY=imageY-maxY
        const finalY= imageY-(resultY+20)
        const finalX= (finalY/16)*9
        $("#image_view").css("width", finalX+"px");
        $(".imagePreview").width(finalX+"px");
        $("#image_view").css("height", finalY+"px");
        $("#image_view").css("object-fit", "contain");
    }
}