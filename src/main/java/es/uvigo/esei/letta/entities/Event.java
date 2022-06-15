package es.uvigo.esei.letta.entities;

import static java.util.Objects.requireNonNull;

/**
 * An entity that represents an event.
 * 
 * @author DRM
 */
public class Event {
    private int id;
    private String ubicacion;
    private String nombre;
    private String descripcion;
    private String date; //YYYY-MM-DD
    private int id_persona; // clave foránea

    // Constructor needed for the JSON conversion
    Event() {
    }

    /**
     * Constructs a new instance of {@link Event}.
     *
     * @param id      identifier of the event.
     * @param ubicacion    location of the event.
     * @param nombre name of the event.
     * @param descripcion description of the event.
     * @param id_persona person who goes to the event.
     */
    public Event(int id, String ubicacion, String nombre, String descripcion, String date, int id_persona) {
        this.id = id;
        this.setUbicacion(ubicacion);
        this.setNombre(nombre);
        this.setDescripcion(descripcion);
        this.setDate(date);
        this.setIdPersona(id_persona);
    }

    /**
     * Returns the identifier of the event.
     * 
     * @return the identifier of the event.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the location of the event.
     * 
     * @return the location of the event.
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Set the location of this event.
     * 
     * @param ubicacion the new location of the event.
     * @throws NullPointerException if the {@code location} is {@code null}.
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = requireNonNull(ubicacion, "Ubicacion no puede ser nulo");
    }

    /**
     * Returns the name of the event.
     * 
     * @return the name of the event.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Set the name of this event.
     * 
     * @param nombre the new surname of the event.
     * @throws NullPointerException if the {@code name} is {@code null}.
     */
    public void setNombre(String nombre) {
        this.nombre = requireNonNull(nombre, "Nombre no puede ser nulo");
    }

    /**
     * Returns the organizer of the event.
     *
     * @return the organizer of the event.
     */
    public int getIdPersona() {
        return id_persona;
    }
    /**
     * Set the organizer of this event.
     *
     * @param id_persona the new organizer of the event.
     * @throws NullPointerException if the {@code person_id} is {@code null}.
     */
    public void setIdPersona(int id_persona) {
        this.id_persona = requireNonNull(id_persona, "Id_persona can't be null");
    }

    /**
     * Returns the description of the event.
     *
     * @return the description of the event.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Set the description of this event.
     *
     * @param descripcion the new description of the event.
     * @throws NullPointerException if the {@code description} is {@code null}.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = requireNonNull(descripcion, "Descripcion no puede ser nulo");
    }

    /**
     * Returns the date of the event.
     *
     * @return the date of the event.
     */
    public String getDate() {
        return date;
    }
    /**
     * Set the date of this event.
     *
     * @param date the new date of the event.
     * @throws NullPointerException if the {@code date} is {@code null}.
     */
    public void setDate(String date) {
        this.date = requireNonNull(date, "Fecha no puede ser nulo");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Event))
            return false;
        Event other = (Event) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
