let childmain1 =document.getElementById("childmain1")


let childmain2Ptag = document.getElementById("childmain2Ptag")
let childmainImage = document.getElementById("animaImage2")

let childmain3 = document.getElementById("childmain3")
let childmain4 = document.getElementById("childmain4")
let childmain5 = document.getElementById("childmain5")



window.onload = function (){
    childmain1.style.animation = 'inout 2s forwards'
}

// 애니메이션 두번째 차일드
window.addEventListener("scroll", function (){
    let scrollValue2 = window.scrollY
    console.log(scrollValue2)


    if(window.innerWidth >= 1100){
        if(scrollValue2 > 100 && scrollValue2 < 1200){
            childmainImage.style.animation= "image2TopandBottom 2s"
            childmain2Ptag.style.animation = "childmain2RightSlide 2s ease-out"
        }else{
            childmainImage.style.animation= "image2BottomandTop 2s forwards"
            childmain2Ptag.style.animation = "RightSlideOut 2s forwards"
        }
    }else if(window.innerWidth >= 1000 && window.innerWidth < 1100){
        if(scrollValue2 > 1000 && scrollValue2 < 2000){
            childmainImage.style.animation= "image2TopandBottom 2s"
            childmain2Ptag.style.animation = "childmain2RightSlide 2s ease-out"
        }else{
            childmainImage.style.animation= "image2BottomandTop 2s forwards"
            childmain2Ptag.style.animation = "RightSlideOut 2s forwards"
        }
    }

    if(window.innerWidth >= 1100){
        if(scrollValue2 > 1500 && scrollValue2 < 2400){
            childmain3.style.animation= "inout 2s"
        }else{
            childmain3.style.animation= "outin 2s forwards"
        }

    }else if(window.innerWidth >= 1000 && window.innerWidth < 1100){
        if(scrollValue2 > 2200 && scrollValue2 < 2900){
            childmain3.style.animation= "inout 2s"
        }else {
            childmain3.style.animation= "outin 2s forwards"
        }

    }

    if(window.innerWidth >= 1100){
        if(scrollValue2 > 2200 && scrollValue2 < 3300){
            childmain4.style.animation= "inout 2s"
        }else{
            childmain4.style.animation= "outin 2s forwards"
        }

    }else if(window.innerWidth >= 1000 && window.innerWidth < 1100){
        if(scrollValue2 > 2900 && scrollValue2 < 3800){
            childmain4.style.animation= "inout 2s"
        }else {
            childmain4.style.animation= "outin 2s forwards"
        }

    }

    if(window.innerWidth >= 1100){
        if(scrollValue2 > 3300 && scrollValue2 < 4100){
            childmain5.style.animation= "inout 2s"
        }else{
            childmain5.style.animation= "outin 2s forwards"
        }

    }else if(window.innerWidth >= 1000 && window.innerWidth < 1100){
        if(scrollValue2 > 4000 && scrollValue2 < 4600){
            childmain5.style.animation= "inout 2s"
        }else {
            childmain5.style.animation= "outin 2s forwards"
        }

    }






})





