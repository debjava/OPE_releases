function inputsWithText()
{
	$('.with-text').each(function(){						  
		$(this).focus(function(){
			if (this.value==this.defaultValue) {
				this.value="";
			}
		});
		$(this).blur(function(){
			if (this.value=="") {
				this.value=this.defaultValue;
			}
		});
	});
}

function getObjFromInputs(form)
{
	var obj={};
	$(form).find(':input').each (function()
	{
		obj[$(this).attr('name')]=$(this).val();
	});
	return obj;	
}

