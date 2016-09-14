[#ftl]
<!DOCTYPE html >
<html id="ng-app" xmlns:ng="http://angularjs.org" 
    data-ng-app="${baseName}" 
    data-ng-controller="ViewController as ViewCtrl" 
    data-ng-cloak >

<head>
    <meta content="text/html; iso-8859-1" http-equiv="content-type">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
    [#include "/frame-head.ftl" /]

   <script type="text/javascript">var externalId = ${(externalId!0)?c};</script>

	<link type="image/x-icon" href="/images/favicon.ico" rel="shortcut icon">
	<link type="image/x-icon" href="/images/favicon.ico" rel="icon">

    <title>${title!''}</title>

    [#if _csrf??]
	<meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    [/#if]

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
		    	<div class="row" data-ng-include="'/${baseName}/selection-${baseName}.html'"></div>
		    </div><!-- end of page -->
		</section>
	    </div><!--content-->
	</div><!--main-container-->

	[#include "/frame-js.html" /]
	[#include "/frame-custom.html" /]

</body>

</html>
