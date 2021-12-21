function infoPopup() {
    const tableList = document.getElementById('tableList');
    for (let i = 1; i < tableList.rows.length; i++) {
        tableList.rows[i].cells[2].onclick = function () {
            let tableList = tableList.rows[i].cells[1].innerText;
            alert(tableList + "을 선택하셨습니다.");
        }
    }
}