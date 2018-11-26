// JavaScript Document

/******************************************************************************************/
/* Project          : ProcessMate                                                         */
/* File Name        : ProcessMate2.js                                                     */
/* Date of Creation : 13-Mar-2006                                                         */
/* Created By       : Biju K R                                                            */
/* Purpose          : These functions creates an xml to change the styles[CSS] for a user */
/******************************************************************************************/

xmlFileCSS='';

function createXMLFileCSS(cntrl) 
{


	getxmlFileCSS();
	var style=cntrl.value;
	var beg_root='<idealinvent>';
	var end_root='</idealinvent>';
	var beg_ent='<changeCSS>';
	var end_ent='</changeCSS>';
	var beg_cols='';
	
		beg_cols+='<style>'+style+'</style>';		
  try 
  {
    var f,src;
    f = new ActiveXObject("Scripting.FileSystemObject");
	
	src = f.OpenTextFile(xmlFileCSS, 2, true); 
	src.writeline(beg_root+beg_ent+beg_cols+end_ent+end_root);
    src.Close();
  }
	catch(err)
	{
		var strErr = 'Error:';
	    strErr += '\nNumber:' + err.number;
	    strErr += '\nDescription:' + err.description;
	    document.write(strErr);
  }
  parent.location.href="main.htm"
}


/* This function is used to read and xml file 
   and write custom columns in some user interface pages.
   */


var xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
function loadXMLCSS() 
{
        xmlDoc.async="false";
        xmlDoc.onreadystatechange=verify;
        xmlDoc.load(xmlFileCSS);
}

function verifyCSS() 
{ 
        if(xmlDoc.readyState!=4)
                return false; 
}

function findEntityCSS(tree,entity) 
{
        if(tree.hasChildNodes()) 
		{	
			if(tree.tagName==entity)		
			{        
					extractColsCSS(tree);
			}
      	    for(var i=0; i<tree.childNodes.length; i++)
	            findEntityCSS(tree.childNodes(i),entity);
        }
} 

var dispCSS='';

function extractColsCSS(tree)
{	
	if(tree.hasChildNodes()) 
	{	
		for(var i=0; i<tree.childNodes.length; i++)
           	extractCols(tree.childNodes(i));
		if(tree.tagName=='style')
			changeCSS(tree.text);
	}
}


function initiateXMLCSS(entity) 
{
		getxmlFileCSS();
        loadXMLCSS();
        var doc=xmlDoc.documentElement;
        findEntityCSS(doc,entity);
		
		
}

function getxmlFileCSS()
{
	var URL = unescape(location.href);

	var st = URL.lastIndexOf("/") + 1;

	var ed = URL.length;

	var pathname = URL.substring(0,st);

	xmlFileCSS= pathname +'changeCSS.xml';
	xmlFileCSS=xmlFileCSS.replace('file:///','');
	xmlFileCSS=xmlFileCSS.replace(/\//g,"//");
}

/* XML function ends here */

/* Changing Style Sheets */
initiateXMLCSS('changeCSS');

var doAlerts=false;

function changeCSS(whichSheet)
{
  whichSheet=whichSheet-1;
  if(document.styleSheets)
  {
    var c = document.styleSheets.length;
    if (doAlerts) alert('Change to Style '+(whichSheet+1));
    for(var i=0;i<c;i++)
	{
      if(i!=whichSheet)
	  {
        document.styleSheets[i].disabled=true;
      }
	  else
	  {
        document.styleSheets[i].disabled=false;
      }
    }
  }
}

/* function is used to cancel the current task and go back to main page */

function doCancel()
{
  parent.location.href="main.htm";	
}


