window.entlike = window.entlike || {};

window.entlike.init = function (eid){
    console.log('like eid: ',eid);
    checkLikeStatus(eid);
}
function likeEnt(){
    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;
    fetch('/insertlike', {
        method: 'POST',
        headers: {
            'X-Requested-With': "XMLHttpRequest",
            'Accept': 'application/json',
            'X-XSRF-Token': token,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({eid: eid})
    }).then(response=>{
        if(!response.ok)
            throw new Error();
        else
            return response.json();
    }).then(data=>{
        updateLikeButton(data.liked);
        liked = true
    }).catch(error=>{
        console.log(error);
    })
}
function checkLikeStatus() {
    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;
    let query="uid="+1+"&eid="+eid;
    let liked = false;

    fetch('/checklike?'+query, {
        headers: {
            'X-Requested-With': "XMLHttpRequest",
            'Accept': 'application/json',
            'X-XSRF-Token': token,
            //  'Content-Type': 'application/json'
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    }).then(response => {
        if (!response.ok)
            throw new Error();
        else
            return response.json();
    }).then(data => {
        // 초기 좋아요 상태 업데이트
        liked = true;
        updateLikeButton(data.liked);
        console.log("liked: ", data.liked);

    }).catch(error => {
        console.log(error);
    })
}
function updateLikeButton(liked) {
    const likeBtn = document.getElementById('likeBtn');
    if (liked === true) {
        likeBtn.src = '/images/like_filled.png';
    } else {
        likeBtn.src = '/images/like.png';

    }
}
/*
function likeEnt(){
    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;
    fetch('/insertlike',{
        method: 'POST'
        ,headers: {
             'X-Requested-With': "XMLHttpRequest"
            , 'Accept': 'application/json'
            , 'X-XSRF-Token': token
            ,'Content-Type': 'application/json'
        }
        ,body: JSON.stringify({eid:eid})
    }).then(response=>{
        if(!response.ok)
            throw new Error();
        else
            return response.text();
    }).then(date=>{
        alert('좋아요');
        location.href="/entdetail/"+eid;
    }).catch(error=>{
        console.log(error);
    })
}*/
