Group Project Management System

A Java desktop application for managing students, groups, tasks, assignments, progress, and reports.

1. Compile and Run

Open Command Prompt inside the project folder.

type:
	i. if exist out rmdir /s /q out
	ii. mkdir out
	iii. javac -d out src\model\*.java src\controller\*.java src\view\*.java

If there are no errors, run:

	java -cp out Main

2. Students & Groups
	
	Add Student
	Edit/Delete Student
	Add Group
	Edit/Delete Group
	Manage Members

3. Tasks & Assignments

	Add Task
	Edit/Delete Task.

Update Status

Select a task and click Update Status of Selected Task.

	Available statuses:

	* NOT_STARTED
	* IN_PROGRESS
	* COMPLETED
	* BLOCKED

Filter Tasks

4. Reports

Open the Reports tab to view:

* Member Progress
* Group Progress
* Task Status Report

**Important**

The application stores data in memory only.

Closing the application will remove all data created during that session.