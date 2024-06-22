let eid = '';
function init(data){
    eid = data;
    console.log(eid);
}
async function delEnt(){
    await fetch('/delentexam/'+eid,{
        method: 'GET'
        ,headers: {
            "Accept": "application/json"
            ,"Content-Type": "application/json"
        }
    }).then(response=>{
        if(!response.ok)
            throw new Error();
        else
            return response.text();
    }).then(date=>{
        alert(date);
        location.href="/entexam";
    }).catch(error=>{
        console.log(error);
    });
}