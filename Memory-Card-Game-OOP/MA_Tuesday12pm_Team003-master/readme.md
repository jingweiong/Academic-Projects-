# Fiery Dragon Game

This is a Java Swing implementation of the Fiery Dragon game,
where players uncover dragon cards and move their dragon 
tokens around the board to reach their cave to win.

## Organisation

This project is organised as follows:

### 'Project' Directory
'action' package contains classes representing different types of movements
used in the game

'Action.java': Abstract class for defining movement of DragonToken on VolcanoCard

'DoNothingAction.java','MoveBackwardAction.java','MoveForwardAction.java': classes
inherited from abstract Action for types of movements

'Animal' package contains classes representing different 
animal types used in the game

'Animal.java': Interface defining common methods for animal 
objects

'Bat.java', 'BabyDragon.java', 'Spider.java', 'Salamander.java':
Classes implementing the Animal interface for specific animal types

'game' package consists of all the important components of the game

'CardClickListener.java': Interface class which notifies GameManager class
when a DragonCard is flipped

'GameBoard.java': Main class representing the game board. It 
handles the initialization of the game components, such as 
volcano cards, dragon cards, dragon tokens, and caves. It 
initialises GameManager to handle the gameflow.

'GameManager.java': Class controlling the major functions of the game.
It ensures every player's turn is handled with proper game logic and manages 
the state of the game. Uses singleton design pattern 

'Cave.java': Class representing the caves where players start 
the game. Each cave is associated with a specific animal 
type

'DragonCard.java': Class representing the dragon cards flipped 
by players. Each card has an animal and a count associated 
with it.

'DragonToken.java': Class representing the dragon tokens that 
move around the board. Each token is associated with a 
specific animal type and can move based on the flipped 
dragon card.

'VolcanoCard.java': Class representing the volcano cards 
placed around the board. Each card has a animal associated 
with it and is connected to neighboring cards.

'Player.java': Class representing the player.

'FieryDragonGameTest.java': Class to test the key game functionalities


### 'Documentation' Directory

This directory contains 3 files which are
'Sprint1 Doc','Sprint2 Doc' and 'Sprint3 Doc' which stores all the PDF files