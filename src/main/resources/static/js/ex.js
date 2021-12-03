$(document).ready(function () {
    // 초기 값 지정
    $('#value').text($("#select option:selected").val());
    $('#code').text($("#select option:selected").attr('code'));
    $('#no').text($("#select option:selected").attr('no'));

    // select 선택이 변경 됐을 때 호출 되는 함수 
    $('#select').change(function() {
        $('#value').text($("#select option:selected").val());
        $('#code').text($("#select option:selected").attr('code'));
        $('#no').text($("#select option:selected").attr('no'));
    });
});
