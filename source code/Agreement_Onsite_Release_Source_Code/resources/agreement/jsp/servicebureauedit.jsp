<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri= "struts-bean.tld" prefix= "bean" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri= "struts-logic.tld" prefix= "logic" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="agreement.tld" prefix="ag" %>

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:requestEncoding value="UTF-8"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<fmt:setLocale value="${sessionObj.languageId}"/>

<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />

<fmt:bundle basename="${language}">

<head>

<title><fmt:message key="service_bureau"/>-<fmt:message key="edit"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<html:xhtml/>
<html:base/>

<script language="javascript" type="text/javascript" src="../js/agreement1.js"></script>
<script language="javascript" type="text/javascript" src="../js/ope_ajax.js"></script>

<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ifixpng2.js"></script>
<script type="text/javascript" src="../js/js.js"></script>
<script type="text/javascript" src="../js/dm.js"></script>

<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

<script>
<!--
function ajaxReturnToFunctionKEYS(xmlDoc)
{
	var key_KEK = xmlDoc.getElementsByTagName('key_kek');
	var key_AUK = xmlDoc.getElementsByTagName('key_auk');
	var key_KEKPart1 = xmlDoc.getElementsByTagName('key_kek_part1');
	var key_KEKPart2 = xmlDoc.getElementsByTagName('key_kek_part2');
	var key_KVV = xmlDoc.getElementsByTagName('key_kvv');
	var generation_number = xmlDoc.getElementsByTagName('generation_no');
	var keyKEK = key_KEK[0].text
	var keyAUK = key_AUK[0].text
    var keyKEKPart1 = key_KEKPart1[0].text;
	var keyKEKPart2 = key_KEKPart2[0].text;
	var keyKVV = key_KVV[0].text;
    var generationNumber=generation_number[0].text;
    
    	var KEK_GENERATION_NO = xmlDoc.getElementsByTagName('KEK_GENERATION_NO');
		
		var KVV = xmlDoc.getElementsByTagName('KVV');
		var KEK_GENERATION_NO = xmlDoc.getElementsByTagName('KEK_GENERATION_NO');
		var AUK_GENERATION_NO = xmlDoc.getElementsByTagName('AUK_GENERATION_NO');
		var KEK_GENERATION_NO1 = xmlDoc.getElementsByTagName('KEK_GENERATION_NO1');
		var GENERATION_DATE = xmlDoc.getElementsByTagName('GENERATION_DATE');
		var GENERATION_DATE1 = xmlDoc.getElementsByTagName('GENERATION_DATE1');
		var KVV1 = xmlDoc.getElementsByTagName('KVV1');
		var KEY_STATUS = xmlDoc.getElementsByTagName('KEY_STATUS');
		
		var kek_generation_no = KEK_GENERATION_NO[0].text
		var generation_date = GENERATION_DATE[0].text
		var kvv = KVV[0].text
		var auk_generation_no = AUK_GENERATION_NO[0].text
		var kek_generation_no1 = KEK_GENERATION_NO1[0].text
		var generation_date1 = GENERATION_DATE1[0].text
		var kvv1 = KVV1[0].text
		var key_status = KEY_STATUS[0].text
		
		document.getElementById('kek_generation_no').value=kek_generation_no;
		document.getElementById('generation_date').value=generation_date.substring(0,19);
		//document.getElementById('kvv').value=kvv;
		document.getElementById('auk_generation_no').value=auk_generation_no;
		document.getElementById('kek_generation_no1').value=kek_generation_no1;
		document.getElementById('generation_date1').value= generation_date1.substring(0,19);
		//document.getElementById('kvv1').value=kvv1;
		document.getElementById('key_status').value=key_status;
	   
	if(generationNumber==''){
		generationNumber='0';
	}
	
	if(generationNumber=='0'){		
		var cc = document.getElementById('btnGenerateKEK').value='<fmt:message key="re_generate_keys"/>';
		document.getElementById('status').value='R';
		//document.getElementById('btnResetKeys').disabled=false;
	}
	var keyMsg = document.getElementById('genkey').value;
	if(key_status == 'G'){
		document.getElementById('btnGenerateKEK').value='<fmt:message key="re_generate_keys"/>';
		//alert(bureauMesg[6]); //alert('Keys have been re-generated successfully');
		if(keyMsg == 'GK'){
			alert(bureauMesg[5]);
			document.getElementById('genkey').value="RK";

			document.getElementById('btnGenerateKEK').disabled=false;	
			document.getElementById('btnResetKeys').disabled=false;
			document.getElementById('btnDeleteKeys').disabled=false;
		    document.getElementById('printkeys').disabled=false;

		}else{
			alert(bureauMesg[6]); //alert('Keys have been re-generated successfully');

			document.getElementById('btnGenerateKEK').disabled=false;	
			document.getElementById('btnResetKeys').disabled=false;
			document.getElementById('btnDeleteKeys').disabled=false;
		    document.getElementById('printkeys').disabled=false;
		}
	}
	
	if(key_status =='R'){
		alert(bureauMesg[8]); //alert('Keys have been reset successfully');

			document.getElementById('btnGenerateKEK').disabled=false;	
			document.getElementById('btnResetKeys').disabled=false;
			document.getElementById('btnDeleteKeys').disabled=false;
		    document.getElementById('printkeys').disabled=false;
	}

	if(key_status =='D'){
		alert(bureauMesg[15]); //alert('Keys have been deleted successfully');

			document.getElementById('btnGenerateKEK').disabled=false;	
			document.getElementById('btnResetKeys').disabled=false;
			document.getElementById('btnDeleteKeys').disabled=false;
		    document.getElementById('printkeys').disabled=false;
	}	
		
}

//Reset key response from ajax

