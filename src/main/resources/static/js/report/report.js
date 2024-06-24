let reportList = [];

function init(data) {
    reportList = data;
    console.log(reportList);
}

async function delReport(rid) {
    await fetch('/delreport/' + rid, {
        method: 'GET',
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
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
// function delReport(rid) {
//     $.ajax({
//         type: 'GET',
//         url: '/delreport/' + rid,
//         success: function(response) {
//             alert(response);
//             location.reload(); // 페이지 새로고침하여 삭제된 항목 반영
//         },
//         error: function() {
//             alert('ERROR');
//         }
//     });
// }