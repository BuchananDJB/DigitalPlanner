# DigitalPlanner
A digital planner with tabs, calendar, notes, and todo lists. Created using Java/Java Swing for use on a desktop environment.

## Features
### Tabs
The user can add, close, open, and delete tabs as needed. Closing a tab simply hides that tab from the main view, which can then be reopened by right-clicking and reopening the tab. Deleting the tab will remove the tab from the planner and delete all data associated with that tab.
Within each tab is housed the main functionality of the planner, as described below.

### Calendar
There is a calendar view in the top left. Dates are selectable, and upon clicking on a date, the right side displays another tabbed view with a todo list and a notes section that correspond to the selected date.

Individual dates are bolded when there are either items on the todo list or there are notes saved for that particular date.

Double-clicking the month will select the current date.


### Todo List
Each todo list has a "Complete" and an "Incomplete" section. Each item has a description and a priority column. Upon clicking the checkbox and marking an incomplete item as done, it is automatically moved to the "Complete" section, and vice versa.
There are currently two tabs for each todo list: "General" and "Social".

### Notes
A simple text field for adding notes. Has typical right-click functionality for text (cut, copy, paste) and scrolls when the text length exceeds the view size.

### General
On the bottom and on the middle-left of the planner display are also a notes and a todo list that are not tied to a specific date. These are intended for general notes and todo items that don't need to correspond to a specific date. 

Everything described within the Calendar, Todo List, Notes, and General sections of this README are contained within individual tabs, as described in the Tab section.

### Themes
Color themes can be changed in the "File" menu.

### Saving
The digital planner has several options for saving data. The user can manually save data from the "File" menu. The digital planner also autosaves data every 60 seconds, and will also save all data upon exiting the program.


## Known Bugs
### Theme
Currently, changing the color theme does not properly update inactive visual components (e.g. The user selects January 1 and then selects January 2 while on the LightFlat theme. The user then changes the theme to SolarizedLight while January 2 is selected. Upon selecting January 1 again, the user will find the todo list and notes corresponding to January 1 will still have the LightFlat color scheme.)

### Todo List
Currently, right-clicks in the empty space of todo lists are not detected. The user currently must right-click on an item on the list to display the right-click menu.
