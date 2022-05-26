# World Cup Score Board
A little game that simulates the group stages for the World Cup in 2022. The game
is written in Java 17, with no external libraries. The project can be built with 
maven and the main-class that starts the app is inside the module world-cup-ui, 
which can be started with the jar-file itself after the command "mvn clean package"
or started directly from an IDE (IntelliJ and Eclipse is both tested). 
The UI is written using Swing, and is as simple as it gets.

## Data
The data for the world cup board is stored inside the module world-cup-data.
##### Team
A team consists of a name and which group it is in. When a match is finished in the 
group stage, statistics is added to the team; points accumulated, amount of goals scored, 
amount of goals conceded, yellow cards and red cards. 
##### Group
A group consists of a name, a list of teams and a list of results for the matches played
by the teams in the group against each other. When the group stage is complete, a list
of who qualifies for the knockout stages can be retrieved.
##### Match
The match object consists of two teams, and an enum telling us which stage the match is
being played at; group stages, quarter-finals, final etc. When a match is started via the
startMatch-method, a RunningMatch-object is created which keeps track of amount of goals
scored for each team and if the match is playing or ended. Updating goals for this RunningMatch
is done via a background task that runs async. How often a goal is scored and how many goals
each match can possibly have is controlled via some constants inside the MatchCallable-object. 
Each time a goal is scored or the match ends will notify every implementation of the 
interface MatchNotifier.
##### ScoreBoard
The score board receives a list of groups, and then each group sets up the matches between
the teams inside the group. The score board keeps track of all group matches and has
methods for getting the finished matches, starting a match and finishing a match. 
The intention is to expand this to keep track of which teams qualify for the knockout stages
after the groups are finished playing. And setting up matches for each stage after 
the group stage is finished. 

## UI
The UI for this app is stored inside the module world-cup-ui. The UI is really simple at this
stage, as it takes some time to design a nice looking UI and putting it all together. 
#### Main
The main-class creates a JFrame which serves as the main window when the app starts. It sets
up a method for closing the application, which also closes down the score board which contains
a ThreadPool where we might have some running threads. 
##### MatchPanel
A MatchPanel consists of one match, where the match can be started by pressing the button
"Start Match". When the match is started the label updates whenever a goal is scored. The
match can be stopped manually by pressing the button "Stop Match", or the match will be 
stopped automatically based on the constants set in the Match-object. The button 
"Remove Match" will be enabled when a match is done, and pressing this will remove 
the MatchPanel from the list of games. 
##### SummaryDialog
This dialog show every match that is finished. When a match stops automatically or 
manually this list is automatically updated. The button "Refresh" can also be clicked
to cause a refresh of the finished matches.
