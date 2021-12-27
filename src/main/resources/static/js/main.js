function changemarqueeText(){
    var newstatus = $('#chagestatus').val()
    location.href = "/board/changeStatus/"+newstatus

}

function kakaoGoJoinPage(){
    location.href = "/auth/emailauth"
}


function  portboardWriteGo(){
    location.href = "/portboard/write"
}

function portboardWrite() {
    var titleName = $("#title").val()
    var description = $("#boardDescription").val() // 그냥 description 을 사용하면 헤드값에 이미 있기때문에 안남음

    if(titleName != ""){
        let form = {
            title : titleName,
            content : $("#content").val(),
            description : description
        }
        $.ajax({
            type: "POST",
            url: "/portboard/save",
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

function  goModifywritePortfolio(boardId){
    location.href="/portboard/goPortBoardmodify/"+boardId
}

function PortfolioDelete(boardId){
    $.ajax({
        type: "DELETE",
        url: "/portboard/delete/"+boardId,
        dataType: "json"
    }).done(function (resp){
        alert("삭제가 완료되었습니다")
        location.href = "/"
    }).fail(function (resp){
        alert("삭제가 실패하였습니다")
    })
}

function  portboardModify(boardId){
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
            url: "/portboard/modify",
            data: JSON.stringify(form),
            contentType : "application/json; charset=utf-8", //body데이터가 어떤 타입인지 (MIME타입)
            dataType: "json" // 요청을 서버로 해서 응답이 왔을때, String문자열임
        }).done(function (resp){
            if(resp.data === 1 ){
                alert("포트폴리오 글수정완료입니다 : "+resp.status)
                location.href = "/"
            }else{
                alert("포트폴리오 글수정실패 : "+resp.status)
            }
        }).fail(function (){
            alert("포트폴리오 글수정 실패 (문제확인 해주세요)")
        })

    }else{
        alert("제목을 입력해주세요")
    }

}

function KaKaoLogin(loginRequestURI){
    location.href = loginRequestURI
}

function kakaoLogin(Loginredirect_uri){
    location.href = Loginredirect_uri

}

function portSummernoteImageFile(file, editor) {
    data = new FormData();
    data.append("file", file);
    $.ajax({
        data : data,
        type : "POST",
        url : "/portboard/temporarystorageImagefile",
        contentType : false,
        processData : false,
        success : function(data) {
            console.log(data.url)
            //항상 업로드된 파일의 url이 있어야 한다.
            $(editor).summernote('insertImage', data.url);
        }
    });
}

function searchBoard(){

    var searchTitle = $("#searchTitle").val()
    var category = $("#categoryBoard").val()

    location.href ="/auth/search/"+category+"/"+searchTitle


}
