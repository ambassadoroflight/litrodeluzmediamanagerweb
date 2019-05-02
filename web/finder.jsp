<%-- 
    COMTOR MEDIA MANAGER WEB
    Document   : index
    Created on : Jan 23, 2019, 16:45
    Author     : juandiego@comtor.net
--%>

<%@page contentType="text/html"  buffer="500kb" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="WEB-INF/comtortags.tld" prefix="comtor"%>
<!DOCTYPE html>
<html>
    <head>
        <%
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            String input_visible_name = request.getParameter("input_visible_name");
            String input_hidden_name = request.getParameter("input_hidden_name");
            String call_method = request.getParameter("call_method");
        %>

        <script type='text/javascript' language="JavaScript">
            function insert_title(title, value) {
                $(parent.opener.document).find('#<%=input_visible_name%>').val(title);
                $(parent.opener.document).find('#<%=input_hidden_name%>').val(value);

                parent.close();
            }

            function insert_values(title_visible, title_hidden) {
                var document1 = parent.opener.document;

                var <%= input_visible_name%> = document1.getElementById('<%= input_visible_name%>');
            <%= input_visible_name%>.value = title_visible;

                var <%= input_hidden_name%> = document1.getElementById('<%= input_hidden_name%>');
            <%= input_hidden_name%>.value = title_hidden;

            <%if ((call_method != null) && (call_method.trim().length() > 0)) {%>
                if (parent.opener.<%=call_method%>) {
                    parent.opener.<%=call_method%>;
                }
            <%}%>

                parent.close();
            }
        </script>

        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title><comtor:keytranslation key="finder.title"></comtor:keytranslation></title>
        <comtor:cssmain/>
        <comtor:jsfinder/>
    </head>

    <body>
        <comtor:ifsessionexists>
        </comtor:ifsessionexists>
        <comtor:content defaultpagefactory="net.comtor.framework.pagefactory.finder.CodeFinderFactory"
                        defaultloginfactory="net.comtor.framework.pagefactory.finder.DefaultFinderFactoryI18n" />
    </body>
</html>
