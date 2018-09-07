<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/infstyle.css" type="text/css" > 
<link rel="stylesheet" href="css/personal.css" type="text/css" > 

<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />


<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/jquery.validate.min.js" type="text/javascript"></script>
<script src="js/yanzhengma.js" type="text/javascript"></script>
<!-- 引入自定义css文件 style.css -->
<link rel="stylesheet" href="css/style.css" type="text/css" />
</head>
<body>

<!-- 引入header.jsp -->
			<jsp:include page="/header.jsp"></jsp:include>
			<div class="center">
			
			<div class="col-main">
				<div class="main-wrap">

					<div class="user-info">
						<!--标题 -->
						<div class="am-cf am-padding">
							<div class="am-fl am-cf"><strong class="am-text-danger am-text-lg">个人资料</strong> / <small>Personal&nbsp;information</small></div>
						</div>
						<hr/>

						<!--头像 -->
						<div class="user-infoPic">

							<div class="filePic">
								<input type="file" class="inputPic" allowexts="gif,jpeg,jpg,png,bmp" accept="image/*">
								<img class="am-circle am-img-thumbnail" src="images/getAvatar.do.jpg" alt="" />
							</div>

							<p class="am-form-help">头像</p>

							<div class="info-m">
								<div><b><i>${user.username}</i></b></div>
								
							</div>
							<div align="right"><i><a href="${pageContext.request.contextPath}/delete.jsp">删除账户</a></i></div>
								
								
							
						</div>

						<!--个人信息 -->
						<div class="info-main">
							<form class="am-form am-form-horizontal" action="#" method="POST" id="userForm">

								<div class="am-form-group">
									<label for="user-name2" class="am-form-label">昵称</label>
									<div class="am-form-content">
										<input type="text" id="user-name" placeholder="nickname" value=${user.username}>

									</div>
								</div>

								<div class="am-form-group">
									<label for="user-name" class="am-form-label">姓名</label>
									<div class="am-form-content">
										<input type="text" id="user-name2" placeholder="name" value=${user.name}>
									</div>
								</div>

							
								<div class="am-form-group">
									<label class="am-form-label">性别</label>
									
									<div class="am-form-content sex">
										<label class="am-radio-inline">
											<input type="radio" name="radio10" value="male" data-am-ucheck id="checked"> 男
										</label>
										<label class="am-radio-inline">
											<input type="radio" name="radio10" value="female" data-am-ucheck id="checked2"> 女
										</label>
									</div>
								</div>
							
								<div class="am-form-group">
									<div class="form-group">
						<label for="date" class="am-form-label">出生日期</label>
						<div class="col-sm-6">
							<input type="date" class="form-control" id="birthday" name="birthday" value=${user.birthday}>
						</div>
					</div>
							
								</div>
								<div class="am-form-group">
									<label for="user-phone" class="am-form-label">电话</label>
									<div class="am-form-content">
										<input id="user-phone" placeholder="telephonenumber" type="tel" value=${user.telephone}>

									</div>
								</div>
								<div class="am-form-group">
									<label for="user-email" class="am-form-label">电子邮件</label>
									<div class="am-form-content">
										<input id="user-email" placeholder="Email" type="email" value=${user.email}>

									</div>
								</div>
								<div align="center" >
									<a onclick="infoChange()" id="span1">保存修改</a>
								</div>

							</form>
						</div>

					</div>

				</div>
				</div>
				</div>
<script type="text/javascript">
if("${user.sex}"=="男"||"${user.sex}"=="male"){
	document.getElementById("checked").checked=true;
	document.getElementById("checked2").checked=false;
}
else if("${user.sex}"=="女"||"${user.sex}"=="female"){
	document.getElementById("checked").checked=false;
	document.getElementById("checked2").checked=true;
}



function infoChange(){
      //获取文本框值
     var username=document.getElementById("user-name").value;
     var name = document.getElementById("user-name2").value;
     var birthday = document.getElementById("birthday").value;
     var telephone = document.getElementById("user-phone").value;
     var email = document.getElementById("user-email").value;
     var csex;
     var sexchange;
     if("${user.sex}"=="男"||"${user.sex}"=="male"){
    	 
    		 sexchange = document.getElementById("checked2").checked;
    		 if(sexchange){
    			 csex = "female";
    		 }
    		 else{
    			 csex = "male";
    		 }
     }
     else{
    	 sexchange = document.getElementById("checked").checked
    	 if(sexchange){
    		 csex = "male";
    	 }
    	 else{
    		 csex = "female";
    	 }
     }
     
     
     
     if(username!="${user.username}"||name!="${user.name}"||birthday!="${user.birthday}"
    		 ||telephone!="${user.telephone}"||email!="${user.email}"||sexchange){
    	//1.创建Ajax核心对象XMLHttpRequest
         var xhr=createXmlHttp();
         //2.设置监听
         xhr.onreadystatechange = function(){
           //Ajax引擎状态为成功
    	   if(xhr.readyState == 4){
    	       //HTTP协议状态为成功
    		   if(xhr.status == 200){
    		            //显示返回结果信息
    					document.getElementById("span1").innerHTML = xhr.responseText;	//请求结果内容			
    			}
    		 }
    	  }
         //3.打开连接
           //设置请求方式为Get，设置请求的URL，设置为异步提交（true）
           
//      alert(username+"${user.username}");
//      alert(name+"${user.name}");
//      alert(birthday+" "+"${user.birthday}");
//      alert(telephone+" "+"${user.telephone}");
//      alert(email+" "+"${user.email}");
//      alert(csex+" "+sexchange);
           xhr.open("POST","${pageContext.request.contextPath}/user?method=infoChange&time="+ new Date().getTime()+
        		   "&username="+username+"&name="+name+"&sex="+csex+"&birthday="+birthday+
        		   "&telephone="+telephone+"&email="+email+"&uid"+"=${user.uid}",true);
         //4.发送  
         
          xhr.send();
          var data=xhr.responseText;
     }
      function createXmlHttp(){
         var xmlHttp;
         try{//非IE浏览器，如Firefox
              xmlHttp=new XMLHttpRequest();
         }
         catch(e){
           try{//IE
               xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
           }
           catch(e){
               try{
                    xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
               }
               catch(e){}
           }
        }
        return xmlHttp;

     }
      return false;

  }

	
</script>
</body>

</html>