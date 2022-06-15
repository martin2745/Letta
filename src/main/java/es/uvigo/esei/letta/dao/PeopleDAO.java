package es.uvigo.esei.letta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.letta.entities.Person;

/**
 * DAO class for the {@link Person} entities.
 *
 * @author DRM
 */
public class PeopleDAO extends DAO {
    private final static Logger LOG = Logger.getLogger(PeopleDAO.class.getName());

    /**
     * Returns a person stored persisted in the system.
     *
     * @param id identifier of the person.
     * @return a person with the provided identifier.
     * @throws DAOException             if an error happens while retrieving the
     *                                  person.
     * @throws IllegalArgumentException if the provided id does not correspond
     *                                  with any persisted person.
     */
    public Person get(int id)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM people WHERE person_id=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);

                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return rowToEntity(result);
                    } else {
                        throw new IllegalArgumentException("Invalid id");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error getting a person", e);
            throw new DAOException(e);
        }
    }


    /**
     * Returns the role of a person stored persisted in the system.
     *
     * @param login login of the user.
     * @return role of the person with the provided login.
     * @throws DAOException             if an error happens while retrieving the
     *                                  person.
     * @throws IllegalArgumentException if the provided login does not correspond
     *                                  with any persisted person.
     */
    public Person getRole(String login)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM users join people p on users.login = p.login WHERE p.login=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, login);

                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return rowToEntityWithRole(result);
                    } else {
                        throw new IllegalArgumentException("Invalid id");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error getting a person", e);
            throw new DAOException(e);
        }
    }


    /**
     * Returns an id of a person stored persisted in the system.
     *
     * @param login of the person.
     * @return the id person with the provided login.
     * @throws DAOException             if an error happens while retrieving the
     *                                  person.
     * @throws IllegalArgumentException if the provided login does not correspond
     *                                  with any persisted person.
     */
    public Person getId(String login)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM people WHERE login=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, login);

                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return rowToEntity(result);
                    } else {
                        throw new IllegalArgumentException("Invalid login");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error getting a person", e);
            throw new DAOException(e);
        }
    }
    public Person recuperaId(String login)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM letta.people WHERE login=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, login);

                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return rowToEntity(result);
                    } else {
                        throw new IllegalArgumentException("Invalid login");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error getting a person", e);
            throw new DAOException(e);
        }
    }

    /**
     * Returns a list with all the people persisted in the system.
     *
     * @return a list with all the people persisted in the system.
     * @throws DAOException if an error happens while retrieving the people.
     */
    public List<Person> list() throws DAOException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM people";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                try (final ResultSet result = statement.executeQuery()) {
                    final List<Person> people = new LinkedList<>();

                    while (result.next()) {
                        people.add(rowToEntity(result));
                    }

                    return people;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error listing people", e);
            throw new DAOException(e);
        }
    }

    /**
     * Persists a new person in the system. An identifier will be assigned
     * automatically to the new person.
     *
     * @param name    name of the new person. Can't be {@code null}.
     * @param surname surname of the new person. Can't be {@code null}.
     * @param login login of the new person. Can't be {@code null}.
     * @return a {@link Person} entity representing the persisted person.
     * @throws DAOException             if an error happens while persisting the new
     *                                  person.
     * @throws IllegalArgumentException if the name or surname or login are {@code null}.
     */
    public Person add(String name, String surname, String login)
            throws DAOException, IllegalArgumentException {
        if (name == null || surname == null || login == null) {
            throw new IllegalArgumentException("name, surname and login can't be null");
        }

        try (Connection conn = this.getConnection()) {
            final String query = "INSERT INTO people VALUES(null, ?, ?, ?)";

            try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, name);
                statement.setString(2, surname);
                statement.setString(3, login);
                if (statement.executeUpdate() == 1) {
                    try (ResultSet resultKeys = statement.getGeneratedKeys()) {
                        if (resultKeys.next()) {
                            return new Person(resultKeys.getInt(1), name, surname, login);
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
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error adding a person", e);
            throw new DAOException(e);
        }
    }

    /**
     * Modifies a person previously persisted in the system. The person will be
     * retrieved by the provided id and its current name and surname and login will be
     * replaced with the provided.
     *
     * @param person a {@link Person} entity with the new data.
     * @throws DAOException             if an error happens while modifying the new
     *                                  person.
     * @throws IllegalArgumentException if the person is {@code null}.
     */
    public void modify(Person person)
            throws DAOException, IllegalArgumentException {
        if (person == null) {
            throw new IllegalArgumentException("person can't be null");
        }

        try (Connection conn = this.getConnection()) {
            final String query = "UPDATE people SET person_name=?, person_surname=?, login=?  WHERE person_id=?";

            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, person.getName());
                statement.setString(2, person.getSurname());
                statement.setString(3, person.getLogin());
                statement.setInt(4, person.getId());

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("name and surname can't be null");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error modifying a person", e);
            throw new DAOException();
        }
    }

    /**
     * Removes a persisted person from the system.
     *
     * @param id identifier of the person to be deleted.
     * @throws DAOException             if an error happens while deleting the
     *                                  person.
     * @throws IllegalArgumentException if the provided id does not correspond
     *                                  with any persisted person.
     */
    public void delete(int id, String login)
            throws DAOException, IllegalArgumentException {
        
        try (final Connection conn = this.getConnection()) {

            final String query = "DELETE FROM people WHERE person_id=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("Invalid id");
                }
            }
            
            final String queryUser = "DELETE FROM users WHERE login=?";

            try (final PreparedStatement statement = conn.prepareStatement(queryUser)) {
                statement.setString(1, login);

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("Invalid login");
                }
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error deleting a person", e);
            throw new DAOException(e);
        }
    }

    /**
     * Returns a person stored persisted in the system.
     *
     * @param login login of the person.
     * @return a person with the provided login.
     * @throws DAOException             if an error happens while retrieving the
     *                                  person.
     * @throws IllegalArgumentException if the provided id does not correspond
     *                                  with any persisted person.
     */
    public Person getData(String login)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM people WHERE login=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, login);

                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return rowToEntity(result);
                    } else {
                        throw new IllegalArgumentException("Invalid login");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error getting a person", e);
            throw new DAOException(e);
        }
    }

    private Person rowToEntity(ResultSet row) throws SQLException {
        return new Person(
                row.getInt("person_id"),
                row.getString("person_name"),
                row.getString("person_surname"),
                row.getString("login"));
    }

    private Person rowToEntityWithRole(ResultSet row) throws SQLException {
        return new Person(
                row.getInt("person_id"),
                row.getString("person_name"),
                row.getString("person_surname"),
                row.getString("login"),
                row.getString("role"));
    }
}
