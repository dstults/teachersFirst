package edu.lwtech.csd297.skeleton;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.logging.log4j.*;
import freemarker.template.*;

import edu.lwtech.csd297.skeleton.daos.*;
import edu.lwtech.csd297.skeleton.pojos.*;

@WebServlet(name = "skeleton", urlPatterns = {"/*"}, loadOnStartup = 0)
public class SkeletonServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;        // Unused
    private static final Logger logger = LogManager.getLogger(SkeletonServlet.class);

    private static final String SERVLET_NAME = "skeleton";
    private static final String RESOURCES_DIR = "/WEB-INF/classes";
    private static final Configuration freeMarkerConfig = new Configuration(Configuration.getVersion());

    private DAO<Skeleton> skeletonDAO = null;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        logger.warn("");
        logger.warn("===========================================================");
        logger.warn("          " + SERVLET_NAME + " init() started");
        logger.warn("               http://<team-server>");
        logger.warn("===========================================================");
        logger.warn("");

        String resourcesDir = config.getServletContext().getRealPath(RESOURCES_DIR);
        logger.info("resourcesDir = {}", resourcesDir);

        logger.info("Initializing FreeMarker...");
        String templateDir = resourcesDir + "/templates";
        try {
            freeMarkerConfig.setDirectoryForTemplateLoading(new File(templateDir));
        } catch (IOException e) {
            String msg = "Template directory not found: " + templateDir;
            logger.fatal(msg, e);
            throw new UnavailableException(msg);
        }
        logger.info("Successfully initialized FreeMarker");

        logger.info("Initializing the DAOs...");
        skeletonDAO = new SkeletonMemoryDAO();

        if (!skeletonDAO.initialize(""))
            throw new UnavailableException("Unable to initialize the SkeletonDAO.");
        logger.info("Successfully initialized the DAOs!");

        logger.warn("");
        logger.warn("Servlet initialization complete!");
        logger.warn("");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();

        String logInfo = request.getRemoteAddr() + " " + request.getMethod() + " " + request.getRequestURI();
        logInfo += getSanitizedQueryString(request);

        // Get the cmd parameter from the URI (defaults to 'home')
        String command = request.getParameter("cmd");
        if (command == null)
            command = "home";
        if (command != "health")                        // Don't log "health" commands
            logger.debug("IN - {}", logInfo);

        try {
            // Initialize the variables that control Freemarker's output
            // These should be changed to appropriate values inside of the corresponding case statement below
            String templateName = null;
            Map<String, Object> templateDataMap = new HashMap<>();

            // Process the GET command
            switch (command) {
                case "home":
                    List<Skeleton> skeletons = skeletonDAO.retrieveAll();
                    templateDataMap.put("skeletons", skeletons);
                    templateName = "home.ftl";
                    break;

                case "health":
                    try {
                        response.sendError(HttpServletResponse.SC_OK, "OK");
                    } catch (IOException e) {
                        logger.error("IO Error sending health response: ", e);
                    }
                    return;

                default:
                    command = sanitizedString(command);
                    logger.info("Unknown GET command received: {}", command);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
            }

            if (templateName == null) {
                // Send 404 error response
                try {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                    logger.error("Unable to send 404 response code.", e);
                }
                return;
            }

            // Have Freemarker merge the template and seond out the result
            processTemplate(response, templateName, templateDataMap);

        } catch (IOException e) {
            // Typically, this is because the connection was closed prematurely
            logger.debug("Unexpected I/O exception: ", e);
        } catch (RuntimeException e) {
            logger.error("Unexpected runtime exception: ", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Oh no! Something went wrong. The appropriate authorities have been alerted.");
            } catch (IOException ex) {
                logger.error("Unable to send 500 response code.", ex);
            }
        }
        long time = System.currentTimeMillis() - startTime;
        logger.info("OUT- {} {}ms", logInfo, time);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    @Override
    public void destroy() {
        skeletonDAO.terminate();
        logger.warn("-----------------------------------------");
        logger.warn("  " + SERVLET_NAME + " destroy() completed!");
        logger.warn("-----------------------------------------");
        logger.warn(" ");
    }

    @Override
    public String getServletInfo() {
        return "skeleton Servlet";
    }

    // =================================================================

    private static int parseInt(String s) {
        int i = -1;
        if (s != null) {
            try {
                i = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                i = -2;
            }
        }
        return i;
    }

    private static void processTemplate(HttpServletResponse response, String template, Map<String, Object> model) {
        logger.debug("Processing Template: " + template);

        try (PrintWriter out = response.getWriter()) {
            Template view = freeMarkerConfig.getTemplate(template);
            view.process(model, out);
        } catch (TemplateException | MalformedTemplateNameException e) {
            logger.error("Template Error: ", e);
        } catch (IOException e) {
            logger.error("IO Error: ", e);
        }
    }

    private String getSanitizedQueryString(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (queryString == null)
            return "";

        try { 
            queryString = URLDecoder.decode(queryString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Should never happen
            throw new IllegalStateException(e);
        }
        queryString = sanitizedString(queryString);
        return queryString;
    }

    private String sanitizedString(String s) {
        return s.replaceAll("[\n|\t]", "_");
    }

}
