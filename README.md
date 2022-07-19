# BATTLE_SHIPS_CLIENT
 
## About
BattleShips is a classic game played by two players in alternating turns. Each players has a 10x10 grid where they place their ships during the setup part. Later, they make a guess about the position of their opponent's ships. If the guess is correct (the guessed place is occupied by a ship) that section of the ship sinks. This goes on until on of the players has no remaining ships (or ship sections) in their board. First player to sink all of their opponent's ships wins.

This specific game takes this to another level. By creating an account you can easily play against your friends on your local network. You can invite online players to play the game with you or write to the public chat if anyone is up for a game. By winning games you can increase your score and may become one of the top players. Have fun :)

## BackEnd
Game connects to a server that is currently running on my computer. You can also find the code to that backend program in my github account at https://github.com/Onnrr/BATTLE_SHIPS. TCP/IP protocole is used for all the communication and the messages are sent in the string format. All user information are stored in a database using mySql which can only be reached by the server application. Clients send and receive information only via server commands. All of the information about the communication with the server (input and output formats and commands) and database connection can be found in the README file of that project.

## Classes
### Player
Stores the data of the current user session. Player object is created during login using the information from the database and then is sent through scenes in order to pass information. It stores id, userName, mail, score, opponentID, gameTable and the server connection components.

### Cell
Cell is a subclass of javafx button. Each button on the grids are actually a cell object. Cells have column and row properties as integer and isOccupied property as a boolean.

### AppManager
Controls the scene changes. Helps initialising the new scene with the current data stored at the player object.

### OnlinePlayer
It is a subclass of javafx AnchorPane. Includes players name an invite button and an image. It is added to the scene when a player joins the game.

### InviteButton
Subclass of button. Has additional properties like ID and root. ID is the id of the player that the invite will be sent to and root is the pane that the invite button is on.

### Notification
Subclass of AnchorPane. Has 3 different constructors for notifications, chat messages, and ranking. Displays the userName and message. Accept and decline buttons are also available in some constructors.

### Settings
Subclass of AnchorPane. Shows user information, how to play section and delete account - logour buttons.

### Chat
Subclass of AnchorPane. Shows the messages of other players and a textfield to send new messages.
Messages are not stored at the database so they are only available during the current session.

### Initialise
Javafx standart interface Initialisable for initialising new scenes cannot be used because the player object has to be passed through scenes. So, Initialise interface is identical to Initialisable but with one extra argument Player.



#### Bugs
* Chat messages seem weird when returned to lobby

#### TODO
* Warnings in login,signup, delete account etc.
* Dynamic score table update (might add profiles so names will be links to them)
* Message notification (Automatically deletes when the chat is opened)
* Notification order (new notifications should be on top)
* Styling
* how to play
* Loading

#### MAYBE LATER
* Add player profiles search or click on their name to go to their profile.
* Friendship with other users
* Upgraded score. (Dynamically update scores, see all scores not only top 5, maybe make a whole section for ranking)
* Upgraded settings. (Changing username password..., dark-light mode)
* Upgraded chat. (Private chat rooms, emoji send)
* Different game modes. (Against computer, on same computer...)
* Profile photos.
* Special names, photos??? for players in a score range
* Watching other people's games
* Storing game history in profile