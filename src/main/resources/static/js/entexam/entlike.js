window.entlike = window.entlike || {};

window.entlike.init = function (eid){
    console.log('like eid: ',eid);
    checkLikeStatus(eid);

}

async function toggleLike() {
    const likeBtn = document.getElementById('likeBtn');
    let fn=likeBtn.alt;
    console.log("data...", fn.indexOf("filled"));

    if (fn.indexOf("filled")>=0) {
        await unlikeEnt();
    } else {
        await likeEnt();
    }
}
async function likeEnt(){
    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;
    const likeBtn = document.getElementById('likeBtn');
    let likenum = likeBtn.alt;
    console.log(likenum);
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
        console.log('Received data:', data);
        // liked = data;
        updateLikeButton(data);
        console.log('Liked status:', data);
        alert('게시글을 좋아합니다')
        location.href="/entdetail/"+eid;
    }).catch(error=>{
        console.log(error);
    })
}
async function unlikeEnt(){
    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;
    const likeBtn = document.getElementById('likeBtn');
      let likenum = likeBtn.alt;

    likenum= likenum.slice(likenum.indexOf('_')+1);
    console.log(likenum);

    fetch('/dellike/'+likenum,{
        method: 'POST'
        ,headers: {
            'X-Requested-With': "XMLHttpRequest",
            'Accept': 'application/json',
            'X-XSRF-Token': token,
            'Content-Type': 'application/json'
        }
    }).then(response=>{
        if(!response.ok)
            throw new Error()
        else
            return response.json();
    }).then(data=>{
        console.log('Received data:', data);

        updateLikeButton(likenum);

        alert('좋아요 취소')
        location.href="/entdetail/"+eid;
    }).catch(error=>{
        console.log(error);
    })
}
async function checkLikeStatus() {
    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;
    let query="uid="+1+"&eid="+eid;
    // let liked = null;
    // console.log('초기값',liked);

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
        // liked = (data.text());
        console.log("data.text",(data), typeof data);
        console.log(data);
        updateLikeButton(data);

    }).catch(error => {
        console.log(error);
    })
}
async function updateLikeButton(data) {
    console.log("btn",data);
    const likeBtn = document.getElementById('likeBtn');
    if ( data!=null) {
        likeBtn.src = '/images/like_filled.png';
        likeBtn.alt = 'filled_'+data;
    } else {
        likeBtn.src = '/images/like.png';
        likeBtn.alt = 'like';
    }
}