function ajaxReturnToPrintKEYS(xmlDoc){
	
	var key_KEK = xmlDoc.getElementsByTagName('key_kek');
	var key_AUK = xmlDoc.getElementsByTagName('key_auk');
	var key_KEKPart1 = xmlDoc.getElementsByTagName('key_kek_part1');
	var key_KEKPart2 = xmlDoc.getElementsByTagName('key_kek_part2');
	var customer_name1 = xmlDoc.getElementsByTagName('customer_name1');
    var customer_name2 = xmlDoc.getElementsByTagName('customer_name2');

	var customer_address1 = xmlDoc.getElementsByTagName('customer_address1');
    var customer_address2 = xmlDoc.getElementsByTagName('customer_address2');

    var control_code = xmlDoc.getElementsByTagName('control_code');
	var generation_no = xmlDoc.getElementsByTagName('generation_no');
	var generation_date = xmlDoc.getElementsByTagName('generation_date');
	var key_kvv = xmlDoc.getElementsByTagName('key_kvv');

	var keykekpart1 = key_KEKPart1[0].text;
	var keykekpart2 = key_KEKPart2[0].text;

	var kekgenerationno1 = generation_no[0].text;

	var generationDate = generation_date[0].text;

	var customerName1 = customer_name1[0].text;
	var customerName2 = customer_name2[0].text;

	var customerAdd1 = customer_address1[0].text;
	var customerAdd2 = customer_address2[0].text;

	var controlCode = control_code[0].text;
	var keykvv = key_kvv[0].text;

	
	
	//document.getElementById('customeName2').value=customerName2;

	document.getElementById("customerName1").innerHTML=customerName1;
	document.getElementById("customerName2").innerHTML=customerName2;

	document.getElementById("customerAdd1").innerHTML=customerAdd1;
	document.getElementById("customerAdd2").innerHTML=customerAdd2;

    document.getElementById('keykekpart1').innerHTML=keykekpart1;
	document.getElementById('keykekpart2').innerHTML=keykekpart2;
	document.getElementById('controlCode').innerHTML=controlCode;
	document.getElementById('controlCode1').innerHTML=controlCode;
	
	document.getElementById('kekgenerationno1').innerHTML=kekgenerationno1;
	document.getElementById('kekgenerationno2').innerHTML=kekgenerationno1;

	document.getElementById('generationdate1').innerHTML=generationDate.substring(0,19);
	document.getElementById('generationdate').innerHTML=generationDate.substring(0,19);
	
	//document.getElementById('keykvv1').innerHTML=keykvv;
	document.getElementById('keykvv2').innerHTML=keykvv;

	document.getElementById('spanCountry1').className='dropDownHide';
	document.getElementById('spanCountry2').className='dropDownHide';
	showDialog();
	enableButtons('all','','a3,tdnext,tdpre,btnGenerateKEK,btnResetKeys,btnDeleteKeys,printkeys');

  }

function ajaxReturnToDisplayKEYS(xmlDoc){

        var KEK_GENERATION_NO = xmlDoc.getElementsByTagName('KEK_GENERATION_NO');
		var GENERATION_DATE = xmlDoc.getElementsByTagName('GENERATION_DATE');
		var KVV = xmlDoc.getElementsByTagName('KVV');
		var AUK_GENERATION_NO = xmlDoc.getElementsByTagName('AUK_GENERATION_NO');
		var KEK_GENERATION_NO1 = xmlDoc.getElementsByTagName('KEK_GENERATION_NO1');
		var GENERATION_DATE1 = xmlDoc.getElementsByTagName('GENERATION_DATE1');
		var KVV1 = xmlDoc.getElementsByTagName('KVV1');
		var KEY_STATUS = xmlDoc.getElementsByTagName('KEY_STATUS');
		
		var kek_generation_no = KEK_GENERATION_NO[0].text
		var generation_date = GENERATION_DATE[0].text
		var kvv = KVV[0].text
		var auk_generation_no = AUK_GENERATION_NO[0].text
		var kek_generation_no1 = KEK_GENERATION_NO1[0].text
		var generation_date1 = GENERATION_DATE1[0].text
		var kvv1 = KVV1[0].text
		var key_status = KEY_STATUS[0].text
		
		document.getElementById('kek_generation_no').value=kek_generation_no;
		document.getElementById('generation_date').value=generation_date.substring(0,19);
		//document.getElementById('kvv').value=kvv;
		document.getElementById('auk_generation_no').value=auk_generation_no;
		document.getElementById('kek_generation_no1').value=kek_generation_no1;
		document.getElementById('generation_date1').value=generation_date1.substring(0,19);
		//document.getElementById('kvv1').value=kvv1;
		document.getElementById('key_status').value=key_status;

		document.getElementById('btnResetKeys').disabled=true;
		document.getElementById('btnDeleteKeys').disabled=true;
		document.getElementById('printkeys').disabled=true;
		
}


function ajaxReturnToFunctionResetKEYS(xmlDoc){

	    var generation_number = xmlDoc.getElementsByTagName('generation_no');
    	var generationNumber = generation_number[0].text;
	
		var KEK_GENERATION_NO = xmlDoc.getElementsByTagName('KEK_GENERATION_NO');
		var GENERATION_DATE = xmlDoc.getElementsByTagName('GENERATION_DATE');
		var KVV = xmlDoc.getElementsByTagName('KVV');
		var AUK_GENERATION_NO = xmlDoc.getElementsByTagName('AUK_GENERATION_NO');
		var KEK_GENERATION_NO1 = xmlDoc.getElementsByTagName('KEK_GENERATION_NO1');
		var GENERATION_DATE1 = xmlDoc.getElementsByTagName('GENERATION_DATE1');
		var KVV1 = xmlDoc.getElementsByTagName('KVV1');
		var KEY_STATUS = xmlDoc.getElementsByTagName('KEY_STATUS');
		
		var kek_generation_no = KEK_GENERATION_NO[0].text
		var generation_date = GENERATION_DATE[0].text
		var kvv = KVV[0].text
		var auk_generation_no = AUK_GENERATION_NO[0].text
		var kek_generation_no1 = KEK_GENERATION_NO1[0].text
		var generation_date1 = GENERATION_DATE1[0].text
		var kvv1 = KVV1[0].text
		var key_status = KEY_STATUS[0].text
		
		document.getElementById('kek_generation_no').value=kek_generation_no;
		document.getElementById('generation_date').value=generation_date.substring(0,19);
		//document.getElementById('kvv').value=kvv;
		document.getElementById('auk_generation_no').value=auk_generation_no;
		document.getElementById('kek_generation_no1').value=kek_generation_no1;
		document.getElementById('generation_date1').value=generation_date1.substring(0,19);
		//document.getElementById('kvv1').value=kvv1;
		document.getElementById('key_status').value=key_status;

	if(generationNumber != ''){
		//document.getElementById('btnGenerateKEK').value='<fmt:message key="generate_keys"/>';
		document.getElementById('status').value='R';
		//document.getElementById('btnResetKeys').disabled=true;
		alert(bureauMesg[8]); //alert('Keys have been reset successfully');

			document.getElementById('btnGenerateKEK').disabled=false;	
			document.getElementById('btnResetKeys').disabled=false;
			document.getElementById('btnDeleteKeys').disabled=false;
		    document.getElementById('printkeys').disabled=false;
		
	} else if(generationNumber == ''){
		alert(bureauMesg[15]); //alert('Could not reset the keys. Please try again.');
			document.getElementById('btnGenerateKEK').disabled=false;	
			document.getElementById('btnResetKeys').disabled=false;
			document.getElementById('btnDeleteKeys').disabled=false;
		    document.getElementById('printkeys').disabled=false;
	}
}

function generateKeys(id,editorHold,path){

	if(document.getElementById('status').value == 'G'){

		if(confirm(bureauMesg[10])==true){  //confirm('Do you wish to generate the keys?')
			
			document.getElementById('btnGenerateKEK').disabled=true;	
			document.getElementById('btnResetKeys').disabled=true;
			document.getElementById('btnDeleteKeys').disabled=true;
		    document.getElementById('printkeys').disabled=true;

			getAjaxValueForKEYS(id,'0',editorHold,path,'G');
			return true;
		}else {
			return false;
		}
	} else if(document.getElementById('status').value == 'R'){

		if(confirm(bureauMesg[11])==true){ //confirm('Do you wish to re-generate the keys?')

			document.getElementById('btnGenerateKEK').disabled=true;	
			document.getElementById('btnResetKeys').disabled=true;
			document.getElementById('btnDeleteKeys').disabled=true;
		    document.getElementById('printkeys').disabled=true;

			getAjaxValueForKEYS(id,'0',editorHold,path,'G');
			return true;
		}else {
			return false;
		}
	}
}

