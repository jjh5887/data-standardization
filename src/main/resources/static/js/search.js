var input = document.getElementById("search");
const THead_Word = document.querySelector('#word').innerHTML;
const THead_Domain = document.querySelector('#domain').innerHTML;
const THead_Dict = document.querySelector('#dict').innerHTML;

const params = {
    pageNumber: 0,
    pageSize: 20,
    paged: true,
    unpaged: false
}

window.addEventListener('load', () => {

});

const fetchMethod = (url, tableCode) => {
    console.log(url)
     fetch(url).then((res => res.json())).then(result => {

     })
}

const getURL = (tableName, searchType) => {
    const key = Object.keys(Field_Const).find((item) => item == tableName.toUpperCase());
    const searchValue = Object.values(Field_Const[key]).find((item) => item.name == searchType);
    const target_Value = searchValue.code;

    let query = Object.keys(params).map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k])) .join('&');
    let url;
    switch (tableName) {
        case TABLE_CODE.DICT:
            break;
        case TABLE_CODE.DOMAIN:
            url = `http://localhost:8080/${tableName}/${target_Value}/${input.value}?` + query + `&sort.sorted=false&sort.unsorted=true`;
            break;
        case TABLE_CODE.WORD:
            url = `http://localhost:8080/${tableName}/${target_Value}/${input.value}?` + query + `&sort.sorted=false&sort.unsorted=true`;
            break;
        default:
            break;
    }
    return url;
}

document.getElementById('btn1').onclick = function(){
    const select1 = document.getElementById('mySelect1');
    const select2 = document.getElementById('mySelect2');

    const url = getURL(select1.value, select2.value)
    fetch(url)
        .then(response => response.json())
        .then(result => {
            let wordResult = THead_Word;
            let domainResult = THead_Domain;
            let dictResult = THead_Dict;

            wordResult += '<tbody>'
            domainResult += '<tbody>'
            dictResult += '<tbody>'
            for (let i = 0; i < result.content?.length; i++) {
                if (Object.keys(result.content[i].base).includes('caseStyle')){
                    dictResult += '<tr onclick="">'
                    + '<th>' + '<button name="T_Btn" type="button" class="btn-extension" onClick="">'
                    + '상세보기' + '</button>'
                    + ' </th>'
                    + '<td>' + result.content[i].base?.name + '</td>'
                    + '<td>' + result.content[i].base?.engName + '</td>'
                    + '<td>' + result.content[i].base?.screenName + '</td>'
                    + '<td>' + result.content[i].base?.domains + '</td>'
                    + '<td>' + result.content[i].base?.dataType  +'</td>'
                    + '<td>' + result.content[i].base?.size +'</td>'
                    + '<td>' + result.content[i].base?.scale  +'</td>'
                    + '<td>' + result.content[i].base?.nullable + '</td>'
                    + '<td>' + result.content[i].base?.isCommon + '</td>'
                    + '<td>' + result.content[i].base?.caseStyle + '</td>'
                    + '<td>' + result.content[i].base?.description + '</td>'
                    + '<td>' + result.content[i].status + '</td>'
                    + '</tr>';
            } else if (Object.keys(result.content[i].base).includes('db')) {
                domainResult += '<tr>'
                    + '<th>' + '<button name="T_Btn" type="button" class="btn-extension" onClick="">'
                    + '상세보기' + '</button>'
                    + ' </th>'
                    + '<td>' + result.content[i].base?.name + '</td>'
                    + '<td>' + result.content[i].base?.db + '</td>'
                    + '<td>' + result.content[i].base?.dataType + '</td>'
                    + '<td>' + result.content[i].base?.size + '</td>'
                    + '<td>' + result.content[i].base?.scale + '</td>'
                    + '<td>' + result.content[i].base?.nullable + '</td>'
                    + '<td>' + result.content[i].status + '</td>'

                    + '</tr>';
            }
                else if (Object.keys(result.content[i]).includes('base')) {
                    wordResult += '<tr>'
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
            dictResult += '</tbody>';
            domainResult += '</tbody>';
            wordResult += '</tbody>';
            document.getElementById('dict').innerHTML = dictResult;
            document.getElementById('domain').innerHTML = domainResult;
            document.getElementById('word').innerHTML = wordResult;

        });


}
input.onkeydown = function (event) {
    if (event.keyCode == 13) {
        document.getElementById("btn1").click();
        event.preventDefault();
    }
}

/*/!* 페이징 *!/
function pagination(){
    fetch(url)
        .then(response => response.json())
        .then(result => {


        });
}*/
