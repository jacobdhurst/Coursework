# Boggle
<b>Course: CS351L - Design of Large Programs<br>
Author: Jacob Hurst<br>
Project: Boggle</b><br>

# Versions:
<b>Version 1 (V1): (command-line)</b><br>
  Dictionary is loaded, Boggle tray is generated, user can search dictionary for words.<br><br>
<b>Version 2 (V2): (command-line)</b><br>
  Added player class, player can check if words exist in both the dictionary and tray (player earns points for entered word),<br>
  list of  all possible words in tray and dictionary is shown after.<br><br>
<b>Version 3 (V3): (graphical user interface)</b><br>
  First complete game, user types in words via textfield and earns points, timer runs for 2.5 minutes.<br><br>
<b>Version 4 (V4): (graphical user interface)</b><br> 
  Enhancements on game: added click-drag-release functionality on the tray (for word building), enhanced the look of the tray<br>
  (when clicked-dragged over), improved end of game dialog with options for a new game, sound effects added<br>
  (end of game buzzer, valid and invalid word sounds), fixed bugs in correct/incorrect labels in game, minor styling of game added,
  possible score dialog added in-game, fixed bug where jar file wasn't finding text resource.<br><br>

# How to use:
<b>V1:</b><br>
  'S' to search, 'L' to load.<br> 
  When searching dictionary, exit loop by entering any key other than 'S'.<br>
  When loading tray enter either 4 or 5 to select dimensions.<br><br>
<b>V2:</b><br>
  'S' to search, 'L' to load.<br> 
  When searching dictionary, exit loop by entering any key other than 'S'.<br>
  When loading tray enter your name first, either 4 or 5 to select dimensions, then type a word you see on the tray to check.<br><br>
<b>V3:</b><br>
  Select either "Start 4x4" or "Start 5x5" to start their respective games, use the textfield to enter as many words as you can in 2.5
  minutes!<br><br>
<b>V4:</b><br>
  Edit the time (if you'd like to play for a different duration than the default), Select either "New 4x4" or "New 5x5" to start their respective games, use the textfield or click-drag-release over letters with the mouse to enter as many words as you can in the allotted time!<br><br>

# Known Bugs:
  When run from outside of the intelliJ project directory, the version 3 jar files text resource does not load (resulting in no dictionary loaded). I discovered this issue when building the jar file for version 4 & luckily, was able to fix it for version 4 (by loading in the text resource as an InputStream). However, the issue still remains in the version 3 jar file (as my code from that version has been overwritten). 