function resetKeys(id,editorHold,path,action){


	if(document.getElementById('status').value == 'R'){

		if(confirm(bureauMesg[12])==true){  //confirm('Do you wish to reset the keys?')==true)

			document.getElementById('btnGenerateKEK').disabled=true;	
			document.getElementById('btnResetKeys').disabled=true;
			document.getElementById('btnDeleteKeys').disabled=true;
		    document.getElementById('printkeys').disabled=true;

		     getAjaxValueToResetKEYS(id,'0',editorHold,path,'R');
			return true;
		}else {
			return false;
		}
	} 
}

function deleteKeys(id,editorHold,path,action){

//	if(document.getElementById('status').value == 'D'){
 		if(confirm(bureauMesg[14])==true){  //confirm('Do you wish to Delete the keys?')==true)

			document.getElementById('btnGenerateKEK').disabled=true;	
			document.getElementById('btnResetKeys').disabled=true;
			document.getElementById('btnDeleteKeys').disabled=true;
		    document.getElementById('printkeys').disabled=true;

			getAjaxValueToDeleteKEYS(id,editorHold,path,'D');
			return true;
		}else {
			return false;
		}
//	} 
}



function displayKeys(id, path, editorHold){
	if(id != ''){
			getAjaxValueToDisplayKeys(id, path, editorHold);
			return true;
	}else {
			return false;
	}
}

function editWindow(){
    //alert("EDIT WINDOW");

	document.getElementById('btnResetKeys').disabled=true;
	document.getElementById('btnDeleteKeys').disabled=true;
	document.getElementById('printkeys').disabled=true;

}
//-->internalBureauId


function enableDisableGenerationKeys(){

	if(document.getElementById('status').value=='R'){
			
		return enableButtons('all','','a3,tdnext,tdpre');

	}else{
		return enableButtons('all','','a3,tdnext,tdpre,btnResetKeys,btnDeleteKeys,printkeys');
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
	if(name.match(/^[a-zA-Z0-9]+$/)) {
	return true;
	} else {
	alert(agmtMesg[9]); // Special characters are not allowed
	document.getElementById(obj.id).value='';
	document.getElementById(obj.id).focus();
	return false;
	}
	}
}

function checkSpecialCharWithSpace(obj) 
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

function validateForm(form){

		// store prev values
		populateArray(form, prevVals);
			
		var result = compareArray(currVals, prevVals);

		if(result == true){	
            // Do wish to save ?					
			if(confirm(agmtMesg[10])==true){			
				return true;
			}else{
				return false;
			}
			
		}else{
            // Do wish to save ?
			if(confirm(agmtMesg[10])==true){			
				return true;
			}else{
				return false;
			}			
		}
	}//validateForm Ends

	function saveBureauAfterEnterKey()
	{
		var key;
		if (window.event)
		key = window.event.keyCode;
		var editorHold = document.getElementById('editorHold');
		if(key==13)
		{
			validateAction('ServiceBureauEdit',editorHold,'EDIT');
		}

	}

	function editBureauAfterEnterKey()
	{
		var key;
		if (window.event)
		key = window.event.keyCode;
		if(key==13)
		{
			hover('tdpre','left-arrow');
			hover('a3','nohand');
			changePreviousIcon();
			enableDisableGenerationKeys();
		}
		
	}

</script>

<style type="text/css" media="screen">
	.hideonscreen {display: none;}
</style>

<style type="text/css" media="print">
	.hideonprint {display: none;}
</style>

<style type="text/css" media="screen">
	#hideonscreen_id {display: none;}
</style>

<style type="text/css" media="print">
	#hideonprint_id {display: none;}
</style>

<style>
.dropDownHide { visibility:hidden;display:block;}
.dropDownShow { visibility:show;display:block;}

.winHide { visibility:hidden;display:none;}

.winShow { visibility:show;display:block;}

.widgets-area .col1 {
	float:center;
	width:46%;
	position:absolute;
	left:49%;
	top:25%;
	
}

.widgets-area .col2 {
	float:center;
	width:46%;
	margin-left:1%;
	position:absolute;
	
	top:25%;
	
}

.widgets-area .col1-pad, .widgets-area .col2-pad {
	padding:14px 7px 14px 7px;
	
}

* html .widgets-area .col1-pad, * html .widgets-area .col2-pad {
	
}

.closepwpatu {
	display:block;
	overflow:hidden;
	width:14px;
	height:14px;
	background:url(../images/but-tab-close.gif) 0 -14px no-repeat;
	text-indent:0;
	left:278px;
	top:6px;
	position:absolute;
	z-index:100;
	cursor:pointer;
	font-size:0;
}
</style>

<script>
var part1;
var part2;
function showDialog(){
try{
   document.getElementById('maindialog').className='winShow';
   document.getElementById('maindialog1').className='winShow';
   part1="0";
   part2="0";
   
  }catch(ex){
	alert(ex);
 }
}

function hideDialogPart1(){
 try{
	part1="1";
    document.getElementById('maindialog1').className='winHide';
	document.getElementById('spanCountry1').className='dropDownShow';
	document.getElementById('spanCountry2').className='dropDownShow';
	//enableButtons('all','','a3,tdnext,tdpre');
	checkCloseStatus();
  }catch(ex){
	alert(ex);
 }
}

function hideDialogPart2(){
 try{
   // document.getElementById('maindialog1').className='winHide';
    part2="1";
    document.getElementById('maindialog').className='winHide';
	//document.getElementById('spanCountry1').className='dropDownShow';
	//document.getElementById('spanCountry2').className='dropDownShow';
	//enableButtons('all','','a3,tdnext,tdpre');
	checkCloseStatus();
 }catch(ex){
	alert(ex);
 }
}

function checkCloseStatus(){
  if(part1=="1" && part2=="1"){
    enableButtons('all','','a3,tdnext,tdpre');
  }
}

</script>


<script type="text/javascript">
function ClickHereToPrintKekPart2(){
    try{ 
	   var agree=confirm('<fmt:message key="confirm_msg"/>');
	   if (agree)
	   {
        var oIframe = document.getElementById('ifrmPrintPart2');
        var oContent = document.getElementById('kekdialog2').innerHTML;
        var oDoc = (oIframe.contentWindow || oIframe.contentDocument);
        if (oDoc.document) oDoc = oDoc.document;
		oDoc.write("<html><head><title></title>");
		oDoc.write('<style type="text/css" media="screen">.hideonscreen {display: none;}</style>');
        oDoc.write('<style type="text/css" media="print"> .hideonprint {display: none;}</style>');
		oDoc.write("</head><body onload='this.focus(); this.print();'>");
		oDoc.write(oContent + "</body></html>");	    
		oDoc.close(); 	
		}else{
        return false;
	  }
    }
    catch(e){
	    self.print();
    }
}

