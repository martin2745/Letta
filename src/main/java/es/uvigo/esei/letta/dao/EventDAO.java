package es.uvigo.esei.letta.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.letta.entities.Event;

/**
 * DAO class for the {@link Event} entities.
 * 
 * @author Daniel
 *
 */
public class EventDAO extends DAO {
    private final static Logger LOG = Logger.getLogger(EventDAO.class.getName());

    /**
     * Returns an event stored persisted in the system.
     * 
     * @param event_id identifier of the event.
     * @return an event with the provided identifier.
     * @throws DAOException             if an error happens while retrieving the
     *                                  event.
     * @throws IllegalArgumentException if the provided id does not correspond
     *                                  with any persisted event.
     */
    public Event get(int event_id)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM events WHERE event_id=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, event_id);

                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return rowToEntity(result);
                    } else {
                        throw new IllegalArgumentException("Invalid id");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error getting an event", e);
            throw new DAOException(e);
        }
    }

    /**
     * Returns a list with all the events persisted in the system.
     *
     * @param id_persona id of the organizer of the events
     *
     * @return a list with all the events from an organizer persisted in the system.
     * @throws DAOException if an error happens while retrieving the events.
     */
    public List<Event> list(int id_persona) throws DAOException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM events e WHERE e.organizer_id = ?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id_persona);
                try (final ResultSet result = statement.executeQuery()) {
                    final List<Event> Events = new LinkedList<>();

                    while (result.next()) {
                        Events.add(rowToEntity(result));
                    }

                    return Events;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error listing Events", e);
            throw new DAOException(e);
        }
    }

    /**
     * Returns a list with all the events persisted in the system.
     *
     * @param person_id id of the person not subscribed for the events
     *
     * @return a list with all the events from a not subscribed person persisted in the system.
     * @throws DAOException if an error happens while retrieving the events.
     */
    public List<Event> listAll(int person_id) throws DAOException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM events e WHERE e.event_id NOT IN (SELECT pe.event_id FROM people_events pe WHERE pe.person_id = ?)";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, person_id);
                try (final ResultSet result = statement.executeQuery()) {
                    final List<Event> Events = new LinkedList<>();
                    while (result.next()) {
                        Events.add(rowToEntity(result));
                    }
                    return Events;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error listing Events", e);
            throw new DAOException(e);
        }
    }

    /**
     * Returns a list with all the events persisted in the system.
     *
     * @param person_id id of the person subscribed for the events
     *
     * @return a list with all the events from a subscribed person persisted in the system.
     * @throws DAOException if an error happens while retrieving the events.
     */
    public List<Event> listAllSubs(int person_id) throws DAOException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM events e WHERE e.event_id IN (SELECT pe.event_id FROM people_events pe WHERE pe.person_id = ?)";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, person_id);
                try (final ResultSet result = statement.executeQuery()) {
                    final List<Event> Events = new LinkedList<>();
                    while (result.next()) {
                        Events.add(rowToEntity(result));
                    }
                    return Events;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error listing Events", e);
            throw new DAOException(e);
        }
    }

    /**
     * Persists a new event in the system. An identifier will be assigned
     * automatically to the new event.
     * 
     * @param ubicacion location of the new event. Can't be {@code null}.
     * @param nombre name of the new event. Can't be {@code null}.
     * @param descripcion description of the new event. Can't be {@code null}.
     * @param date date of the new event. Can't be {@code null}.
     * @param id_persona organizer of the new event. Can't be {@code null}.
     * @return a {@link Event} entity representing the persisted event.
     * @throws DAOException             if an error happens while persisting the new
     *                                  event.
     * @throws IllegalArgumentException if the name,description,location,organizer_id or date are {@code null}.
     */
    public Event add(String ubicacion, String nombre, String descripcion, String date, int id_persona, String image)
            throws DAOException, IllegalArgumentException {
        if (ubicacion == null || nombre == null || descripcion == null || date == null || id_persona == 0) {
            throw new IllegalArgumentException("ubicacion, nombre, descripcion, date and id_persona can't be null");
        }
        try (Connection conn = this.getConnection()) {
            final String query = "INSERT INTO events VALUES(null, ?, ?, ?, ?, ?,?)";

            try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, ubicacion);
                statement.setString(2, nombre);
                statement.setString(3, descripcion);
                statement.setString(4, date);
                statement.setInt(5, id_persona);
                statement.setString(6, image);

                if (statement.executeUpdate() == 1) {
                    try (ResultSet resultKeys = statement.getGeneratedKeys()) {
                        if (resultKeys.next()) {
                            return new Event(resultKeys.getInt(1), ubicacion, nombre, descripcion, date, id_persona);
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
            LOG.log(Level.SEVERE, "Error adding a Event", e);
            throw new DAOException(e);
        }
    }

    /**
     * Search an event in the system.
     *
     * @param ubicacion location of the new event.
     * @param nombre name of the new event.
     * @param descripcion description of the new event.
     * @param date date of the new event.
     * @return a list with all the events from the search query.
     * @throws DAOException             if an error happens while searching the
     *                                  event.
     * @throws IllegalArgumentException if the name,description,location,organizer_id or date are not valid.
     */

    public List<Event> search(String ubicacion, String nombre, String descripcion, String date)
            throws DAOException, IllegalArgumentException {
        try (Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM events WHERE event_location LIKE ? AND event_name LIKE ? AND event_description LIKE ? AND event_date LIKE ?";
            final String ubicacion_busqueda = "%" + ubicacion + "%";
            final String nombre_busqueda = "%" + nombre + "%";
            final String descripcion_busqueda = "%" + descripcion + "%";
            final String date_busqueda = "%" + date + "%";
            try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, ubicacion_busqueda);
                statement.setString(2, nombre_busqueda);
                statement.setString(3, descripcion_busqueda);
                statement.setString(4, date_busqueda);

                try (final ResultSet result = statement.executeQuery()) {
                    final List<Event> Events = new LinkedList<>();

                    while (result.next()) {
                        Events.add(rowToEntity(result));
                    }
                    return Events;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error searching a Event", e);
            throw new DAOException(e);
        }
    }

    /**
     * Subscribe a person to an event
     *
     * @param id_persona id of the person subscribing to the event. Can't be {@code null}.
     * @param event_id id of the event. Can't be {@code null}.
     * @throws DAOException             if an error happens while subscribing to the
     *                                  event.
     * @throws IllegalArgumentException if the name,description,location,organizer_id or date are not valid.
     */
    public String[] subscribe(int id_persona, int event_id)
            throws DAOException, IllegalArgumentException {
        if (id_persona == 0 || event_id == 0) {
            throw new IllegalArgumentException("id_persona and event_id can't be null");
        }
        try (Connection conn = this.getConnection()) {
            final String query = "INSERT INTO people_events VALUES(?, ?)";

            try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, id_persona);
                statement.setInt(2, event_id);

                if (statement.executeUpdate() != 1) {
                    LOG.log(Level.SEVERE, "Error subscribing");
                    throw new SQLException("Error subscribing");
                } else {
                    return new String[]{"event_id: " + event_id, "person_id: " + id_persona};
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error subscribing to an Event", e);
            throw new DAOException(e);
        }
    }

    /**
     * Unsubscribe a person to an event
     *
     * @param id_persona id of the person unsubscribing to the event. Can't be {@code null}.
     * @param event_id id of the event. Can't be {@code null}.
     * @throws DAOException             if an error happens while unsubscribing to the
     *                                  event.
     * @throws IllegalArgumentException if the name,description,location,organizer_id or date are not valid.
     */
    public String[] unsubscribe(int id_persona, int event_id)
            throws DAOException, IllegalArgumentException {
        if (id_persona == 0 || event_id == 0) {
            throw new IllegalArgumentException("id_persona and event_id can't be null");
        }
        try (Connection conn = this.getConnection()) {
            final String query = "DELETE FROM people_events WHERE person_id = ? AND event_id = ?";

            try (PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, id_persona);
                statement.setInt(2, event_id);

                if (statement.executeUpdate() != 1) {
                    LOG.log(Level.SEVERE, "Error unsubscribing");
                    throw new SQLException("Error unsubscribing");
                } else {
                    return new String[]{"event_id: " + event_id, "person_id: " + id_persona};
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error unsubscribing to an Event", e);
            throw new DAOException(e);
        }
    }

    /**
     * Modifies an event previously persisted in the system. The event will be
     * retrieved by the provided id.
     * 
     * @param Event a {@link Event} entity with the new data.
     * @throws DAOException             if an error happens while modifying the new
     *                                  event.
     * @throws IllegalArgumentException if the event is {@code null}.
     */
    public void modify(Event Event)
            throws DAOException, IllegalArgumentException {
        if (Event == null) {
            throw new IllegalArgumentException("Event can't be null");
        }

        try (Connection conn = this.getConnection()) {
            final String query = "UPDATE events SET event_location=?, event_name=?, event_description=?, event_date=? WHERE event_id=?";

            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, Event.getUbicacion());
                statement.setString(2, Event.getNombre());
                statement.setString(3, Event.getDescripcion());
                statement.setString(4, Event.getDate());
                statement.setInt(5, Event.getId());

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException(
                            "ubicacion, nombre, descripcion, date and id_persona can't be null");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error modifying a Event", e);
            throw new DAOException();
        }
    }

    /**
     * Removes a persisted event from the system.
     * 
     * @param id identifier of the event to be deleted.
     * @throws DAOException             if an error happens while deleting the
     *                                  event.
     * @throws IllegalArgumentException if the provided id does not correspond
     *                                  with any persisted event.
     */
    public void delete(int id)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "DELETE FROM events WHERE event_id=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("Invalid id");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error deleting a Event", e);
            throw new DAOException(e);
        }
    }

    private Event rowToEntity(ResultSet row) throws SQLException {
        return new Event(
                row.getInt("event_id"),
                row.getString("event_location"),
                row.getString("event_name"),
                row.getString("event_description"),
                row.getString("event_date"),
                row.getInt("organizer_id"));
    }
}