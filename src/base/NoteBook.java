package base;

import java.util.ArrayList;

public class NoteBook {
    private ArrayList<Folder> folders;

    public NoteBook() {
        this.folders = new ArrayList<Folder>();
    }

    public boolean createTextNote(String folderName, String title) {
        TextNote note = new TextNote(title);
        return insertNote(folderName, note);
    }

    public boolean createImageNote(String folderName, String title) {
        ImageNote note = new ImageNote(title);
        return insertNote(folderName, note);
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public boolean insertNote(String folderName, Note note) {
        Folder target_folder = null;

        for (Folder f : folders) {
            if(f.getName().equals(folderName)) {
                target_folder = f;
                break;
            }
        }

        if (target_folder == null) {
            Folder new_folder = new Folder(folderName);
            new_folder.addNote(note);
            folders.add(new_folder);
            return true;
        } else {
            for (Note n : target_folder.getNotes()) {
                if (n.equals(note)) {
                    System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed");
                    return false;
                }
            }
            target_folder.addNote(note);
            return true;
        }
    }
}
