$(document).ready(function() {
	initTable();
})

function initTable ()
{
	$(".search-result #select-all").click(selectCheckboxes);
	$(".search-result tbody tr")
	.hover(function()
	{
		$(this).addClass('over');
	}, function()
	{
		$(this).removeClass('over');
	})
	.unbind('onDblClick').click(function()
	{
		//openWindow('Test window - Edit','edit.html'); 
	});
}

function selectCheckboxes()
{
	var all=$(this).parents('.search-result').find('input[type=checkbox]');
	if ($(this).attr('checked'))
		all.attr('checked',true);
	else 
		all.attr('checked',false);
}

