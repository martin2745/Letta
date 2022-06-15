package es.uvigo.esei.letta.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uvigo.esei.letta.dao.DAOException;
import es.uvigo.esei.letta.dao.SendEventDAO;
import es.uvigo.esei.letta.entities.SendEvent;

/**
 * REST resource for managing SendEvents.
 *
 * @author Josemi & Ana
 */
@Path("/sendEvents")
@Produces(MediaType.APPLICATION_JSON)
public class SendEventResource {
    private final static Logger LOG = Logger.getLogger(SendEventResource.class.getName());

    private final SendEventDAO dao;

    /**
     * Constructs a new instance of {@link SendEventResource}.
     */
    public SendEventResource() {
        this(new SendEventDAO());
    }

    // Needed for testing purposes
    SendEventResource(SendEventDAO dao) {
        this.dao = dao;
    }

    /**
     * Returns a send event with the provided identifier.
     *
     * @return a 200 OK response with a send event that has the provided identifier.
     * If the identifier does not correspond with any send event, a 400 Bad
     * Request
     * response with an error message will be returned. If an error happens
     * while retrieving the list, a 500 Internal Server Error response with
     * an
     * error message will be returned.
     */
    @GET
    @Path("/{rrpp_id}&{event_id}&{destiny_id}")
    public Response get(@PathParam("rrpp_id") int rrpp_id, @PathParam("event_id") int event_id, @PathParam("destiny_id") int destiny_id) {
        try {
            final SendEvent event = this.dao.get(rrpp_id, event_id, destiny_id);

            return Response.ok(event).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid sendEvent id in get method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error getting a SendEvent", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Returns the complete list of send events received by destiny stored in the system.
     *
     * @return a 200 OK response with the complete list of send events stored in the
     * system. If an error happens while retrieving the list, a 500 Internal
     * Server Error response with an error message will be returned.
     */
    @GET
    @Path("/{destiny_id}")
    public Response list(@PathParam("destiny_id") int destiny_id) {
        try {
            return Response.ok(this.dao.list(destiny_id)).build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error listing event", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * Creates a new send event in the system.
     *
     * @param rrpp_id id of the public relations.
     * @param event_id id of the event.
     * @param destiny_id id of the receiver.
     * @return a 200 OK response with a send event that has been created. If the
     *         params are not provided, a 400 Bad Request response with
     *         an error message will be returned. If an error happens while retrieving
     *         the list, a 500 Internal Server Error response with an error message will
     *         be returned.
     */
    @POST
    public Response add(
            @FormParam("rrpp_id") int rrpp_id,
            @FormParam("event_id") int event_id,
            @FormParam("destiny_id") int destiny_id) {
        try {
            final SendEvent newSendEvent = this.dao.add(rrpp_id, event_id, destiny_id);

            return Response.ok(newSendEvent).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid sent event id in add method", iae);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error adding a sent event", e);
            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a send event from the system.
     *
     * @param event_id the identifier of the event to be deleted.
     * @return a 200 OK response with the identifier of the send event that has
     *         been deleted. If the identifier does not correspond with any event, a
     *         400
     *         Bad Request response with an error message will be returned. If an
     *         error
     *         happens while retrieving the list, a 500 Internal Server Error
     *         response
     *         with an error message will be returned.
     */
    @DELETE
    @Path("/{event_id}&{person_id}")
    public Response delete(
            @PathParam("event_id") int event_id,
            @PathParam("person_id") int destiny_id) {
        try {
            this.dao.delete(event_id,destiny_id);

            return Response.ok(event_id).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid event id in delete method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error deleting a event", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }
}
