package base;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Collections;

public class Folder implements Comparable<Folder> {
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

    @Override
    public int compareTo(Folder folder) {
        return name.compareTo(folder.name);
    }

    public void sortNotes() {
        Collections.sort(notes);
    }

    public ArrayList<Note> searchNotes(String keywords) {
		ArrayList<Note> result = new ArrayList<Note>();

        // tokenize and lowercase the keywords
        String[] tokens = keywords.toLowerCase().split(" ");
        
		ArrayList<String> searchTokens = new ArrayList<String>();
		for (int i = 0; i < tokens.length; ++i) {
            if (i + 2 < tokens.length && tokens[i + 1].equals("or")) {
                // or
                searchTokens.add(tokens[i] + " " + tokens[i + 2]);
                i += 2;
            } else {
                // not or
                if (!tokens[i].equals("or")) searchTokens.add(tokens[i]);
            }
		}

		for (Note note: notes) {
            boolean match = true;
			for (String token: searchTokens) {
                //or
				if (token.contains(" ")) {
					String[] t = token.split(" ");
					if (note instanceof TextNote) {
						TextNote textNote = (TextNote) note;
                        String title = textNote.getTitle().toLowerCase();
                        String content = textNote.getContent().toLowerCase();
						if (!(title.contains(t[0]) || title.contains(t[1]) || content.contains(t[0]) || content.contains(t[1]))) {
                            match = false;
							break;
						}
					}
					else if (note instanceof ImageNote) {
                        String title = note.getTitle().toLowerCase();
                        if (!(title.contains(t[0]) || title.contains(t[1]))) {
                            match = false;
                            break;
                        }
					}
				} else {
                    // not or
                    if (note instanceof TextNote) {
                        TextNote textNote = (TextNote) note;
                        String title = textNote.getTitle().toLowerCase();
                        String content = textNote.getContent().toLowerCase();
                        if (!(title.contains(token) || content.contains(token))) {
                            match = false;
                            break;
                        }
                    } else if (note instanceof ImageNote) {
                        String title = note.getTitle().toLowerCase();
                        if (!title.contains(token)) {
                            match = false;
                            break;
                        }
                    }
                }
			}
            if (match) result.add(note);
		}
		return result;
	}
}
