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
    var description = $("#boardDescription").val()

    let form= {
        title  : skilltitle,
        content : content,
        description : description
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

function skillboardDelete(boardId){
    $.ajax({
        type: "DELETE",
        url: "/skillboard/delete/"+boardId,
        dataType: "json" // 받는 데이터
    }).done(function (resp){
        alert("삭제가 완료되었습니다.")
        location.href = "/"
    }).fail(function (resp){
        alert("삭제 실패")
    })

}

function gomodifyskillboard(boardId){
    location.href="/skillboard/gomodify/"+boardId

}

function  skillboardModify(boardId){
    var titleName= $("#title").val()
    var description = $("#boardDescription").val()

    if( titleName !=null){
        let form = {
            id : boardId,
            title : titleName,
            content : $("#content").val(),
            description : description
        }
        $.ajax({
            type: "PUT",
            url: "/skillboard/modify",
            data: JSON.stringify(form),
            contentType : "application/json; charset=utf-8", //body데이터가 어떤 타입인지 (MIME타입)
            dataType: "json" // 요청을 서버로 해서 응답이 왔을때, String문자열임
        }).done(function (resp){
            if(resp.data === 1 ){
                alert("글수정완료입니다 : "+resp.status)
                location.href = "/"
            }else{
                alert("글수정실패 : "+resp.status)
            }
        }).fail(function (){
            alert("글수정 실패 (문제확인 해주세요)")
        })

    }else{
        alert("제목을 입력해주세요")
    }

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




