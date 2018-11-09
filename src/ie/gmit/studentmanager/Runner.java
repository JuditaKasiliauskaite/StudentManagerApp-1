package ie.gmit.studentmanager;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Runner extends Application implements Serializable {
	// A SerialVersionUID identifies the unique original class version for which
	// this class is capable of writing streams and from which it can read.
	// Read more here: https://docs.oracle.com/javase/7/docs/platform/serialization/spec/class.html
	private static final long serialVersionUID = 1L;

	// Instance Variables
	StudentManager sm = new StudentManager();
	Scene scene1;
	Scene scene2; // Used for adding Student
	GridPane gridPane1;
	VBox vBox1;
	
	@Override
	public void start(Stage primaryStage) {
		// Text for top of scene 1
		Text myText = new Text("Please select an option below:");
		TextArea myOutput = new TextArea();
		myOutput.setPrefHeight(100); //sets height of the TextArea to 400 pixels 
		myOutput.setPrefWidth(100); //sets width of the TextArea to 300 pixels
		
		// Text for top of scene 2
		Text myText2 = new Text("Please Enter All Student details below:");
		TextArea myOutput2 = new TextArea();
		myOutput2.setPrefHeight(100); //sets height of the TextArea to 400 pixels 
		myOutput2.setPrefWidth(100); //sets width of the TextArea to 300 pixels
		
		Button buttonLoadDB = new Button("Load DB");
		TextField tfDBPath = new TextField();
		tfDBPath.setPromptText("Enter Database Path");
		buttonLoadDB.setOnAction(e -> {
			if (tfDBPath.getText().trim().equals("")) { // If text field is empty
				myOutput.appendText("Please enter path to DB\n");
			} else {
				sm = sm.loadDB(tfDBPath.getText());
				if (sm == null) {
					myOutput.setText("ERROR: DB path " + tfDBPath.getText() + " does not exist\n");
					myOutput.appendText("Please check DB path and try again");
					tfDBPath.clear();
				} else {
					myOutput.setText("DB loaded successfully from " + tfDBPath.getText());
					tfDBPath.clear();
				}
			}
		});
		
		// Add Student
		Button buttonAdd = new Button("Add Student");
		Button addStudentDetailsBtn = new Button("Add Student");
		Button cancelScene2 = new Button("Cancel");
		TextField tfStudentFirstName = new TextField();
		tfStudentFirstName.setPromptText("Enter Student First Name");
		TextField tfStudentSurname = new TextField();
		tfStudentSurname.setPromptText("Enter Student Surname");
		TextField tfStudentID = new TextField();
		tfStudentID.setPromptText("Enter Student ID");
		// Setting the scene to Stage
		buttonAdd.setOnAction(e -> primaryStage.setScene(scene2));
		addStudentDetailsBtn.setOnAction(e -> {
			// If any of the Student fields are empty print prompt message
			if (tfStudentID.getText().trim().equals("") || 
					tfStudentFirstName.getText().trim().equals("") ||
					tfStudentSurname.getText().trim().equals("")) { 
				myOutput2.appendText("Please enter ALL Student details\n");
			} else {
				// Create new Student with information in text fields
				Student student = new Student(tfStudentID.getText(), tfStudentFirstName.getText(), tfStudentSurname.getText());
				sm.add(student); // Add student to student list
				// Print success message
				myOutput.setText("Student " + tfStudentID.getText() +
						" has been added to the students list");
				// Clear input fields
				tfStudentID.clear();
				tfStudentFirstName.clear();
				tfStudentSurname.clear();
				// Return to scene 1
				primaryStage.setScene(scene1);
			}
		});
		
		// Cancel button action for scene 2. If the user decides not to add a 
		// Student the can go back to the main scene.
		cancelScene2.setOnAction(e -> {
			// Clear input fields
			tfStudentID.clear();
			tfStudentFirstName.clear();
			tfStudentSurname.clear();
			// Return to scene 1
			primaryStage.setScene(scene1);
		});
		
		// Delete Student
		Button buttonDelete = new Button("Delete Student");
		TextField tfStudentDel = new TextField();
		tfStudentDel.setPromptText("Enter Student ID");
		buttonDelete.setOnAction(e -> {
			if (tfStudentDel.getText().trim().equals("")) { // If text field is empty
				myOutput.setText("Please enter the Student ID you want to delete");
			} else {
				boolean deleteStatus;
				deleteStatus = sm.delete(tfStudentDel.getText());
				if (deleteStatus == true) {
					myOutput.setText("Student " + tfStudentDel.getText() + " deleted");
					tfStudentDel.clear();
				} else {
					myOutput.setText("Student " + tfStudentDel.getText() + " not found\n");
					myOutput.appendText("No student deleted!");
					tfStudentDel.clear();
				}
			}
		});
		
		// Search by ID
		Button buttonSearchByID = new Button("Search by ID");
		TextField tfSearchID = new TextField();
		tfSearchID.setPromptText("Enter Student ID");
		buttonSearchByID.setOnAction(e -> {
			if (tfSearchID.getText().trim().equals("")) {
				myOutput.setText("Please enter the Student ID you want to search for");
			} else {
				Student studentObj = sm.getStudentByID(tfSearchID.getText());
				if (studentObj != null) {
					myOutput.setText(studentObj.toString());
				} else {
					myOutput.setText("No Student Found with ID " + tfSearchID.getText());
				}
				tfSearchID.clear();
			}
		});
		
		// Search by First Name
		Button buttonSearchByFirstName = new Button("Search by First Name");
		TextField tfSearchFirstName = new TextField();
		tfSearchFirstName.setPromptText("Enter Student First Name");
		buttonSearchByFirstName.setOnAction(e -> {
			if (tfSearchFirstName.getText().trim().equals("")) {
				myOutput.setText("Please enter the Student First Name you want to search for");
			} else {
				List<Student> sameNamesList = sm.getStudentsByFirstName(tfSearchFirstName.getText());
				if (!(sameNamesList == null)) {
					System.out.println("Igot here");
					for (Student student : sameNamesList) {
						myOutput.setText(student.toString());
					}
				} else {
					myOutput.setText("No Studnets found with First name: " + tfSearchFirstName.getText());
					tfSearchFirstName.clear();
				}
			}
		});
		
		// Show total number of students
		Button buttonShowTotal = new Button("Show Total Students");
		TextField tfTotalNumberOfStudents = new TextField();
		tfTotalNumberOfStudents.setEditable(false); // This box is not editable. Only displays result.
		tfTotalNumberOfStudents.setPromptText("0");
		buttonShowTotal.setOnAction(e -> {
			tfTotalNumberOfStudents.setText(Integer.toString(sm.findTotalStudents()));
		});
		
		Button buttonSaveDB = new Button("Save DB");
		buttonSaveDB.setOnAction(e -> {
			try {
	    		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("studentsDB.ser"));
	    		out.writeObject(sm);
	    		out.close();
	    		myOutput.setText("Student Database Saved");
	    	} catch (Exception exception) {
	    		System.out.print("[Error] Cannont save DB. Cause: ");
	    		exception.printStackTrace();
	    		myOutput.setText("ERROR: Failed to save Students DB!");
	    	}
		});
		
		Button buttonQuit = new Button("Quit");	
		buttonQuit.setOnAction(e -> Platform.exit());

		gridPane1 = new GridPane();
		vBox1 = new VBox();
		
		// Setting the padding
		gridPane1.setPadding(new Insets(10, 10, 10, 10));
		vBox1.setPadding(new Insets(10, 10, 10, 10));
		
		// Setting the vertical and horizontal gaps between the columns
		gridPane1.setVgap(10);
		gridPane1.setHgap(10);
		
		// Setting the Grid alignment
		gridPane1.setAlignment(Pos.CENTER);
		vBox1.setAlignment(Pos.CENTER);
		
		// Arranging all the nodes in the grid
		gridPane1.add(myText, 0, 0);
		gridPane1.add(buttonLoadDB, 0, 1);
		gridPane1.add(tfDBPath, 1, 1);
		gridPane1.add(buttonAdd, 0, 2);
		//gridPane1.add(tfStudentID, 1, 2);
		gridPane1.add(buttonDelete, 0, 3);
		gridPane1.add(tfStudentDel, 1, 3);
		gridPane1.add(buttonSearchByID, 0, 4);
		gridPane1.add(tfSearchID, 1, 4);
		gridPane1.add(buttonSearchByFirstName, 0, 5);
		gridPane1.add(tfSearchFirstName, 1, 5);
		gridPane1.add(buttonShowTotal, 0, 6);
		gridPane1.add(tfTotalNumberOfStudents, 1, 6);
		gridPane1.add(buttonSaveDB, 0, 7);
		gridPane1.add(buttonQuit, 0, 8);
		gridPane1.add(myOutput, 0, 9, 2, 1);

		// Arranging all the nodes in a vertical box for scene 2 student add
		vBox1.getChildren().addAll(myText2, 
				tfStudentID, 
				tfStudentFirstName, 
				tfStudentSurname, 
				addStudentDetailsBtn, 
				cancelScene2,
				myOutput2);

		/* Preparing the Scene */
		// Create a Scene by passing the root group object, height and width
		scene1 = new Scene(gridPane1, 400, 450);
		scene2 = new Scene(vBox1, 400, 450);
		
		/* Preparing the Stage (i.e. the container of any JavaFX application) */
		// Setting the title to Stage.
		primaryStage.setTitle("Student Manager Application");
		// Setting the scene to Stage
		primaryStage.setScene(scene1);
		// Displaying the stage
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
