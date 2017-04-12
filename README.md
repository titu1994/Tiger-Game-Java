# Tiger Game in Java
Tiger Game in Java 8, based on the paper [Planning and acting in partially observable
stochastic domains](http://people.csail.mit.edu/lpk/papers/aij98-pomdp.pdf) by Leslie Kaelbling et al.

Requirements
--------------------------------------
Java 8

Agent
--------------------------------------
Modify the code in agent.py (specifically the 'act' method).

Actions and Observations must be checked against their global constants in Action and Observation classes, as shown in the example model (which uses
Leslie Kaelbling's optimal Finite State Machine model for the Tiger game).

Arguments
--------------------------------------
-i      : Number of games to be played (default 1 million)
-t      : Number of timesteps to play each game (default 100)
-load   : Load the agent which was trained previously.
          Will ask permission to overwrite old agent with new agent if used.
-r      : Set random seed.

Performance
--------------------------------------

100 million games with default settings - 2 minutes 14 seconds
