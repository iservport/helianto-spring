[#ftl]
<html >
<head>
    <title>${titlePage!'Approval'}</title>
    [#include "/frame-head.ftl" /]
    <style>${inLineCss!''}</style>
    [#if captchaKey?? ][/#if]
</head>

<body class="heliantoLogin" >
    <div id="main" class="container">
        <!--
         ! Logo
         !-->
        <br/>
        <div class="clearfix text-center">
            <a href="#" class="text-center" target="_self"><img src="${baseLogo!'/images/logo.png'}" alt="iservport" ></a>
        </div>
        <div class="row">
            <div class="col-md-offset-3 col-md-6" >
                <h1>${msg('access.form.APPROVAL')}</h1>
                <p>${msg('access.form.QUESTION', client.clientId)}</p>
            </div>
            <div class="col-md-offset-3 col-md-3" >
                <form id="confirmationForm" name="confirmationForm" action="/oauth/authorize" method="post"><input
                        name="user_oauth_approval" value="true" type="hidden">
                    <input type="hidden" name="_csrf" value="'${_csrf.token}'">
                    <input name="authorize" class="btn btn-primary btn-block" value="${msg('access.form.AUTHORIZE')}" type="submit">
                </form>
            </div>
            <div class="col-md-3" >
                <form id="denialForm" name="denialForm" action="/oauth/authorize" method="post">
                    <input name="user_oauth_approval" value="false" type="hidden">
                    <input type="hidden" name="_csrf" value="'${_csrf.token}'">
                    <input name="deny" class="btn btn-danger btn-block" value="${msg('access.form.DENY')}" type="submit">
                </form>
            </div>
        </div>
    </div>
</body>
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
