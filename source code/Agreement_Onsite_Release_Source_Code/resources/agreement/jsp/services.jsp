<div id="agreement_service_table">
<table width="100%" border="0">
 <script>
 function showService1(serviceCode,chkbx)
 {
	
	if(document.getElementById(chkbx).checked==true)
	{
		var internalAgreementId = document.getElementById('internalAgreementId').value;
		 showPopUp('../jsp/getservicespecification.do?serviceCode='+serviceCode+'&internalAgreementId='+internalAgreementId+'&versionNo=1&versionPos=Y',420,650,'no','no');
	 return true;
	}
	else
	return false;
 }

// changing color of service's link based on checkbox 
function hovering(id,checkBoxId)
{
	var newClass;
	if(document.getElementById(checkBoxId).checked==true){
	newClass='ahand';
	identity=document.getElementById(id);
	identity.className=newClass;
	}else{
	newClass='nohand';
	identity=document.getElementById(id);
	identity.className=newClass;
	}
}


// this function is for checking services specification is enetered or not

 function checkFlags()
 {
	 if((document.getElementById('domestic_bill_payment').checked==true) && document.getElementById('S001F').value!='Y')
	 {
        alert(agmtMesg[6]+' '+'<fmt:message key="domestic_bill_payment"/>');
		return false;
	 }
	 else if((document.getElementById('back_reporting_cross_border_payment').checked==true) && document.getElementById('S002F').value!='Y')
	 {
		alert(agmtMesg[6]+' '+'<fmt:message key="back_reporting_cross_border_payment"/>');
		return false;
	 }
	 else if((document.getElementById('salary_payment_service').checked==true) && document.getElementById('S003F').value!='Y')
	 {
	    alert(agmtMesg[6]+' '+'<fmt:message key="salary_payment_service"/>');
		return false;
	 }
	 else if((document.getElementById('intraday_real_time_balance').checked==true) && document.getElementById('S004F').value!='Y')
	 {
	    alert(agmtMesg[6]+' '+'<fmt:message key="intraday_real_time_balance"/>');
		return false;
	 }
	 else if((document.getElementById('cross_border_payments').checked==true) && document.getElementById('S005F').value!='Y')
	 {
	    alert(agmtMesg[6]+' '+'<fmt:message key="cross_border_payments"/>');
		return false;
	 }
	 else if((document.getElementById('intraday_transaction_statement').checked==true) && document.getElementById('S006F').value!='Y')
	 {
	    alert(agmtMesg[6]+' '+'<fmt:message key="intraday_transaction_statement"/>');
		return false;
	 }
	 else if((document.getElementById('account_statement').checked==true) && document.getElementById('S007F').value!='Y')
	 {
	    alert(agmtMesg[6]+' '+'<fmt:message key="account_statement"/>');
		return false;
	 }
	 else if((document.getElementById('incoming_reference_payment').checked==true) && document.getElementById('S008F').value!='Y')
	 {
	    alert(agmtMesg[6]+' '+'<fmt:message key="incoming_reference_payment"/>');
		return false;
	 }
	 else if((document.getElementById('preadvice_of_incoming_cross_border_payment').checked==true) && document.getElementById('S009F').value!='Y')
	 {
	    alert(agmtMesg[6]+' '+'<fmt:message key="preadvice_of_incoming_cross_border_payment"/>');
		return false;
	 }
	 else if((document.getElementById('currency_exchange_rate').checked==true) && document.getElementById('S010F').value!='Y')
	 {
	    alert(agmtMesg[6]+' '+'<fmt:message key="currency_exchange_rate"/>');
		return false;
	 }
	 else if((document.getElementById('account_statement_of_account_with_other_bank').checked==true) && document.getElementById('S011F').value!='Y')
	 {
	    alert(agmtMesg[6]+' '+'<fmt:message key="account_statement_of_account_with_other_bank"/>');
		return false;
	 }
	 else
	 {
       return true;
	 }
 }
 </script>

