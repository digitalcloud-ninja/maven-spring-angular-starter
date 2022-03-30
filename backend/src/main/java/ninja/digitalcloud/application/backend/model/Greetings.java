package ninja.digitalcloud.application.backend.model;

/**
 * Simple Greetings Object
 */
public class Greetings {

    private long id;
    private String content;

    /**
     * Injectable Constructor
     * @param id
     * @param content
     */
    public Greetings(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("Greetings: { 'id': '%d', 'content': '%s'}", id, content);
    }
}
