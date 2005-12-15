<%@page
contentType="text/html; charset=windows-1251"
language="java"
import="mill.db.DBconnect,
        mill.a3.AuthSession,
        mill.tools.TransferFile,
        mill.tools.ExceptionTools,
        org.apache.log4j.Category"

%><%!
/**
 *  $Revision$ $Date$
 *
 */
    private static Category cat = Category.getInstance( "member-sa-upload-upload.jsp");

%>
<%
    DBconnect dbDyn = DBconnect.getInstance( true );
    try
    {
        AuthSession auth_ = AuthSession.check(request, response);
        if ( auth_==null )
            return;

        if( auth_.getRight("SYSADMIN","UPLOAD_FILE_X509", "A") )
        {

            try
            {
                TransferFile.processData(request);
                out.println( "Upload data is successfull<br>");
            }
            catch(Exception e)
            {
                cat .error("Error upload signed file.", e);
                out.println( "Error uploading file<br>"+
                        ExceptionTools.getStackTrace(e, 30, "<br>")
                );
            }
        }

    }
    finally
    {
        if (dbDyn != null)
        {
            DBconnect.close( dbDyn );
            dbDyn = null;
        }
    }

%>
