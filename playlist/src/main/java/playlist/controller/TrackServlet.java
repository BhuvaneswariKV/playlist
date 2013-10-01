package playlist.controller;

import playlist.model.TracksDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stevelowenthal
 * Date: 9/19/13
 * Time: 5:22 PM
 *
 */
public class TrackServlet extends HttpServlet {

 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String artist = request.getParameter("artist");

    List<TracksDAO> tracks = TracksDAO.listSongsByArtist(artist, getServletContext());

    request.setAttribute("artist", artist);
    request.setAttribute("tracks", tracks);
    getServletContext().getRequestDispatcher("/tracks.jsp").forward(request,response);

  }

}
