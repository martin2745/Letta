package es.uvigo.esei.letta.dao;

import es.uvigo.esei.letta.entities.Event;
import es.uvigo.esei.letta.entities.SendEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO class for the {@link SendEvent} entities.
 *
 * @author Josemi
 */
public class SendEventDAO extends DAO {
    private final static Logger LOG = Logger.getLogger(SendEventDAO.class.getName());

    /**
     * Returns a send event stored persisted in the system.
     *
     * @param rrpp_id    identifier of the public relations.
     * @param event_id   identifier of the event.
     * @param destiny_id identifier of the person that receives the event.
     * @return send event with the provided identifier.
     * @throws DAOException             if an error happens while retrieving the
     *                                  event.
     * @throws IllegalArgumentException if the provided id does not correspond
     *                                  with any persisted sent event.
     */
    public SendEvent get(int rrpp_id, int event_id, int destiny_id)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT * FROM send_events WHERE rrpp_id=? & event_id=? & destiny_id=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, rrpp_id);
                statement.setInt(2, event_id);
                statement.setInt(3, destiny_id);

                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return rowToEntity(result);
                    } else {
                        throw new IllegalArgumentException("Invalid id");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error getting a sendEvent", e);
            throw new DAOException(e);
        }
    }

    /**
     * Returns a list with all the send events for a destiny person persisted in the system.
     *
     * @param destiny_id id of the destiny person who receives the event
     *
     * @return a list with all the send events for a destiny person persisted in the system.
     * @throws DAOException if an error happens while retrieving the events.
     */
    public List<Event> list(int destiny_id) throws DAOException {
        try (final Connection conn = this.getConnection()) {
            final String query = "SELECT e.event_id, event_location, event_name, event_description, event_date, organizer_id FROM events e, send_events se WHERE destiny_id=? AND e.event_id = se.event_id";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, destiny_id);
                try (final ResultSet result = statement.executeQuery()) {
                    final List<Event> Events = new LinkedList<>();

                    while (result.next()) {
                        Events.add(rowToEntityEvent(result));
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
     * Persists a new send event in the system. An identifier will be assigned
     * automatically to the new person.
     *
     * @param rrpp_id    identifier of the public relations.
     * @param event_id   identifier of the event.
     * @param destiny_id identifier of the person that receives the event.
     * @return an entity representing the persisted sent event.
     * @throws DAOException             if an error happens while persisting the new
     *                                  send event.
     * @throws IllegalArgumentException if the ids are {@code null}.
     */
    public SendEvent add(int rrpp_id, int event_id, int destiny_id)
            throws Exception {
        if (rrpp_id == 0 || event_id == 0 || destiny_id == 0) {
            throw new IllegalArgumentException("rrpp_id, event_id, destiny_id can't be null");
        }
        //Comprueba si el evento ya se ha mandado a la persona
        String sql = "SELECT rrpp_id FROM send_events WHERE  event_id = ? AND destiny_id = ?";
        boolean check;
        try (PreparedStatement stmt = this.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, event_id);
            stmt.setInt(2, destiny_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    check = false;
                }
                else{
                    check = true;
                }
            }
        }
        catch (SQLException e) {
            throw new IllegalStateException(sql, e);
        }
        if(check){
            throw new Exception("The event was already sent to that user");
        }
        try (Connection conn = this.getConnection()) {
            final String query = "INSERT INTO send_events VALUES(?, ?, ?)";

            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, rrpp_id);
                statement.setInt(2, event_id);
                statement.setInt(3, destiny_id);

                if (statement.executeUpdate() == 1) {
                    return new SendEvent(rrpp_id, event_id, destiny_id);
                } else {
                    LOG.log(Level.SEVERE, "Error inserting value");
                    throw new SQLException("Error inserting value");
                }
            }
        } catch (
                SQLException e) {
            LOG.log(Level.SEVERE, "Error adding a SendEvent", e);
            throw new DAOException(e);
        }

    }

    /**
     * Removes a persisted send event from the system.
     *
     * @param event_id identifier of the event to be deleted.
     * @param destiny_id identifier of the person who receives the event.
     * @throws DAOException             if an error happens while deleting the
     *                                  send event.
     * @throws IllegalArgumentException if the provided id does not correspond
     *                                  with any persisted send event.
     */

    public void delete(int event_id, int destiny_id)
            throws DAOException, IllegalArgumentException {
        try (final Connection conn = this.getConnection()) {
            final String query = "DELETE FROM send_events WHERE event_id=? AND destiny_id=?";

            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, event_id);
                statement.setInt(2, destiny_id);

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException("Invalid id");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error deleting a Event", e);
            throw new DAOException(e);
        }
    }

    private SendEvent rowToEntity(ResultSet row) throws SQLException {
        return new SendEvent(
                row.getInt("rrpp_id"),
                row.getInt("event_id"),
                row.getInt("destiny_id"));
    }

    private Event rowToEntityEvent(ResultSet row) throws SQLException {
        return new Event(
                row.getInt("event_id"),
                row.getString("event_location"),
                row.getString("event_name"),
                row.getString("event_description"),
                row.getString("event_date"),
                row.getInt("organizer_id"));
    }
}