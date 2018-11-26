<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri="c.tld" prefix="c" %>
<%@ taglib uri="fmt.tld" prefix="fmt" %>
<%@ taglib uri="agreement.tld" prefix="ag" %>

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:requestEncoding value="UTF-8"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<fmt:setLocale value="${sessionObj.languageId}"/>

<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />
<c:set var="language_id" value="${sessionObj.languageId}" />
<fmt:bundle basename="${language}">

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ifixpng2.js"></script>
<script type="text/javascript" src="../js/js.js"></script>
<script type="text/javascript" src="../js/dm.js"></script>
<script type="text/javascript" src="../js/search.js"></script>

<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement1.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement3.js"/>'></script>

<link type="text/css" rel="stylesheet" href='<c:url value="/resources/agreement/calender/theme.css"/>'>
<script src='<c:url value="/resources/agreement/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-${language_id}.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-setup.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/ope_ajax.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/checkdate.js"/>'></script>


<TITLE><fmt:message key="agreement"/>-<fmt:message key="new"/></TITLE>

<!-- this function is for getting  list of check boxes has checked -->

 <SCRIPT LANGUAGE="JavaScript">

var eventFlag='Y';

         function getSelected(opt) {
            var selected = new Array();
            var index = 0;
            for (var intLoop = 0; intLoop < opt.length; intLoop++) {
               if ( (opt[intLoop].checked))
			   {
				  
                  index = selected.length;
				 
                  selected[index] = new Object;
				  
                  selected[index].value = opt[intLoop].value;
				 
                  selected[index].index = intLoop;
                  
               }
            }
            return selected;
         }

         function outputSelected() {
		    var dd=document.getElementsByName('services');
			var sel = getSelected(dd);	
            var strSel = "";
            for (var item in sel)       
            strSel += sel[item].value + ",";
			
			if(strSel=="" || strSel==null)
			 {
				alert(agmtMesg[2]); //select atleast one service
				return false;
			 }
			 else
			 {
				 document.getElementById('selectedServices').value=strSel;
				 return true;
			 }		 			
         }

		function checkSpecialCharForTelephone(obj) 
		{
			var name = document.getElementById(obj.id).value;
			if(name!=''){
			if(name.match(/^[0-9+ -]+$/)) {
			return true;
			} else {
			alert(agmtMesg[12]); // Invalid telephone number
			document.getElementById(obj.id).value='';
			document.getElementById(obj.id).focus();
			return false;
			}
			}
		}

		function checkSpecialCharForZipCode(obj) 
		{
			var name = document.getElementById(obj.id).value;
			if(name!=''){
			if(name.match(/^[0-9A-Za-z ]+$/)) {
			return true;
			} else {
			alert(agmtMesg[13]); // Invalid zip code
			document.getElementById(obj.id).value='';
			document.getElementById(obj.id).focus();
			return false;
			}
			}
		}

		function checkSpecialChar(obj) 
		{
			var name = document.getElementById(obj.id).value;
			if(name!=''){
			if(name.match(/^[a-zA-Z0-9 ]+$/)) {
			return true;
			} else {
			alert(agmtMesg[9]); // Special characters are not allowed
			document.getElementById(obj.id).value='';
			document.getElementById(obj.id).focus();
			return false;
			}
			}
		}

		//Ajax response to know the agreement clear status

		function ajaxReturnToFunctionClearAgreement(xmlEnterpriseDates)
		{
			var xmlDoc = xmlEnterpriseDates.documentElement;
			// Variable to store the Delete Status. 
			var status = xmlDoc.getElementsByTagName("delete_status")[0].childNodes[0].nodeValue;
			if( status =='0')
			{
				return false;
			} else
				return true;
		}

		function clearAgreementdetails()
		{
			
			var list_input = document.getElementsByTagName ('input');
			var flag= false;
			if (list_input)
			{
				for (var i = 0; i < list_input.length; ++i)
				{
					var e = list_input[i]; 
					if (e.type == "checkbox"){
							
					
						if(e.checked==true) {
							flag=true;
							break;
						}
					}
				}
			}
			if(flag == true){
				agreementId=document.getElementById('internalAgreementId').value;
				getAjaxForClearAgreement(agreementId);		        
			}
			document.agreementnewform.reset();
			closeWindows();
			
        }
          
		
		

 </SCRIPT> 
