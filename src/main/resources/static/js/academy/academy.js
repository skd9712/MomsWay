let nid = '';
function init(data) {
    nid = data;
    console.log(nid);
}

function modAcademy(){
    location.href=`/modacademy/${aid}`;
}

async function delAcademy(){
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