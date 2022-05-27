<!DOCTYPE html>
<html>
<head>
    <title><g:if env="development">Grails Runtime Exception</g:if><g:else>Error</g:else></title>
    <meta name="layout" content="ala"/>
    <g:if env="development"><link rel="stylesheet" href="${resource(dir: 'css', file: 'errors.css')}" type="text/css"></g:if>
</head>
<body>
<h1>
    An error has occurred
</h1>
<g:if env="development">
    <h2>The following exception is only displayed in development</h2>
    <g:renderException exception="${exception}" />
</g:if>
<g:else>
    <ul class="errors">
        <li>Error: unknown</li>
    </ul>
</g:else>
<g:if test="${flash.message}">
    <ul class="errors">
        <li>${flash.message}</li>
    </ul>
</g:if>
</body>
</html>
