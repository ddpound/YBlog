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