/**
 * 
 */

var existUser = false

function onRegister(){
		var username = $('#username').val();
		var password = $('#password').val();
		var nickname = $('#nickname').val()
		var user = {
				username: username, 
				password: password,
				sex: '男',
				nickname: nickname,
		}
		$.ajax({
			url: 'register',
			type: 'POST',
			data: JSON.stringify(user),
			dataType: 'JSON',
			contentType: 'application/json;charset=utf-8',
			success: function(msg) {
				if (msg.status != null && msg.status == "success") {
					window.location.href = 'Chat'
				}else {
					alert ('注册失败！' + '原因可能是：\n' + msg.reason)
				}
			},
			error :function(msg) {
				console.log(msg)
			}
		})
}

function invalideUser(elem){
	inputBlur(elem)
	checkUserExists()
}

function invalideNickname(elem){
	inputBlur(elem)
	checkNickname()
}

function invalidePassword(elem) {
	inputBlur(elem)
	checkPassword()
}

function userRefocus(elem){
	inputFocus(elem)
	var classname = $('.username-status').attr('class')
	classname = classname.replace(/\s*show-indicator\s*/, '')
	$('.username-status').attr('class', classname)
}

function nicknameRefocus(elem){
	inputFocus(elem)
	var classname = $('.nickname-status').attr('class')
	classname = classname.replace(/\s*show-indicator\s*/, '')
	$('.nickname-status').attr('class', classname)
}

function passwordRefocus(elem){
	inputFocus(elem)
	var classname = $('.password-status').attr('class')
	classname = classname.replace(/\s*show-indicator\s*/, '')
	$('.password-status').attr('class', classname)
}

function checkNickname (){
	var nickname = $('#nickname').val()
	if (null != nickname && nickname.trim().length > 3) {
		var classname = $('.nickname-status').attr('class')
		classname += ' show-indicator ';
		$('.nickname-status').attr('class', classname)
	}
}

function checkPassword(){
	var nickname = $('#password').val()
	if (null != nickname && nickname.trim().length > 6) {
		var classname = $('.password-status').attr('class')
		classname += ' show-indicator ';
		$('.password-status').attr('class', classname)
	}
}

function checkInput() {
	var usernmae = $('#username').val()
    if (usernmae == null || usernmae.trim().length <= 0) {
		alert('输入用户名')
        $('#username')[0].focus()
        return false
    }
    var password = $('#password').val()
    if (null == password || password.trim().length <= 0) {
        $('#password')[0].focus()
		alert('输入密码，以登录系统！')
        return false
    }
    var nickname = $('#nickname').val()
    if (null == nickname || nickname.trim().length <= 0) {
        $('#nickname')[0].focus()
		alert('填写昵称，让更多人认识你')
        return false
    }
    // 注册
    if (existUser) {
    	alert('用户名已经存在！')
    }
    onRegister()
    return false
}

function checkUserExists() {
	var username = $('#username').val();
	$('#register').attr('disabled', 'disabled')
	if (null != username && username.trim().length >= 5) {
		$.ajax({
			url: 'uservalidation',
			type: 'POST',
			data: JSON.stringify({username:username}),
			dataType: "json",
			contentType: "application/json;charset=utf-8",
			success: function(msg) {
				if (msg.status != null && msg.status == "success") {
					var classname = $('.username-status').attr('class')
					classname += ' show-indicator ';
					$('.username-status').attr('class', classname)
					$('#register')[0].removeAttribute('disabled')
					existUser = false;
				}else { // 用户名存在
					var classname = $('.username-status').attr('class')
					classname = classname.replace(/\s*show-indicator\s*/, '');
					$('.username-status').attr('class', classname)
					$('#register').attr('disabled', 'disabled')
					existUser = true
					alert('用户名已存在！')
				}
			},
			error: function(response) {
				
			}
		})
	}else {
		alert('您输入的用户名可能太短！')
	}
}