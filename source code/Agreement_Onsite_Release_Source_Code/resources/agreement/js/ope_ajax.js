var _ms_XMLHttpRequest_ActiveX = ""; 
var _drop;                           
var _logger = true;                  
var _status_area;

if (!window.Node || !window.Node.ELEMENT_NODE) {
    var Node = { ELEMENT_NODE: 1, ATTRIBUTE_NODE: 2, TEXT_NODE: 3, CDATA_SECTION_NODE: 4, ENTITY_REFERENCE_NODE: 5,
                  ENTITY_NODE: 6, PROCESSING_INSTRUCTION_NODE: 7, COMMENT_NODE: 8, DOCUMENT_NODE: 9, DOCUMENT_TYPE_NODE: 10, 
    		  DOCUMENT_FRAGMENT_NODE: 11, NOTATION_NODE: 12 };
}

function logger( text, clear ) {
    if (_logger) {
        if (!_status_area) {
            _status_area = document.getElementById("status_area");
        }

        if (_status_area) {
            if (clear) {
                _status_area.value = "";
            }

            var old = _status_area.value;
            _status_area.value = text + ((old) ? "\r\n" : "") + old;
        }
    }
}

function executeReturn( DROP ) {
    if (DROP.readyState == 4) {
        if (DROP.status == 200) {
            logger('DROPRequest is complete: ' + DROP.readyState + "/" + DROP.status + "/" + DROP.statusText);
	    if ( DROP.responseText ) {
		    logger(DROP.responseText);
		    logger("-----------------------------------------------------------");
		    eval(DROP.responseText);
	    }
	}
    }
}

function DROPRequest( method, url, data, process, async, dosend) {
    
    var self = this;

    // check the dom to see if this is IE or not
    if (window.XMLHttpRequest) {
	// Not IE
        self.DROP = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
	// Hello IE!
        // Instantiate the latest MS ActiveX Objects
        if (_ms_XMLHttpRequest_ActiveX) {
            self.DROP = new ActiveXObject(_ms_XMLHttpRequest_ActiveX);
        } else {
	    // loops through the various versions of XMLHTTP to ensure we're using the latest
	    var versions = ["Msxml2.XMLHTTP.7.0", "Msxml2.XMLHTTP.6.0", "Msxml2.XMLHTTP.5.0", "Msxml2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP",
                        "Microsoft.XMLHTTP"];

            for (var i = 0; i < versions.length ; i++) {
                try {
		    
                    self.DROP = new ActiveXObject(versions[i]);

                    if (self.DROP) {
                        _ms_XMLHttpRequest_ActiveX = versions[i];
                        break;
                    }
                }
                catch (objException) {
                // trap; try next one
                } ;
            }

          //  ;
        }
    }
    
    
    if (typeof process == 'undefined' || process == null) {
        	process = executeReturn;
    }
		
	if (process == 'ajaxReturnToPatuId') {
        process = ajaxReturnToPatuId;
    }

	
	if (process == 'ajaxReturnToCustomerInformation') {
        process = ajaxReturnToCustomerInformation;
    }

	if (process == 'ajaxReturnToAccountInformation') {
        process = ajaxReturnToAccountInformation;
    }

	if(process == 'ajaxReturnToServiceBureauName'){
       process = ajaxReturnToServiceBureauName;
	}
	
	if (process == 'ajaxReturnToKEYS') {
        process = ajaxReturnToKEYS;
    }

	if (process == 'ajaxReturnToResetKEYS') {
        process = ajaxReturnToResetKEYS;
    }

	if (process == 'ajaxReturnToDeleteServiceSpecification') {
        process = ajaxReturnToDeleteServiceSpecification;
    }
	if (process == 'ajaxReturnToServiceId') {
        process = ajaxReturnToServiceId;
    }

	if (process == 'ajaxReturnToDelete') {
        process = ajaxReturnToDelete;
    }

	if (process == 'ajaxReturnToDisplayKey') {
        process = ajaxReturnToDisplayKey;
    }

	if (process == 'ajaxReturnToPrintKey') {
        process = ajaxReturnToPrintKey;
    }

	if (process == 'ajaxReturnToClearAgreement') {
        process = ajaxReturnToClearAgreement;
    }

	if (process == 'ajaxReturnClearTempTableData') {
        process = ajaxReturnClearTempTableData;
    }
		
	self.process = process;
	
    self.DROP.onreadystatechange = function( ) {        
        self.process(self.DROP);
    }
    
    if (!method) {
        method = "POST";
    }
    method = method.toUpperCase();
    if (typeof async == 'undefined' || async == null) {
        async = true;
    }
    logger("----------------------------------------------------------------------");
    logger("DROP Request: " + ((async) ? "Async" : "Sync") + " " + method + ": URL: " + url + ", Data: " + data);
    self.DROP.open(method, url, async);

    if (method == "POST") {
        self.DROP.setRequestHeader("Connection", "close");
        self.DROP.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        self.DROP.setRequestHeader("Method", "POST " + url + "HTTP/1.1");
    }
    
    if ( dosend || typeof dosend == 'undefined' ) {
	    if ( !data ) data=""; 

	    self.DROP.send(data);
    }        
    return self.DROP;
}

