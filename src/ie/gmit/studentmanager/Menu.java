package ie.gmit.studentmanager;

import java.util.*; // Needed for Scanner
import java.io.*; // Need to save and load DB

public class Menu {
    private Scanner userInput;
    private StudentManager sm;
    private boolean keepRunning;
    
    public Menu() {
    	// Create new scanner object to capture input from the user
    	userInput = new Scanner(System.in);
        sm = new StudentManager(); // Create a new StudentManager object
        keepRunning = true; // Initialise loop checker
    }
    
    public void display(){
        while (keepRunning){
        	// Display Menu to user
        	showOptions();
        	
        	// Save user Menu option selection 
            int userMenuOption = userInput.nextInt();
            
            // Execute option
            selectOption(userMenuOption);
            pressEnterKeyToContinue();
        }        
    }
    
    private void showOptions(){
    	System.out.println("#####################################");
        System.out.println("#          Student Manager          #");
        System.out.println("#####################################");
        System.out.println("(1) Load Student DB");
        System.out.println("(2) Add a Student.");
        System.out.println("(3) Delete a Student.");   
        System.out.println("(4) Search for a Student by ID."); 
        System.out.println("(5) Search for Students by Firt Name."); 
        System.out.println("(6) Show total Number of Students.");          
        System.out.println("(7) Save DB.");
        System.out.println("(8) Quit.");          
        System.out.println("\nSelect an option [1-8]>\n");
    }
    
    private void selectOption(int userSelection){
    	if (userSelection == 1){ // Load Students DB
            loadDB();
        }else if (userSelection == 2){ //Add Student
        	addMenu();
        }else if (userSelection == 3){ //Delete
            deleteMenu();
        }else if (userSelection == 4){ //Search by ID
            searchByIDMenu();
        }else if (userSelection == 5){ //Search by first name
            searchByName();
        }else if (userSelection == 6){ //Output total Students
            totalMenu();
        }else if (userSelection == 7){ //Save DB of Students
         //   saveDB();
        }else if (userSelection == 8){ //Quit
        	System.out.println("Student Manager Closing - Goodbye!");
            keepRunning = false;                
        }else{ //Invalid input
            System.out.println("Invalid choice!"); 
        }
    }
    
    private void loadDB(){
    	System.out.println("Enter path to DB>"); 
    	String dbPath = userInput.next();
    	sm.loadDB(dbPath);
//    	try {
//			ObjectInputStream in = new ObjectInputStream(new FileInputStream(dbPath));
//			sm = (StudentManager) in.readObject();
//    		in.close();
//    		System.out.print("\nStudents DB loaded successfully!");
//    	} catch (Exception e) {
//    		System.out.print("[Error] Cannont load DB. Cause: ");
//    		e.printStackTrace();
//    	}
    }
    
    private void addMenu(){
        System.out.println("Enter Student ID>"); 
        String sid = userInput.next();
        
        System.out.println("Enter Student First Name>"); 
        String fname = userInput.next();

        System.out.println("Enter Student Surname>"); 
        String sname = userInput.next();
        
        Date dob = new Date(); // Give date with current date time. (can improve)
        
        Student newStudent = new Student(sid, fname, sname, dob);
        boolean result = sm.add(newStudent);
        
        if (result){
            System.out.println("Student " + sid + " has been added successfully.");             
        } else {
        	System.out.println("ERROR: No space to add student!");
        	System.out.println("Please delete a student before you add one");
        }      
    }
    
    private void deleteMenu(){
        System.out.println("Enter Student ID>"); 
        String sid = userInput.next();
        
        boolean result = sm.delete(sid);
        
        if (result){
            System.out.println("Student " + sid + " has been deleted");             
            System.out.println(sm.findTotalStudents() + " students found\n");
        }else{
            System.out.println("No student found with an ID of " + sid + "\n"); 
        }
    }
    
    private void searchByIDMenu(){
        System.out.println("Enter Student ID>"); 
        String sid = userInput.next();
        
		Student s = sm.getStudentByID(sid);
		if (s == null) {
			System.out.println("No Student with ID " + sid + " found!");
		} else {
			System.out.println(s);
		}
	}
    
    private void searchByName(){
        System.out.println("Enter Student First Name>"); 
        String name = userInput.next();       
        
        List<Student> results = sm.getStudentsByFirstName(name);
        
        if (results == null) {
			System.out.println("No Students found with name: " + name);
		} else {
			System.out.println(results);
		}
    }

    private void totalMenu(){
        int totalStudents = sm.findTotalStudents();
        System.out.println("Total number of students = " + totalStudents);
    }
    
//    private void saveDB() {
//    	sm.saveDBToFile();
//    }
    
    // Optional extra to improve user experience
    private void pressEnterKeyToContinue() { 
           System.out.println("\nPress Enter key to continue...\n");
           try {
               System.in.read();
           } catch(Exception e) {
        	   e.printStackTrace();
           }  
    }
    
}