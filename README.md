# CS597 Project Prototype

## Prototype Tasks

1. Locate two open seats together to study with a friend.
   - Specific Demo Task: Locate 2 seats with a noise level of 2 or less in Gateway South.
2. View information about a building’s seating availability.
   - Specific Demo Task: Find out how many seats are available on Gateway North's 2nd floor.
3. Get notified when a specific seat is available.
   - Specific Demo Task: Favorite a seat. 

## Setup

### 1. Clone the repository

Open a terminal in the folder where you want the project to live, then run:

```
git clone https://github.com/ThomasKain/CS597Project.git
```

You can also clone from inside Android Studio: on the Welcome screen choose Get from VCS, paste the URL above, and click Clone.

### 2. Open the project

If you cloned from a terminal, open Android Studio and choose File > Open, then select the `CS597Project` folder (the one containing the `build.gradle` or `build.gradle.kts` file).

### 3. Sync Gradle

Go to File > Sync Project with Gradle Files. Wait for the sync to finish (watch the progress bar at the bottom of the window). If Android Studio prompts you to install missing SDK platforms or build tools, accept and install them.

## Running the App

1. In the top toolbar, make sure `app` is selected in the run configuration dropdown.
2. Select a target device in the device dropdown next to it.
3. Click the green Run button (the triangle next to `app`).
4. If no emulator is available or running:
   1. Open Device Manager from the right-hand panel.
   2. Click Create Device if none exist, and choose a device definition and a system image (API level [fill in minimum SDK, e.g. 24] or higher).
   3. Click the play icon next to the device to start it, then run the app again.
5. If the app does not open automatically on the emulator, find its icon in the app drawer and open it manually.

## Team Contribution Statement

- Maya: Created the initial skeleton that included page navigation to and from each page. Contributed to task 1: Took Tanav’s seat matching algorithm and made a display for the matched seats that take you to the next page depending on which seat you click. Then created the seat layout page with dummy maps (added by Thomas later) and the directional layout page and map. Took notes for our second meeting. 
- Thomas: Created the GitHub repo and initial template. Contributed to task 3; implemented the main View Buildings menu and its per-building/floor seat maps (incl. interactivity, zooming, panning).
- Tanav: Created GlobalSeatMap for dummy data of open/closed seats for searching seats and displaying on map. Created user input process for task 1, taking noise level, number of seats together, and preferred building(s). Processed user search with seat map and passed result of algorithm for Maya's later pages of selection and direction. Took notes for first meeting.
- Jessica: Contributed to task 2; Worked on layout and logic for the View Buildings path; Building Busyness layout 
- Hanqing: Contributed to task 3; Created the floor map images for two buildings; Built the seat detail page that opens when tapping a seat on the map, with a Notifiy button and an info button; Hooked up the Building Busyness page.
