let num = '';
window.entreply = window.entreply || {};

window.entreply.init = function (eid) {
    console.log('entreply init with eid:', eid);
    num = eid;
    // 추가적인 초기화 로직
};


const init_json = async function () {
    await fetch("/replist/" + num, {
        method: 'GET'
        , headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok)
                throw new Error('not loaded');
            return response.json();
        })
        .then((data) => {
            const comments = document.getElementById('comments-list');
            comments.innerText = '';
            data.forEach(item => {
                let ele_li = document.createElement("li");
                ele_li.classList.add('comment');

                let writer = document.createElement("h3")
                writer.classList.add('writer');
                writer.textContent = item.nickname;

                let content = document.createElement("p");
                content.classList.add('content');
                content.textContent = item.content;


                ele_li.appendChild(writer);
                ele_li.appendChild(content);
                comments.appendChild(ele_li);
            })
        })
        .catch(error => console.log(error))
        .finally(() => {
            console.log('finally');
        })
}
window.onload = function () {
    init_json();
    document.getElementById('submit').onclick = async function () {
        const token = document.querySelector("meta[name='_csrf']").content;
        const header = document.querySelector("meta[name='_csrf_header']").content;
        let content = document.querySelector('#content');
        let eid = document.querySelector('#eid');

        let d = {'content': content.value, 'eid': eid.value};

        await fetch("/insertrep", {
            method: 'POST'
            , headers: {
                'Content-Type': 'application/json;utf-8'
                // , 'header': header
                , 'X-Requested-With': "XMLHttpRequest"
                , 'Accept': 'application/json'
                , 'X-XSRF-Token': token

            }
            , body: JSON.stringify(d)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Fail')
                }
                return response.json();
            })
            .then((data) => {
                console.log('Response data:', data); // 디버깅 로그 추가
                init_json();
            })
            .catch(error => {
                console.log(error)
            }).finally(() => {
                console.log('finally');
                content.value = '';
            });

    }
}