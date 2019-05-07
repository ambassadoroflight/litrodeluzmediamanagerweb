<%-- 
    COMTOR MEDIA MANAGER WEB
    Document   : index
    Created on : Jan 23, 2019, 16:45
    Author     : juandiego@comtor.net
--%>
<%@page import="web.global.LitroDeLuzImages"%>
<%@page import="net.comtor.framework.images.Images"%>
<%@page import="net.comtor.util.connection.ConnectionType"%>
<%@page import="net.comtor.dao.ComtorDaoException"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="net.comtor.framework.global.ComtorGlobal"%>
<%@page import="net.comtor.framework.images.Images"%>
<%@page import="net.comtor.aaa.ComtorPrivilege"%>
<%@page import="net.comtor.framework.jsptag.*"%>
<%@page import="net.comtor.aaa.ComtorUser"%>
<%@page import="web.global.GlobalWeb"%>
<%@page import="i18n.WebFrameWorkTranslationHelper"%>
<%@page contentType="text/html"  buffer="500kb" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="WEB-INF/comtortags.tld" prefix="comtor"%>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    String title = GlobalWeb.PROJECT_NAME;

    switch (GlobalWeb.CONNECTION_TYPE) {
        case DEVELOPMENT:
            title += " (Desarrollo)";
            break;
        case TEST:
            title += " (Pruebas)";
            break;
    }
%>
<html>
    <head>
        <title><%=title%></title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link href="framework/images/favicon.png" rel="shortcut icon" />
        <comtor:cssmain></comtor:cssmain>
            <link type="text/css" rel="stylesheet" href="framework/css/login.css"/>
            <link type="text/css" rel="stylesheet" href="framework/css/simple-grid.min.css"/>
            <link type="text/css" rel="stylesheet" href="framework/css/mediamanager.css"/>
        <comtor:jsjquery></comtor:jsjquery>
        <comtor:jscomtorframework></comtor:jscomtorframework>
        </head>

        <body>
        <comtor:ifsessionexists>
            <div id="top">
                <div id ="logo">
                    <a href="index.jsp">
                        <img src="<%=LitroDeLuzImages.MEDIA_MANAGER_LOGO_APP%>" alt="<comtor:keytranslation key="html.client.logo.alt"></comtor:keytranslation>"/>
                        </a>
                    </div>
                    <div id="header">
                        <span id="title"><%=title%></span>
                </div>

                <div class="menu">                   
                    <span id="classicMenu" class="topnav">
                        <comtor:menuli internationalized="true" menuid="sysMenu"></comtor:menuli>
                        </span>

                        <a href="#" class="menu-icon" onclick="showMenu();" title="Expandir Menú">&#9776;</a>

                    <comtor:guielement clazz="web.gui.userInfo.HtmlUserSpecificMenu"></comtor:guielement>
                    </div>

                    <div class="clear"></div>
                </div>
        </comtor:ifsessionexists>

        <div id="content">
            <comtor:content defaultpagefactory="web.gui.Index" 
                            defaultloginfactory="web.gui.login.Login" />
        </div>

        <div id="footer">
            <span id="disclaimer">
                <span class="disclaimerText">© <a href="http://unlitrodeluzcolombia.org"> Litro de Luz Colombia</a> - <%=ComtorGlobal.CURRENT_YEAR%></span>
            </span>
        </div>

        <comtor:jsprj></comtor:jsprj>
        <script>
            function showMenu() {
                var sysMenu = $("#sysMenu");

                if (sysMenu.hasClass("responsive")) {
                    sysMenu.removeClass("responsive");
                } else {
                    sysMenu.addClass("responsive");
                }
            }
        </script>
    </body>
</html>
