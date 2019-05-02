package org.unlitrodeluzcolombia.mediamanager.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.comtor.dao.ComtorDaoException;
import net.comtor.framework.request.validator.RequestBasicValidator;
import net.comtor.util.StringUtil;
import org.unlitrodeluzcolombia.mediamanager.element.Movie;
import org.unlitrodeluzcolombia.mediamanager.element.Song;
import org.unlitrodeluzcolombia.mediamanager.enums.FilePrefix;
import org.unlitrodeluzcolombia.mediamanager.facade.MovieDAOFacade;
import org.unlitrodeluzcolombia.mediamanager.facade.SongDAOFacade;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 22, 2019
 */
public class ImagesServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ImagesServlet.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ComtorDaoException {
        String code = RequestBasicValidator.getStringFromRequest(request, "code");

        if (!StringUtil.isValid(code)) {
            response.sendError(403, "Parámetro no válido");

            return;
        }

        String imageName = "";

        if (code.startsWith(FilePrefix.MOVIE.getCode())) {
            Movie movie = new MovieDAOFacade().findByProperty("code", code);

            imageName = movie.getPoster();
        } else if (code.startsWith(FilePrefix.MUSIC.getCode())) {
            Song movie = new SongDAOFacade().findByProperty("code", code);

            imageName = movie.getCover();
        }

        if (!StringUtil.isValid(imageName)) {
            LOG.log(Level.INFO, "Archivo " + imageName + " no encontrado");

            response.sendError(404, "Recurso no encontrado");

            return;
        }

        String ext = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();
        String mime = "image/" + ext;

        response.setContentType(mime);

        try (ServletOutputStream os = response.getOutputStream()) {
            File file = new File(GlobalWeb.IMAGES_DIRECTORY_PATH + File.separator + imageName);

            if (!file.exists()) {
                LOG.log(Level.INFO, "Archivo " + imageName + " no existe");

                response.sendError(404, "Recurso no encontrado");

                return;
            }

            byte[] bytes = Files.readAllBytes(file.toPath());

            os.write(bytes);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