function ClickHereToPrintKekPart1(){
    try{ 
	   var agree=confirm('<fmt:message key="confirm_msg"/>');
	   if (agree)
	   {
		var oIframe = document.getElementById('ifrmPrintPart1');
        var oContent = document.getElementById('kekdialog1').innerHTML;
        var oDoc = (oIframe.contentWindow || oIframe.contentDocument);
        if (oDoc.document) oDoc = oDoc.document;
		oDoc.write("<html><head><title></title>");
		oDoc.write('<style type="text/css" media="screen">.hideonscreen {display: none;}</style>');
        oDoc.write('<style type="text/css" media="print"> .hideonprint {display: none;}</style>');
		oDoc.write("</head><body onload='this.focus(); this.print();'>");
		oDoc.write(oContent + "</body></html>");	    
		oDoc.close(); 	    
	    }else{
        return false;
	  }
    }
    catch(e){
	    self.print();
    }
}

function onloadWindowSize(){
 var width = document.documentElement.clientWidth;
 if(width >= 1000){
	document.getElementById('a3').disabled=true;
    document.getElementById('tdnext').disabled=true;
    document.getElementById('tdnext').disabled=true;
    document.getElementById('printkeys').disabled=true;
  }
}

</script>

<SCRIPT TYPE="text/javascript">
function disableselect(e){
return false
} 
function reEnable(){
return true
} 
//if IE4+
document.onselectstart=new Function ("return false") 
//if NS6

if (window.sidebar){
document.onmousedown=disableselect
document.onclick=reEnable
}

// -->
</SCRIPT>


<SCRIPT TYPE="text/javascript"> 
<!-- 
//Disable right click script 
//visit http://www.rainbow.arch.scriptmania.com/scripts/ 
var message="Sorry, right-click has been disabled"; 
/////////////////////////////////// 
function clickIE() {if (document.all) {(message);return false;}} 
function clickNS(e) {if 
(document.layers||(document.getElementById&&!document.all)) { 
if (e.which==2||e.which==3) {(message);return true;}}} 
if (document.layers) 
{document.captureEvents(Event.MOUSEDOWN);document.onmousedown=clickNS;} 
else{document.onmouseup=clickNS;document.oncontextmenu=clickIE;} 
document.oncontextmenu=new Function("return false") 
// --> 
</SCRIPT>



<style type="text/css" media="screen">
.noprint {
display:none;
}
</style>


</head>
</fmt:bundle>
<body onload="onloadWindowSize();" onKeyDown="return disableCtrlKeyCombination(event);" class="shadowbody" >
<%@ include file="common/JavascriptErrorMsg.jsp" %>
<!-- Tool Bar -->

<c:choose>
<c:when test="${not empty requestScope.bureauDetails}">
<!-- Begin Edit Deaprtment -->

<c:forEach items='${bureauDetails}' var='lst'>  

<html:form action="/resources/agreement/jsp/ServiceBureauEdit" >

<input type="hidden" name="findNewOrEditHold" value="EH">
<input type="hidden" id="key_status" name="key_status" value="<c:out value="${key_status}"/>">
<html:hidden property= "editorHold"  styleId="editorHold"/>

<c:url value="/resources/agreement/jsp/generateKeys.do" var="generateKeys"/>
<c:url value="/resources/agreement/jsp/ResetKeys.do" var="resetKeyPath"/>
<c:url value="/resources/agreement/jsp/printkeys.do" var="printkeys"/>

<fmt:bundle basename="${language}">

<c:forEach var="lstRights" items="${sessionObj.roleList}">
	<c:if test="${lstRights.entityCode eq 'SVRB'}">		
		<c:set var="rights" value="${lstRights.rightNo}"/>
	</c:if>
 </c:forEach>
 <c:set var="NEW" value="${fn:substring(rights, 0, 1)}" />
  <ag:dropDown var="banks" scope="page" key="ACTIVE_BANKNAMES" />
	 <c:forEach items="${banks}" var="bank">
	 <c:set var="bankId" value="${bank.key}" />
	 <c:set var="bankName" value="${bank.value}" />
 </c:forEach>	
<div class="popup" ><span class="popup_shadow">
	<div class="popup_title" ><span class="popup_text"><fmt:message key="service_bureau"/> - <fmt:message key="edit"/></span></span></div></span>
</div>

  <ul class="toolbar2">
    <!-- Subnavigation -->		
   
  <li class="save">
	 <a onClick="return validateAction('ServiceBureauEdit',editorHold,'EDIT');" disabled onMouseOver="hover('a1','ahand');" 
        onMouseOut="hover('a1','nohand');" tabindex="1" onkeyPress="saveBureauAfterEnterKey();" id="a1" title="<fmt:message key="save_and_close"/>">
	<fmt:message key="save_and_close"/></a>
 </li>
 
 <c:forEach var="lstRights" items="${sessionObj.roleList}">
	<c:if test="${lstRights.entityCode eq 'SVRB'}">		
		<c:set var="rights" value="${lstRights.rightNo}"/>
	</c:if>
 </c:forEach>

<c:set var="EDIT" value="${fn:substring(rights, 1, 2)}" />
 <li class="edit">
   <c:choose>
        <c:when test="${EDIT=='1'}">
		   <c:choose>
             <c:when test="${lst.versionPos=='N'}">
				<a disabled="true" title="<fmt:message key="edit"/>"><fmt:message key="edit"/></a>				
		     </c:when>
             
			 <c:otherwise>				 
				  <c:choose>
					<c:when test="${lst.status=='D'}">				 
						<a disabled="true" title="<fmt:message key="edit"/>"><fmt:message key="edit"/></a>						
				   </c:when>
				<c:otherwise>				 				
			    <a onClick="hover('tdpre','left-arrow');hover('a3','nohand');changePreviousIcon(); return enableDisableGenerationKeys();" tabindex="2" onkeypress="editBureauAfterEnterKey();"
			      	onMouseOver="hover('a3','ahand');" 
			       	onMouseOut="hover('a3','nohand');" id="a3" title="<fmt:message key="edit"/>"><fmt:message key="edit"/></a>
				</c:otherwise>
				</c:choose>
		     </c:otherwise>
	       </c:choose>
     </c:when>
	  <c:otherwise>

			<c:choose>
             <c:when test="${lst.versionPos=='N'}">
		        <a disabled="true" title="<fmt:message key="edit"/>"><fmt:message key="edit"/></a>
		     </c:when>
             
			 <c:otherwise>
			        <a onClick="hover('tdpre','left-arrow');hover('a3','nohand');changePreviousIcon(); return enableButtons('all','','a3,tdnext,tdpre');"
				  onMouseOver="hover('a3','ahand');" tabindex="2" onkeypress="editBureauAfterEnterKey();"
		          onMouseOut="hover('a3','nohand');" id="a3" title="<fmt:message key="edit"/>" disabled><fmt:message key="edit"/></a>
		     </c:otherwise>
	       </c:choose>
		 
	  </c:otherwise>  
   </c:choose>
  </li>  
  </ul>

