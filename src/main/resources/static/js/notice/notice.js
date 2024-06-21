let nid = '';
function init(data) {
    nid = data;
    console.log(nid);
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