function clearSelect() {
    for (var i = 0; i < arguments.length; i++) {
        var element = arguments[i];

        if (typeof element == 'string')
            element = document.getElementsByName(element)[1];

        if (element && element.options) {
            element.options.length = 0;
            element.selectedIndex = -1;
        }
    }
}

// clear all values from combo-box
function clearComboBox(colObj){
	var count = colObj.options.length;
	if(count>1){
		for(var i=count-1; i>=1; i--)
		{
			colObj.remove(i);
		}
	}
}

// Get function get PATU ID

function getAjaxValueForPatuId(companyCode,eh,actionPath) {
	return new DROPRequest("POST", actionPath,"editorHold="+eh+"&companyName="+companyCode,"ajaxReturnToPatuId");
}

/* Function to return PATU ID. Getting output as XML format */

function ajaxReturnToPatuId(ajaxResponse)
{	
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToFunctionPatuId(ajaxResponse.responseText);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}


function getAjaxValueForCustomer(obj) {
	
if(document.getElementById(obj.id).readOnly!=true)
{
obj.value=Trim(obj.value);

var customerId=obj.value;

var eh="GET_CUSTOMER";

var actionPath='../jsp/getCustomerInformation.do';

var index=obj.id;

	if(customerId.length!=0){
		return new DROPRequest("POST", actionPath,"editorHold="+eh+"&customerId="+customerId+"&index="+index,"ajaxReturnToCustomerInformation");
	}
}
}

