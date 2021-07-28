function patchboardWriteGo() {
    location.href="/patchnote/gowrite"
}

function PatchNoteDelete(boardId){
    $.ajax({
        type: "DELETE",
        url: "/patchboard/delete/"+boardId,
        dataType: "json"
    }).done(function (resp){
        alert("삭제가 완료되었습니다")
        location.href = "/"
    }).fail(function (resp){
        alert("삭제가 실패하였습니다")
    })
}


function patchnoteWrite(){
    var titleName = $("#title").val()

    if(titleName != null){
        let form = {
            title : titleName,
            content : $("#content").val()
        }
        $.ajax({
            type: "POST",
            url: "/patchnote/write",
            data: JSON.stringify(form),
            contentType : "application/json; charset=utf-8", //body데이터가 어떤 타입인지 (MIME타입)
            dataType: "json" // 요청을 서버로 해서 응답이 왔을때, String문자열임
        }).done(function (resp){
            if(resp.data === 1 ){
                alert("패치노트 업데이트 완료입니다 : "+resp.status)
                location.href = "/"
            }else{
                alert("실패했습니다. : "+resp.status)
            }
        }).fail(function (){
            alert("패치노트 업데이트 실패 (문제확인 해주세요)")
        })
    }else{
        alert("제목을 입력해주세요")
    }



}

function SearchUserDelete(){
    location.href = "/admin/userManageView"
}