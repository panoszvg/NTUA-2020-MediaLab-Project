# MediaLab Project

This is my implementation of the project "Battleships" for the lab of the NTUA/ECE course "Multimedia Technology".

For this project I used Java and JavaFX (SceneBuilder). The purpose of this project is to make students more familiar with Java and OOP principles. 

This game consists of two boards: the player's and the enemy's. The enemy is a bot the user plays against and the ships are placed based on the locations described in the files in the folder "scenarios". To change the current scenario, the user can go to the app's 'Applications > Load' and insert the number of scenario they want to use (file must already exist, otherwise it throws an exception). If ships aren't placed correctly (e.g. adjacent/out of the board) this operation throws an exception. In case the user wants to replay a game, they can use 'Applications > Start' and if they want to exit they can use 'Applications > Exit'.

![image](https://user-images.githubusercontent.com/44406295/142826285-26ba4c92-0a5b-458b-ae58-1f77bb57cd30.png)

## Gameplay:
- The game starts and a player is selected to play first (randomly).
- To shoot in a position either input the coordinates in the bottom or click on the position on the enemy's board.
- Each player has a maximum of 40 shots and the game ends if a player's ships are all sunk or if all 40 shots are used for both players.
- After the game ends, if not all of the enemy's ships are sunk, their remaining positions will be shown.

![image](https://user-images.githubusercontent.com/44406295/142825313-9ca834b0-4014-4a26-966d-6625b4f6ac64.png)

## Information
Other than the information on the GUI, the user can go to the menu for more information:

### Details
The user can see the enemy's ships condition, his ships condition and the last 5 shots the enemy took.

![image](https://user-images.githubusercontent.com/44406295/142827745-e4b98641-b15c-4ede-88bd-553142b514d2.png)

![image](https://user-images.githubusercontent.com/44406295/142827639-5ffa4dee-e119-4ab1-8365-904b09c88a69.png)

### History
The user can see the all the shots they/the enemy took.

![image](https://user-images.githubusercontent.com/44406295/142828118-5c6ba339-9f48-49f4-aa5d-7352e71c8495.png)

![image](https://user-images.githubusercontent.com/44406295/142828171-a5522ad1-6fe7-48e9-9b9e-8ea20a545fc7.png)










Panagiotis Zevgolatakos <panoszevg@outlook.com>