<title><fmt:message key="agreement"/> - <fmt:message key="new"/></title>

</HEAD>

<!-- closeWindows() method closes all the child windows of this screen,
      method body is defined in agreement1.js file -->

<BODY class="shadowbody" onload="setService();" onUnLOad="closeWindows();" onbeforeunload="callDeleteAction();">
<html:form action="/resources/agreement/jsp/agreementnew">
<%@ include file="common/JavascriptErrorMsg.jsp" %>

<div class="popup"><span class="popup_shadow">
	<div class="popup_title" ><span class="popup_text"><fmt:message key="agreement"/> - <fmt:message key="new"/></span></span></div></span>
</div>

<c:forEach var="lstRights" items="${sessionObj.roleList}">
	<c:if test="${lstRights.entityCode eq 'AGMT'}">		
		<c:set var="rights" value="${lstRights.rightNo}"/>
	</c:if>
 </c:forEach>

<c:set var="NEW" value="${fn:substring(rights, 0, 1)}" />

   <ul class="toolbar2">    
	<!-- Subnavigation    -->		
		  <li class="save">
		  <c:choose>	 
			<c:when test="${NEW=='1'}">
			  <a onClick="javascript:validateAgreement();" tabindex="1" onkeyPress="methodCallAfterEnterKey('validateAgreement');"
			  onMouseOver="hover('a1','ahand');" 
			  onMouseOut="hover('a1','nohand');" id="a1" title="<fmt:message key="save_and_close"/>">
			  <fmt:message key="save_and_close"/></a> 
			</c:when>

			<c:otherwise>
			  <a disabled > <fmt:message key="save_and_close" /></a>
			</c:otherwise>    
		  </c:choose>
			
		  </li>
		  <li class="clear" ><a onClick="javascript:clearAgreementdetails();" tabindex="2" onkeyPress="methodCallAfterEnterKey('clearAgreementdetails');"
			onMouseOver="hover('a3','ahand');" 
			onMouseOut="hover('a3','nohand');" id="a3" title="<fmt:message key="clear"/>"><fmt:message key="clear"/></a>
		  </li>
	</ul>

<div id="agreement_new_scrollbar">

<%@ include file="common/ErrorHandler.jsp" %>
<div class="record-def">
<input type="hidden" name="editorHold" value="NEW">
<!-- hidden field for holding selected services -->
<input type="hidden" name="selectedServices" id="selectedServices">

