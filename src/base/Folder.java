package base;

import java.util.ArrayList;

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
    public boolean equals(Object obj) {
        Folder f = (Folder) obj;

        return f.getName() == name;
    }
}
