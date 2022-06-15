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
import es.uvigo.esei.letta.dao.EventDAO;
import es.uvigo.esei.letta.entities.Event;

/**
 * REST resource for managing Events.
 * 
 * @author Daniel
 */
@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {
    private final static Logger LOG = Logger.getLogger(EventResource.class.getName());

    private final EventDAO dao;

    /**
     * Constructs a new instance of {@link EventResource}.
     */
    public EventResource() {
        this(new EventDAO());
    }

    // Needed for testing purposes
    EventResource(EventDAO dao) {
        this.dao = dao;
    }

    /**
     * Returns an event with the provided identifier.
     * 
     * @param event_id the identifier of the event to retrieve.
     * @return a 200 OK response with an event that has the provided identifier.
     *         If the identifier does not correspond with any event, a 400 Bad
     *         Request
     *         response with an error message will be returned. If an error happens
     *         while retrieving the list, a 500 Internal Server Error response with
     *         an
     *         error message will be returned.
     */
    @GET
    @Path("/{event_id}")
    public Response get(@PathParam("event_id") int event_id) {
        try {
            final Event event = this.dao.get(event_id);

            return Response.ok(event).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid event id in get method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error getting a event", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Returns the complete list of events from organizer stored in the system.
     * 
     * @return a 200 OK response with the complete list of events stored in the
     *         system. If an error happens while retrieving the list, a 500 Internal
     *         Server Error response with an error message will be returned.
     */
    @GET
    @Path("/person/{id_persona}")
    public Response list(@PathParam("id_persona") int id_persona) {
        try {
            return Response.ok(this.dao.list(id_persona)).build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error listing event", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * Returns the complete list of events from a person not subscribed stored in the system.
     *
     * @return a 200 OK response with the complete list of events stored in the
     *         system. If an error happens while retrieving the list, a 500 Internal
     *         Server Error response with an error message will be returned.
     */
    @GET
    @Path("/listEvents/{person_id}")
    public Response listAll(
            @PathParam("person_id") int person_id
    ) {
        try {
            return Response.ok(this.dao.listAll(person_id)).build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error listing event", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * Returns the complete list of events from a person subscribed stored in the system.
     *
     * @return a 200 OK response with the complete list of events stored in the
     *         system. If an error happens while retrieving the list, a 500 Internal
     *         Server Error response with an error message will be returned.
     */
    @GET
    @Path("/listSubsEvents/{person_id}")
    public Response listAllSubs(
            @PathParam("person_id") int person_id
    ) {
        try {
            return Response.ok(this.dao.listAllSubs(person_id)).build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error listing event", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * Returns the complete list of events from the search query stored in the system.
     *
     * @return a 200 OK response with the complete list of events stored in the
     *         system. If an error happens while retrieving the list, a 500 Internal
     *         Server Error response with an error message will be returned.
     */
    @POST
    @Path("/searchEvents/")
    public Response search(
            @FormParam("ubicacion") String ubicacion,
            @FormParam("nombre") String nombre,
            @FormParam("descripcion") String descripcion,
            @FormParam("date") String date) {
        try {
            return Response.ok(this.dao.search(ubicacion, nombre, descripcion, date)).build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error searching event", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * Returns the event from a person subscribing to it stored in the system.
     *
     * @return a 200 OK response with the complete list of events stored in the
     *         system. If an error happens while retrieving the list, a 500 Internal
     *         Server Error response with an error message will be returned.
     */
    @GET
    @Path("/person/subscribe/{id_persona}/{event_id}")
    public Response subscribe(@PathParam("id_persona") int id_persona, @PathParam("event_id") int event_id) {
        try {
            return Response.ok(this.dao.subscribe(id_persona, event_id)).build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error subscribing to an event", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    /**
     * Returns the event from a person unsubscribing to it stored in the system.
     *
     * @return a 200 OK response with the complete list of events stored in the
     *         system. If an error happens while retrieving the list, a 500 Internal
     *         Server Error response with an error message will be returned.
     */
    @GET
    @Path("/person/unsubscribe/{id_persona}/{event_id}")
    public Response unsubscribe(@PathParam("id_persona") int id_persona, @PathParam("event_id") int event_id) {
        try {
            return Response.ok(this.dao.unsubscribe(id_persona, event_id)).build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error unsubscribing to an event", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * Creates a new event in the system.
     * 
     * @param ubicacion the location of the new event.
     * @param nombre the name of the new event.
     * @param descripcion the descripcion of the new event
     * @param date the date of the new event
     * @param id_persona the organizer of the new event
     * @return a 200 OK response with an event that has been created. If the
     *         params are not provided, a 400 Bad Request response with
     *         an error message will be returned. If an error happens while retrieving
     *         the list, a 500 Internal Server Error response with an error message will
     *         be returned.
     */
    @POST
    public Response add(
            @FormParam("ubicacion") String ubicacion,
            @FormParam("nombre") String nombre,
            @FormParam("descripcion") String descripcion,
            @FormParam("date") String date,
            @FormParam("id_persona") int id_persona,
            @FormParam("formFile") String image){
        try {
            final Event newEvent = this.dao.add(ubicacion, nombre, descripcion, date, id_persona,image);

            return Response.ok(newEvent).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid event id in add method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error adding a event", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Modifies the data of an event.
     * 
     * @param id      identifier of the event to modify.
     * @param ubicacion    the new location of the event.
     * @param nombre the new name of the event.
     * @param descripcion the new description of the event.
     * @param date the new date of the event.
     * @param id_persona the new organizer of the event.
     * @return a 200 OK response with an event that has been modified. If the
     *         identifier does not correspond with any event or the params
     *         are
     *         not provided, a 400 Bad Request response with an error message will
     *         be
     *         returned. If an error happens while retrieving the list, a 500
     *         Internal
     *         Server Error response with an error message will be returned.
     */
    @PUT
    @Path("/{id}")
    public Response modify(
            @PathParam("id") int id,
            @FormParam("ubicacion") String ubicacion,
            @FormParam("nombre") String nombre,
            @FormParam("descripcion") String descripcion,
            @FormParam("date") String date,
            @FormParam("id_persona") int id_persona) {
        try {
            final Event modifiedEvent = new Event(id, ubicacion, nombre, descripcion, date, id_persona);
            this.dao.modify(modifiedEvent);

            return Response.ok(modifiedEvent).build();
        } catch (NullPointerException npe) {
            final String message = String.format(
                    "Invalid data for event (ubicacion: %s, nombre: %s, descripcion: %s, date: %s, id_persona: %s)",
                    ubicacion,
                    nombre, descripcion, date, id_persona);

            LOG.log(Level.FINE, message);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message)
                    .build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid event id in modify method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error modifying a event", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Deletes an event from the system.
     * 
     * @param id the identifier of the event to be deleted.
     * @return a 200 OK response with the identifier of the event that has
     *         been deleted. If the identifier does not correspond with any event, a
     *         400
     *         Bad Request response with an error message will be returned. If an
     *         error
     *         happens while retrieving the list, a 500 Internal Server Error
     *         response
     *         with an error message will be returned.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(
            @PathParam("id") int id) {
        try {
            this.dao.delete(id);

            return Response.ok(id).build();
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