<table align="center" cellpadding="1" cellspacing="1" border="0" width="97%">
	<tr>
	  
	  <c:if test="${requestScope.requestAgreementId eq null}">		
			<ag:AgreementId var="internalAgreementId" scope="page"/>
			 <input type="hidden" name="internalAgreementId" id="internalAgreementId" value="<c:out value="${internalAgreementId}"/>">
			 <td><p class="label_normal"><fmt:message key="agreement_id"/>:</p></td> 
			 <td><p class="label_bold"><span class="asterisk">&nbsp;&nbsp;</span><c:out value="${internalAgreementId}"/></p></td>
			 <td>&nbsp;</td>

	  </c:if>

	  <c:if test="${requestScope.requestAgreementId ne null}">		
			 <input type="hidden" name="internalAgreementId" id="internalAgreementId" value="<c:out value="${requestScope.requestAgreementId}"/>">	 

			 <td><p class="label_normal"><fmt:message key="agreement_id"/>:</p></td> 
			 <td><p class="label_bold"><span class="asterisk">&nbsp;&nbsp;</span><c:out value="${requestScope.requestAgreementId}"/></p></td>
			 <td>&nbsp;</td>


	  </c:if>

	 
	 

	  
	</tr>

	<tr>
		<td><p class="label_normal"><fmt:message key="agreement_title"/>:</p></td> 
        <c:set var="agreement_title"><fmt:message key="agreement_title"/></c:set>
		<td colspan="3" ><span class="asterisk">*</span><html:text  property="agreementTitle" maxlength="50" styleId="${agreement_title}" onkeypress="return alphanumericWithSpace(this);" onblur="javascript: this.value=Trim(this.value);checkSpecialChar(this);" size="40"/></td>
	</tr>
    <tr>
		<td><p class="label_normal"><fmt:message key="primary_contact"/>:</p></td>
		<c:set var="primary_contact"><fmt:message key="primary_contact"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="primaryContact" maxlength="40" styleId="${primary_contact}" size="30" onblur="javascript: this.value=Trim(this.value);"/></td>
	</tr>

	<tr>
		<td><p class="label_normal"><fmt:message key="street_address"/>:</p></td> 
        <c:set var="street_address"><fmt:message key="street_address"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="streetAddress" maxlength="255" styleId="${street_address}" size="50" onblur="javascript: this.value=Trim(this.value);"/></td>
   </tr>

    <tr>
        <td><p class="label_normal"><fmt:message key="city"/>:</p></td>
        <c:set var="city"><fmt:message key="city"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="city" maxlength="32" styleId="${city}" onblur="javascript: this.value=Trim(this.value);"/></td>
   </tr>

   <tr>
		<td><p class="label_normal"><fmt:message key="zip_code"/>:</p></td>
		<c:set var="zip_code"><fmt:message key="zip_code"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="zipCode" maxlength="8" size="14" styleId="${zip_code}" onkeypress="return alphanumericWithSpace(this);" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForZipCode(this);"/></td>
	</tr>
	<tr>
        <td><p class="label_normal"><fmt:message key="country"/>:</p></td>
        <c:set var="country"><fmt:message key="country"/></c:set>
		<ag:dropDown var="countries" scope="page" key="COUNTRY" />
        <td colspan="3"><span class="asterisk">*</span><html:select property="country" styleId="${country}" styleClass="select">
                          <html:option value=""><fmt:message key="select"/></html:option>
				          <c:forEach items="${countries}" var="country1">
				           <option value ="${country1.key}" <c:if test="${country1.key eq 'FI'}">selected</c:if> ><c:out value="${country1.value}"/></option>
			              </c:forEach>
                        </html:select>
        </td>
	</tr>
	<tr>
		<td><p class="label_normal"><fmt:message key="telephone"/>:</p></td>
        <c:set var="telephone"><fmt:message key="telephone"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="telephone" styleId="${telephone}" maxlength="13" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForTelephone(this);" onkeypress="return chkTelFax(this);" /></td>
	</tr>
    <tr>
		<td><p class="label_normal"><fmt:message key="email"/>:</p></td>
        <c:set var="email"><fmt:message key="email"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="email" size="30" maxlength="50"  styleId="${email}" onblur="javascript: chkEmail(this);"
		/></td>
	</tr>

    <tr>
		<td ><p class="login_userpass"><fmt:message key="valid_from"/>:</p></td>
		 <c:set var="valid_from"><fmt:message key="valid_from"/></c:set>
		<td><span class="asterisk">*</span><input type="text" name="validFrom" size="10" id="${valid_from}"  
			onkeypress="numcheck(this.value,'${sessionObj.dateFormat}');" 
			onkeyup="return mask(this.value,this,'${sessionObj.dateFormat}');" onblur="DateFormat(this,this.value,event,true,'${sessionObj.dateFormat}');
			return mask(this.value,this,'${sessionObj.dateFormat}');" readOnly="true" />
			
			<A title="<fmt:message key="click_for_calendar"/>" class="ahand" id="calendar">
			<img id="stdate_trigger" src="../images/cal_img.gif" 
		    style="{vertical-align:bottom;}" border="0"  >
			</A>
		</td>
		<td ><p class="login_userpass"><fmt:message key="valid_to"/>:</p></td>
		<td>
		    <c:set var="valid_to"><fmt:message key="valid_to"/></c:set>
			<input type="text" name="validTo" size="10" id="${valid_to}"  
			onkeypress="numcheck(this.value,'${sessionObj.dateFormat}');" 
			onkeyup="return mask(this.value,this,'${sessionObj.dateFormat}');" onblur="DateFormat(this,this.value,event,true,'${sessionObj.dateFormat}'); return mask(this.value,this,'${sessionObj.dateFormat}');" 
			readOnly="true"/>

			<A title="<fmt:message key="click_for_calendar"/>" class="ahand" id="calendar1">
			<img id="stdate_trigger1" src="../images/cal_img.gif" 
			style="{vertical-align:bottom;}" border="0">
			</A>  
		</td>
	</tr>	
    
