<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<#noparse>
<link rel="stylesheet" type="text/css" href="${f:url('/css/global.css')}"/>
</#noparse>
</head>
<body>

<html:errors/>

<form action="insert" >

<table class="tablebg">
<#list mappings as mapping>
	<tr>
		<td> ${mapping.javaFieldName} </td>
		<td>
			<#noparse>
				<input type="text" value="${f:h(</#noparse>${mapping.javaFieldName}<#noparse>)}" name="</#noparse>${mapping.javaFieldName}<#noparse>" />
			</#noparse>
		</td>	
	</tr>
</#list>

</table>

<input type="submit" name="insert" value="CREATE" />
</form>
<br/><br/>

<a href="list">list page</a>

<body>
</html>