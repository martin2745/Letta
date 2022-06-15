package es.uvigo.esei.letta.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.letta.entities.Person;
import es.uvigo.esei.letta.entities.User;
import java.util.LinkedList;
import java.util.List;

/**
 * DAO class for managing the users of the system.
 *
 * @author DRM
 */
public class UsersDAO extends DAO {

    private final static Logger LOG = Logger.getLogger(UsersDAO.class.getName());

    /**
     * Returns a user stored persisted in the system.
     *
     * @param login the login of the user to be retrieved.
     * @return a user with the provided login.
     * @throws DAOException if an error happens while retrieving the user.
     * @throws IllegalArgumentException if the provided login does not
     * corresponds with any persisted user.
     */
    public User get(String login) throws DAOException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM users WHERE login=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, login);

                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return rowToEntity(result);
                    } else {
                        throw new IllegalArgumentException("Invalid id");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error checking login", e);
            throw new DAOException(e);
        }
    }

    public Object add(String name, String surname, String login, String password, String role) throws DAOException, SQLException {
        try (final Connection conn = this.getConnection()) {
            final String query = "INSERT INTO users VALUES(?, ?, ?)";
            final String query2 = "INSERT INTO people VALUES(null,?, ?, ?)";

            try ( PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, login);
                statement.setString(2, encodeSha256(password));
                statement.setString(3, role);
                if (statement.executeUpdate() == 1) {
                    LOG.log(Level.FINE, "user inserted in user table");
                } else {
                    LOG.log(Level.SEVERE, "Error inserting user");
                    throw new SQLException("Error inserting user");
                }
            } catch (SQLException e) {
                LOG.log(Level.SEVERE, "Error adding an user", e);
                throw new DAOException(e);
            }
            try ( PreparedStatement statement2 = conn.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS)) {
                statement2.setString(1, name);
                statement2.setString(2, surname);
                statement2.setString(3, login);
                if (statement2.executeUpdate() == 1) {
                    try ( ResultSet resultKeys = statement2.getGeneratedKeys()) {
                        if (resultKeys.next()) {
                            return new String[]{"name: " + name, "surname: " + surname, "login: " + login, "password: " + password, "role: " + role};
                        } else {
                            LOG.log(Level.SEVERE, "Error retrieving inserted id");
                            throw new SQLException("Error retrieving inserted id");
                        }
                    }
                } else {
                    LOG.log(Level.SEVERE, "Error inserting value");
                    throw new SQLException("Error inserting value");
                }

            }
        }
    }

    public User addGestion(String login, String password, String role)
            throws DAOException, IllegalArgumentException {
        if (login == null || password == null || role == null) {
            throw new IllegalArgumentException("login,password and role can't be null");
        }

        try ( Connection conn = this.getConnection()) {
            final String query = "INSERT INTO users VALUES(?, ?, ?)";

            try ( PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, login);
                statement.setString(2, password);
                statement.setString(3, role);
                if (statement.executeUpdate() == 1) {

                    return new User(login, password, role);

                } else {
                    LOG.log(Level.SEVERE, "Error inserting value");
                    throw new SQLException("Error inserting value");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error adding a user", e);
            throw new DAOException(e);
        }
    }

    /* try ( Connection conn = this.getConnection()) {
            final String query = "UPDATE users SET password=?, role=?  WHERE login=?";
            final String queryPass = "SELECT * FROM users WHERE login=?";

            try (PreparedStatement statement = conn.prepareStatement(queryPass)) {
                try (final ResultSet result = statement.executeQuery()) {
                    statement.setString(1, user.getLogin());
                    String resultado = result.getString(queryPass);

                    System.out.println("\n\n\n\naaaaaaaaaaaaaaaaaa" + resultado +"\n\n\n");
                    

                }
            }

            try ( PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(3, user.getLogin());
                statement.setString(1, user.getPassword());
                statement.setString(2, user.getRole());

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("password and role can't be null");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error modifying a person", e);
            throw new DAOException();
        }*/
    public void modify(User user)
            throws DAOException, IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("user can't be null");
        }
        String password;
        try (final Connection conn = this.getConnection()) {

            final String query = "SELECT * FROM users WHERE login=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, user.getLogin());
                ResultSet res = statement.executeQuery();

                res.next();
                String pass = res.getString("password");
                if (user.getPassword() != pass) {
                    password = encodeSha256(user.getPassword());
                } else {
                    password = user.getPassword();
                }

            }

            final String queryUpdate = "UPDATE users SET password=?, role=?  WHERE login=?";

            try ( PreparedStatement statement = conn.prepareStatement(queryUpdate)) {
                statement.setString(3, user.getLogin());
                statement.setString(1, password);
                statement.setString(2, user.getRole());

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("password and role can't be null");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error deleting a person", e);
            throw new DAOException(e);
        }

    }

    public void delete(String login)
            throws DAOException, IllegalArgumentException {

        try (final Connection conn = this.getConnection()) {
            final String query = "DELETE FROM users WHERE login=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, login);

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("Invalid login");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error deleting a user", e);
            throw new DAOException(e);
        }
    }

    public List<User> list() throws DAOException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM users";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                try (final ResultSet result = statement.executeQuery()) {
                    final List<User> users = new LinkedList<>();

                    while (result.next()) {
                        users.add(rowToEntity(result));
                    }

                    return users;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error listing users", e);
            throw new DAOException(e);
        }
    }

    /**
     * Checks if the provided credentials (login and password) correspond with a
     * valid user registered in the system.
     *
     * <p>
     * The password is stored in the system "salted" and encoded with the
     * SHA-256 algorithm.
     * </p>
     *
     * @param login the login of the user.
     * @param password the password of the user.
     * @return {@code true} if the credentials are valid. {@code false}
     * otherwise.
     * @throws DAOException if an error happens while checking the credentials.
     */
    public boolean checkLogin(String login, String password) throws DAOException {
        try {
            final User user = this.get(login);

            final String dbPassword = user.getPassword();
            final String shaPassword = encodeSha256(password);

            return shaPassword.equals(dbPassword);
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    private final static String encodeSha256(String text) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] digested = digest.digest(text.getBytes());

            return hexToString(digested);
        } catch (NoSuchAlgorithmException e) {
            LOG.log(Level.SEVERE, "SHA-256 not supported", e);
            throw new RuntimeException(e);
        }
    }

    private final static String hexToString(byte[] hex) {
        final StringBuilder sb = new StringBuilder();

        for (byte b : hex) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
    }

    private User rowToEntity(ResultSet result) throws SQLException {
        return new User(
                result.getString("login"),
                result.getString("password"),
                result.getString("role"));
    }
}
