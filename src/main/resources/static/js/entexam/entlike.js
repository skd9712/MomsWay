window.entlike = window.entlike || {};

window.entlike.init = function (eid){
    console.log('like eid: ',eid);
    checkLikeStatus(eid);
}
async function toggleLike(likeBtn, eid) {
    if (liked) {
        await unlikeEnt(eid);
    } else {
        await likeEnt(eid);
    }
}
async function likeEnt(){
    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;
    await fetch('/insertlike', {
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
        liked = data.liked;
        updateLikeButton(liked);
        alert('게시글을 좋아합니다')
        location.href="/entdetail/"+eid;
    }).catch(error=>{
        console.log(error);
    })
}
async function checkLikeStatus() {
    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;
    let query="uid="+1+"&eid="+eid;
    liked = false;
    console.log('초기값',liked);

    await fetch('/checklike?'+query, {
        headers: {
            'X-Requested-With': "XMLHttpRequest",
            'Accept': 'application/json',
            'X-XSRF-Token': token,
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    }).then(response => {
        if (!response.ok)
            throw new Error();
        else
            return response.json();
    }).then(data => {
        // 초기 좋아요 상태 업데이트
        liked = data;
        console.log(data);
        updateLikeButton(liked);
        console.log("liked: ", liked);

    }).catch(error => {
        console.log(error);
    })
}
async function updateLikeButton(liked) {
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
