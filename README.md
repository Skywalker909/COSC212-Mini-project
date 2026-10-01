Group Project Management System

A Java desktop application for managing students, groups, tasks, assignments, progress, and reports.

1. Compile and Run

Open Command Prompt inside the project folder.

Compile the project:

if exist out rmdir /s /q out
mkdir out
javac -d out src\model\*.java src\controller\*.java src\view\*.java

If there are no errors, run:

java -cp out Main

2. Students & Groups

Add Student

Click Add Student, enter the student’s ID, name, and email, then click OK.

Edit/Delete Student

Select a student and click Edit Student or Delete Student.

Add Group

Click Add Group, enter the group name and description, then click OK.

Edit/Delete Group

Select a group and click Edit Group or Delete Group.

Manage Members

Select a student and group, then click Add Selected Student to Selected Group.

To remove a member, select the group member and click Remove Member.

3. Tasks & Assignments

Add Task

Click Add Task, enter:

* Title
* Description
* Deadline (yyyy-mm-dd)
* Group
* Group member to assign the task to

Then click OK.

Edit/Delete Task

Select a task and click Edit Task or Delete Task.

Update Status

Select a task and click Update Status of Selected Task.

Available statuses:

* NOT_STARTED
* IN_PROGRESS
* COMPLETED
* BLOCKED

Filter Tasks

Use Filter by Group or Filter by Member, then click Apply Filter.

Click Clear Filter to show all tasks again.

4. Reports

Open the Reports tab to view:

* Member Progress
* Group Progress
* Task Status Report

5. Recommended Order

For a new user:

Students → Groups → Members → Tasks → Status → Reports

6. Important

The application stores data in memory only.

Closing the application will remove all data created during that session.