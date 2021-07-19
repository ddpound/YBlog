function boardWrite(){
    var titleName = $("#title").val()

    if(titleName != null){
        let form = {
            title : titleName,
            content : $("#content").val()
        }
        $.ajax({
            type: "POST",
            url: "/board/save",
            data: JSON.stringify(form),
            contentType : "application/json; charset=utf-8", //body데이터가 어떤 타입인지 (MIME타입)
            dataType: "json" // 요청을 서버로 해서 응답이 왔을때, String문자열임
        }).done(function (resp){
            if(resp.data === 1 ){
                alert("글쓰기완료입니다 : "+resp.status)
                location.href = "/"
            }else{
                alert("글쓰기실패 : "+resp.status)
            }
        }).fail(function (){
            alert("글쓰기 실패 (문제확인 해주세요)")
        })
    }else{
        alert("제목을 입력해주세요")
    }


}

function boarddelete(boardId){

    $.ajax({
        type: "DELETE",
        url: "/board/delete/"+boardId,
        dataType: "json"
    }).done(function (resp){
        alert("삭제가 완료되었습니다")
        location.href = "/"
    }).fail(function (resp){
        alert("삭제가 실패하였습니다")
    })

}

function gomodifywrite(boardId){
    location.href="board/gomodify/"+boardId

}

function  boardModify(boardId){
    var titleName= $("#title").val()
    if( titleName !=null){
        let form = {
            id : boardId,
            title : titleName,
            content : $("#content").val()
        }
        $.ajax({
            type: "PUT",
            url: "/board/modify",
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