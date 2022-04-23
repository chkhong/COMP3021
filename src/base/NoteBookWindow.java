package base;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";
	/**
	 * current note selected by the user
     */
	String currentNote = "";

    Stage stage;

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
        this.stage = stage;
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load");
		buttonLoad.setPrefSize(100, 20);
		Button buttonSave = new Button("Save");
		buttonSave.setPrefSize(100, 20);

        Label labelSearch = new Label("Search: ");
        TextField inputSearch = new TextField();
        Button buttonSearch = new Button("Search");
        Button buttonClearSearch = new Button("Clear Search");


		hbox.getChildren().addAll(buttonLoad, buttonSave,labelSearch, inputSearch, buttonSearch, buttonClearSearch);

        buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Please choose a file which contains notebook object");

                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showOpenDialog(stage);

                if (file != null) {
                    loadNoteBook(file);
                }
            }
        });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Please choose a file to save the notebook object");

                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showSaveDialog(stage);

                if (file != null) {
                    noteBook.save(file.getName());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully Saved");
                    alert.setContentText("Your file has been saved to file " + file.getName());
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });

        buttonSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentSearch = inputSearch.getText();
                textAreaNote.setText("");
                updateListView(currentFolder, currentSearch);
            }
        });
        
        buttonClearSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentSearch = "";
                inputSearch.setText("");
                textAreaNote.setText("");
            }
        });

		return hbox;
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
        HBox hbox = new HBox();
        hbox.setSpacing(8);
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		// []TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
        for (Folder f: noteBook.getFolders()) {
            foldersComboBox.getItems().add(f.getName());
        }

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// []TODO update listview
                if (t != null && !t.toString().equals(currentFolder)) {
                    updateListView(currentFolder, null);
                }

			}

		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				// This is the selected title
				// []TODO load the content of the selected note in
				// textAreNote
				String content = "";
                for (Folder f: noteBook.getFolders()) {
                    if (f.getName().equals(currentFolder)) {
                        for (Note n : f.getNotes()) {
                            if (n instanceof TextNote && n.getTitle().equals(title)) {
                                content = ((TextNote) n).getContent();
                                break;
                            }
                        }
                    }
                }
                if (!content.equals("")) {
                    currentNote = title;
                    textAreaNote.setText(content);
                }

			}
		});

        Button addFolderButton = new Button("Add a Folder");

        addFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog("Add a Folder");
                dialog.setTitle("Input");
			    dialog.setHeaderText("Add a new folder for your notebook:");
			    dialog.setContentText("Please enter the name you want to create:");
			    // Traditional way to get the response value.
			    Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    if (result.get().equals("")) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("Please input a valid folder name!");
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                System.out.println("Pressed OK.");
                            }
                        });
                        return;
                    } else {
                        for (Folder f: noteBook.getFolders()) {
                            if (f.getName().equals(result.get())) {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Warning");
                                alert.setContentText("You already have a folder named with " + result.get());
                                alert.showAndWait().ifPresent(rs -> {
                                    if (rs == ButtonType.OK) {
                                        System.out.println("Pressed OK.");
                                    }
                                });
                                return;
                            }
                        }
                    }
                    noteBook.addFolder(result.get());
                    foldersComboBox.getItems().add(result.get());
                    foldersComboBox.setValue(result.get());
                }
            }
        });

        Button addNoteButton = new Button("Add a Note");

        addNoteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentFolder == "-----") {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please choose a folder first!");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                    return;
                }

                TextInputDialog dialog = new TextInputDialog("Add a Note");
                dialog.setTitle("Input");
                dialog.setHeaderText("Add a new note to your notebook:");
                dialog.setContentText("Please enter the title you want to create:");
                // Traditional way to get the response value.
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    noteBook.createTextNote(currentFolder, result.get());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful!");
                    alert.setContentText("Insert note " + result.get() + " to folder " + currentFolder + " successfully!");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                    updateListView(currentFolder, null);
                }
            }
        });

		vbox.getChildren().add(new Label("Choose folder: "));
		hbox.getChildren().addAll(foldersComboBox, addFolderButton);
        vbox.getChildren().add(hbox);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
        vbox.getChildren().add(addNoteButton);

		return vbox;
	}

	private void updateListView(String selectedFolderName, String searchString) {
		ArrayList<String> list = new ArrayList<String>();

		// []TODO populate the list object with all the TextNote titles of the
		// currentFolder
        Folder selectedFolder = null;
        for (Folder f: noteBook.getFolders()) {
            if (f.getName().equals(selectedFolderName)) {
                selectedFolder = f;
                break;
            }
        }
        if (searchString == null) {
            for (Note n: selectedFolder.getNotes()) {
                list.add(n.getTitle());
            }
        }else {
            for (Note n: selectedFolder.searchNotes(searchString)) {
                list.add(n.getTitle());
            }
        }
		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
        
        ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
        saveView.setFitHeight(18);
        saveView.setFitWidth(18);
        saveView.setPreserveRatio(true);
        Button saveButton = new Button("Save Note", saveView);
        
        ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
        deleteView.setFitHeight(18);
        deleteView.setFitWidth(18);
        deleteView.setPreserveRatio(true);
        Button deleteButton = new Button("Delete Note", deleteView);

        HBox hBox = new HBox(saveButton, deleteButton);
        hBox.setSpacing(8);

		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		// 0 0 is the position in the grid
		grid.add(hBox, 0, 0);
		grid.add(textAreaNote, 0, 1);

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentFolder == "-----" || currentNote == "") {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please choose a folder and a note");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                    return;
                }
                for (Folder f: noteBook.getFolders()) {
                    if (f.getName().equals(currentFolder)) {
                        for (Note n: f.getNotes()) {
                            if (n.getTitle().equals(currentNote)) {
                                ((TextNote) n).setContent(textAreaNote.getText());
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Successful!");
                                alert.setContentText("Save note " + currentNote + " to folder " + currentFolder + " successfully!");
                                alert.showAndWait().ifPresent(rs -> {
                                    if (rs == ButtonType.OK) {
                                        System.out.println("Pressed OK.");
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
            }
        });

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentFolder == "-----" || currentNote == "") {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please choose a folder and a note");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                    return;
                }
                for (Folder f: noteBook.getFolders()) {
                    if (f.getName().equals(currentFolder)) {
                        for (Note n: f.getNotes()) {
                            if (n.getTitle().equals(currentNote)) {
                                if (f.removeNotes(n.getTitle())) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Successful!");
                                    alert.setContentText("Your note has been successfully removed!");
                                    alert.showAndWait().ifPresent(rs -> {
                                        if (rs == ButtonType.OK) {
                                            System.out.println("Pressed OK.");
                                        }
                                    });
                                    updateListView(currentFolder, null);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        });

		return grid;
	}

    private void loadNoteBook(File file) {
        foldersComboBox.getItems().clear();
        NoteBook notebook = new NoteBook(file.getName());
        ArrayList<Folder> folders = notebook.getFolders();
        for (Folder f: folders) {
            foldersComboBox.getItems().add(f.getName());
        }
        foldersComboBox.setValue("-----");
        noteBook = notebook;
    }

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called �𦭛he most shocking play in NFL history�� and the Washington Redskins dubbed the �嚹hrowback Special��: the November 1985 play in which the Redskins�� Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award�𢘛inning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything�㽘ntil it wasn�脌. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant�𤤗 part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether�䏭 Daddy Was a Number Runner and Dorothy Allison�䏭 Bastard Out of Carolina, Jacqueline Woodson�䏭 Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood�𤴆he promise and peril of growing up�𤤗nd exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}

}