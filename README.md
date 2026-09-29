# COSC212-Mini-project
GROUP PROJECT MANAGEMENT SYSTEM

COSC 212 MINI PROJECT

1. PROJECT TITLE

Group Project Management System

2. PROJECT DESCRIPTION

The Group Project Management System is a Java desktop application developed using Java Swing/AWT.

The system is designed to help a lecturer manage students, organize students into groups, assign responsibilities to group members, track task progress, and generate reports on group and individual contributions.

The application stores its data in memory using Java collections. No database is required.

3. PROJECT FEATURES

The system provides the following features:

* Add, edit, delete, and view students.
* Create, edit, delete, and view groups.
* Add and remove students from groups.
* Create and manage responsibilities/tasks.
* Assign responsibilities to group members.
* Set task descriptions, deadlines, and statuses.
* Update the status of responsibilities.
* Track individual member progress.
* Calculate group progress.
* Generate group progress reports.
* Display member contribution information.
* Display task status information.
* Navigate between Students & Groups, Tasks & Assignment, and Reports sections.

4. TECHNOLOGIES USED

* Java
* Java Swing/AWT
* Java Collections
* Object-Oriented Programming (OOP)
* MVC (Model-View-Controller) architecture

5. PROJECT STRUCTURE

The source code is organized into model, controller, and view packages.

GroupProjectSystem/
│
├── src/
│   ├── controller/
│   │   ├── GroupController.java
│   │   ├── ProgressController.java
│   │   ├── ResponsibilityController.java
│   │   └── StudentController.java
│   │
│   ├── model/
│   │   ├── DataManager.java
│   │   ├── Group.java
│   │   ├── ProgressReport.java
│   │   ├── Responsibility.java
│   │   └── Student.java
│   │
│   ├── view/
│   │   ├── MainFrame.java
│   │   ├── ReportsPanel.java
│   │   ├── StudentsGroupsPanel.java
│   │   └── TasksAssignmentsPanel.java
│   │
│   ├── pics/
│   │   └── Project screenshots
│   │
│   ├── Main.java
│   ├── TestProgress.java
│   └── gaps.txt
│
└── README.txt

6. MODEL PACKAGE

The model package contains the main data classes used by the application.

* Student.java
    Stores student information such as student ID, name, and email.
* Group.java
    Represents a project group and its members.
* Responsibility.java
    Represents a task/responsibility, including its title, description, deadline, status, and assigned member.
* DataManager.java
    Manages the in-memory collections used by the application.
* ProgressReport.java
    Represents progress and reporting information.

7. CONTROLLER PACKAGE

The controller package contains classes that manage application operations and connect the user interface with the model.

* StudentController.java
    Handles student-related operations.
* GroupController.java
    Handles group-related operations.
* ResponsibilityController.java
    Handles responsibility/task operations.
* ProgressController.java
    Handles progress calculations for students and groups.

8. VIEW PACKAGE

The view package contains the graphical user interface of the application.

* MainFrame.java
    Main application window and navigation.
* StudentsGroupsPanel.java
    Provides the interface for managing students and groups.
* TasksAssignmentsPanel.java
    Provides the interface for managing responsibilities and task assignments.
* ReportsPanel.java
    Displays progress and report information.

9. DATA STORAGE

The application uses in-memory storage.

Students, groups, responsibilities, and progress information are stored using Java collections while the program is running.

No database or external file storage is required.

Data is cleared when the application is closed.

10. REQUIREMENTS

The following software is required to compile and run the project:

* Java Development Kit (JDK)
* Java compiler (javac)
* Java Runtime Environment

11. COMPILATION

Open Command Prompt or a terminal in the project root folder.

For example:

C:\Users\Moshood Kilaso\Documents\GitHub\COSC212-Mini-project\GroupProjectSystem>

Create an output folder:

mkdir out

Compile the project using:

javac -d out src\Main.java src\model*.java src\controller*.java src\view*.java

12. RUNNING THE APPLICATION

After successful compilation, run the main class using:

java -cp out Main

13. PROJECT SCREENSHOTS

Screenshots of the application are stored in:

src\pics\

The screenshots show different sections and features of the Group Project Management System.

14. TESTING

The project includes TestProgress.java for testing progress-related functionality.

The application was also tested by compiling the source files and running the graphical interface.

15. PROJECT TEAM

Group Number: 12

Course: COSC 212

Institution: ABU Distance Learning Centre

16. NOTES

This project was developed as an academic mini project.

The application is intended to demonstrate the use of Java, object-oriented programming, MVC structure, Java Swing/AWT, event handling, collections, and basic project management functionality.

Because the application uses in-memory storage, information entered during one session is not permanently saved after the application is closed.