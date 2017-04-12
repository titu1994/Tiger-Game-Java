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

Caveats
-----------
Due to the different implementations of Python's random number generator and Java's Random number generater, the same listeneing accuracy of 85 % provides very different results on Java than on Python. 

Below are the comparisons of Java and Python on 100 million runs.

- Java (85 % accuracy) = 32.46 +- 1.824 
- Python (85 % accuracy) = 197.496 +- 0.598

This is due to a constant bias in Java's RNG, compared to Python's RNG. On subtracting the constant bias factor of 3 % to the generated probabilities, the scores match.

- Java (85 % accuracy, 3 % bias) = 198.17 +- 0.632

Performance
-----------

100 million games with default settings - 2 minutes 14 seconds
