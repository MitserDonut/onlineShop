<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员登录</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<!-- 引入自定义css文件 style.css -->
<link rel="stylesheet" href="css/style.css" type="text/css" />

<style>
body {
	margin-top: 20px;
	margin: 0 auto;
}

.carousel-inner .item img {
	width: 100%;
	height: 300px;
}
</style>
<script type="text/javascript">
	function addCart(){
		var buyNum = $("#buyNum").val();//获取购买数量
		//跳转
		window.location="${pageContext.request.contextPath}/product?method=addCart&pid=${product.pid}&buyNum="+buyNum;
	}
</script>
</head>

<body>
	<!-- 引入header.jsp -->
	<jsp:include page="/header.jsp"></jsp:include>

	<div class="container">
		<div class="row">
		
			<div
				style="border: 1px solid #e4e4e4; width: 930px; margin-bottom: 10px; margin: 0 auto; padding: 10px; margin-bottom: 10px;">
				<!-- <a href="./index.htm">首页&nbsp;&nbsp;&gt;</a> <a href="./蔬菜分类.htm">蔬菜&nbsp;&nbsp;&gt;</a> -->
				<!-- 返回商品列表 -->
				<h4><a href="${pageContext.request.contextPath}/product?method=product_list&cid=${cid}&currentPage=${currentPage}">返回</a></h4>
			</div>
			
			<div style="margin: 0 auto; width: 950px;">
				<div class="col-md-6">
					<img style="opacity: 1; width: 400px; height: 350px;" title=""
						class="medium"
						src="${pageContext.request.contextPath}/${product.pimage}">
				</div>

				<div class="col-md-6">
					<div>
						<strong>${product.pname}</strong>
					</div>
					<div
						style="border-bottom: 1px dotted #dddddd; width: 350px; margin: 10px 0 10px 0;">
						<div>编号：751</div>
					</div>

					<div style="margin: 10px 0 10px 0;">
						参 考 价： ￥：<del>${product.market_price }元/份 </del>促销价：<strong style="color: #ef0101;">￥${product.shop_price}元/份</strong>
						
					</div>

					<div style="margin: 10px 0 10px 0;">
						促销: <a target="_blank" title="限时抢购 (2014-07-30 ~ 2015-01-01)"
							style="background-color: #f07373;">限时抢购</a>
					</div>

					<div
						style="padding: 10px; border: 1px solid #e7dbb1; width: 330px; margin: 15px 0 10px 0;; background-color: #fffee6;">
						<div style="margin: 5px 0 10px 0;">白色</div>

						<div
							style="border-bottom: 1px solid #faeac7; margin-top: 20px; padding-left: 10px;">
							购买数量: <input id="buyNum" name="quantity" value="1"
								maxlength="4" size="10" type="text">
						</div>

						<div style="margin: 20px 0 10px 0;; text-align: center;">
							<!-- 不让超链接直接跳转  -->
							<a href="javascript:void(0)" onclick="addCart()"> <input
								style="background: url('./images/product.gif') no-repeat scroll 0 -600px rgba(0, 0, 0, 0); height: 36px; width: 127px;"
								value="加入购物车" type="button">
							</a> &nbsp;
						</div>
					</div>
				</div>
			</div>
			<div class="clear"></div>
			<div style="width: 950px; margin: 0 auto;">
			
			<br>
			<br>
			<br>
				<div style="background-color: #d3d3d3; width: 930px; padding: 10px 10px; margin: 10px 0 10px 0;">
					<strong>商品介绍</strong>
				</div>
                <!-- 商品详情 -->
				<div>
					<img height="300" width="300"
						src="${pageContext.request.contextPath}/${product.pimage}">
						商品描述: <strong style="color: #ef0101;">${product.pdesc }</strong>
				</div>

				

				

		</div>
	</div>


	<!-- 引入footer.jsp -->
	<jsp:include page="/footer.jsp"></jsp:include>

</body>

</html>