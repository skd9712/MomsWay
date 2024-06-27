let nid = '';
function init(data) {
    nid = data;
    console.log(nid);
}

function modNotice(){
    location.href=`/updateNotice/${nid}`;
}

async function delNotice(){
    await fetch(`/delnotice/${nid}`,{
        method: "get"
        ,headers: { "Accept": "application/json", "Content-Type" : "application/json" }
    }).then(res=>{
        if(!res.ok)
            throw new Error();
        else
            return res.text();
    }).then(data=>{
        alert(data);
        location.href="/notice";
    }).catch(error=>{
        console.error(error);
    });
}

window.onload=function () {
    const tolistBtn = document.querySelector("#toList");
    tolistBtn.addEventListener("click",function (e) {
        e.preventDefault();
        //console.log(document.referrer);
        location.href=document.referrer;
    });
}
