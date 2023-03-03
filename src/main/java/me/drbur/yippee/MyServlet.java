package me.drbur.yippee;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String RESOURCE_PATH = "/static/build/index.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            // Open the resource file
            InputStream inputStream = getClass().getResourceAsStream(RESOURCE_PATH);

            // Check if the resource exists
            if (inputStream == null) {
                resp.setStatus(HttpStatus.NOT_FOUND_404);
                return;
            }

            // Set content type and length headers
            resp.setContentType("text/html");
            resp.setContentLength(inputStream.available());

            // Copy the resource file to the output stream
            OutputStream outputStream = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        // Handle HTTP POST requests
    }
}
