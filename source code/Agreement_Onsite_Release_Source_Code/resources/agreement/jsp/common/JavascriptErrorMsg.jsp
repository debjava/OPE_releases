<!--
This file contains all the javascript functions which alerts a message to the UI.
File localizes all the messages which alerts to the UI.
-->

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:requestEncoding value="UTF-8"/>
<c:set var="property"  value="errors.AgreementErrorResources" />
<c:set var="language1" value="${property}_${sessionObj.languageId}" />

<fmt:setLocale value="${sessionObj.languageId}"/>
<fmt:bundle basename="${language1}">

<script>
	var mesg = new Array();
	mesg[0]= '<fmt:message key="WARGD-100"/>';
	mesg[1]= '<fmt:message key="ERRGD-100"/>';
	mesg[2]= '<fmt:message key="WARGD-101"/>';
	mesg[3]= '<fmt:message key="ERRGD-101"/>';
	mesg[4]= '<fmt:message key="ERRGD-102"/>';
	mesg[5]= '<fmt:message key="WARGD-102"/>';
	mesg[6]= '<fmt:message key="WARGD-103"/>';
	mesg[7]= '<fmt:message key="WARGD-104"/>';
	mesg[8]= '<fmt:message key="WARGD-105"/>';
	mesg[9]= '<fmt:message key="WARGD-106"/>';
	mesg[10]= '<fmt:message key="ERRGD-103"/>';
	mesg[11]= '<fmt:message key="WARGD-107"/>';
	mesg[12]= '<fmt:message key="DECGD-100"/>';
	mesg[13]= '<fmt:message key="DECGD-101"/>';
	mesg[14]= '<fmt:message key="DECGD-102"/>';
	mesg[15]= '<fmt:message key="WARGD-108"/>';
	mesg[16]= '<fmt:message key="DECGD-103"/>';
	mesg[17]= '<fmt:message key="WARGD-109"/>';
	mesg[18]= '<fmt:message key="WARGD-110"/>';
	mesg[19]= '<fmt:message key="WARGD-111"/>';
	mesg[20]= '<fmt:message key="WARGD-112"/>';
	mesg[21]= '<fmt:message key="WARGD-113"/>';
	mesg[22]= '<fmt:message key="WARGD-114"/>';
	mesg[23]= '<fmt:message key="WARGD-115"/>';
	mesg[24]= '<fmt:message key="WARGD-116"/>';
	mesg[25]= '<fmt:message key="WARGD-117"/>';
	mesg[26]= '<fmt:message key="ERRGD-104"/>';
	mesg[27]= '<fmt:message key="ERRGD-105"/>';
	mesg[28]= '<fmt:message key="ERRGD-106"/>';
    mesg[29]= '<fmt:message key="DECGD-105"/>';
	mesg[30]= '<fmt:message key="ERRMG-001"/>';
	mesg[31]= '<fmt:message key="WARGD-118"/>';
	mesg[32]= '<fmt:message key="WARGD-119"/>';
	mesg[33]= '<fmt:message key="WARGD-120"/>';
	mesg[34]= '<fmt:message key="WARUD-001"/>';
	mesg[35]= '<fmt:message key="WARGD-122"/>';
	mesg[36]= '<fmt:message key="WARGD-123"/>';
	mesg[37]= '<fmt:message key="WARGD-124"/>';
	
	mesg[38]='<fmt:message key="ERRGD-054"/>';
	mesg[39]='<fmt:message key="ERRGD-055"/>';
	
	mesg[40]='<fmt:message key="WARGD-127"/>';
	mesg[41]='<fmt:message key="WARGD-126"/>';
    mesg[42]='<fmt:message key="ERRGD-107"/>';
	mesg[43]='<fmt:message key="ERRGD-041"/>';
	mesg[44]='<fmt:message key="WARGD-129"/>';
	mesg[45]='<fmt:message key="WARGD-130"/>';	
	mesg[47]='<fmt:message key="WARGD-131"/>';	
	
	mesg[69]='<fmt:message key="WAR-PRO-001"/>';
	mesg[70]='<fmt:message key="ERRGD-108"/>';
	mesg[71]='<fmt:message key="ERRGD-109"/>';

	mesg[83]='<fmt:message key="WARREP-001"/>';
	mesg[84]='<fmt:message key="WARREP-002"/>';
	mesg[85]='<fmt:message key="WARREP-003"/>';
    mesg[86]='<fmt:message key="WARREP-004"/>';

	mesg[94]='<fmt:message key="WAR-USER-001"/>';
	mesg[95]='<fmt:message key="WAR-USER-002"/>';
	mesg[96]='<fmt:message key="WAR-USER-003"/>';

	var bureauMesg = new Array();

	bureauMesg[0]= '<fmt:message key="WAR-SVB-001"/>'; // Please enter company name
	bureauMesg[1]= '<fmt:message key="WAR-SVB-002"/>'; // PATU ID is already generated
	bureauMesg[2]= '<fmt:message key="WAR-SVB-003"/>'; // Company Name should be minimum 5 characters
	bureauMesg[3]= '<fmt:message key="WAR-SVB-004"/>'; // Failed to generate the PATU ID. Please try later.
	bureauMesg[4]= '<fmt:message key="WAR-SVB-005"/>'; //Service Bureau Id should be 17 characters.

	bureauMesg[5]= '<fmt:message key="WAR-SVB-006"/>'; // Keys have been generated successfully
	bureauMesg[6]= '<fmt:message key="WAR-SVB-007"/>'; // Keys have been re-generated successfully
	bureauMesg[7]= '<fmt:message key="WAR-SVB-008"/>'; // Could not generate the keys
	bureauMesg[8]= '<fmt:message key="WAR-SVB-009"/>'; // Keys have been reset successfully
	bureauMesg[9]= '<fmt:message key="WAR-SVB-010"/>'; // Could not reset the keys. Please try again.

	bureauMesg[10]= '<fmt:message key="DEC-SVB-001"/>'; // Do you wish to generate the keys?
	bureauMesg[11]= '<fmt:message key="DEC-SVB-002"/>'; // Do you wish to re-generate the keys?
	bureauMesg[12]= '<fmt:message key="DEC-SVB-003"/>'; // Do you wish to reset the keys?

	bureauMesg[13]= '<fmt:message key="WAR-SVB-011"/>'; // Warning: Keys have to be generated for the service bureau
	bureauMesg[14]= '<fmt:message key="DEC-SVB-004"/>'; // Do you wish to reset the keys?
	bureauMesg[15]= '<fmt:message key="WAR-SVB-012"/>'; // Keys deleted succssfully
	bureauMesg[16]= '<fmt:message key="WAR-SVB-013"/>'; // PATU Id generation is in progress. Try again.

	
	var agmtMesg= new Array();
	
	agmtMesg[0]= '<fmt:message key="WAR-AGR-001"/>'; // Valid From should be greater than or equal to Date of Agreement
	agmtMesg[1]= '<fmt:message key="WAR-AGR-002"/>'; // Valid To should be greater than or equal to Valid From
	agmtMesg[2]= '<fmt:message key="WAR-AGR-003"/>'; // Select atleast one service
	agmtMesg[3]= '<fmt:message key="WAR-AGR-004"/>'; // Customer Id does not exists
	agmtMesg[4]= '<fmt:message key="WAR-AGR-005"/>'; // Duplicate not allowed for customer id
	agmtMesg[5]= '<fmt:message key="WAR-AGR-006"/>'; // Cannot Delete row
	agmtMesg[6]= '<fmt:message key="WAR-AGR-007"/>'; // Please enter service specification for
	agmtMesg[7]= '<fmt:message key="WAR-AGR-008"/>'; // Please generate the PATU Keys in Service Bureau
    agmtMesg[8]= '<fmt:message key="WAR-AGR-009"/>'; // Valid From should be greater than or equal to current date
    agmtMesg[9]= '<fmt:message key="WAR-AGR-010"/>'; // Special characters are not allowed
    agmtMesg[10]= '<fmt:message key="WAR-AGR-011"/>'; // Do you wish to save?
    agmtMesg[11]= '<fmt:message key="WAR-AGR-012"/>'; // Do you wish to save the changes?
    agmtMesg[12]= '<fmt:message key="WAR-AGR-013"/>'; // Invalid telephone number
    agmtMesg[13]= '<fmt:message key="WAR-AGR-014"/>'; // Invalid zip code

	var svpMesg= new Array();
    
	svpMesg[0]= '<fmt:message key="WAR-SVP-001"/>'; // Account Number does not exists
	svpMesg[1]= '<fmt:message key="WAR-SVP-002"/>'; // Account Number already exists
	svpMesg[2]= '<fmt:message key="WAR-SVP-003"/>'; // Cannot Delete row
	svpMesg[3]= '<fmt:message key="WAR-SVP-004"/>'; // Bureau Name does not exists
	svpMesg[4]= '<fmt:message key="WAR-SVP-005"/>'; // Service Id already exists
	svpMesg[5]= '<fmt:message key="WAR-SVP-006"/>'; // No Records to Edit
	svpMesg[6]= '<fmt:message key="WAR-SVP-007"/>'; // No service Id is entered
	svpMesg[7]= '<fmt:message key="WAR-SVP-008"/>'; // Service Id, Bureau Id combination already exists for this Service type
	svpMesg[8]= '<fmt:message key="WAR-SVP-009"/>'; // Please enter the customer id 
	svpMesg[9]= '<fmt:message key="WAR-SVP-010"/>'; // Please enter the account number 
	svpMesg[10]= '<fmt:message key="WAR-SVP-011"/>'; // please create service specification 
	svpMesg[11]= '<fmt:message key="WAR-SVP-012"/>'; // please create service specification 

	var patuAuditMesg= new Array();

	patuAuditMesg[0]= '<fmt:message key="WAR-RPTP-001"/>'; // Date To should be greater than or equal to Date From

	var fileTransferMesg= new Array();

	fileTransferMesg[0]= '<fmt:message key="WAR-RPTF-001"/>'; // Date To should be greater than or equal to Date From
	fileTransferMesg[1]= '<fmt:message key="WAR-RPTF-002"/>'; // Hours in date to should be greater than or equal to hours in date from
		
/* This function is used in the above function */

function validateMe(list_array)
{
	if (!list_array)
	   	return;
	for (var i = 0; i < list_array.length; ++i)
    	if(list_array[i].id.indexOf('*')!=-1 && list_array[i].value=="" || list_array[i].value=="Select" )
			disp_text+=list_array[i].id.replace('*','') + "\n"
}

function validateCTM(list_array)
{
	if (!list_array)
	   	return;

	for (var i = 0; i < list_array.length; ++i)
	{
    	if(list_array[i].id.indexOf('*')!=-1 && (list_array[i].value=="" || list_array[i].value=="Select" ))
		{
			disp_text+=list_array[i].id.replace('*','') + "\n"
		}
	}
}

</script>
</fmt:bundle>
