let reportList = [];

function init(data) {
    reportList = data;
    console.log(reportList);
}

async function delReport(rid) {
    // CSRF 토큰 값 가져오기
    const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    await fetch('/delreport/' + rid, {
        method: 'GET',
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
        }
    }).then(response => {
        if (!response.ok)
            throw new Error();
        else
            return response.text();
    }).then(data => {
        alert(data);
        location.href = "/report";
    }).catch(error => {
        console.log(error);
    });
}
