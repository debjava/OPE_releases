package com.dnb.common.session;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * This filter performs some basic checks on the user's session before allowing
 * access to a URL.  
 * The filter performs some basic checks on the user's SessionDataHandler object,
 * checking for a valid user object & the current/view Enterprise objects.
 * 
 */
public final class UserSessionFilter implements Filter
{
    /** The logger for logging messages */
    private static Logger logger = Logger.getLogger(UserSessionFilter.class);

    /** Name of action to which this filter requires rights */
    private FilterConfig config;

    /**
     * Get the minimum Role required for accessing the admin application from
     * the init param configured in the web.xml of the application.
     * 
     * @param filterConfig The filter configurations such as initParams etc.
     */
    public void init(FilterConfig filterConfig)
    {
        this.config = filterConfig;
    }

    /**
     * This is the method called by the container to intercept all requests.
     * 
     * Checks if the current user has the rights to access this application. The
     * role of the user is obtained from the users session and is checked
     * against the configured action name via SecurityManager.  If the user has
     * the sufficient privileges he is allowed access into the application, else
     * he is forwarded to the system error page
     * 
     * @param req The servlet request object passed from the
     *            container
     * @param resp The servlet response object passed from the container
     * @param chain The chain of components to be executed
     * @throws IOException If an error occurred during executing a servlet
     *             method
     * @throws ServletException If an error occurred during executing a servlet
     *             method
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException
    {
		String user=null,branch=null,dateFormat=null;
		boolean action=true;

        // Check for session validity
		HttpSession statementSession = ((HttpServletRequest)req).getSession();
        SessionDataHandler sdh = SessionDataHandler.getSessionDataHandler(statementSession);
       		
        String path=((HttpServletRequest)req).getServletPath();
		String excludePath = config.getInitParameter("exclude");
		
		//logger.debug("PATH : "+path);

		// Setting charset Encoding to UTF-8

		HttpServletRequest request = (HttpServletRequest) req;		
		request.setCharacterEncoding("UTF-8");
		
		// Setting charset for Response from server.
		resp.setCharacterEncoding("UTF-8");
		
	   
		if(excludePath.length()>0)
		{
			StringTokenizer token = new StringTokenizer(excludePath,",");
		
			while (token.hasMoreTokens()) 
			{
				String tokString = token.nextToken();
				
				if(path.equals(tokString) || path.indexOf(tokString)!=-1 || path.trim().length()==0)
				{
					chain.doFilter(req, resp);
					action = false;
				}
			}
		}
		if(action)
		{
			if (sdh == null)
		    {
			    logger.debug("No session data - probably session timeout");
				req.getRequestDispatcher("../../common/jsp/SessionTimeout.html").forward(req, resp);
			}
			else
	        {
		        // We get the user & org objects just to provide some standard
			    // session validation
				user = sdh.getCurrentUser();
	            branch = sdh.getBranch();
				dateFormat = sdh.getDateFormat();
				List lst = sdh.getRoleList();
			
	            if ((user == null)  || (dateFormat == null) || (lst == null))
		        {
	            	req.getRequestDispatcher("../../common/jsp/SessionTimeout.html").forward(req, resp);
	            }
		    }
		
			if ( user != null)
	        {
		        chain.doFilter(req, resp);        
			}
		}
    }

    /**
     * Called by the container when unloading the filter.
     */
    public void destroy()
    {
		this.config = null;
    }
}
