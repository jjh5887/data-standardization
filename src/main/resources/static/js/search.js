var input = document.getElementById("search");

document.getElementById('btn1').onclick = function(){
    let no, filter, tr, td, i, txtValue;
    const select1 = document.getElementById('mySelect1');
    const select2 = document.getElementById('mySelect2');

    const table = Table_Const.find((item) => item.value === select1.value);
    const target = table.name;

    const word = Field_Const.WORD.find((item) => item.name === select2.value);
    const target_word = word.value;

    const params = {
        pageNumber: 0,
        pageSize: 20,
        paged: true,
        unpaged: false
    }
    let query = Object.keys(params).map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k])) .join('&');
    const url = `http://localhost:8080/${target}/${target_word}/${input.value}?` + query + `&sort.sorted=false&sort.unsorted=true`;
    fetch(url)
        .then(response => response.json())
        .then(result => {
            console.log(result.content.length);
            let wordResult = document.querySelector('#word').innerHTML;
            wordResult += '<tbody>'
            for (let i = 0; i < result.content.length; i++) {
                if (Object.keys(result.content[i]).includes('base')) {
                    wordResult += '<tr onclick="">'
                        + '<th>' + '<button name="T_Btn" type="button" class="btn-extension" onClick="">'
                        + '상세보기' + '</button>'
                        + ' </th>'
                        + '<td>' + result.content[i].base?.engName + '</td>'
                        + '<td>' + result.content[i].base?.name + '</td>'
                        + '<td>' + result.content[i].base?.orgEngName + '</td>'
                        + '<td>' + result.content[i].status + '</td>'
                        + '</tr>';
                }
            }
            wordResult += '</tbody>';
            document.getElementById('word').innerHTML = wordResult;

        });

}
input.onkeydown = function (event) {
    if (event.keyCode == 13) {
        document.getElementById("btn1").click();
        event.preventDefault();
    }

}

/* 페이징 */
function pagination(){
    fetch(url)
        .then(response => response.json())
        .then(result => {


        });
}
<<<<<<< HEAD
=======


>>>>>>> 5627cf7 (FWF-3)