<!-- flags for all service -->
 <input type="hidden" name="S001F" id="S001F" value="N">
 <input type="hidden" name="S002F" id="S002F" value="N">
 <input type="hidden" name="S003F" id="S003F" value="N">
 <input type="hidden" name="S004F" id="S004F" value="N">
 <input type="hidden" name="S005F" id="S005F" value="N">
 <input type="hidden" name="S006F" id="S006F" value="N">
 <input type="hidden" name="S007F" id="S007F" value="N">
 <input type="hidden" name="S008F" id="S008F" value="N">
 <input type="hidden" name="S009F" id="S009F" value="N">
 <input type="hidden" name="S010F" id="S010F" value="N">
 <input type="hidden" name="S011F" id="S011F" value="N">

 <tr>
   
   <td><input type="checkbox" name="services" class="checkbox" id="domestic_bill_payment" value="S001"></td>
   <td><p class="label_normal" onclick="javascript: return showService1('S001','domestic_bill_payment')" id="S001" onmouseover="hovering('S001','domestic_bill_payment');" onmouseout="hover('S001','nohand');"><fmt:message key="domestic_bill_payment"/></p></td>

   <td><input type="checkbox" name="services" class="checkbox" id="back_reporting_cross_border_payment" value="S002"></td>
   <td><p class="label_normal" onclick="javascript: return showService1('S002','back_reporting_cross_border_payment')" id="S002" 
   onmouseover="hovering('S002','back_reporting_cross_border_payment')" onmouseout="hover('S002','nohand')">
   <fmt:message key="back_reporting_cross_border_payment"/></p></td>
 </tr>
 
 <tr>
   <td><input type="checkbox" name="services" class="checkbox" id="salary_payment_service" value="S003"></td>
   <td><p class="label_normal" onclick="javascript:return showService1('S003','salary_payment_service')" id="S003"
   onmouseover="hovering('S003','salary_payment_service')" onmouseout="hover('S003','nohand')"><fmt:message key="salary_payment_service"/></p></td>

   <td><input type="checkbox" name="services" class="checkbox" id="intraday_real_time_balance" value="S004"></td>
   <td><p class="label_normal" onclick="javascript:return showService1('S004','intraday_real_time_balance')" id="S004"
   onmouseover="hovering('S004','intraday_real_time_balance')" onmouseout="hover('S004','nohand')">
   <fmt:message key="intraday_real_time_balance"/></p></td>
 </tr>

 <tr>
   <td><input type="checkbox" name="services" class="checkbox" id="cross_border_payments" value="S005"></td>
   <td><p class="label_normal" onclick="javascript:return showService1('S005','cross_border_payments')" id="s005" 
   onmouseover="hovering('S005','cross_border_payments')" onmouseout="hover('S005','nohand')"><fmt:message key="cross_border_payments"/></p></td>

   <td><input type="checkbox" name="services" class="checkbox" id="intraday_transaction_statement" value="S006"></td>
   <td><p class="label_normal" onclick="javascript:return showService1('S006','intraday_transaction_statement')" id="S006" 
   onmouseover="hovering('S006','intraday_transaction_statement')" onmouseout="hover('S006','nohand')">
   <fmt:message key="intraday_transaction_statement"/></p></td>
 </tr>
 
 <tr>
   <td>&nbsp;</td>
   <td>&nbsp;</td>
   <td><input type="checkbox" name="services" class="checkbox" id="account_statement" value="S007"></td>
   <td><p class="label_normal" onclick="javascript:return showService1('S007','account_statement')" id="S007" 
   onmouseover="hovering('S007','account_statement')" onmouseout="hover('S007','nohand')"><fmt:message key="account_statement"/></p></td>
 </tr>

 <tr>
   <td>&nbsp;</td>
   <td>&nbsp;</td>
   <td><input type="checkbox" name="services" class="checkbox" id="incoming_reference_payment" value="S008"></td>
   <td><p class="label_normal" onclick="javascript:return showService1('S008','incoming_reference_payment')" id="S008"
   onmouseover="hovering('S008','incoming_reference_payment')" onmouseout="hover('S008','nohand')">
   <fmt:message key="incoming_reference_payment"/></p></td>
 </tr>

 <tr>
   <td>&nbsp;</td>
   <td>&nbsp;</td>
   <td><input type="checkbox" name="services" class="checkbox" id="preadvice_of_incoming_cross_border_payment" value="S009"></td>
   <td><p class="label_normal" onclick="javascript:return showService1('S009','preadvice_of_incoming_cross_border_payment')" id="S009"
   onmouseover="hovering('S009','preadvice_of_incoming_cross_border_payment')" onmouseout="hover('S009','nohand')">
   <fmt:message key="preadvice_of_incoming_cross_border_payment"/></p></td>
 </tr>

 <tr>
   <td>&nbsp;</td>
   <td>&nbsp;</td>
   <td><input type="checkbox" name="services" class="checkbox" id="currency_exchange_rate" value="S010"></td>
   <td><p class="label_normal" onclick="javascript:return showService1('S010','currency_exchange_rate')" id="S010"
   onmouseover="hovering('S010','currency_exchange_rate')" onmouseout="hover('S010','nohand')">
   <fmt:message key="currency_exchange_rate"/></p></td>
 </tr>

 <tr>
   <td>&nbsp;</td>
   <td>&nbsp;</td>
   <td><input type="checkbox" name="services" class="checkbox" id="account_statement_of_account_with_other_bank" value="S011"></td>
   <td><p class="label_normal" onclick="javascript:return showService1('S011','account_statement_of_account_with_other_bank')" id="S011"
   onmouseover="hovering('S011','account_statement_of_account_with_other_bank')" onmouseout="hover('S011','nohand')">
   <fmt:message key="account_statement_of_account_with_other_bank"/></p></td>
 </tr> 
 
</table>
</div>



















