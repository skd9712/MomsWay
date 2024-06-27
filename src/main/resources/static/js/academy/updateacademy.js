
function updateImg(e) {
    console.log(e);
    let path = decodeURIComponent(e.src);
    console.log(path.split("/"));
    let fname = path.split("/")[4];
    let uploadImgs = document.getElementById("uploadImgs");
    let input = document.createElement("input");
    input.setAttribute("type","input");
    input.setAttribute("name", "imgPaths");
    input.setAttribute("value",fname);
    input.hidden = true;
    uploadImgs.appendChild(input);
    e.remove();
}

window.onload=function () {
    let fileinput = document.getElementById("files");
    fileinput.onchange=function (e) {
        //console.log(e.target.files);
        let files = e.target.files;
        let addfiles = document.querySelectorAll(".addfile");
       //console.log(addfiles,addfiles.length);
        if(addfiles.length>0){
            for(let j=0; j<addfiles.length; j++){
                addfiles[j].parentNode.removeChild(addfiles[j])
            }
        }
        for (let i = 0; i < files.length; i++) {
            let img = document.createElement("img");
            img.setAttribute("class", "thumb addfile");
            if (files[i]) {
                const reader = new FileReader(); // FileReader 객체 생성
                reader.onload = function (e) {
                    //console.log("reader",e.target);
                    img.setAttribute("src", `${e.target.result}`);
                };
                reader.readAsDataURL(files[i]);
            }
            img.setAttribute("alt", "insert_img");
            document.getElementById("imgs").appendChild(img);
        }
    }

    const submitBtn = document.querySelector("#submitBtn");
    submitBtn.onclick=function () {
        let form = document.querySelector("#form");
        //console.log(form.files.files.length);
        if(form.title.value==""){
            alert("제목을 입력하세요");
        } else if(form.content.value==""){
            alert("내용을 입력하세요");
        } else if(form.files.files.length>5){
            alert("이미지는 한번에 최대 5개까지 등록할 수 있습니다.");
        } else {
            form.submit();
        }

    }


}


