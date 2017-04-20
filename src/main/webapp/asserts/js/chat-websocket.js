/**
 * 
 */

var byte;
var messageTo = null;

var histories = [];

var localStorage = window.localStorage;
var sessionStrage = window.localStorage;


Date.prototype.pattern = function (fmt) {         
    var o = {         
    "M+" : this.getMonth()+1, //月份         
    "d+" : this.getDate(), //日         
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
    "H+" : this.getHours(), //小时         
    "m+" : this.getMinutes(), //分         
    "s+" : this.getSeconds(), //秒         
    "q+" : Math.floor((this.getMonth()+3)/3), //季度         
    "S" : this.getMilliseconds() //毫秒         
    };         
    var week = {         
    "0" : "/u65e5",         
    "1" : "/u4e00",         
    "2" : "/u4e8c",         
    "3" : "/u4e09",         
    "4" : "/u56db",         
    "5" : "/u4e94",         
    "6" : "/u516d"        
    };         
    if(/(y+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
    }         
    if(/(E+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);         
    }         
    for(var k in o){         
        if(new RegExp("("+ k +")").test(fmt)){         
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
        }         
    }         
    return fmt;         
}

function History(htmls, id) {
	this.id = id;
	this.htmls = htmls;
	return this;
}


$(document).ready(function(){
	initConnection();
    getOnlineUsers()
    loadChatHistory()
    $('#switch-list').click(function(){
        var classname = $('.online-list').attr('class')
        if (classname.indexOf('hide-list') != -1) {
            classname = classname.replace(/\s*hide-list\s*/, '')
        }else {
            classname += ' hide-list '
        }
        $('.online-list').attr('class', classname)
    })
    
    $('#opt-close').click(function(){
        var classname = $('.online-list').attr('class')
        classname = classname.replace(/\s*hide-list\s*/, '')
        classname += ' hide-list '
        $('.online-list').attr('class', classname)
    })
    
    $('#send-message').click(function(){
        sendMessage()
    })
    
    $('#logout').click(function(){
    	closeConnection()
    })
    
    // 显示及隐藏二进制数据传输
    $('#other-message').click(function(){
        var classname = $('.binary-source').attr('class')
        if (null != classname && classname.indexOf('hide-source') != -1) {
            classname = classname.replace(/\s*hide-source\s*/, '')
        }else {
            classname += ' hide-source '
        }
        if (null != classname) {
            $('.binary-source').attr('class', classname)
            var inputElem = $('#message-content')[0]
            inputElem.scrollIntoView(false)
        }
    })
    
    // 检查是否支持多媒体
//    initAudio();
    $('#image').click(function(){
        $('#image-input').click();
        $('#image-input')[0].onchange = function(event) {
            var reader = new FileReader();
            var preview = new FileReader()
            var input = event.target;
            var fileUrl = input.files[0];
            reader.readAsArrayBuffer(fileUrl)
            preview.readAsDataURL(fileUrl)
            reader.onload = function(event){
            	websocket.binaryType="arraybuffer";
            	byte = event.target.result;
               websocket.send(event.target.result);
            }
            preview.onload = function(event) {
            	var url = event.target.result;
                var time = new Date();
//                var timeStr = time.getHours() + ':' + time.getMinutes();
            	var imgMsgTemplate = '<li class="message-cell message-sent">\
						                    <a class="user-face icon-face"></a>\
						                    <span class="message-grid">\
						                        <a class="message-time">{{time}}</a>\
						                        <a class="message-content"><img src="{{src}}" onclick="previewImage()"></a>\
						                    </span>\
						                </li>';
				var msgText = imgMsgTemplate.replace(/\s*{{src}}\s*/, event.target.result).replace(/\s*{{time}}\s*/, time.pattern("HH:mm"));
				$('.messages-list').append(msgText)
            };
        }
    })
    
    
    $('#multichat').click(function(){
    	if (null == messageTo){
    		return;
    	}
    	$('#chat-title').html('群聊中...')
        backupCurHistory()
    	var his = getHistory(null)
    	if (null != his){
    		$('.messages-list').html(his.htmls)
    	}else {
    		$('.messages-list').html('');
    	}
    	messageTo = null;
    })
    
})

function findHistory(history) {
	for (var i = 0;i < histories.length; i++) {
		var cur = histories[i]
		if (cur.id == history.id && cur.from == history.from) {
			return cur;
		}
	}
}
	var websocket = null;
	function initConnection(){
		if ("WebSocket" in window) {
			websocket = new WebSocket(url)
		}else{
			alert ('您当前的浏览器不支持WebSocket！')
		}
		websocket.onerror = function(){
			alert('error!');
		}
		websocket.onopen = function(){
		}
		websocket.onmessage = function(event){
			
			if (event.data instanceof ArrayBuffer) {
				// 二进制数据
				byte = event.data;
				return;
			}else if (event.data instanceof Blob) {
                byte = event.data
                var reader = new FileReader()
                reader.readAsDataURL(byte)
                var time = new Date()
//                var timeStr = time.getHours() + ':' + time.getMinutes()
                reader.onload = function(event) {
                	var imgMsgTemplate = '<li class="message-cell message-recieved">\
						                    <a class="user-face icon-face"></a>\
						                    <span class="message-grid">\
						                        <a class="message-time">{{time}}</a>\
						                        <a class="message-content"><img src="{{src}}" onclick="previewImage()"></a>\
						                    </span>\
						                </li>';
                    var msgText = imgMsgTemplate.replace(/\s*{{src}}\s*/, event.target.result).replace(/\s*{{time}}\s*/, time.pattern("HH:mm"));
                    $('.messages-list').append(msgText)
                }
                return;
            }
            var msg = JSON.parse(event.data)
            if (msg.to == null) {
            	// 群聊消息
            	if (messageTo == null) {
            		// 当前为群聊模式
            		
            	}else {
            		// 当前非群聊模式，切换至群聊模式，并备份当前聊天记录
                    $('#multichat').click()
//            		var chatHistory = $('.messages-list').html()
//            		var id = messageTo;
//            		var his = new History(chatHistory, id)
//            		var curHistory = null;
//            		if ((curHistory = findHistory(his)) != null) {// 存在，更新原记录
//            			curHistory.htmls = his.htmls;
//            			curHistory.id = his.id;
//            		}else {// 不存在，直接push
//            			histories.push(his);
//            		}
//            		// 将聊天记录写入界面
//            		his.id = null;
//            		his.htmls = null;
//            		curHistory = findHistory(his)
//            		if (null == curHistory) {
//            			// 不存在记录
//            			 $('.messages-list').html('')
//            		}else {
//            			 $('.messages-list').html(curHistory.htmls)
//            		}
//            		
//            		messageTo = null;// 置为null表示群聊模式
//            		$('#chat-title').html('群聊中...')
            	}
            }
            // 当前为私聊模式
            if (null != msg.to) {
            	// 私聊消息
            	if (messageTo != msg.from) { // 当前私聊对象与消息到来的对象不同,备份聊天数据
                    backupCurHistory()
//            		var htmls = $('.messages-list').html()
//            		var id = messageTo;
//            		var his = new History(htmls, id);
//            		var foundHis = null;
//            		if ((foundHis = findHistory(his)) == null) { //不存在该历史纪录
//            			histories.push(his)
//            		}else {
//            			// 存在该记录
//                        histories.pop(foundHis)
//                        histories.push(his)
//            		}
            		// 加载历史纪录
//            		his.htmls = null;
//            		his.id = msg.to;
//            		foundHis = findHistory(his)
//            		if (foundHis == null) // 不存在历史纪录
//            			$('.messages-list').html('')
//            		else
//            			$('.messages-list').html(foundHis.htmls)
                    var storedHis = getHistory(msg.from)
                    if (null == storedHis) {
                        $('.messages-list').html('')
                    }else {
                        $('.messages-list').html(storedHis.htmls)
                    }
            	}
            	messageTo = msg.from; // 当前私信对象更改
            	$.ajax({
            		url: 'information?username=' + messageTo,
            		typte: 'GET',
            		success: function(data) {
            			$('#chat-title').html('与' + data.nickname + '聊天中...')
            	    	$('#message-content').removeAttr('disabled');
            		},
            		error: function(){
            			
            		}
            	})
            }
            // 需要判断msg类型
            if (null != msg.messageType && msg.messageType.trim() == 'ChatMessage') {
                addRecievedMessage(msg)
            }else {
                // 系统消息，用于通知更新用户列表
                if (null != msg.messageType && msg.messageType == 'SystemNotify') {
                    getOnlineUsers()// 获取用户列表
                }
            }
		}
		websocket.onclose = function(){
            logout()
            alert('您当前已经被注销！请重新登录');
            window.location.href = window.location.href;
		}
		window.onbeforeunload = function(){
			backupCurHistory()
			return  '确定离开当前页面吗？\n注意：您的连接可能暂时被关闭，我们将自动为您备份聊天记录！'
		}
	}
	
	function closeConnection(){
		websocket.close();
	}


function sendMessage() {
    var content = $('#message-content').val()
        if (content == null || content.trim().length <= 0) {
            $('#message-content')[0].focus()
            return false;
        }
        var date = new Date();
        var msg = {
            time: date.pattern("HH:mm"),
            messageContent: content,
            messageType: 'ChatMessage',
            from: loginedUsername,
        }// 群聊消息
        if (null != messageTo) {
        	msg.to = messageTo
		}
        addSentMessage(msg)
        $('#message-content').val('')// 滞空
        // 发送
        if (websocket == null) {
            initConnection()
        }
        websocket.send(JSON.stringify(msg))
        return false;
}

function addSentMessage(msg) {
    var sentMsgTemplate = '<li class="message-cell message-sent">\
                    <a class="user-face icon-face"></a>\
                    <span class="message-grid">\
                        <a class="message-time">{{time}}</a>\
                        <a class="send-user">{{user}}</a>\
                        <a class="message-content">{{content}}</a>\
                    </span>\
                </li>';
    if (msg.time == null) {
        var now = new Date()
        msg.time = now.pattern("HH:mm")
    }
    var msgText = sentMsgTemplate.replace(/\s*{{time}}\s*/, msg.time).replace(/\s*{{content}}\a*/, msg.messageContent).replace(/\s*{{user}}\s*/, loginedUsernick);
    $('.messages-list').append(msgText);
    scrollToBottom()
}

function addRecievedMessage(msg) {
    var recievedMsgTemplate = '<li class="message-cell message-recieved">\
                    <a class="user-face icon-face"></a>\
                    <span class="message-grid">\
                        <a class="message-time">{{time}}</a>\
                        <a class="send-user">{{user}}</a>\
                        <a class="message-content">{{content}}</a>\
                    </span>\
                </li>';
    if (msg.time == null) {
        var now = new Date()
        msg.time = now.pattern("HH:mm")
    }
    var msgText = recievedMsgTemplate.replace(/\s*{{time}}\s*/, msg.time).replace(/\s*{{content}}\a*/, msg.messageContent).replace(/\s*{{user}}\s*/, msg.fromNick);
    $('.messages-list').append(msgText)
    scrollToBottom()
}

function scrollToBottom() {
    var destView = $('.bottom-anchors')[0];
    destView.scrollIntoView(false);
}

function logout(){
    $.ajax({
        url: 'logout',
        type: 'POST',
        data: JSON.stringify({}),
        dataType: "JSON",
        contentType: "application/json;charset=utf-8",
        success: function (){
        },
        error: function(){
            
        }
    })
}

function getOnlineUsers(){
    $.ajax({
        url: 'userlist',
        type: 'GET',
        success: function(data){
             var onlineUserTemplate = '<li class="user-cell">\
                            <a class="user-face icon-face"></a>\
                            <span class="user-info"><a class="user-name">{{username}}</a><a class="user-nick">{{nickname}}</a></span>\
                            <a class="opt-chat icon-singlechat" href="javascript:;" id="opt-chat" onclick="startSingleChat()"></a>\
                        </li>';
            var htmls = [];
            var allUsers = data
            if (allUsers instanceof Array) {
                for (var i = 0; i < allUsers.length; i++) {
                    var user = allUsers[i];
                    var _tmpHtml = onlineUserTemplate.replace(/\s*{{username}}\s*/, user.username).replace(/\s*{{nickname}}\s*/, user.nickname);
                    htmls.push(_tmpHtml)
                }
            }else {
                
            }
            $('.users-list').html('');
            $('.users-list').append(htmls.join(''))
            // 绑定事件
        },
        error: function(){
            
        }
    })
}

function startSingleChat(){
	var target = event.target;
	var username = target.previousElementSibling.firstElementChild.innerHTML;
	var nickname = target.previousElementSibling.lastElementChild.innerHTML;
	var destUser = {
			username: username,
			nickname: nickname,
	}
	var selfuser = {
			username: loginedUsername,
			nickname: loginedUsernick,
	}
	if (selfuser.username == destUser.username) {
		alert('不能同自己发送消息！');
		return;
	}
	// 请求建立连接
	$('#message-content').attr('disabled', 'disabled');
	$.ajax({
		url: 'singlechat',
		type: 'POST',
		data: JSON.stringify({from: selfuser, to: destUser}),
		dataType: "JSON",
		contentType: "application/json;charset=utf-8",
		success: function(msg){
			$('#chat-title').html('与' + destUser.nickname + '聊天中...')
	    	$('#message-content').removeAttr('disabled');
            backupCurHistory()
            // 查找记录
            storedHis = getHistory(destUser.username);
            if (null == storedHis) {
                $('.messages-list').html('')
            }else {
                $('.messages-list').html(storedHis.htmls)
            }
			messageTo = destUser.username
		},
		error: function() {
			
		}
	})
}
var data,p;
function initAudio(){
        //调整兼容
    var AudioContext=AudioContext||webkitAudioContext;
    var context;
        context=new AudioContext;
        navigator.getUserMedia=
      navigator.getUserMedia||
      navigator.webkitGetUserMedia||
      navigator.mozGetUserMedia;
    //请求麦克风
    navigator.getUserMedia({audio:true},function(e){
      
      //从麦克风的输入流创建源节点
      var stream=context.createMediaStreamSource(e);
      //用于录音的processor节点
      var recorder=context.createScriptProcessor(1024,1,0);
      recorder.onaudioprocess=function(e){
        if(record.value=="停止")data.push(e.inputBuffer.getChannelData(0));
      };
      //用于播放的processor节点
      var player=context.createScriptProcessor(1024,0,1);
      player.onaudioprocess=function(e){
        if(!data)return;
        var i,s=data[p++];
        if(!s)return play.value=="停止"&&play.click();
        var buffer=e.outputBuffer.getChannelData(0);
        for(i=0;i<s.length;i++)buffer[i]=s[i];
      };
      //录音/停止 按钮点击动作
      $('#record').click(function(){
        if(record.value=="录音")
          return data=[],stream.connect(recorder),this.value="停止";
        if(record.value=="停止")
          return stream.disconnect(),this.value="录音";
      });
//      //播放/停止 按钮点击动作
//      play.onclick=function(){
//        if(this.value=="播放")
//          return p=0,player.connect(context.destination),this.value="停止";
//        if(this.value=="停止")
//          return player.disconnect(),this.value="播放";
//      };
//      //保存
//      save.onclick=function(){
//        var frequency=context.sampleRate; //采样频率
//        var pointSize=16; //采样点大小
//        var channelNumber=1; //声道数量
//        var blockSize=channelNumber*pointSize/8; //采样块大小
//        var wave=[]; //数据
//        for(var i=0;i<data.length;i++)
//          for(var j=0;j<data[i].length;j++)
//            wave.push(data[i][j]*0x8000|0);
//        var length=wave.length*pointSize/8; //数据长度
//        var buffer=new Uint8Array(length+44); //wav文件数据
//        var view=new DataView(buffer.buffer); //数据视图
//        buffer.set(new Uint8Array([0x52,0x49,0x46,0x46])); //"RIFF"
//        view.setUint32(4,data.length+44,true); //总长度
//        buffer.set(new Uint8Array([0x57,0x41,0x56,0x45]),8); //"WAVE"
//        buffer.set(new Uint8Array([0x66,0x6D,0x74,0x20]),12); //"fmt "
//        view.setUint32(16,16,true); //WAV头大小
//        view.setUint16(20,1,true); //编码方式
//        view.setUint16(22,1,true); //声道数量
//        view.setUint32(24,frequency,true); //采样频率
//        view.setUint32(28,frequency*blockSize,true); //每秒字节数
//        view.setUint16(32,blockSize,true); //采样块大小
//        view.setUint16(34,pointSize,true); //采样点大小
//        buffer.set(new Uint8Array([0x64,0x61,0x74,0x61]),36); //"data"
//        view.setUint32(40,length,true); //数据长度
//        buffer.set(new Uint8Array(new Int16Array(wave).buffer),44); //数据
//        //打开文件
//        var blob=new Blob([buffer],{type:"audio/wav"});
//        open(URL.createObjectURL(blob));
//      };
    },function(e){
      console.log("请求麦克风失败");
    });
}

function endPreview(){
    var div = event.target
    div.remove()
}

// 预览图像
function previewImage() {
    // 获取图片宽高
    var img = event.target;
    var _tmpImg = document.createElement('img')
    _tmpImg.src = img.src;
    var w = _tmpImg.width
    var h = _tmpImg.height
    var sW = window.innerWidth
    var sH = window.innerHeight
    var r
    if (w > sW) {
        r = h / w
        w = sW - 30;
        h = w * r
    }
    if (h > sH) {
        r = w / h
        h = sH - 30;
        w = h * r
    }
    var top = (sH - h) / 2
    var left = (sW - w) / 2
    var previewView = '<div class="preview-img" onclick="endPreview()">\
        <img src="{{src}}" width="{{width}}" height="{{height}}" style="margin-top:{{top}};margin-left:{{left}}">\
    </div>';
    previewView = previewView.replace(/\s*{{src}}\s*/, img.src).replace(/\s*{{top}}\s*/, top + 'px').replace(/\s*{{left}}\s*/, left + 'px').replace(/\s*{{width}}\s*/, w + 'px').replace(/\s*{{height}}\s*/, h + 'px');
    $('body').append(previewView)
}

function backupCurHistory(){
    // 备份当前历史纪录
	// 从localStorage读取历史纪录
    histories = getHistories()
    var curHis = new History($('.messages-list').html(), messageTo)
    curHis.from = loginedUsername
    var storedHis = findHistory(curHis);
    if (null == storedHis) {
        histories.push(curHis);
    }else {
        histories.splice(histories.indexOf(storedHis), 1);
        histories.push(curHis)
    }
    saveHistories(histories)
}

function getHistory(historyid) {
    // 读取记录
    histories = getHistories()
    var desthis = new History(null, historyid);
    desthis.from = loginedUsername
    // 查找记录
    storedHis = findHistory(desthis);
    return storedHis;
}

function getHistories() {
    histories = window.localStorage.getItem('histories')
    if (null == histories) {
        histories = new Array()
    }else {
        histories = JSON.parse(histories)
    }
    return histories;
}


function saveHistories(his) {
    // 将历史纪录存储到storage
    window.localStorage.setItem('histories', JSON.stringify(his));
}

function loadChatHistory() {
    histories = getHistories();
    var destHis = new History(null, messageTo);
    destHis.from = loginedUsername
    var storedHis = findHistory(destHis);
    if (null == storedHis) {
        $('.messages-list').html('')
    }else {
        $('.messages-list').html(storedHis.htmls)
    }
}