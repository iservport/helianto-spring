[#ftl]
<!DOCTYPE html>
<html id="ng-app" xmlns:ng="http://angularjs.org" 
	data-ng-app="security" 
	data-ng-controller="ViewController as ViewCtrl" 
>
<head>
    <title>${titlePage!'Security'}</title>
	[#include "/frame-head.ftl" /]
</head>
<body style="background-color: white; background-image: none;">
	<div id="main" class="container">
        [#include "/${main!'login.ftl'}" /]
	</div><!-- #main -->
	<footer class="footer" >
        <div class="row">
            <div class="col-md-12">
                <hr>
                <p>
                    <a class="text-muted" target="_new" href="/agreement/privacy">{{'_PRIVACY_POLICY'|common}}</a>  |
                    <a target="_new" class="text-muted" href="/agreement/terms">{{'_TERMS_OF_USE'|common}}</a>
                </p>
                <p><small>${copyright!''} | build ${buildNumber!0}</small></p>
            </div>
        </div>
        <p></p>
    </footer>
	[#include "/frame-js.html" /]
</body>
</html>