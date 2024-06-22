let eid = '';
function init(data){
    eid = data;
    console.log(eid);
}
document.addEventListener('DOMContentLoaded',function (){
    document.getElementById('mod').addEventListener('click',function (){
        window.location.href='/entupdate/'+eid;
    })
})
