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
import es.uvigo.esei.letta.dao.PeopleDAO;
import es.uvigo.esei.letta.dao.UsersDAO;
import es.uvigo.esei.letta.entities.Person;
import es.uvigo.esei.letta.entities.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * REST resource for managing people.
 *
 * @author DRM
 */
@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
public class PeopleResource {

    private final static Logger LOG = Logger.getLogger(PeopleResource.class.getName());

    private final PeopleDAO dao;
    private final UsersDAO daoUser;

    /**
     * Constructs a new instance of {@link PeopleResource}.
     */
    public PeopleResource() {
        this(new PeopleDAO(), new UsersDAO());

    }

    // Needed for testing purposes
    PeopleResource(PeopleDAO dao, UsersDAO daoUser) {
        this.dao = dao;
        this.daoUser = daoUser;
    }

    private final static String hexToString(byte[] hex) {
        final StringBuilder sb = new StringBuilder();

        for (byte b : hex) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
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

    /**
     * Returns the identifier of a person with the provided login.
     *
     * @param login the login of the person to retrieve.
     * @return a 200 OK response with a person that has the provided login.
     * If the login does not correspond with any user, a 400 Bad
     * Request
     * response with an error message will be returned. If an error happens
     * while retrieving the list, a 500 Internal Server Error response with
     * an
     * error message will be returned.
     */
    @GET
    @Path("/getId/{login}")
    public Response getId(
            @PathParam("login") String login) {
        try {
            final Person person = this.dao.getId(login);

            return Response.ok(person).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in get method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error getting a person", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Returns the role of a person with the provided login.
     *
     * @param login the login of the person to retrieve.
     * @return a 200 OK response with the role of a person that has the provided login.
     * If the login does not correspond with any user, a 400 Bad
     * Request response with an error message will be returned. If an error happens
     * while retrieving the list, a 500 Internal Server Error response with
     * an error message will be returned.
     */
    @GET
    @Path("/role/{login}")
    public Response getRole(
            @PathParam("login") String login) {
        try {
            final Person person = this.dao.getRole(login);
            return Response.ok(person).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in get method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error getting a role", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Returns a person with the provided identifier.
     *
     * @param id the identifier of the person to retrieve.
     * @return a 200 OK response with a person that has the provided identifier.
     * If the identifier does not correspond with any user, a 400 Bad Request
     * response with an error message will be returned. If an error happens
     * while retrieving the list, a 500 Internal Server Error response with an
     * error message will be returned.
     */
    @GET
    @Path("/{id}")
    public Response get(
            @PathParam("id") int id) {
        try {
            final Person person = this.dao.get(id);

            return Response.ok(person).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in get method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error getting a person", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Returns the complete list of people stored in the system.
     *
     * @return a 200 OK response with the complete list of people stored in the
     * system. If an error happens while retrieving the list, a 500 Internal
     * Server Error response with an error message will be returned.
     */
    @GET
    public Response list() {
        try {
            return Response.ok(this.dao.list()).build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error listing people", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * Creates a new person in the system.
     *
     * @param name the name of the new person.
     * @param surname the surname of the new person.
     * @return a 200 OK response with a person that has been created. If the
     * name or the surname are not provided, a 400 Bad Request response with an
     * error message will be returned. If an error happens while retrieving the
     * list, a 500 Internal Server Error response with an error message will be
     * returned.
     */
    @POST
    public Response add(
            @FormParam("name") String name,
            @FormParam("surname") String surname,
            @FormParam("login") String login) {
        try {
            final Person newPerson = this.dao.add(name, surname, login);

            return Response.ok(newPerson).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in add method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error adding a person", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/addGestion")
    public Response addGestion(
            @FormParam("name") String name,
            @FormParam("surname") String surname,
            @FormParam("login") String login,
            @FormParam("password") String password,
            @FormParam("role") String role) {
        try {
            final Person newPerson = this.dao.add(name, surname, login);

            final User newUser = this.daoUser.addGestion(login, encodeSha256(password), role);

            return Response.ok(newPerson).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in add method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error adding a person and user", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }

    }

    /**
     * Modifies the data of a person.
     *
     * @param id identifier of the person to modify.
     * @param name the new name of the person.
     * @param surname the new surname of the person.
     * @return a 200 OK response with a person that has been modified. If the
     * identifier does not corresponds with any user or the name or surname are
     * not provided, a 400 Bad Request response with an error message will be
     * returned. If an error happens while retrieving the list, a 500 Internal
     * Server Error response with an error message will be returned.
     */
    @PUT
    @Path("/{id}")
    public Response modify(
            @PathParam("id") int id,
            @FormParam("name") String name,
            @FormParam("surname") String surname,
            @FormParam("login") String login) {
        try {
            final Person modifiedPerson = new Person(id, name, surname, login);
            this.dao.modify(modifiedPerson);

            return Response.ok(modifiedPerson).build();
        } catch (NullPointerException npe) {
            final String message = String.format("Invalid data for person (name: %s, surname: %s, login: %s)", name,
                    surname, login);

            LOG.log(Level.FINE, message);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message)
                    .build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in modify method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error modifying a person", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * Deletes a person from the system.
     *
     * @param id the identifier of the person to be deleted.
     * @return a 200 OK response with the identifier of the person that has been
     * deleted. If the identifier does not corresponds with any user, a 400 Bad
     * Request response with an error message will be returned. If an error
     * happens while retrieving the list, a 500 Internal Server Error response
     * with an error message will be returned.
     */
    @DELETE
    @Path("/{id}/{login}")
    public Response delete(
            @PathParam("id") int id,
            @PathParam("login") String login) {
        try {
            this.dao.delete(id, login);

            return Response.ok(id).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in delete method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error deleting a person", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/userProfile/{login}")
    public Response getData(@PathParam("login") String login) {
        try {
            final Person person = this.dao.getData(login);

            return Response.ok(person).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person login in get method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error getting a person", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }
}
