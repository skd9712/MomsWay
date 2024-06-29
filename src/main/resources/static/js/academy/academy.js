let aid = '';
function init(data) {
    aid = data;
    console.log(aid);
}

function modAcademy(){
    location.href=`/updateAcademy/${aid}`;
}

async function delAcademy(){
    if(window.confirm("정말 삭제하시겠습니까?")){
        await fetch(`/delacademy/${aid}`,{
            method: "get"
            ,headers: { "Accept": "application/json", "Content-Type" : "application/json" }
        }).then(res=>{
            if(!res.ok)
                throw new Error();
            else
                return res.text();
        }).then(data=>{
            alert(data);
            location.href="/academy";
        }).catch(error=>{
            console.error(error);
        });
    }

}