$('#image_up').submit(function() {
    var imgFile = $('#formFileLg').val();
    var fileForm = /(.*?)\.(jpg|jpeg|png|gif|bmp|pdf)$/;

        if ($('#formFileLg').val() == '') {
            alert('이미지가 없습니다.');
            return false;
        }
        else if (!imgFile.match(fileForm)) {
            alert("이미지 파일만 업로드 가능");
            return false;
        }
        
}); // end submit()

$(document).ready(function() {
    $("#formFileLg").on("change", handleImgFileSelect);
});

var sel_file;

function handleImgFileSelect(e) {
    var files = e.target.files;
    var filesArr = Array.prototype.slice.call(files);

    filesArr.forEach(function(f) {
        if(!f.type.match("image.*")) {
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
