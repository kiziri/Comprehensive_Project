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
        }
        reader.readAsDataURL(f);
    });
}

console.log('js test demo')
