## BRICK DESTROY

# _Instructions_ 

A to move left, D to move right, Esc to pause game, Alt+Shift+F1 to open debug.

- To read all of the refactoring and additions done, please refer to the Class description section of the javadocs. Changes shown below are limited due to word count.

# _Maintenance Done_

Large classes broken down - Brick.java, GameBoard.java, GamePlay.java, HomeMenu.java

>The above classes have been broken down in a manner to promote single responsibility and maintainability. The content of these classes have been moved to new classes and packages.

Design Patterns - “drawcomponents” package

>Methods like “drawBall”, “drawPlayer'' and “drawBrick” which was originally in GameBoard had been moved into drawcomponents package to create a factory design pattern where each object will be generated and drawn when required inside the game loop. This heightens readability and maintainability of the code.

Interfaces - BallInterface.java, Drawable.java

>“BallInterface” interface has been created to make the implementation of methods flexible and maintainable for the children classes of “Ball”.
“Drawable” interface has been created to support the factory design pattern mentioned above.

Renaming classes - Wall.java has been renamed to GamePlay.java as it holds the gameplay.

Encapsulation - Ball.java, Brick.java, Player.java, Crack.java, GamePlay.java, Gameboard.java, WallSetup.java, LevelScore.java and more.
>Many public variables and methods in above classes have been converted to private and provided with getter methods in order to promote encapsulation within the project.

Deleted Unused Variables - Ball.java, Brick.java, Crack.java, GameBoard.java
>A bunch of unused methods and variables were deleted from above classes in order to increase readability.

JUnit Tests 
>JUnit5 testing has been done on important classes to ensure the code runs as planned after refactoring and extensions.

Build Tools 
>The project has been converted to maven to create build files.

Package Naming/Organization 
>Classes are grouped into meaningful, smaller packages.

New classes 
>Crack.java, ColorBrick.java, PauseMenu.java, HighScore.java, LevelScore.java, InfoScreen.java, HalvePlayerDrop.java, IncreaseSpeedDrop.java, WallSetup.java

# _Extension - New features and Reward/Penalty System_

Added background images - Home Menu, Info and High Score Screens with new look.

Permanent high score with screen and level score with popup.

New level has been added to the game which uses a new type of brick, ball and player bar.

Two scoring systems are added to the game. One score is the total score the user gains for playing the game and another score is a score the user gains for each level.

There is a timer added to each level, the longer the player takes to break bricks in a level, the lesser he will score for that individual level. The total score will increment irrespective of the time. 

For every brick the user breaks, the player bar will extend until a certain width as a reward.

Score for one brick increases with level.

Successfully completing level gives back 3 lives.

Level 5 consists of 2 drops that affect ball speed and player width size when impacted.

Level 5 increases ball speed for every ball you lose.