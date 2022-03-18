package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class TextNote extends Note {
    String content;

    public TextNote(String title) {
        super(title);
    }

    public TextNote(String title, String content) {
        super(title);
        this.content = content;
    }

    public TextNote(File f) {
        super(f.getName());
        this.content = getTextFromFile(f.getAbsolutePath());
    }

    public String getContent() {
        return content;
    }

    private String getTextFromFile(String absolutePath) {
        String result = "";
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath)));

            result = br.readLine();
            br.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void exportTextToFile(String pathFolder) {
        BufferedWriter bw = null;
        File file = new File(pathFolder + File.separator + this.getTitle().replaceAll(" ", "_") + ".txt");

        try {
            bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
