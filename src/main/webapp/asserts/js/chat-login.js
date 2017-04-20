/**
 * 
 */
$(document).ready(function(){
	$('#register').click(function(){
		window.location.href = 'Register';
	})
})
function onLogin(){
		var username = $('#username').val();
		var password = $('#password').val();
		var user = {
				username: username, 
				password: password,
		    }
		$.ajax({
			url: 'login',
			type: 'POST',
			data: JSON.stringify(user),
			dataType: 'JSON',
			contentType: 'application/json;charset=utf-8',
			success: function(msg) {
				if (msg.status != null && msg.status == "success") {
					window.location.href = "Chat"
				}else {
					alert(msg.reason)
				}
			},
			error :function(msg) {
				console.log(msg)
			}
		})
	}

function checkUser(){
    var username = $('#username').val()
    if (username == null || username.trim().length <= 0) {
        var elem = $('#username')[0].parentElement
        elem.className += ' invalide-input '
        $('#username')[0].focus()
        return false
    }
    var password = $('#password').val()
    if (password == null || password.trim().length <= 0) {
        var elem = $('#password')[0].parentElement
        elem.className += ' invalide-input '
        $('#password')[0].focus()
        return false
    }
    onLogin()
    return false;
}