</table>

<!--- here list of services is adding--->
<table width="97%" border="0">
<tr>
  <td><p class="label_bold"><fmt:message key="services"/><span class="asterisk">*</span>:</p></td>
</tr>
</table>

<table width="97%" border="0">
<tr>
  <td>
    <table width="97%">
		<tr>
			<td width="40%"><p class="label_bold"><fmt:message key="customer_sends_to_bank"/></p></td>
			<td><p class="label_bold"><fmt:message key="bank_sends_to_customer"/></p></td>
		</tr>
	</table>
  </td>
</tr>
<tr>
    <td><%@include file="services.jsp" %></td>
</tr>
</table>

<table border="0" width="97%">
    <tr><td >&nbsp;</td></tr>

	<tr>
		<td width="130px"><p class="label_bold"><fmt:message key="security_method"/><span class="asterisk">*</span>:</p></td>
		<td><p class="label_normal">
		<input type="radio" name="securityMethod" checked class="radio" value="PATU" maxlength="10">
		<fmt:message key="PATU"/></p></td>
		<td>&nbsp;</td>
		<td width="30px">&nbsp;</td>
		<td>&nbsp;</td>
	</tr>

	<tr>
	    <td>&nbsp;</td>
	    <td><p class="label_normal">
		<input type="radio" name="securityMethod" class="radio" value="AUT_RSA" maxlength="10" disabled="true" >
		<fmt:message key="AUTACK_RSA"/></p></td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	</tr>
	<tr>
	    <td>&nbsp;</td>
	    <td><p class="label_normal">
		<input type="radio" name="securityMethod" class="radio" value="PKI" maxlength="10" disabled="true" >
		<fmt:message key="PKI"/></p></td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><p class="label_normal">
		<input type="radio" name="securityMethod" class="radio" value="VPN" maxlength="10" disabled="true" >
		<fmt:message key="VPN"/></p></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><p class="label_normal">
		<input type="radio" name="securityMethod" class="radio" value="SSL_SSH" maxlength="10" disabled="true" >
		<fmt:message key="SSL_SSH"/></p></td>
		<td>&nbsp;</td>
		<td width="30px">&nbsp;</td>
		<td>&nbsp;</td>
	</tr>

   <tr>
		<td></td>
		<td></td>
  </tr>

</table>

</div>
</div>

<c:choose>
<c:when test="${sessionObj.dateFormat=='yyyy-MM-dd'}">
<c:set var="date_format" value="${'%Y-%m-%d'}"/>
<c:set var="date_type" value="1"/>
</c:when>
<c:when test="${sessionObj.dateFormat=='MM-dd-yyyy'}">
<c:set var="date_format" value="${'%m-%d-%Y'}"/>
<c:set var="date_type" value="2"/>
</c:when>
<c:otherwise>
<c:set var="date_format" value="${'%d-%m-%Y'}"/>
<c:set var="date_type" value="3"/>
</c:otherwise>
</c:choose>

