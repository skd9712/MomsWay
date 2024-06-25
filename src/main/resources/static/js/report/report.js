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
