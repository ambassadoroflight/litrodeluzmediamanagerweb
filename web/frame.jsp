<%-- 
    COMTOR MEDIA MANAGER WEB
    Document   : index
    Created on : Jan 23, 2019, 16:45
    Author     : juandiego@comtor.net
--%>

<%@page import="net.comtor.framework.images.Images"%>
<%@page import="i18n.WebFrameWorkTranslationHelper"%>
<%@page import="net.comtor.aaa.ComtorPrivilege"%>
<%@page import="net.comtor.framework.jsptag.*"%>
<%@page import="net.comtor.aaa.ComtorUser"%>
<%@page import="web.global.GlobalWeb"%>
<%@page contentType="text/html"  buffer="500kb" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="WEB-INF/comtortags.tld" prefix="comtor"%>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<html>
    <head>
        <comtor:cssmain></comtor:cssmain>
    </head>
    
    <body>
        <div id="content">
            <comtor:content defaultpagefactory="web.gui.Index"/>
        </div>
    </body>
</html>
