[#ftl]
<!DOCTYPE html>
<html id="ng-app" xmlns:ng="http://angularjs.org" 
	data-ng-app="security" 
	data-ng-controller="ViewController as ViewCtrl" 
>
<head>
    <title>${titlePage!'Security'}</title>
	[#include "/frame-head.ftl" /]
    <style>${inLineCss!''}</style>
</head>
<body class="heliantoLogin"
      data-ng-controller="SecurityController as securityCtrl">
	<div id="main" class="container">
        <!--
         ! Logo
         !-->
        <br/>
        <div class="clearfix text-center">
            <a href="#" class="text-center" target="_self"><img src="${baseLogo!'/images/logo.png'}" alt="iservport" ></a>
        </div>
        <script >
            var signed = false;
		    var email = [#if email??]'${email}' [#else]''[/#if];
        </script>
        [#include "/${main!'login.ftl'}" /]
	</div><!-- #main -->
	<footer class="footer" >
        <div class="row">
            <div class="col-md-3 offset-md-3"></div>
            <div class="col-md-6 text-center" >
                <hr>
                <p>
                    <a class="text-muted" target="_new" href="/agreement/privacy">${msg("legal.PRIVACY_POLICY")}</a>  |
                    <a target="_new" class="text-muted" href="/agreement/terms">${msg("legal.TERMS_OF_SERVICE")}</a>
                </p>
                <p><small>${copyright!''} | build ${buildNumber!0}</small></p>
            </div>
        </div>
        <p></p>
    </footer>
	[#include "/frame-js.html" /]
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
