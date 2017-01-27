[#ftl]
<!DOCTYPE html >
<html id="ng-app" xmlns:ng="http://angularjs.org" 
    data-ng-app="app"
    data-ng-controller="ViewController as ViewCtrl" 
    data-ng-cloak >

<head>

	[#include "/frame-head.ftl" /]

</head>
<body id="app" 
	data-ng-controller="${baseName?capitalize}Controller as ${baseName}Ctrl"
	class="app ng-animate [#if navCollapsedMin?? && navCollapsedMin]nav-collapsed-min[#else]nav-collapsed-min-hide [/#if] ">
	
	<section id="header"
		class="header-container header-fixed bg-dark" >
		[#include "/frame-top.html" /]
	</section>
	
	<div class="main-container" >
	    <aside id="nav-container" 
	    	class="nav-container nav-fixed nav-vertical bg-white">
	    	[#include "/_menu.html" /]
	    </aside>

	    <div id="content" class="content-container ui-ribbon-container ui-ribbon-primary">
	    [#if ribbon??]
	        <div class="ui-ribbon-wrapper">
	            <div class="ui-ribbon">${ribbon}</div>
	        </div>
	    [/#if]
	    <section class="view-container animate-fade-up">
			<div class="page page-dashboard" >
		    	<div class="row">
					[#include "/selection-${baseName}.html" /]
				</div>
		    </div><!-- end of page -->
		</section>
	    </div><!--content-->
	</div><!--main-container-->

	[#include "/frame-js.html" /]
	[#include "/frame-custom.html" /]

</body>

</html>
