$(document).ready(function() {
	$.ifixpng('img/blank.gif');
	$('.png').ifixpng();
	dm_tree();
	dm_left_blocks();
	dm_resize();
	$(window).resize(dm_resize);
	initTabber();
	//addTab("Dashboard","dashboard.html");

})

function dm_tree ()
{
	$('ul.tree .folder > a').click( function ()
	{
		if ($(this).parent().hasClass('closed'))
		{
			$(this).parent().removeClass('closed');
			$(this).next().slideDown(200);
		}
		else 
		{
			$(this).next().slideUp(200).parent().addClass('closed');
		}
		
		return false;
	});
	
	$('ul.tree .page > a').click( function ()
	{
		addTab( $(this).text(), $(this).attr('href'));
		
		return false;
	});
}

function dm_left_blocks()
{
	$('.left-block .min-max').click(function ()
	{
		if ($(this).hasClass('closed'))
		{
			$(this).removeClass('closed');
			$(this).parent().next().slideDown(400);
		}
		else 
		{
			$(this).parent().next().slideUp(400);
			$(this).addClass('closed');
		}
		return false;
	});
}

function dm_resize()
{
	if ($.browser.msie) {
		$('.tabber').css('width',$(window).width()-$('.sidebar').width()-31);	
	}	
	$('.container').height($(window).height()-39);
	$('#content').css('height',$(window).height()-75);		
	checkTabsArrows();
}


function initTabber ()
{
	$('.tabber .tab').click(tabClick).find('.close').click(tabClose);
	$('.tab-arrows  a').click(tabArrowClick);
}

function tabArrowClick ()
{
	var tabber=$('.tabber');
	var tabWidth=$('.tabber .tab').width()+2;
	if (!$(this).hasClass('right-disabled') && !$(this).hasClass('left-disabled'))
	{
		if ($(this).hasClass('left'))
		{
			tabber.scrollLeft((tabber.scrollLeft()-tabWidth));
		} else {
			tabber.scrollLeft((tabber.scrollLeft()+tabWidth));
		}
	}
	chechArrowsDisable();
}

function chechArrowsDisable()
{
	var tabber=$('.tabber');
	var tabWidth=$('.tabber .tab').width()+2;
	var tabNums=$('.tabber .tab').length;
	var allTabsWidth=tabNums*tabWidth;
	if (allTabsWidth-tabber.scrollLeft()>tabber.width())
		$('.tab-arrows .right').removeClass('right-disabled');
	else 
		$('.tab-arrows .right').addClass('right-disabled');
		
	if (tabber.scrollLeft()>0)
		$('.tab-arrows .left').removeClass('left-disabled');
	else 
		$('.tab-arrows .left').addClass('left-disabled');		
}


function tabClose()
{
	var tab=$(this).parent();
	if (tab.hasClass('active'))
	{
		if (tab.next().addClass('active').length<=0)
		{
			tabActivate(tab.prev());
			tab.prev().addClass('active');
		}
		else 
		{
			tabActivate(tab.next());			
		}
	}
	tab.remove();
	
	var tabContentID='tabContent_'+tab.attr('id').replace('tab_','');
	$('#'+tabContentID).remove();
	
	chechArrowsDisable();
	checkTabsArrows();
	
	return false;
}


function tabClick()
{
	if (!$(this).hasClass('active'))
		tabActivate(this);
	return false;
}

function tabActivate(tab)
{
	if ($('.tabber .tab').length>1)
	{
		$('.tabber .active').removeClass('active');
		$(tab).addClass('active');
		$('.active-tab').removeClass('active-tab');
		var tab_id='tabContent_'+$(tab).attr('id').replace('tab_','');
		$('#'+tab_id).addClass('active-tab');
	}
}

function checkTabsArrows () 
{
	var tab=$('.tabber .tab');
	var tabber=$('.tabber');
	var width=tab.length*(tab.width()+2);
	var arrows=$('.tab-arrows');
	if (width>$('.tabber').width() || tabber.scrollLeft()>0)
	{
		chechArrowsDisable();		
		arrows.show();
	}
	else
	{
		arrows.hide();
	}
}

function addTab(title,url)
{
	
	var tabID='tab_'+$('.tabber .tab').length;
	var tabContentID='tabContent_'+$('.tabber .tab').length;
	
	var tab='<a href="'+url+'" id="'+tabID+'" class="tab active"><span class="shadow">'+title+'</span><span class="text">'+title+'</span></a>';
	$('.tabber .tab').removeClass('active');
	$('.tabber .tab-area').prepend(tab);
	$('.active-tab').removeClass('active-tab');
	$('#content-loader').show();
	$.get(url,null,function(data)
	{
		$('#content').prepend('<div id="'+tabContentID+'" class="tab-content">'+data+'</div>');
		
		if ($('#'+tabID).hasClass('active'))
		{
			$('#'+tabContentID).addClass('active-tab');
		}
		$('#content-loader').hide();
		
		if ( ($('.window').length>0) && ($.browser.msie && Number($.browser.version)<=6) )
			hideSelects();
			
	}, "jsp");

	initTabber();
	
	chechArrowsDisable();
	checkTabsArrows();		
}

function hideSelects()
{
	$('select').hide();
}

function showSelects()
{
	$('select').show();
}

function openWindow(title,url)
{
	var wcode='<div class="window-bg"></div><div class="window"><div class="title"><span class="shadow">'+title+'</span><span class="text">'+title+'</span><a href="#" class="close">X</a></div><div class="window-content"><div class="window-loader"></div></div></div>';
	$('body').prepend (wcode);
	if ($.browser.msie && Number($.browser.version)<=6) {
		hideSelects();
		$('.window-bg').height( $(document).height() );
	}		
	windowToCenter();
	$.get(url,null,function(data)
	{
		$('.window .window-content').prepend(data);
		$('.window .window-loader').hide();		  
	}, "html");	
	
	$('.window .title .close').click (windowClose);
	
}

function openCustomWindow(title,url,width,height)
{	
	var wcode='<div class="window" style="{width:'+width+';}"><div class="title"><span class="shadow">'+title+'</span><span class="text">'+title+'</span><a href="#" class="close">X</a></div><div class="window-content" style="{height:'+height+';}"><div class="window-loader"></div></div></div>';
	$('body').prepend (wcode);
	if ($.browser.msie && Number($.browser.version)<=6) {
		hideSelects();
		$('.window-bg').height( $(document).height() );
	}		
	windowToCenter();
	$.get(url,null,function(data)
	{
		$('.window .window-content').prepend(data);
		$('.window .window-loader').hide();		  
	}, "html");
	
	$('.window .title .close').click (windowClose);
	
}

function windowClose ()
{
	$('.window-bg').remove();
	$('.window').remove();
}

function windowToCenter()
{

	var xpos=($(window).width()/2)-($('.window').width()/2);
	var ypos=($(window).height()/2)-($('.window').height()/2);
	
	$('.window').css({left:xpos,top:ypos});
}