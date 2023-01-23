
var numContactsSelected = 0;

function initContactCounter(){
    numContactsSelected = document.getElementById("counterValTxt").innerHTML;
}

function doDeletion(msg){
    var answer = confirm(msg);
    return answer;
}

function doCount(element){
    if(element.checked){
        numContactsSelected++;
    }else{
        numContactsSelected--;
    }

    document.getElementById("counterValTxt").innerHTML = numContactsSelected;

}

function countChars($val){
   var str = $val.value.length;
   document.getElementById("result_characters_chars").innerHTML = str;
}

function disableAfterSent(){
    document.getElementById("submit").disable=true;
    
}