<fmt:bundle basename="${language}">
<script language="javascript" type="text/javascript">
	Calendar.setup({
        inputField     :    "${valid_from}",      // id of the input field
        ifFormat       :    "<c:out value='${date_format}'/>",      // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "stdate_trigger",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    });
	
		Calendar.setup({
        inputField     :    "${valid_to}",      // id of the input field
        ifFormat       :    "<c:out value='${date_format}'/>",       // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "stdate_trigger1",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    });

	</script>
</fmt:bundle>
</html:form>
</BODY>

<script>
function DoDatesCompare1(){

		if(compareDates(document.getElementById('<c:out value="${date_of_agreement}"/>').value,'${sessionObj.dateFormat}',
						 document.getElementById('<c:out value="${valid_from}"/>').value,
						 '${sessionObj.dateFormat}')==true)
			{
					alert (agmtMesg[0]); // Valid From should be greater than or equal to Date of Agreement
					return false;       
			}
		 else
			{
				 return true;
		    }
	 }

function DoDatesCompare2(){

		if(compareDates(document.getElementById('<c:out value="${valid_from}"/>').value,'${sessionObj.dateFormat}',
						 document.getElementById('<c:out value="${valid_to}"/>').value,
						 '${sessionObj.dateFormat}')==true)
			{
					alert (agmtMesg[1]); // Valid To should be greater than or equal to Valid From
					document.getElementById('<c:out value="${valid_to}"/>').value='';
					document.getElementById('<c:out value="${valid_to}"/>').focus();
					return false;
			}
		 else
			{
				 return true;
		    }
	 }

// This function is used to compare valid from date with system date
function compareSystemDate()
 {
	     var dateType= "<c:out value='${date_type}'/>" ;
		 var d = new Date();

		 var date= d.getDate();
		 if(date<10)
	     date='0'+date;
	 
		 var month=d.getMonth()+1;
		 if(month<10)
		 month='0'+month;
		 
		 var year=d.getYear();
		 var sysDate;

		  if(dateType==1) // yyyy-MM-dd
		  {
            sysDate=year+'-'+month+'-'+date;
		  }
		  else if(dateType==2) //MM-dd-yyyy
		  {
            sysDate=month+'-'+date+'-'+year;
		  }else  // dd-MM-yyyy        
		  {
            sysDate=date+'-'+month+'-'+year;
		  }
		  
		  if(compareDates(sysDate,'${sessionObj.dateFormat}',document.getElementById('<c:out value="${valid_from}"/>').value,
						 '${sessionObj.dateFormat}')==true)
			{       
			        alert (agmtMesg[8]); // Valid From should be always greater than or equal to current date
					document.getElementById('<c:out value="${valid_from}"/>').value='';
					document.getElementById('<c:out value="${valid_from}"/>').focus();
					return false;
			}
			else
			{
				return true;
		    }

 }


function validateAgreement()
{
	var list_input = document.getElementsByTagName ('input');
	var list_select = document.getElementsByTagName ('select');
	document.getElementById('editorHold').value='NEW';
	
	
	if(document.getElementById('<c:out value="${agreement_title}"/>').value=="" || document.getElementById('<c:out value="${agreement_title}"/>').value=="Select"){
		disp_text='<c:out value="${agreement_title}"/>\n';	
	}
    
	if(document.getElementById('<c:out value="${primary_contact}"/>').value=="" || document.getElementById('<c:out value="${primary_contact}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${primary_contact}"/>\n';	
	}

	if(document.getElementById('<c:out value="${street_address}"/>').value=="" || document.getElementById('<c:out value="${street_address}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${street_address}"/>\n';
	}

	if(document.getElementById('<c:out value="${city}"/>').value=="" || document.getElementById('<c:out value="${city}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${city}"/>\n';
	}

	if(document.getElementById('<c:out value="${zip_code}"/>').value=="" || document.getElementById('<c:out value="${zip_code}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${zip_code}"/>\n';
	}

	if(document.getElementById('<c:out value="${country}"/>').value=="" || document.getElementById('<c:out value="${country}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${country}"/>\n';
	}

	if(document.getElementById('<c:out value="${telephone}"/>').value=="" || document.getElementById('<c:out value="${telephone}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${telephone}"/>\n';
	}

	if(document.getElementById('<c:out value="${email}"/>').value=="" || document.getElementById('<c:out value="${email}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${email}"/>\n';
	}

	if(document.getElementById('<c:out value="${valid_from}"/>').value=="" || document.getElementById('<c:out value="${valid_from}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${valid_from}"/>\n';
	}
    	

	if(displayMesg(2))
	{
		
		if(displayMesg(3))
		{
			
			if(displayMesg(130))
			{
				if(outputSelected())
				{
				 if(checkFlags())
					 {
					   if(compareSystemDate()== true)
					   {
						if(document.getElementById('<c:out value="${valid_to}"/>').value.length!=0)
							{
							   if(DoDatesCompare2()==true)
							   {
								   if(confirmations()){
									 eventFlag='N';
									 document.agreementnewform.submit();
								   }
							   }else
								return false;
							}else{
								if(confirmations()){
								  eventFlag='N';
								  document.agreementnewform.submit();
								}
							}					
			           }
					   else
                       return false;
					 }
			   }
			}
		}
	}
}

</script>
</fmt:bundle>

</HTML>

<fmt:bundle basename="${language}">
<script>
	doFocusOnFiled('<fmt:message key="agreement_title"/>');
</script>
</fmt:bundle>


<fmt:bundle basename="errors.AgreementErrorResources">
<script>

	function callDeleteAction()	{

		if(eventFlag=='Y') {
			//	var servicecode = document.getElementById('serviceCode').value;
			var internalAgreementId = document.getElementById('internalAgreementId').value;

			event.returnValue ='<fmt:message key="WAR-CPW-001"/>';

			clearTempTableData("", internalAgreementId);

			//	event.returnValue ='testing';
			//	window.opener.location.reload();
			//	window.close();
		}
	}

	//Ajax to check service id,bureau id combinations
	function ajaxReturnToFunctionClearTempTableData(xmlData)
	{

		var xmlDoc = xmlData.documentElement;
		var service_id = xmlDoc.getElementsByTagName("service_id")[0].childNodes[0].nodeValue;
	   
		if( service_id ==1)
		{
			//	alert("Content Deleted Successfully..!!"); 
			//	alert(svpMesg[7]); // Service Id, Bureau Id combination already exists for this Service type
			//	document.getElementById('<fmt:message key="service_id"/>').focus();
			return true;
		}else
		{	
			//	setFlags();
			//	addNewService();
			//	document.servicespecifictypenewineditform.submit();
			return false;
		}
	}



	function setService() {

			var requestServicesNames = '<c:out value="${requestScope.requestServices}"/>';

		// 	alert('I m inside setService - >' + requestServicesNames);

			var servicesArray=requestServicesNames.split(",");

			/*
					cross_border_payments - S005
					intraday_transaction_statement - S006
 					account_statement - S007
					incoming_reference_payment - S008
					preadvice_of_incoming_cross_border_payment - S009
					currency_exchange_rate - S010
					account_statement_of_account_with_other_bank - S011
			*/

			for(var i=0; i < servicesArray.length; i++) {

				if(servicesArray[i]=='S001') {

					document.getElementById('domestic_bill_payment').checked=true;
					document.getElementById('S001F').value='Y';

				} else if(servicesArray[i]=='S002') {
					
					document.getElementById('back_reporting_cross_border_payment').checked=true;
					document.getElementById('S002F').value='Y';

				} else if(servicesArray[i]=='S003') {
					
					document.getElementById('salary_payment_service').checked=true;
					document.getElementById('S003F').value='Y';

				}  else if(servicesArray[i]=='S004') {
					
					document.getElementById('intraday_real_time_balance').checked=true;
					document.getElementById('S004F').value='Y';

				} else if(servicesArray[i]=='S005') {
					
					document.getElementById('cross_border_payments').checked=true;
					document.getElementById('S005F').value='Y';

				} else if(servicesArray[i]=='S006') {
					
					document.getElementById('intraday_transaction_statement').checked=true;
					document.getElementById('S006F').value='Y';

				} else if(servicesArray[i]=='S007') {
					
					document.getElementById('account_statement').checked=true;
					document.getElementById('S007F').value='Y';

				} else if(servicesArray[i]=='S008') {
					
					document.getElementById('incoming_reference_payment').checked=true;
					document.getElementById('S008F').value='Y';

				} else if(servicesArray[i]=='S009') {
					
					document.getElementById('preadvice_of_incoming_cross_border_payment').checked=true;
					document.getElementById('S009F').value='Y';

				} else if(servicesArray[i]=='S010') {
					
					document.getElementById('currency_exchange_rate').checked=true;
					document.getElementById('S010F').value='Y';

				} else if(servicesArray[i]=='S011') {
					
					document.getElementById('account_statement_of_account_with_other_bank').checked=true;
					document.getElementById('S011F').value='Y';

				} else {
					// 
				}


			}


/*
			var ServicesNamesArray = requestServicesNames.split(",");

			for(int i =0; i <ServicesNamesArray.length; i++) {
				alert(ServicesNamesArray[i]);
			}
*/
		}

</script>
</fmt:bundle>
