function boardWrite(){
    let form = {
        title : $("#title").val(),
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






}