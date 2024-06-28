/* 배너 슬라이드 */
window.onload = function (){
    let banners = document.querySelectorAll(".banner_img");
    let prev_btn = document.getElementById("prev_btn");
    let next_btn = document.getElementById("next_btn");
    let curr = 0;

    let slide_fn = function (e){
        for(let i=0; i<banners.length; i++){
            banners[i].style.display = "none";
        }
        banners[e].style.display = 'block';
    }

    slide_fn(curr);

    prev_btn.onclick = function(){
            if(curr > 0)
                curr -= 1;
            else
                curr = banners.length-1;
            slide_fn(curr);
        }

    next_btn.onclick = function(){
        if(curr < banners.length-1)
            curr += 1;
        else
            curr = 0;
        slide_fn(curr);
    }

    let loop = setInterval(()=>{
        if(curr < banners.length-1)
            curr += 1;
        else
            curr = 0;
        slide_fn(curr);
    }, 3000); // 3초마다 자동으로 next
}