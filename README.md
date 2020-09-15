# RailApp
# Features:
# Screen 1 : contains 2 Tabbed child fragments 
## Tab1: Text Search Fragment
* Search by station in autocomplete search field
* Searches are saved to a favourites list 
* The favourites ist is persisted to db in onPause
* Submitting a search goes to screen 2, the scheduled trains screen
* Clicking on a favourite search goes to screen 2
* Text search was implemented for it's simplicity and it's convenience (maps can be slow to load)

## Tab2: Map Fragment (as requested )
* To use the map, you will need to supply your own Google maps api key in the meta tag in the manifest
* Loads all stations as red markers in Google maps
* Zooms to the users current location (green marker) if available
* If current location not available, the app zooms to Connolly station for demo purposes
* Clicking on a station marker goes to screen2, the scheduled trains screen


# Screen 2:
* Displays lists of scheduled trains for a particular stop organized by destination
* Each tab represents a destination and hosts 0..n scheduled trains
* Each scheduled train is represented by a card in the list
* Each card shows the route (origin -> destination), train type( Dart, train, etc.), Due time, ETA 
and status (includes late/on-time status, whether the train is en-route and the last station visited)
* If a train is late, the card header is displayed with an orange background, otherwise it's green

# Techniques/Technologies demonstrated:
## UI Layer - MVVM:
The app uses an MVVM UI architecture, separating the code in UI and logic layers for organization 
and testability
## Repository Pattern:
The app uses the repository pattern for data retrieval, abstracting away the source of the data 
which can be network sourced or database sourced.
## Dependency Injection: 
Dependencies are injected using Dagger Hilt, the latest Android DI wrapper 
from Google. This cuts down significantly on complicated boilerplate in both Dagger-Android 
and vanilla Dagger2.
Dependencies in the app are organized into a single app module and installed in the application scope, 
for simplity (It would have been to break them down into Network, Repo and Db modules given enough time)
## Database technology: 
The Room ORM is used for interacting with the Db. Tables are interacted with by using DAOs which
 correspond 1:1 with tables. Each Dao contains one or more methods annotated with SQL queries which
  ROOM compiles into concrete logic at compilation (DAOs in Room are interfaces so just describe 
  the desired behaviour)
## Testing & Mocking
* Unit tests are executed using JUNIT4
* Mockk is the Mocking technology used for Mocks. This is simpler to use than traditional Java 
Mocking technologies such as Mockito, particularly when dealing with Coroutines

# Issues:
## Landscape orientation 
1. The app is ok in portrait but has scrolling issues in landscape
## Unit tests:
1. Unit tests have only been implemented on the Repository class. The view model classes need to be tested
## Maps: 
1. A text search/zoom function has not been implemented. this would make the map a lot more usable
2. Side scolling on the Viewpager needs to disabled in onTouch on the map
3. Retrieving the current Location is flaky and needs some investigation
4. Trains use only standad red markers - this should be improved

## Text Search Fragment:
1. Favourites can't currently be deleted 
2. Styles could be improved

## Data:
1. Stations data isn't currently saved to the db (only favourites are ). The station specific data 
(as opposed to route data) should be saved as it doesn't change. The route data changes a lot so it
 would not be a good candidate for db persistence
 
## Screen 2
The train list screen needs the progress indicators fixed on this screen

## Detailed Train data
This screen has not been completed - the UI/List namespace has the initial classes




