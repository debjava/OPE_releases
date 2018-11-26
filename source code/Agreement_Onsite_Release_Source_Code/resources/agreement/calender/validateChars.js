function chars(field,str) 
{
	var valid;
	var ok = true;
	var temp;
	
	valid = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
			
	for (var i=0; i<field.value.length; i++) 
	{
		temp = "" + field.value.substring(i, i+1);		
		if (valid.indexOf(temp) == "-1") ok = false;				
	}
	if (!ok) 
	{
		alert(str);
		field.focus();
		return false;
	}
	else
	{
		return true;
	}
}
function alphanumeric(field,str) 
{
	var valid;
	var ok = true;
	var temp;
	
	valid = "0123456789 ()-.";
			
	for (var i=0; i<field.value.length; i++) 
	{
		temp = "" + field.value.substring(i, i+1);		
		if (valid.indexOf(temp) == "-1") ok = false;				
	}
	if (!ok) 
	{
		alert(str);
		field.focus();		
		return false;
	}
	else
	{
		return true;
	}
}
function alpha(field,str) 
{
	var valid;
	var ok = true;
	var temp;
	
	valid = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 -";
			
	for (var i=0; i<field.value.length; i++) 
	{
		temp = "" + field.value.substring(i, i+1);		
		if (valid.indexOf(temp) == "-1") ok = false;				
	}
	if (!ok) 
	{
		alert(str);
		field.focus();		
		return false;
	}
	else
	{
		return true;
	}
}
function nums(field,str) 
{
	var valid = "0123456789";
	var ok = true;
	var temp;	
	
	for (var i=0; i<field.value.length; i++) 
	{
		temp = "" + field.value.substring(i, i+1);		
		if (valid.indexOf(temp) == "-1") ok = false;		
	}
	if (!ok) 
	{
		alert(str);
		field.focus();		
		return false;
	}
	else
	{
		return true;
	}
}
function trim(s) {
  while (s.substring(0,1) == ' ') {
    s = s.substring(1,s.length);
  }
  while (s.substring(s.length-1,s.length) == ' ') {
    s = s.substring(0,s.length-1);
  }
  return s;
}