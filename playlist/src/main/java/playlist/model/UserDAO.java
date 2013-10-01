package playlist.model;

import com.datastax.driver.core.Row;
import playlist.exceptions.UserExistsException;
import playlist.exceptions.UserLoginException;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: stevelowenthal
 * Date: 9/20/13
 * Time: 8:28 AM
 *
 */

public class UserDAO extends CassandraData {

  private String email;
  private String password;
  private UUID userid;
  private List<String> playlists;
  private Map<String,String> playlists_genre;

  UserDAO(Row row) {
    email = row.getString("email");
    password = row.getString("password");
    userid = row.getUUID("user_id");
    playlists = row.getList("playlists",String.class);
    playlists_genre = row.getMap("playlists_genre",String.class,String.class);
  }

  UserDAO(String email, String password, UUID userid) {
    this.userid = userid;
    this.password = password;
    this.email = email;
  }

  public static UserDAO addUser(String email, String password, ServletContext context) throws UserExistsException {

    // TODO Should read and write a quorum for this because of the unique requirement
    // TODO or better should use a transaction

    UUID userId = UUID.randomUUID();

    if (getUser(email, context) != null) {
      throw new UserExistsException();
    }


    String queryText = "INSERT INTO users (email, password, user_id) values ('"
            + email + "','"
            + password + "',"
            + userId + ")";

    getSession(context).execute(queryText);

    // Return the new user so the caller can get the userid
    return new UserDAO(email, password, userId);

  }

  public static void deleteUser(String email, ServletContext context) {
    String queryText = "DELETE FROM users where email = '"
            + email + "'";

    getSession(context).execute(queryText);

  }

  public static UserDAO getUser(String email, ServletContext context) {

    String queryText = "SELECT * FROM users where email = '"
            + email + "'";

    Row userRow = getSession(context).execute(queryText).one();

    if (userRow == null) {
      return null;
    }

    return new UserDAO(userRow);

  }

  public static UserDAO validateLogin (String email, String password, ServletContext context) throws UserLoginException {

    UserDAO user = getUser(email, context);
    if (user == null || !user.password.contentEquals(password)) {
      throw new UserLoginException();
    }

    return user;

  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public UUID getUserid() {
    return userid;
  }

  public List<String> getPlaylists() {
    return playlists;
  }

  public Map<String, String> getPlaylists_genre() {
    return playlists_genre;
  }


}