</fmt:bundle>

<div id="bureau_edit_scrollbar" >

<%@ include file="common/ErrorHandler.jsp" %>

<div class="record-def"<fmt:bundle basename="${language}">
<div class="noprint">
<table border="0" width="96%" align="center" cellpadding="0" cellspacing="0">
	<tr><td>
		<table align="left" border="0" cellpadding="3" cellspacing="2" width="100%">

			<tr>
				<c:set var="service_bureau_id"><fmt:message key="service_bureau_id"/></c:set>
				<td><p class="label_normal"><fmt:message key="service_bureau_id"/>:</p></td> 
				<td><span class="asterisk">*</span><html:text  property="bureauId" styleId="${service_bureau_id}*" maxlength="17" readonly="true" size="22"
				disabled="true" name="lst"/></td>
				<td>&nbsp;</td>
			</tr>

			<html:hidden property= "versionNo" name="lst"/>
			<html:hidden property="internalBureauId" name="lst" styleId="internalBureauId"/>
			<c:set var="genkey" value="KK"/>			
			<c:set var="status" value="R"/>
			<c:set var="btnName"><fmt:message key="generate_keys"/></c:set>
			<c:set var="exist" value="0" />			
			
			<ag:dropDown var="recordList" scope="page" key="KEY_STATUS"/>    			
			<c:forEach items="${recordList}" var="record" varStatus="countRecord">			
				<c:choose>
				<c:when test="${record.key eq lst.internalBureauId}">					
		 			<c:set var="exist" value="1" />
				</c:when>
				<c:otherwise>
				<c:set var="status" value="G"/>					
				</c:otherwise>
			</c:choose>	
						
			
			</c:forEach>

			<c:if test="${exist eq '0'}">
				<c:set var="status" value="G"/>
				<c:set var="btnName"><fmt:message key="generate_keys"/></c:set>		
				<c:set var="genkey" value="GK"/>
			</c:if>

			<c:if test="${exist eq '1'}">
				<c:set var="status" value="R"/>
				<c:set var="btnName"><fmt:message key="re_generate_keys"/></c:set>	
				<c:set var="genkey" value="RK"/>
			</c:if>
			
			<input type="hidden" value="${status}" id="status">					
			<input type="hidden" value="${genkey}" id="genkey">		
			<tr>
				<td><p class="login_userpass">
				<fmt:message key="service_bureau_name"/>:</p></td>

				<c:set var="service_bureau_name"><fmt:message key="service_bureau_name"/></c:set>

			<td><span class="asterisk">*</span><html:text property= "bureauName" maxlength="40" styleClass="login_userpass" styleId="${service_bureau_name}*" name="lst" disabled="true" 
			     onblur="javascript: this.value=Trim(this.value);checkSpecialCharWithSpace(this);" 
				 onkeypress="return alphanumericWithSpace(this);"/>
				</td>
			</tr>

			<tr>
	  <c:set var="patu_user_id"><fmt:message key="patu_user_id"/></c:set>
	  <td><p class="label_normal"><fmt:message key="patu_user_id"/>:</p></td> 
	  <td><span class="asterisk">*</span><html:text  property="companyName" styleId="${patu_user_id}*" maxlength="9" readonly="true" name="lst" disabled="true"/>
	  </td>
  	  <td>&nbsp;</td>
	</tr>

	<tr>
	  <c:set var="patu_id"><fmt:message key="patu_id"/></c:set>
	  <td><p class="label_normal"><fmt:message key="patu_id"/>:</p></td> 
	  <td colspan="3"><span class="asterisk">*</span><html:text  property="patuId" styleId="${patu_id}*" maxlength="17" size="24" readonly="true" name="lst" disabled="true" />
	  </td>
	</tr>
			
	<tr>		
		<td><p class="label_normal"><fmt:message key="service_bureau_description"/>:</p></td> 
		<td colspan="3"><span class="asterisk">&nbsp;&nbsp;</span><html:textarea property="bureauDescription" cols="39" rows="6" styleClass="text_area" name="lst" disabled="true"
					 onkeypress="return checkMaxLength(this,event,255);"
					 onkeydown="textCounter(ServiceBureauEdit.bureauDescription,255);" 
					 onkeyup="textCounter(ServiceBureauEdit.bureauDescription,255);"
					 onblur="textCounter(ServiceBureauEdit.bureauDescription,255);javascript: this.value=Trim(this.value);">
					 </html:textarea>
		</td>
	</tr>
		
	<tr>
		
		<c:set var="patu_contact_person_1"><fmt:message key="patu_contact_person_1"/></c:set>
		<td><p class="label_normal"><fmt:message key="patu_contact_person_1"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="contactPerson1" onblur="javascript: this.value=Trim(this.value);" styleId="${patu_contact_person_1}*" maxlength="32" name="lst" disabled="true"/></td>

		<c:set var="address_1"><fmt:message key="address_1"/></c:set>
		<td width="22%"><p class="label_normal"><fmt:message key="address_1"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="addressLine1" onblur="javascript: this.value=Trim(this.value);" styleId="${address_1}*" maxlength="255" name="lst" disabled="true"/></td>				
	</tr>

	<tr>
	    <c:set var="city_1"><fmt:message key="city_1"/></c:set>
		<td><p class="label_normal"><fmt:message key="city_1"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="city1" onblur="javascript: this.value=Trim(this.value);" styleId="${city_1}*" maxlength="32" name="lst" disabled="true"/></td>

			<c:set var="zip_code_1"><fmt:message key="zip_code_1"/></c:set>
		<td><p class="label_normal"><fmt:message key="zip_code_1"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="pinCode1" styleId="${zip_code_1}*" size="10" maxlength="8" name="lst" disabled="true" onkeypress="return alphanumericWithSpace(this);" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForZipCode(this);"/></td>
    </tr>

	<tr>

	<c:set var="country_1"><fmt:message key="country_1"/></c:set>
	<td><p class="label_normal"><fmt:message key="country_1"/>:</p></td>

	<ag:dropDown var="countries1" scope="page" key="COUNTRY" />
	<td  id="spanCountry1"><span class="asterisk">*</span><html:select property="country1" styleId="${country_1}*" styleClass="select" disabled="true">
				<html:option value="" ><fmt:message key="Select"/></html:option>
				<c:forEach items="${countries1}" var="country1">
				<option value ="<c:out value="${country1.key}" />" 
				<c:if test="${lst.country1==country1.key}">selected</c:if>>
				  <c:out value="${country1.value}"/>
				</option>
				</c:forEach>
	</html:select>
	</td>	

	<c:set var="telephone_1"><fmt:message key="telephone_1"/></c:set>
	    <td><p class="label_normal"><fmt:message key="telephone_1"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="telephoneNo1" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForTelephone(this);" styleId="${telephone_1}*" maxlength="13" name="lst" disabled="true" onkeypress="return chkTelFax(this);"/></td>
	</tr>


	<tr>
		
		<c:set var="patu_contact_person_2"><fmt:message key="patu_contact_person_2"/></c:set>
		<td><p class="label_normal"><fmt:message key="patu_contact_person_2"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="contactPerson2" onblur="javascript: this.value=Trim(this.value);" styleId="${patu_contact_person_2}*"  maxlength="32" name="lst" disabled="true"/></td>
		
		<c:set var="address_2"><fmt:message key="address_2"/></c:set>
		<td><p class="label_normal"><fmt:message key="address_2"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="addressLine2" onblur="javascript: this.value=Trim(this.value);" styleId="${address_2}*" maxlength="255" name="lst" disabled="true"/></td>
	</tr>

	<tr>
	
		<c:set var="city_2"><fmt:message key="city_2"/></c:set>
		<td><p class="label_normal"><fmt:message key="city_2"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="city2" onblur="javascript: this.value=Trim(this.value);" styleId="${city_2}*" maxlength="32" name="lst" disabled="true"/></td>
	
		<c:set var="zip_code_2"><fmt:message key="zip_code_2"/></c:set>
		<td><p class="label_normal"><fmt:message key="zip_code_2"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="pinCode2" styleId="${zip_code_2}*" size="10" maxlength="8" name="lst" disabled="true" onkeypress="return alphanumericWithSpace(this);" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForZipCode(this);"/></td>		
	</tr>

	<tr>		
		
		<c:set var="country_2"><fmt:message key="country_2"/></c:set>
		<td><p class="label_normal"><fmt:message key="country_2"/>:</p></td>
		<ag:dropDown var="countries2" scope="page" key="COUNTRY" />

		<td id="spanCountry2"><span class="asterisk">*</span><html:select property="country2" styleId="${country_2}*" styleClass="select" disabled="true" >
					<html:option value="" ><fmt:message key="Select"/></html:option>
					<c:forEach items="${countries2}" var="country2">
					<option  
					value ="<c:out value="${country2.key}"/>" 
					<c:if test="${lst.country2==country2.key}">selected</c:if>>
					<c:out value="${country2.value}"/>
					</option>
					</c:forEach>
		</html:select>
		</td>

		<c:set var="telephone_2"><fmt:message key="telephone_2"/></c:set>
	    <td><p class="label_normal"><fmt:message key="telephone_2"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="telephoneNo2" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForTelephone(this);" styleId="${telephone_2}*" maxlength="13" name="lst" disabled="true" onkeypress="return chkTelFax(this);"/></td>

	
	</tr>	   
	</table>
	<table width="100%">
    <tr><td colspan="4">&nbsp;</td></tr>
	<tr><td colspan="4"><p class="label_bold"><fmt:message key="patu_key_info"/></p></td></tr>
    <tr><td colspan="4">&nbsp;</td></tr>
	
	<tr>
	    <td colspan="2"><p class="label_bold"><fmt:message key="active_keys"/></p></td>
	    <td colspan="2"><p class="label_bold"><fmt:message key="inactive_keys"/></p></td> 
	</tr>

	<tr>
		<td ><p class="label_normal"><fmt:message key="kek_generation_no"/>:</p></td>	
		<td ><input type="text" id="kek_generation_no" name="kek_generation_no" disabled="true" size="5" readonly="true" value="<c:out value="${kek_generation_no}"/>"/></td>	
		<td><p class="label_normal"><fmt:message key="kek_generation_no"/>:</p></td>	
		<td ><input type="text" id="kek_generation_no1" name="kek_generation_no1" disabled="true" size="5" readonly="true" value="<c:out value="${kek_generation_no1}"/>"/></td>	
	</tr>

	<tr>
		<td ><p class="label_normal"><fmt:message key="kek_generation_time_stamp"/>:</p></td>	
		<td ><input type="text" id="generation_date" name="generation_date" disabled="true" readonly="true" value="<c:out value="${generation_date}"/>"/></td>	
		<td ><p class="label_normal"><fmt:message key="kek_generation_time_stamp"/>:</p></td>	
		<td ><input type="text" id="generation_date1" name="generation_date1" disabled="true" readonly="true" value="<c:out value="${generation_date1}"/>"/></td>	
	</tr>
	<tr>
		<td ><p class="label_normal"><fmt:message key="auk_generation_no"/>:</p></td>	
		<td><input type="text" id="auk_generation_no" name="auk_generation_no" disabled="true" size="5" readonly="true" value="<c:out value="${auk_generation_no}"/>"/></td>	
		<td>&nbsp;</td>	
		<td >&nbsp;</td>	
	</tr>

    <tr>
	<td colspan="4">
	  <c:choose>
		  <c:when test="${lst.versionPos eq 'N' || lst.status eq 'D'}" >
		  <input type="button" class="button" id="btnGenerateKEK" disabled onclick="generateKeys('${lst.internalBureauId}','GENERATE_KEYS','${generateKeys}');" value="${btnName}" title='${btnName}' onmouseover="displayToolTip();" >
		  </c:when>

		  <c:otherwise>
			<input type="button" class="button" id="btnGenerateKEK" 
			disabled onclick="generateKeys('${lst.internalBureauId}','GENERATE_KEYS','${generateKeys}');" value="${btnName}" title='${btnName}' onmouseover="displayToolTip();">
		  </c:otherwise>
	  </c:choose>

	  <c:choose>
		  <c:when test="${lst.versionPos eq 'N' || lst.status eq 'D'}" >
		  <input type="button" class="button" id="btnResetKeys"  disabled onclick="resetKeys('${lst.internalBureauId}','RESET_KEYS','${generateKeys}','R');" value="<fmt:message key="reset_auk"/>" title='<fmt:message key="reset_auk"/>'>
		  </c:when>
		  <c:otherwise>
			<input type="button" class="button" id="btnResetKeys" <c:if test="${exist eq '0'}"> disabled </c:if> onclick="resetKeys('${lst.internalBureauId}','RESET_KEYS','${generateKeys}','R');" value="<fmt:message key="reset_auk"/>" title='<fmt:message key="reset_auk"/>'>
		  </c:otherwise>
	  </c:choose>

	  <c:choose>
		  <c:when test="${lst.versionPos eq 'N' || lst.status eq 'D'}" >
		  <input type="button" class="button" id="btnDeleteKeys"  disabled onclick="deleteKeys('${lst.internalBureauId}','DELETE_KEYS','${generateKeys}','D');" value="<fmt:message key="delete_keys"/>" title='<fmt:message key="delete_keys"/>'>
		  </c:when>

		  <c:otherwise>
			<input type="button" class="button" id="btnDeleteKeys" <c:if test="${exist eq '0'}"> disabled </c:if> onclick="deleteKeys('${lst.internalBureauId}','DELETE_KEYS','${generateKeys}','D');" value="<fmt:message key="delete_keys"/>" title='<fmt:message key="delete_keys"/>'>
		  </c:otherwise>
	  </c:choose>

	  <c:choose>
		  <c:when test="${lst.versionPos eq 'N' || lst.status eq 'D'}" >
		  <input type="button" class="button" onclick="printKeys('${lst.internalBureauId}','KEY_PRINT','${printkeys}','${lst.addressLine1}','${lst.addressLine2}'); " id="printkeys" disabled value="<fmt:message key="print_patu_keys"/>" title='<fmt:message key="print_patu_keys"/>'>
		  </c:when>
		  <c:otherwise>
			<input type="button" class="button" onclick="printKeys('${lst.internalBureauId}','KEY_PRINT','${printkeys}','${lst.addressLine1}','${lst.addressLine2}');" id="printkeys" <c:if test="${exist eq '0'}"> disabled </c:if>value="<fmt:message key="print_patu_keys"/>" title='<fmt:message key="print_patu_keys"/>'>
		  </c:otherwise>
	  </c:choose>
 
	</td>
	</tr>
	</table>
	</td></tr>
	</table>
