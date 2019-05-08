<%-- 
    Document   : about
    Created on : May 8, 2019, 8:46:50 AM
    Author     : juandiego
--%>

<%@page import="net.comtor.framework.global.ComtorGlobal"%>
<%@page import="web.global.GlobalWeb"%>
<%@page import="web.global.LitroDeLuzImages"%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>

<div id="about_window_container" onclick="closeAboutWindow2();" style="display: none;">
    <div id="about_window">
        <span id="about_close" onclick="closeAboutWindow2();">X</span>
        <img src="<%=LitroDeLuzImages.MEDIA_MANAGER_LOGO_LOGIN%>">
        <h1><%=GlobalWeb.PROJECT_NAME%></h1>
        <h2>Versión <%=GlobalWeb.VERSION%></h2>
        <h3><%=GlobalWeb.LAST_UPDATED%></h3>
        <span id="about_footer">© Litro de Luz Colombia - <%=ComtorGlobal.CURRENT_YEAR%></span>
    </div>
</div>

<script type="text/javascript">
    function showAboutWindow2() {
        $("#about_window_container").hide().fadeIn(500);
    }

    function closeAboutWindow2() {
        $("#about_window_container").fadeOut(500, function () {
            $(this).hide();
        });
    }
</script>