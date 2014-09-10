import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HW8S extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>GET Request. No Form Data Posted</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse res)
    throws IOException, ServletException
    {
		doGet(request,response);
    }
}