</div>
</div>

<!-- KEK KEY PART 2  print -->
<div class="winHide" id="maindialog">
   <div class="widgets-area-pad">
    <div class="widgets-area" >
	<iframe id='ifrmPrintPart2' src='#' style="width:0px; height:0px;"></iframe>
      <div class="col1" >
        <div class="col1-pad">
		  <div class="widget" id="kekdialog2">
  <div class="popup"><span class="popup_shadow">
   <div class="popup_title" ><span class="popup_text hideonprint" ><fmt:message key="kek_part_2"/></span>
   <strong><span id="hideonscreen_id" class="popup_text"><fmt:message key="bank_address_1"/></span></strong>
   <BR><strong><span id="hideonscreen_id" class="popup_text"><fmt:message key="bank_address_2"/></span></strong>
   <BR><span id="hideonscreen_id" class="popup_text"><fmt:message key="bank_address_3"/></span>
   </span></a></div></span></div>
    <ul class="toolbar2" id="kekdialog21">
     <li class="print hideonprint">
	  <a onClick="ClickHereToPrintKekPart2();" class="ahand" 
 onMouseOver="hover('a3','ahover2');" 
	onMouseOut="hover('a3','ahover1');" id="a3" title="<fmt:message key="print"/>"><fmt:message key="print"/></a>
 </li>
 <li class="closepwpatu hideonprint"><a onClick="javasrcipt: hideDialogPart2();" class="ahand"
 onMouseOver="hover('a3','ahover2');" 
	onMouseOut="hover('a3','ahover1');" id="a3" title="<fmt:message key="close"/>"><fmt:message key="close"/></a></li>
 </ul>
            <div class="block-content">
              <div class="form-item">
                <table cellpadding="4" cellspacing="4" width="100%" >

				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><strong><span id="hideonscreen_id"><fmt:message key="kek_part_2"/></span></strong></p></td>
				</tr>

				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="patu_contact_person_2"/>: <span id="customerName2"></span>,         <span id="customerAdd2"></span></p></td>
				</tr>

				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="date"/>: <span id="generationdate1"></span></p></td>
				</tr>
				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="control_code"/>: <span id="controlCode"></span></p></td>
				</tr>
				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="bank_name_or_identification"/>: <c:out value="${bankName}"/></p></td>
				</tr>

				<tr class="hideonscreen">
					<td  valign="top"><p class="label_normal"><fmt:message key="bank_id_or_code"/>: <c:out value="${bankId}"/></p></td>
				</tr>

				<tr class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="bank_id_or_code"/>: XXX</p></td>
				</tr>

				<tr class="hideonscreen">
					<td  valign="top"><p class="label_normal"><fmt:message key="kek_generation_no"/>: <span id="kekgenerationno2"></span></p></td>
				</tr>
				
				<tr class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="kek_generation_no"/>: X</p></td>
				</tr>
				
				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen"><p class="label_normal"><fmt:message key="part_2_of_kek"/>: <strong><span id="keykekpart2"></span></strong></p></td>
				</tr>

				<tr id="noprint" class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="part_2_of_kek"/>: XXXXXXXXXXXXXXXX</p></td>
				</tr>
				
					<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen"><p class="label_normal"><fmt:message key="KVV"/>: <strong><span id="keykvv2"></span></strong></p></td>
				</tr>

				<tr id="noprint" class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="KVV"/>: XXXXXX</p></td>
				</tr>

				<!-- to provide space between message and keys in print page, added dummy rows-->

				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><span id="hideonscreen_id"></span></p></td>
				</tr>

				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><span id="hideonscreen_id"></span></p></td>
				</tr>

				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><span id="hideonscreen_id"></span></p></td>
				</tr>
				<!-- ends here -->
				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><span id="hideonscreen_id"><fmt:message key="secure_note"/></span></p></td>
				</tr>

			</table>
                </div>
                    
            </div>
			</div>
			</div>
			</div>

