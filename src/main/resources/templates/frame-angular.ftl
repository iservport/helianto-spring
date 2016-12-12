[#ftl]
<!DOCTYPE html >
<html id="ng-app" xmlns:ng="http://angularjs.org" 
    data-ng-app="app"
    data-ng-controller="ViewController as $view"
    data-ng-cloak >

<head>

    [#include "/frame-head.ftl" /]

</head>
<body id="app" 
	data-ng-controller="${baseName?capitalize}Controller as $${baseName}"
	data-ng-init="$root._csrf='${_csrf.token}'">

    [#include "lang.ftl" /]

	[#include "/frame-top.html" /]
	
	<div class="main-container">

		[#include "/_menu.html" /]

	    <div id="content">

            <section class="container" >[#include "/${baseName}.html" /]</section>

        </div>

	</div><!--main-container-->

	[#include "/frame-js.html" /]
	[#include "/frame-custom.html" /]

</body>

</html>
[#---
 * msg
 *
 * credits: https://github.com/ratherblue/freemarker-example
 *
 * Functions to look up a string with optional parameters
 *
 * @param code string of message code to lookup
 * @param args array of arguments included in string
 * @param escapeHtml boolean to determine whether or not to escape the html
 * @returns a string with the looked up message
 --]
[#function msg code args=[] escapeHtml=true ]

    [#local argsArray = [] /]

    [#if args?is_string]
        [#local argsArray = [args] /]
    [#elseif args?is_sequence]
        [#local argsArray = args /]
    [#else]
        [#return "" /]
    [/#if]

    [#return springMacroRequestContext.getMessage(code, argsArray, "", escapeHtml) /]

[/#function]
