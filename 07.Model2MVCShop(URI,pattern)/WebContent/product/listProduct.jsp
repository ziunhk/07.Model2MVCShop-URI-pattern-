<%@ page contentType="text/html; charset=euc-kr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- /////////////////////// EL / JSTL �������� �ּ� ó�� ////////////////////////

<%@ page import="java.util.*"  %>
<%@ page import="com.model2.mvc.service.domain.*" %>
<%@ page import="com.model2.mvc.common.*" %>
<%@ page import="com.model2.mvc.common.util.CommonUtil"%>

<%
	
	List<Product> list=(List<Product>)request.getAttribute("list");
	Page resultPage = (Page)request.getAttribute("resultPage");
	
	String menu = request.getParameter("menu");
	Search search=(Search)request.getAttribute("search");
	
	//==> null �� ""(nullString)���� ����
	System.out.println("listProduct JSP ���� ���� search(VO) : "+search);//
	
	String searchCondition = CommonUtil.null2str(search.getSearchCondition());
	String searchKeyword = CommonUtil.null2str(search.getSearchKeyword());
	
%>
%> 	/////////////////////// EL / JSTL �������� �ּ� ó�� //////////////////////// --%>

<html>
<head>
<title>��ǰ �����ȸ</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<!--  // �˻� / page �ΰ��� ��� ��� Form ������ ���� JavaScript �̿� --> 
<script type="text/javascript">
	function fncGetUserList(currentPage){
		document.getElementById("currentPage").value = currentPage;
		document.detailForm.submit();
	}
</script>

</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm" action="/product/listProduct" method="post">
<input name="menu" value="${param.menu}" type="hidden"/>
<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37">
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<c:if test = "${param.menu == 'manage'}">
						<td width="93%" class="ct_ttl01">��ǰ ����</td>
					</c:if>
					<c:if test = "${param.menu == 'search' }">
						<td width="93%" class="ct_ttl01">��ǰ �����ȸ</td>
					</c:if>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37">
		</td>
	</tr>
</table>



<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
	
		<c:if test = "${search.searchCondition != null}">
	
			<td align="right">
				<select name="searchCondition" class="ct_input_g" style="width:80px">
					<c:if test = "${search.searchCondition eq '0'}">
						<option value="0" selected>��ǰNO</option>
						<option value="1">��ǰ��</option>
					</c:if>
					<c:if test = "${search.searchCondition eq '1' }">				
						<option value="0">��ǰNO</option>
						<option value="1" selected>��ǰ��</option>
					</c:if>
					
				</select>
				<input 	type="text" name="searchKeyword"  value="${search.searchKeyword}" 
								class="ct_input_g" style="width:200px; height:19px" >
			</td>
		</c:if>
		<c:if test = "${search.searchCondition == null }">	
			<td align="right">
				<select name="searchCondition" class="ct_input_g" style="width:80px">
					<option value="0">��ǰNO</option>
					<option value="1">��ǰ��</option>
				</select>
				<input type="text" name="searchKeyword"  class="ct_input_g" style="width:200px; height:19px" >
			</td>
		</c:if>

		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetProductList();">�˻�</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" >��ü  ${resultPage.totalCount } �Ǽ�, ���� ${resultPage.currentPage} ������</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">��ǰ��</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">����</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="500">�����</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">�������</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	
	<c:set var="i" value="0" />
	<c:forEach var="product" items="${list }">
		<c:set var="i" value="${i+1 }" />
			<tr class="ct_list_pop">
				<td align="center">${i }</td>
				<td></td>
				<c:if test = "${param.menu == 'manage'}">
					<td align="left">	
					<!-- <a href="/updateProductView.do?prodNo=${product.prodNo}&menu=${param.menu}">${product.prodName}</a> -->
					<a href="/product/updateProductView?prodNo=${product.prodNo}&menu=${param.menu}">${product.prodName}</a>
					</td>
				</c:if>
				<c:if test = "${param.menu != 'manage'}">
					<td align="left">	
					<!-- <a href="/getProduct.do?prodNo=${product.prodNo}&menu=${param.menu}">${product.prodName}</a> -->
					<a href="/product/getProduct?prodNo=${product.prodNo}&menu=${param.menu}">${product.prodName}</a>
					</td>
				</c:if>
				<td></td>
				<td align="left">${product.price}</td>
				<td></td>
				<td align="left">${product.regDate}</td>
				<td></td>
				<c:if test="${product.proTranCode  == null}">
				<td align="left">
					�Ǹ���
				</td>
				</c:if>
				<c:if test="${product.proTranCode  == '1  '}">
				<td align="left">
					���ſϷ�
					<c:if test="${param.menu=='manage' }">
						<!-- <a href="/updateTranCodeByProd.do?prodNo=${product.prodNo}&tranCode=2">����ϱ�</a> -->
						<a href="/purchase/updateTranCode?prodNo=${product.prodNo}&tranCode=2">����ϱ�</a>
					</c:if>
				</td>
				</c:if>
				<c:if test="${product.proTranCode == '2  '}">
				<td align="left">
					������Դϴ�.
				</td>
				</c:if>
				<c:if test="${product.proTranCode == '3  '}">
				<td align="left">
					����� �Ϸ�Ǿ����ϴ�.
				</td>
				</c:if>
			</tr>
		<tr>
			<td colspan="11" bgcolor="D6D7D6" height="1"></td>
		</tr>
	</c:forEach>
</table>

<%System.out.println("listProduct.jsp ���� request.getParameter('menu') : "+request.getParameter("menu")); %>

<!-- PageNavigation Start... -->
<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="center">
			<input type="hidden" id="currentPage" name="currentPage" value=""/>
			
			<%-- /////////////////////// EL / JSTL �������� �ּ� ó�� ////////////////////////
			<%System.out.println("listProduct ���� resultPage Ȯ����� : "+resultPage); %>
			<% if( resultPage.getCurrentPage() <= resultPage.getPageUnit() ){ %>
					�� ����
			<% }else{ %>
					<a href="javascript:fncGetProductList('<%=resultPage.getCurrentPage()-1%>')">�� ����</a>
			<% } %>

			<%	for(int i=resultPage.getBeginUnitPage();i<= resultPage.getEndUnitPage() ;i++){	%>
					<a href="javascript:fncGetProductList('<%=i %>');"><%=i %></a>
			<% 	}  %>
	
			<% if( resultPage.getEndUnitPage() >= resultPage.getMaxPage() ){ %>
					���� ��
			<% }else{ %>
					<a href="javascript:fncGetProductList('<%=resultPage.getEndUnitPage()+1%>')">���� ��</a>
			<% } %>
			/////////////////////// EL / JSTL �������� �ּ� ó�� //////////////////////// --%>
			
			<jsp:include page="../common/pageNavigator.jsp"/>
    	</td>
	</tr>
</table>
<!--  ������ Navigator �� -->
</form>
</div>

</body>
</html>