</div>
			</div>
			</div>

<!-- KEK KEY PART 1 print-->
 <div class="winHide" id="maindialog1">
   <div class="widgets-area-pad">
    <div class="widgets-area" >
       <iframe id='ifrmPrintPart1' src='#' style="width:0px; height:0px;"></iframe>
		 <div class="col2">
          <div class="col2-pad">
		  <div class="widget" id="kekdialog1">
		  <div class="popup"><span class="popup_shadow" >
	<div class="popup_title" ><span class="popup_text hideonprint"><fmt:message key="kek_part_1"/></span>
	<strong><span id="hideonscreen_id" class="popup_text"><fmt:message key="bank_address_1"/></span></strong>
	<BR><strong><span id="hideonscreen_id" class="popup_text"><fmt:message key="bank_address_2"/></span></strong>
	<BR><span id="hideonscreen_id" class="popup_text"><fmt:message key="bank_address_3"/></span></div></span>
  </div>
  <ul class="toolbar2" id="kekdialog11">
   <li class="print hideonprint" >
	 <a onClick="ClickHereToPrintKekPart1();" class="ahand"
	  onMouseOver="hover('a3','ahover2');" 
	  onMouseOut="hover('a3','ahover1');" id="a3" title="<fmt:message key="print"/>"><fmt:message key="print"/></a>
  </li>
  <li class="closepwpatu hideonprint">
	  <a onClick="javasrcipt: hideDialogPart1();" class="ahand"
 onMouseOver="hover('a3','ahover2');" 
	onMouseOut="hover('a3','ahover1');" id="a3" title="<fmt:message key="close"/>"><fmt:message key="close"/></a></li>
 </ul>
 
          <div class="block-content">
               <div class="form-item">
                 <table cellpadding="4" cellspacing="4" width="100%" >

				 <tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><strong><span id="hideonscreen_id"><fmt:message key="kek_part_1"/></span></strong></p></td>
				</tr>

				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="patu_contact_person_1"/>: <span id="customerName1"></span>,       <span id="customerAdd1"></span></p></td>
				</tr>
				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="date"/>: <span id="generationdate"></span></p></td>
				</tr>
				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="control_code"/>: <span id="controlCode1"></span></p></td>
				</tr>
					
				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="bank_name_or_identification"/>: <c:out value="${bankName}"/> </p></td>
				</tr>

				<tr class="hideonscreen">
					<td  valign="top"><p class="label_normal"><fmt:message key="bank_id_or_code"/>: <c:out value="${bankId}"/></p></td>
				</tr>

				<tr class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="bank_id_or_code"/>: XXX</p></td>
				</tr>

				<tr class="hideonscreen">
					<td  valign="top" ><p class="label_normal"><fmt:message key="kek_generation_no"/>: <span id="kekgenerationno1"></span></p></td>
				</tr>
				
				<tr class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="kek_generation_no"/>: X</p></td>
				</tr>
				
				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><fmt:message key="part_1_of_kek"/>: <strong> <span id="keykekpart1"></span></strong></p></td>
				</tr>

				<tr id="noprint" class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="part_1_of_kek"/>: XXXXXXXXXXXXXXXX</p></td>
				</tr>	

				<%-- <tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen"><p class="label_normal"><fmt:message key="KVV"/>: <strong><span id="keykvv1"></span></strong></p></td>
				</tr> --%>

				<%-- <tr id="noprint" class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="KVV"/>: XXXXXX</p></td>
				</tr> --%>

				<tr id="noprint" class="hideonprint">
					<td  valign="top"><p class="label_normal">&nbsp;</p></td>
				</tr>

				<!-- to provide space between message and keys in print page, added dummy rows-->

				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><span id="hideonscreen_id"></span></p></td>
				</tr>

				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><span id="hideonscreen_id"></span></p></td>
				</tr>

				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><span id="hideonscreen_id"></span></p></td>
				</tr>
				<!-- ends here -->
				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen" ><p class="label_normal" class="hideonscreen"><span id="hideonscreen_id"><fmt:message key="secure_note"/></span></p></td>
				</tr>

				 </table>
                </div>
              </div>
			</div>
			</div>
			</div>
		    </div>
			</div>
			</div>
 </div>

