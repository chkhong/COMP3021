package base;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Note implements Comparable<Note>, Serializable {
    private Date date;
    private String title;

    public Note(String title) {
        this.title = title;
        this.date = new Date(System.currentTimeMillis());
    }

    public String getTitle() {
        return title;
    }

    public boolean equals(Note note) {
        return title.equals(note.title);
    }


    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(obj == null) return false;

        if(!(obj instanceof Note)) return false;

        Note n = (Note) obj;

        return Objects.equals(title, n.title);
    }

    @Override
    public int compareTo(Note note) {
        if (date.before(note.date)) return -1;
        else if (date.after(note.date)) return 1;
        else return 0;
    }

    @Override
    public String toString() {
        return date.toString() + "\t" + title;
    }
}
