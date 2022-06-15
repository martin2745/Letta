package es.uvigo.esei.letta.entities;

import static java.util.Objects.requireNonNull;

/**
 * An entity that represents an event.
 *
 * @author Josemi & Ana
 */
public class SendEvent {

    private int rrpp_id;
    private int event_id;
    private int destiny_id;

    // Constructor needed for the JSON conversion
    SendEvent() {
    }

    /**
     * Constructs a new instance of {@link SendEvent}.
     *
     * @param rrpp_id    identifier of the public relations.
     * @param event_id   identifier of the event.
     * @param destiny_id identifier of the person that receives the event.
     */
    public SendEvent(int rrpp_id, int event_id, int destiny_id) {
        this.setRrpp_id(rrpp_id);
        this.setEvent_id(event_id);
        this.setDestiny_id(destiny_id);

    }

    /**
     * Returns the identifier of the rrpp.
     *
     * @return the identifier of the rrpp.
     */
    public int getRrpp_id() {
        return rrpp_id;
    }

    /**
     * Set the id of this rrpp.
     *
     * @param rrpp_id the new id of the rrpp.
     */
    public void setRrpp_id(int rrpp_id) {
        this.rrpp_id = rrpp_id;
    }

    /**
     * Returns the identifier of the event.
     *
     * @return the identifier of the event.
     */
    public int getEvent_id() {
        return event_id;
    }

    /**
     * Set the id of this event.
     *
     * @param event_id the new id of the rrpp.
     */
    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    /**
     * Returns the identifier of the receiver.
     *
     * @return the identifier of the receiver.
     */
    public int getDestiny_id() {
        return destiny_id;
    }

    /**
     * Set the id of this receiver.
     *
     * @param destiny_id the new id of the rrpp.
     */
    public void setDestiny_id(int destiny_id) {
        this.destiny_id = destiny_id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + rrpp_id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SendEvent))
            return false;
        SendEvent other = (SendEvent) obj;
        if (rrpp_id != other.rrpp_id)
            return false;
        return true;
    }
}
