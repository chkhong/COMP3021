package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook implements Serializable{
    private ArrayList<Folder> folders;

    private static final long serialVersionUID = 1L;

    public NoteBook() {
        this.folders = new ArrayList<Folder>();
    }

    public NoteBook(String file) {
        this.folders = new ArrayList<Folder>();

        FileInputStream fis = null;
        ObjectInputStream in = null;

        try {
            fis = new FileInputStream(file);
            in = new ObjectInputStream(fis);

            NoteBook notebook = (NoteBook) in.readObject();

            this.folders = notebook.getFolders();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean createTextNote(String folderName, String title) {
        TextNote note = new TextNote(title);
        return insertNote(folderName, note);
    }

    public boolean createTextNote(String folderName, String title, String content) {
        TextNote note = new TextNote(title, content);
        return insertNote(folderName, note);
    }

    public boolean createImageNote(String folderName, String title) {
        ImageNote note = new ImageNote(title);
        return insertNote(folderName, note);
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public void addFolder(String folderName) {
        Folder folder = new Folder(folderName);
        this.folders.add(folder);
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

    public void sortFolders() {
        for (Folder f : folders) {
            f.sortNotes();
        }

        Collections.sort(folders);
    }

    public List<Note> searchNotes(String keywords) {
        ArrayList<Note> result = new ArrayList<Note>();
        for (Folder f : folders) {
            result.addAll(f.searchNotes(keywords));
        }
        return result;
    }

    public boolean save(String file) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
