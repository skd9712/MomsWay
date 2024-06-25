window.onload = function (){
    document.getElementById("search")
        .addEventListener('change', function (){
            let select = this.value;
            let form = document.getElementById("search_form");

            if(select === "EntExam"){
                form.action = "/entexam";
            }else if(select === "Academy"){
                form.action = "/academy";
            }
        })
}