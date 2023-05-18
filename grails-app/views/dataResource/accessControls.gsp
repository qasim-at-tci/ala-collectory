<%@ page import="au.org.ala.collectory.ProviderGroup" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="${grailsApplication.config.skin.layout}"/>
  <title><g:message code="dataResource.base.label" default="Edit data resource access controls" /></title>
</head>
<body onload="load();">
<div class="nav">
  <h1><g:message code="collection.title.editing" />: ${command.name} : Access Controls</h1>
</div>
<div class="body">
  <g:if test="${message}">
    <div class="message">${message}</div>
  </g:if>
  <g:hasErrors bean="${command}">
    <div class="errors">
      <g:renderErrors bean="${command}" as="list"/>
    </div>
  </g:hasErrors>
  <g:form controller="dataResource" action="updateAccessControls">
    <g:hiddenField name="id" value="${command?.id}"/>
    <g:hiddenField name="uid" value="${command?.uid}" />

    <div class="form-group">
      <label for="publicResolutionToBeApplied">Public resolution</label>
      <g:select name="publicResolutionToBeApplied" class="form-control" from="${publicResolutionMap.entrySet()}" optionKey="key" optionValue="value" value="${dataResourceNbn?.publicResolutionToBeApplied ?: 5000}"
                />
    </div>


    <div class="buttons">
      <span class="button"><input type="submit" value="Update" class="save btn btn-success"></span>
      <span class="button"><input type="submit" name="_action_cancel" value="${message(code:"shared.button.cancel")}" class="cancel btn btn-default"></span>
    </div>
  </g:form>
</div>

</body>
</html>
