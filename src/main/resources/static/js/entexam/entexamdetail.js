// let eid = '';
// function init(data){
//     eid = data;
//     console.log(eid);
// }
window.entexamdetail = window.entexamdetail || {};

window.entexamdetail.init = function (eid) {
    console.log('entexamdetail init with eid:', eid);
    // 추가적인 초기화 로직
};
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