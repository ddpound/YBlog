// 적용 잘되었는지 테스트 함수
function uploadTest(){

}


// 글 작성하러 가는 페이지
function skillBoardWriteGo(){
    location.href = "/skillboard/writepage"
}



function skillBoardWrite(){
    var skilltitle = $("#title").val()
    var content = $("#content").val()


    let form= {
        title  : skilltitle,
        content : content
    }



    $.ajax({
        type: "POST",
        url: "/skillboard/skillboardwrite",
        data: JSON.stringify(form),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function (resp){
        if(resp.data === 1 ){
            alert("글쓰기완료입니다 : "+resp.status)
            location.href = "/"
        } else{
            alert("글쓰기실패 : "+resp.status)
        }
    }).fail(function (){
        alert("글쓰기 실패 (문제확인 해주세요)")
    })





}


function skillBoarduploadSummernoteImageFile(file,editor){
    data = new FormData();
    data.append("file", file);

    $.ajax({
        data : data ,
        type : "POST" ,
        url : "/skillboard/temporarystorageImagefile",
        contentType : false ,
        processData : false ,
        success : function(data) {
            console.log(data.url)
            //항상 업로드된 파일의 url이 있어야 한다.
            $(editor).summernote('insertImage', data.url);
        }
    });



}