<c:set var="versionNo" value="${lst.versionNo}" scope="page"/> 
<html:hidden property="versionNo" name="lst"/>

<c:choose>
<c:when test="${lst.versionPos=='' || lst.versionPos=='Y' || lst.versionPos eq null}" >
	<c:set var="lastVersionNo" value="${lst.versionNo}" />
	<input type="hidden" name="lastVersionNo" value="${lastVersionNo}" />
</c:when>
<c:otherwise>
	<c:set var="lastVersionNo" value="${requestScope.maxVersionNo}" />
	<input type="hidden" name="lastVersionNo" value="${lastVersionNo}" />
</c:otherwise>
</c:choose>

<html:hidden property="createdBy" name="lst"/>
<html:hidden property="status" name="lst"/>
<html:hidden property="lastUpdatedBy" name="lst"/>
<html:hidden property="authorizedBy" name="lst"/>
<input type="hidden" name="createdOn" value='<fmt:formatDate pattern= "${sessionObj.dateFormat}" value="${lst.createdOn}"/>'/>
<input type="hidden" name="lastUpdatedOn" value='<fmt:formatDate pattern= "${sessionObj.dateFormat}" value="${lst.lastUpdatedOn}"/>'/>
<input type="hidden" name="authorizedOn" value='<fmt:formatDate pattern= "${sessionObj.dateFormat}" value="${lst.authorizedOn}"/>'/>


 <table class="def-info">
    <tr>
      <th><fmt:message key="created_by"/></th>
      <th><fmt:message key="created_on"/></th>
      <th><fmt:message key="last_updated_by"/></th>
      <th><fmt:message key="last_updated_on"/></th>
	  <th><fmt:message key="authorized_by"/></th>
      <th><fmt:message key="authorized_on"/></th>
      <th><fmt:message key="version_no"/></th>
      <th><fmt:message key="status"/></th>
      <th class="last"></th>      
    </tr>
    <tr>
      <td><c:out value='${lst.createdBy}' /></td>
	  <td><fmt:formatDate pattern="${sessionObj.dateFormat}" value="${lst.createdOn}"/></td>
      <td><c:out value='${lst.lastUpdatedBy}' /></td>
	  <td><fmt:formatDate pattern="${sessionObj.dateFormat}" value="${lst.lastUpdatedOn}"/></td>
	  <td><c:out value='${lst.authorizedBy}' /></td>
	  <td><fmt:formatDate pattern="${sessionObj.dateFormat}" value="${lst.authorizedOn}"/></td>
      <td><c:out value='${lst.versionNo}'/>&nbsp;<fmt:message key="of"/>&nbsp;
			<c:out value='${lastVersionNo}'/></td>
      <td><c:out value='${lst.status}' /></td>
      <td class="last">

	  <c:choose>
        <c:when test="${lst.versionNo>1}">
			<a id="tdpre" onClick="return validate('ServiceBureauEdit',editorHold,'PREVIOUS');" class="left-arrow ahand" title="<fmt:message key="Previous"/>">&larr;</a>	
			<script>hover('apre','prev_next')</script>
        </c:when>
        <c:otherwise>
			<a id="tdpre" disabled onClick="return validate('ServiceBureauEdit',editorHold,'PREVIOUS');" class="left-arrow-disabled" title="<fmt:message key="Previous"/>">&larr;</a>	
        </c:otherwise>
	 </c:choose>
	 
	 <c:choose>
        <c:when test="${lst.versionPos=='N'}">
			<a id="tdnext" onClick="return validate('ServiceBureauEdit',editorHold,'NEXT');" class="right-arrow ahand" title="<fmt:message key="Next"/>">&rarr;</a>
        </c:when>
        <c:otherwise>
			<a id="tdnext" disabled onClick="return validate('ServiceBureauEdit',editorHold,'NEXT');" class="right-arrow-disabled" title="<fmt:message key="Next"/>">&rarr;</a>
        </c:otherwise>
	</c:choose>

      </td>
    </tr>
  </table>
<!--  Added  -->
</fmt:bundle>
</html:form>
</c:forEach>
<!-- End of Edit Department -->   
</c:when>
<c:otherwise>
<fmt:bundle basename="errors.AgreementErrorResources">
	<p class="login_userpass"><fmt:message key="ERRAG-003"/></p>
</fmt:bundle>
</c:otherwise>
</c:choose>
<script>displayKeys(document.getElementById('internalBureauId').value, '${generateKeys}','KEY_DISPLAY');</script>
</body>
</html>
<script>populateArray(document.ServiceBureauEdit, currVals);</script>
<script>
function displayToolTip()
{
	document.getElementById('btnGenerateKEK').title=document.getElementById('btnGenerateKEK').value; 
}
</script>