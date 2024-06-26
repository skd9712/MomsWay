async function entReport() {
    try {
        const eid = document.getElementById("reportEid").value;
        const uid = document.getElementById("reportUid").value;
        const comment = document.getElementById("comment").value;

        // CSRF 토큰 값 가져오기
        const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
        const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

        const response = await fetch('/entreport', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfHeader]: csrfToken
            },
            body: new URLSearchParams({
                eid: eid,
                uid: uid,
                comment: comment
            })
        });

        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }

        const result = await response.text();
        alert(result);

        // 폼 초기화
        document.getElementById("comment").value = "";
        document.getElementById("reportModal").style.display = 'none';
    } catch (error) {
        console.error('Error:', error);
        alert("신고 처리 중 오류가 발생했습니다.");
    }
}
