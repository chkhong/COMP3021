package base;

import java.util.ArrayList;
import java.util.Objects;

public class Folder {
    private ArrayList<Note> notes;
    private String name;

    public Folder(String name) {
        this.notes = new ArrayList<Note>();
        this.name = name;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        int nTxt = 0;
        int nImg = 0;
        for(Note note : notes) {
            if (note instanceof TextNote) {
                nTxt++;
            } else {
                nImg++;
            }
        }

        return name + ":" + nTxt + ":" + nImg;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(obj == null) return false;

        if(!(obj instanceof Folder)) return false;

        Folder n = (Folder) obj;

        return Objects.equals(name, n.name);
    }
}
