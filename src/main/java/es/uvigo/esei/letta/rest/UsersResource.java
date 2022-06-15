package es.uvigo.esei.letta.rest;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import es.uvigo.esei.letta.dao.DAOException;
import es.uvigo.esei.letta.dao.UsersDAO;
import es.uvigo.esei.letta.entities.Person;
import es.uvigo.esei.letta.entities.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * REST resource for managing users.
 *
 * @author DRM
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
    private final static Logger LOG = Logger.getLogger(UsersResource.class.getName());

    private final UsersDAO dao;

    private @Context SecurityContext security;

    /**
     * Constructs a new instance of {@link UsersResource}.
     */
    public UsersResource() {
        this(new UsersDAO());
    }

    // Needed for testing purposes
    UsersResource(UsersDAO dao) {
        this(dao, null);
    }

    // Needed for testing purposes
    UsersResource(UsersDAO dao, SecurityContext security) {
        this.dao = dao;
        this.security = security;
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
     * Returns a user with the provided login.
     *
     * @param login the identifier of the user to retrieve.
     * @return a 200 OK response with an user that has the provided login.
     * If the request is done without providing the login credentials or using
     * invalid credentials a 401 Unauthorized response will be returned. If the
     * credentials are provided and a regular user (i.e. non admin user) tries
     * to access the data of other user, a 403 Forbidden response will be
     * returned. If the credentials are OK, but the login does not corresponds
     * with any user, a 400 Bad Request response with an error message will be
     * returned. If an error happens while retrieving the list, a 500 Internal
     * Server Error response with an error message will be returned.
     */
    @GET
    @Path("/{login}")
    public Response get(
            @PathParam("login") String login
    ) {
        final String loggedUser = getLogin();

        // Each user can only access his or her own data. Only the admin user
        // can access the data of any user.
        if (loggedUser.equals(login) || this.isAdmin()) {
            try {
                return Response.ok(dao.get(login)).build();
            } catch (IllegalArgumentException iae) {
                LOG.log(Level.FINE, "Invalid user login in get method", iae);

                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(iae.getMessage())
                        .build();
            } catch (DAOException e) {
                LOG.log(Level.SEVERE, "Error getting an user", e);

                return Response.serverError()
                        .entity(e.getMessage())
                        .build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * Returns a user with the provided login.
     *
     * @param login the identifier of the user to retrieve.
     * @return a 200 OK response with an user that has the provided login.
     * If the request is done without providing the login credentials or using
     * invalid credentials a 401 Unauthorized response will be returned. If the
     * credentials are provided and a regular user (i.e. non admin user) tries
     * to access the data of other user, a 403 Forbidden response will be
     * returned. If the credentials are OK, but the login does not corresponds
     * with any user, a 400 Bad Request response with an error message will be
     * returned. If an error happens while retrieving the list, a 500 Internal
     * Server Error response with an error message will be returned.
     */
    @POST
    public Response add(
            @FormParam("name") String name,
            @FormParam("surname") String surname,
            @FormParam("login") String login,
            @FormParam("password") String password,
            @FormParam("role") String role
    ) {
        try {
            final Object newObject = this.dao.add(name, surname, login, password, role);

            return Response.ok(newObject).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in add method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error adding an user", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
@POST
     @Path("/addGestion")
    public Response addGestion(
            @FormParam("login") String login,
            @FormParam("password") String password,
            @FormParam("role") String role) {
        try {
            final User newUser = this.dao.addGestion(login, password, role);

            return Response.ok(newUser).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid person id in add method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error adding a user", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{login}")
    public Response modify(
            @PathParam("login") String login,
            @FormParam("password") String password,
            @FormParam("role") String role) {
        try {
            final User modifiedUser = new User(login, encodeSha256(password), role);
            this.dao.modify(modifiedUser);

            return Response.ok(modifiedUser).build();
        } catch (NullPointerException npe) {
            final String message = String.format("Invalid data for user (name: %s, surname: %s, login: %s)", login, password, role);

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
            LOG.log(Level.SEVERE, "Error modifying a user", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{login}")
    public Response delete(
            @PathParam("login") String login) {
        try {
            this.dao.delete(login);

            return Response.ok(login).build();
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINE, "Invalid user login in delete method", iae);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(iae.getMessage())
                    .build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error deleting a user", e);

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    public Response list() {
        try {
            return Response.ok(this.dao.list()).build();
        } catch (DAOException e) {
            LOG.log(Level.SEVERE, "Error listing user", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    private String getLogin() {
        return this.security.getUserPrincipal().getName();
    }

    private boolean isAdmin() {
        return this.security.isUserInRole("ADMIN");
    }

    private boolean isRrpp() {
        return this.security.isUserInRole("RRPP");
    }

    private boolean isMod() {
        return this.security.isUserInRole("MOD");
    }

    private boolean isPromoter() {
        return this.security.isUserInRole("PROMOTER");
    }
}
