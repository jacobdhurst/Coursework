# SmartRail
<b>Course: CS351L - Design of Large Programs<br>
Author: Jacob Hurst & Jaehee Shin<br>
Project: SmartRail</b><br>

# Overview:
SmartRail is a concurrent system concept in which all components of a rail system are individual agents with limited global access. This program simulates that concept. Trains, upon creation, know only their current track and desired destination - each track knows only of it's adjacent neighbors, lights and switches add complexity to the system. 

# Versions:
<b>Version 1: (fully functional command-line simulation + semi-functional graphical user interface)</b><br>
  Command line provides status updates as necessary, multiple trains working, rail class presents a text-based visualizer of state. Trains are not yet added dynamically.<br>
  Graphical user interface works for track only configurations but needs work for lights, switches, options, & overall look/feel.
  <br><br>
<b>Version 2: (functional graphical user interface)</b><br>
  Graphical user interface can now dynamically add trains! Improved look & feel - information center with total elapsed time, total active trains, and current message posts is shown.<br><br>
<b>Version 3: (enhanced graphical user interface)</b><br>
  When trains are added they now prompt for a destination and are only started when they determine the route is found, dynamic changing of configuration now supported & configurations fit to container, more enhancements to look & feel.<br><br>
<b>Version 4: (further enhanced graphical user interface)</b><br>
  Custom configuration builder added! Enhancements to look & feel (each train is now a different color, tracks show current train, lights & switches overhaul). Sound added - trains sound horn upon starting. Bug fixes!
  
# How to use:
<b>Version 1:</b><br>
  Click to select a configuration, click start to begin the simulation, click end to load a new configuration, click pause/resume to control the state of the simulation.<br><br> 
<b>Version 2:</b><br>
  Click to select a configuration, click start to begin the simulation, click pause/resume to control the state of the simulation.<br><br> 
<b>Version 3:</b><br>
  Click to select a configuration, click start to begin the simulation, click pause/resume to control the state of the simulation, click on stations to add trains - trains will then prompt for a destination and begin if they can find that route. <br><br> 
<b>Version 4:</b><br>
  Click to select a configuration & click start to begin the simulation, or click custom to begin building a custom configuration & click start to begin the custom configuration. Clicking custom displays the builder (which provides further instructions & specifications). Click pause/resume to control the state of the simulation, click on stations to add trains - trains will then prompt for a destination and begin if they can find that route or display an error message and re-prompt for destination (close this window to cancel addition of train). <br><br>   

# Configuration file:
If you would like to create a .txt file configuration, rather than using the custom builder, follow these format specifications.<br>
<b>Format specifications:</b><br>
  <b>Key:</b><br>
  Tracks are denoted by 'T'.<br>
  Lights are denoted by 'L'.<br>
  Switches are denoted by 'S'.<br>
  Start & end of rail is denoted by ':' on each side of the rail.<br>
  Station names can be any sequence of characters and should be placed before or after ':' depending on which endpoint the station is 
  to be placed.<br><br>
  
<b>Example:</b><br>
  Albuquerque:TTLSLTT:Rio Rancho<br>
  Santa Fe:TTLSLTT:<br>
  (This would be a two rail configuration with 3 stations)<br><br>
  
<b>Notes:</b><br>
    If you don't want a station on an endpoint just leave the station name empty.<br>
    The ':' should only be used as a seperator from station names and tracks.<br><br>
  
# Known Bugs:
  Pause not working for custom configurations - custom configurations do not stop the animation timer correctly (quick fix to this was just disabling the pause button for custom configurations).<br>
  Configurations from builder (not from file) seem to run slightly differently (train appears to jump on occasion), this issue does not occur with configurations from files.<br>
