/**
 * TODU change identuty code and ajax appliction
 */
function change(){
	document.getElementById("imge").src="checkcode?r="+new Date().getTime();
}
var m = (function disable(){
	var flag = 1;
	return function(){
		if(flag==1){
			var username = document.getElementById("username");
			var password = document.getElementById("password");
			var checkcode = document.getElementById("checkcode");
			username.name="username1";
			password.name="password1";
			checkcode.name="checkcode1";
			flag = 0;
			return flag;
		}
		else{
			var username = document.getElementById("username");
			var password = document.getElementById("password");
			var checkcode = document.getElementById("checkcode");
			username.name="username";
			password.name="password";
			checkcode.name="checkcode";
			flag = 1;
			return flag;
		}
	}
})();

function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}
var n=(function(){
	var flag = 1;
	return function(){
		if(flag ==1){
			var cake = getCookie("username");
			var dog = document.getElementById("username");
			dog.value = cake;
			flag = 0;
			return flag;
		}
		else{
			document.getElementById("username").value = "";
			flag = 1;
			return flag;
		}
	} 
})();

function myFunc(){
	n();
}

function myFunction(){
	m();
}



$.validator.addMethod(
		"passcode",
		function(value,element,params){
			var flags = true;
			
			$.ajax({
				"async":false,
				"url":"passcode",
				"type":"POST",
				"data":{"passcode":value},
				"dataType":"json",
				"success":function(data){
					flag = data.passcode;//true--存在  false--不存在
				}
					
			});
			
			return flag;
	}
)
//$.validator.addMethod(
//		"pass",
//		function(value,element,params){
//			var flags = true;
//			
//			$.ajax({
//				"async":false,
//				"url":"passcode",
//				"type":"POST",
//				"data":{"passcode":value},
//				"dataType":"json",
//				"success":function(data){
//					flag = data.passcode;//true--存在  false--不存在
//				}
//					
//			});
//			
//			return flag;
//	}
//)

$(function(){
		$("#loginForm").validate({
			rules:{
				"username":{
					"required":true
				},
				"password":{
					"required":true,
					"rangelength":[6,12]
				},
				"checkcode":{
					"passcode":true
				}
			},
			messages:{
				"username":{
					"required":"用户名不能为空"
				},
				"password":{
					"required":"密码不能为空",
					"rangelength":"密码长度在6-12位"
				},
				"checkcode":{
					"passcode":"验证码错误"
				}
			},
			errorPlacement: function (error, element) { //指定错误信息位置
			      if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox
		
			       error.appendTo(element.parent().parent()); //将错误信息添加当前元素的父结点后面
			     } else {
			       error.insertAfter(element);
			     }
			   }
			
		}); 
})





