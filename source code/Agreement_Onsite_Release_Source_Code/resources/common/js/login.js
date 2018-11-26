$(document).ready(function() {
	$.ifixpng('../images/blank.gif');
	$('.png').ifixpng();
	//alignLoginForm();
	//$(window).resize(alignLoginForm);
	//loginForm();	
})

/*
function alignLoginForm (ev) {
	var top=$(document).height()/2-100;
	if (top<133) top=133;
	$('#lg-form').css('top',top);
}
*/

function loginForm()
{
	$('#lg-form').bind("submit", function() 
	{ 
		if ($('#lg-form .error').css('display')!='none')
			$('#lg-form .error').fadeOut(300);	
		if ($('#user_id').val()!="" && $('#password').val()!="")
		{
			var url=$('#lg-form').attr('action');
			var data=getObjFromInputs($('#lg-form'));
			$('#lg-form .loader').css('display','block');
			$.post(url,data,formAnswer,"json")
		}
		else 
		{
			showLoginError('Please enter Login and Password');
		}
			return false; 
	});
}

function showLoginError (errorText)
{
	$('#lg-form .error span').text(errorText).parent().fadeIn(500);
	$('#user_id').focus();
}

function formAnswer(data)
{
 	$('#lg-form .loader').css('display','none');
	if (data.errorFlag!=0)
		showLoginError(data.error);
	else 
		window.location=data.url;
}