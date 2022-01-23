function saveCategory(){
    let categoryName = document.getElementById("categoryName").value
    let categoryThumnail = document.getElementById("thumbnailimage").files[0]

    console.log(categoryThumnail)


    /*let cateform ={
        categoryName : categoryName,
        categoryThunmbnail : categoryThumnail
    }*/
    let formDate= new FormData();
    formDate.append("categoryName", categoryName)
    formDate.append("file", categoryThumnail)

        $.ajax({
            type: "POST" ,
            data: formDate,
            url : "/admin/category/save",
            contentType : false,
            processData : false,

        }).done(function (){
            location.href="/admin/category"
        })
}

function modifyButton(id){
    let catenameId = "cateName"+id
    let filesName = "thumbnailimage"+id

    let name = document.getElementById(catenameId).value
    let thumbnail = document.getElementById(filesName).files[0]

    console.log(name)
    console.log(thumbnail)

    let formDate= new FormData();
    formDate.append("categoryName", name)
    formDate.append("categoryId", id)
    formDate.append("file", thumbnail)

    $.ajax({
        type: "PUT" ,
        data: formDate,
        url : "/admin/category/modify",
        contentType : false,
        processData : false,

    }).done(function (){
        location.href="/admin/category"
    })

}

function thumbnailDelete(id){
    $.ajax({
        type: "DELETE" ,
        url : "/admin/category/delete/"+id,
    }).done(function (){
        location.href="/admin/category"
    })
}