function SearchUserDelete(){
    location.href = "/admin/userdelete/"+ $("#userName").val()
}

function SearchUserBoardDelete(){
    location.href = "/admin/delete/userboard/"+ $("#userName").val()
}

function UserBoardDeleteAll(){
    location.href = "/admin/delete/allboard"
}

function deleteip(){
    location.href = "/admin/deleteip/"+$("#userName").val()
}

function alldeleteip(){
    location.href = "/admin/delete/ipall"
}

function useremailban(){
    location.href = "/admin/emailBan/"+$("#userName").val()
}

function all_clear_BRlimit(){
    location.href = "/admin/all_clear_BRlimit"

}

function logfile(){
        location.href = "/admin/log.txt"


}

function webShutDown(){
    if(confirm("정말 서버를 종료하시겠습니까?") === true){
        location.href = "/admin/shut_down"
    }else{
        location.href = "/"
    }

}