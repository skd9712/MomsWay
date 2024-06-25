window.onload = function () {
    /* 이메일 중복체크 */
    let email = document.getElementById("email");
    email.addEventListener("input", function (){
        fetch("/checkEmail?email="+email.value,
            {
                method: 'GET'
                , headers:{
                    'Accept':'application/json'
                }
            }).then(response=>{
            if(!response.ok)
                throw new Error('not load');
            return response.json();
        }).then((data)=>{
            let emsg=document.getElementById("emsg");
            if(emsg)
                emsg.remove();
            if(data===null){
                let ele_p=document.createElement('p');
                let ele_txt=document.createTextNode("이메일을 작성해 주세요.");
                ele_p.appendChild(ele_txt);
                ele_p.id="emsg";
                ele_p.className="msg_error";
                document.getElementById("email_msg").appendChild(ele_p);
            }
            else if(data===true){
                let ele_p=document.createElement('p');
                let ele_txt=document.createTextNode("사용 불가능한 이메일입니다.");
                ele_p.appendChild(ele_txt);
                ele_p.id="emsg";
                ele_p.className="msg_error";
                document.getElementById("email_msg").appendChild(ele_p);
            }
            else if(data===false){
                let ele_p=document.createElement('p');
                let ele_txt=document.createTextNode("사용 가능한 이메일입니다.")
                ele_p.appendChild(ele_txt);
                ele_p.id="emsg";
                ele_p.className="msg_ok";
                document.getElementById("email_msg").appendChild(ele_p);
            }
        })
    });


    /* 닉네임 중복체크 */
    let nickname = document.getElementById("nickname");
    nickname.addEventListener("input", function (){
        fetch("/checkNickname?nickname="+nickname.value,
            {
                method: 'GET'
                , headers:{
                    'Accept':'application/json'
                }
            }).then(response=>{
            if(!response.ok)
                throw new Error('not load');
            return response.json();
        }).then((data)=>{
            let nmsg=document.getElementById("nmsg");
            if(nmsg)
                nmsg.remove();
            if(data===null){
                let ele_p=document.createElement('p');
                let ele_txt=document.createTextNode("닉네임을 작성해 주세요.");
                ele_p.appendChild(ele_txt);
                ele_p.id="nmsg";
                ele_p.className="msg_error";
                document.getElementById("nick_msg").appendChild(ele_p);
            }
            else if(data===true){
                let ele_p=document.createElement('p');
                let ele_txt=document.createTextNode("사용 불가능한 닉네임입니다.");
                ele_p.appendChild(ele_txt);
                ele_p.id="nmsg";
                ele_p.className="msg_error";
                document.getElementById("nick_msg").appendChild(ele_p);
            }
            else if(data===false){
                let ele_p=document.createElement('p');
                let ele_txt=document.createTextNode("사용 가능한 닉네임입니다.")
                ele_p.appendChild(ele_txt);
                ele_p.id="nmsg";
                ele_p.className="msg_ok";
                document.getElementById("nick_msg").appendChild(ele_p);
            }

        })
    });
}