function ajaxReturnToCustomerInformation(ajaxResponse)
{
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToFunctionCustomerInformation(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}


function getAjaxValueForAccount(obj) {
	
if(document.getElementById(obj.id).readOnly!=true)
{
obj.value=Trim(obj.value);

var customerId=obj.value;

var eh="GET_ACCOUNT";

var actionPath='../jsp/getAccountInformation.do';

var index=obj.id;

	if(customerId.length!=0){
		return new DROPRequest("POST", actionPath,"editorHold="+eh+"&customerId="+customerId+"&index="+index,"ajaxReturnToAccountInformation");
	}
}
}

function ajaxReturnToAccountInformation(ajaxResponse)
{
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToFunctionAccountInformation(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}

function getAjaxValueForServiceBureauName(id,eh,actionPath) {
	if(id.length!=0){
		return new DROPRequest("POST", actionPath,"editorHold="+eh+"&bureauId="+id,"ajaxReturnToServiceBureauName");
	}
}

function ajaxReturnToServiceBureauName(ajaxResponse)
{
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToFunctionServiceBureauName(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}
// Ajax to delete service specification from table
function getAjaxForDeleteServiceSpecification(id){
	
	actionPath='../jsp/deleteservicespecification.do';
	eh='DELETE_SERVICE';
    if(id!=''){
		return new DROPRequest("POST", actionPath,"editorHold="+eh+"&internalRefId="+id,"ajaxReturnToDeleteServiceSpecification");
	}
}

// Ajax to delete service specification from table
function getAjaxForDeleteServiceSpecification(id,versionNo){
	
	actionPath='../jsp/deleteservicespecification.do';
	eh='DELETE_SERVICE';
    if(id!=''){
		return new DROPRequest("POST", actionPath,"editorHold="+eh+"&internalRefId="+id+"&versionNo="+versionNo,"ajaxReturnToDeleteServiceSpecification");
	}
}

function ajaxReturnToDeleteServiceSpecification(ajaxResponse)
{
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToFunctionDeleteServiceServiceSpecification(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}

// Ajax to delete Agreements details from table when user clicks the clear button

function getAjaxForClearAgreement(id){
	
	actionPath='../jsp/clearagreementnew.do'; 
	eh='CLEAR';
    if(id!=''){
		return new DROPRequest("POST", actionPath,"editorHold="+eh+"&internalAgreementId="+id,"ajaxReturnToClearAgreement");
	}
}

function ajaxReturnToClearAgreement(ajaxResponse)
{
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToFunctionClearAgreement(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}

//Ajax to find service id, service code & bureau id  combinations

function checkAjaxForServiceId(internalId,sId,bId,servicecode) {

var eh="CHECK_SERVICE_ID";
var actionPath='../jsp/getServiceIdAction.do';

if(sId.length!=0){
		return new DROPRequest("POST", actionPath,"editorHold="+eh+"&internalRefId="+internalId+"&serviceId="+sId+"&bureauId="+bId+"&serviceCode="+servicecode,"ajaxReturnToServiceId");
	}
}


function checkAjaxForServiceId(internalId,sId,bId,servicecode,agreementId) {
var eh="CHECK_SERVICE_ID";
var actionPath='../jsp/getServiceIdAction.do';

if(sId.length!=0){
		// return new DROPRequest("POST", actionPath,"editorHold="+eh+"&internalRefId="+internalId+"&serviceId="+sId+"&bureauId="+bId+"&serviceCode="+servicecode,"ajaxReturnToServiceId");
				return new DROPRequest("POST", actionPath,"editorHold="+eh+"&internalRefId="+internalId+"&serviceId="+sId+"&bureauId="+bId+"&serviceCode="+servicecode+"&agreementId="+agreementId,"ajaxReturnToServiceId");
	}
}

//function to return result to find out duplicate service id, bureau id combinations

function ajaxReturnToServiceId(ajaxResponse)
{
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{		
			ajaxReturnToFunctionServiceId(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}


//Ajax added to delete temporary table data.

function clearTempTableData(servicecode, agreementId) {
// alert('clearTempTableData() -> servicecode-> ' + servicecode + ' agreementId-> ' + agreementId);
var actionPath='../jsp/deleteTempTableDataAction.do';
	return new DROPRequest("POST", actionPath,"serviceCode="+servicecode+"&agreementId="+agreementId,"ajaxReturnClearTempTableData");
}


function ajaxReturnClearTempTableData(ajaxResponse)
{

	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			

			ajaxReturnToFunctionClearTempTableData(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}



// Get function get KEKS

function getAjaxValueForKEYS(internalBureauId,versionNo,eh,actionPath,action) {	
	return new DROPRequest("POST", actionPath,"editorHold="+eh+"&internalBureauId="+internalBureauId+"&versionNo="+versionNo+"&action="+action,"ajaxReturnToKEYS");
}

/* Function to return KEKS. Getting output as XML format */

function ajaxReturnToKEYS(ajaxResponse)
{	
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToFunctionKEYS(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}


// Reset Keys

function getAjaxValueToResetKEYS(internalBureauId,versionNo,eh,actionPath,action) {		
	return new DROPRequest("POST", actionPath,"editorHold="+eh+"&internalBureauId="+internalBureauId+"&versionNo="+versionNo+"&action="+action,"ajaxReturnToResetKEYS");
}

/* Function to Reset KEKS. Getting output as XML format */

function ajaxReturnToResetKEYS(ajaxResponse)
{	
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToFunctionResetKEYS(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}



function getAjaxValueToDeleteKEYS(internalBureauId,eh,actionPath,action) {	
	return new DROPRequest("POST", actionPath,"editorHold="+eh+"&internalBureauId="+internalBureauId+"&action="+action,"ajaxReturnToDelete");
}

//function returns the deletes the KEK and AUK and generates the active keys

function ajaxReturnToDelete(ajaxResponse)
{	
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToFunctionKEYS(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}

function getAjaxValueToDisplayKeys(internalBureauId,actionPath,eh) {	
	return new DROPRequest("POST", actionPath,"editorHold="+eh+"&internalBureauId="+internalBureauId,"ajaxReturnToDisplayKey");
}

//function returns the KEK and AUK values to active and inactive keys in service bureau edit user interface
function ajaxReturnToDisplayKey(ajaxResponse)
{	
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToDisplayKEYS(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}

function printKeys(internalBureauId,editorHold,actionPath,patuKeyAddress1,patuKeyAddress2){
    return new DROPRequest("POST", actionPath,"editorHold="+editorHold+"&internalBureauId="+internalBureauId+"&patuKeyAddress1="+patuKeyAddress1+"&patuKeyAddress2="+patuKeyAddress2,"ajaxReturnToPrintKey");
}
// function returns the patu print key details
function ajaxReturnToPrintKey(ajaxResponse)
{	
	if (ajaxResponse.readyState == 4 ) {
		if (ajaxResponse.status == 200) {
		try{			
			ajaxReturnToPrintKEYS(ajaxResponse.responseXML);			
 		} catch(e) {alert("Action failed, please try later");}
    }
  }
}

function disableCtrlKeyCombination(e)
{
	    var forbiddenKeys = new Array('a','b','c','d','e','f','g','h','i','j','k','l','n','o','p','q','r','s','t','u','v','w','x','y','z');
        var key;
        var isCtrl;
		
		var rkeycode2 = window.event.keyCode;
	   	if(window.event && window.event.keyCode == 17)
        {
			event.keyCode=0;
			return false;
		}

		if(window.event && window.event.keyCode == 114)
         {
			event.keyCode=0;
			return false;
		 }
		if(window.event && window.event.keyCode == 116)
        {
			event.keyCode=0;
			return false;
		}

		if(window.event && window.event.keyCode == 122)
        {
			event.keyCode=0;
			return false;
		}
		if(window.event && window.event.keyCode == 117)
        {
			event.cancelBubble=true;
			event.keyCode=0;
			return false;
		}
		
        if(window.event)
        {
                key = window.event.keyCode;     //IE
		        if(window.event.ctrlKey)
                        isCtrl = true;
                else
                        isCtrl = false;
        }
        else
        {
                key = e.which;                 //firefox
                if(e.ctrlKey)
                        isCtrl = true;
                else
                        isCtrl = false;
        }

        if(isCtrl)
        {
			    for(var i=0; i<forbiddenKeys.length; i++)
                {
                        //case-insensitive comparation
						if(forbiddenKeys[i].toLowerCase() == String.fromCharCode(key).toLowerCase())
                        {
						   event.keyCode=0;
                           return false;
                        }
                }
        }
		
        return true;
}
