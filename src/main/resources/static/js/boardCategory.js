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

        })
}

function thumbnailDelete(id){
    $.ajax({
        type: "DELETE" ,
        url : "/admin/category/delete/"+id,
    })
}