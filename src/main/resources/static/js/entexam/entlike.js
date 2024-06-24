window.entlike = window.entlike || {};

window.entlike.init = function (eid){
    console.log('like eid: ',eid);
}
function likeEnt(){
    fetch('/insertlike',{
        method: 'POST'
        ,headers: {
            'Accept': 'application/json'
            ,'Content-Type': 'application/json'
        }
        ,body: JSON.stringify({eid:eid})
    }).then(response=>{
        if(!response.ok)
            throw new Error();
        else
            return response.text();
    }).then(date=>{
        alert(date);
        location.href="/entdetail/"+eid;
    }).catch(error=>{
        console.log(error);
    })
}