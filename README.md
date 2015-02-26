# Eclipse calculator

Providing probability calculations for [Eclipse board game](http://en.wikipedia.org/wiki/Eclipse_%28board_game%29).

[![Build Status](https://travis-ci.org/DarthKipsu/eclipse-calc.svg)](https://travis-ci.org/DarthKipsu/eclipse-calc) [![Dependencies Status](http://jarkeeper.com/DarthKipsu/eclipse-calc/status.png)](http://jarkeeper.com/DarthKipsu/eclipse-calc)

### Usage

User interface for selecting and upgrading ships [can be found here](http://darth.kipsu.fi/EclipseCalculator/). (First time run can be slow if [Heroku](http://heroku.com/) needs to restart.)

Ships use a simple Clojure map structure. To use the app through repl use ships based on the following form:

```Clojure
{:state "defender",                 ;defending or attacking ship
 :components {:dice1HPmissile 0,    ;number of 1HP missile dice
              :dice2HPmissile 0,    ;number of 2HP missile dice
              :dice1HP 2,           ;number of 1HP dice
              :dice2HP 1,           ;number of 2HP dice
              :dice4HP 0,           ;number of 4HP dice
              :computer 1,          ;bonus from computers
              :shield -1,           ;bonus from shields as a negative number
              :hull 2}              ;strength of hull improvements
  :hits [1 0 0 0]                   ;hits with indexes:
                                    ;0=all hit possibilities 1=0 hits 2=1 hits etc.
  :alive 1                          ;odds for the ship to be alive
  :init 0}                          ;initiative order starting from 0
```

### Todo/ideas

- Change user interface and calculator traffic to support more ships in a fight
- Better input validation
- Allow for user to select how many rounds should be calculated
- Selecting order of target ships
