/**
 * 
 */

function inputFocus(elem){
    var parentElem =  elem.parentElement
    parentElem.className = parentElem.className.replace(/\s*invalide-input\s*/, ' ')
    parentElem.className += ' input-focused ';
}

function inputBlur(elem){
    var parentElem =  elem.parentElement
    parentElem.className = parentElem.className.replace(/\s*input-focused\s*/, ' ');
}