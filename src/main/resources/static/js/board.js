
function uploadSummernoteImageFile(file, editor) {
    data = new FormData();
    data.append("file", file);
    $.ajax({
        data : data,
        type : "POST",
        url : "/board/temporarystorageImagefile",
        contentType : false,
        processData : false,
        success : function(data) {
            console.log(data.url)
            //항상 업로드된 파일의 url이 있어야 한다.
            $(editor).summernote('insertImage', data.url);
        }
    });
}


function boardWrite(){
    var titleName = $("#title").val()

    if(titleName != ""){
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
    location.href="/board/gomodify/"+boardId

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

function saveReply(){
    var replyContent = $("#replyContent").val()
    var boardId = $("#replyboardid").val()

    if(replyContent != null){
        let form = {
            content : replyContent
        }
        $.ajax({
            type: "POST",
            url: "/board/"+boardId+"/reply",
            data: JSON.stringify(form),
            contentType : "application/json; charset=utf-8", //body데이터가 어떤 타입인지 (MIME타입)
            dataType: "json" // 요청을 서버로 해서 응답이 왔을때, String문자열임
        }).done(function (resp){
            if(resp.data === 1 ){
                alert("댓글 쓰기완료입니다 : "+resp.status)
                location.href = "/auth/board/details?id="+boardId
            }else{
                alert("댓글쓰기실패 : "+resp.status)
            }
        }).fail(function (){
            alert("댓글 쓰기 실패 (문제확인 해주세요)")
        })
    }else{
        alert("댓글을 입력해주세요")
    }



}


function replyDelete(boardId,replyId){
    $.ajax({
        type: "DELETE",
        url: "/board/"+boardId+"/reply/"+replyId,
        dataType: "json" // 요청을 서버로 해서 응답이 왔을때, String문자열임
    }).done(function (resp){
        if(resp.data === 1 ){
            alert("댓글 삭제 완료입니다 : "+resp.status)
            location.href = "/auth/board/details?id="+boardId
        }else{
            alert("댓글삭제실패 : "+resp.status)
        }
    }).fail(function (){
        alert("댓글 삭제 실패 (문제확인 해주세요)")
    })



}