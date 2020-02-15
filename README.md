[![Build Status](https://travis-ci.org/artjcod/TennisMatch.svg?branch=master)](https://travis-ci.org/artjcod/TennisMatch)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=tennis&metric=alert_status)](https://sonarcloud.io/dashboard?id=artjcod_TennisMatch)
[![codecov](https://codecov.io/gh/artjcod/TennisMatch/branch/master/graph/badge.svg)](https://codecov.io/gh/artjcod/TennisMatch)
[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/artjcod/TennisMatch/blob/master/LICENSE)

# Tennis Match Kata
  
  This kata consists in implementing a Tennis match and below the rules :
  
💡 __NB__: A match is won when a player has won the majority of the prescribed number of sets [(best-of-five)](https://en.wikipedia.org/wiki/Playoff_format#Best-of-five_playoff) in this kata.
  A __best-of-five__ is a head-to-head competition between two teams, wherein one must win three games to win the series.
  Three is chosen as it constituted a majority of games played. 
  If one team wins the series before reaching game 5, all others are ignored.
 
  
   1.  The game starts with a score of 0 point for each player
   2.  Each time a player wins a point, the Game score changes as follow: 0 -> 15 -> 30 -> 40-> Win game.
   3.  If the 2 players reach the score 40, the DEUCE rule is activated.
   4.  If the score is DEUCE , the player who  win the point take the ADVANTAGE.
   5.  If the player who has the ADVANTAGE win the  point, he wins the game.
   6.  If the player who has the ADVANTAGE loses the point, the score is DEUCE.
   7.  The set starts with a score of 0 Game for each player.
   8.  Each time a player wins a Game (see SPRINT 1), the Set score changes as follow: 1 -> 2 -> 3 -> 4 -> 5 -> 6 (-> 7)
   9.  If a player reaches the Set score of 6 and the other player has a Set score of 4 or lower, the player win the Set.
   10. If a player wins a Game and reach the Set score of 6 and the other player has a Set score of 5, a new Game must be played and the      first player who reach the score of 7 wins the set.
   11. If the 2 players reach the score of 6 Games, the Tie-Break rule is activated.
   12. Each time a player wins a point, the score changes as follow 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 (-> 8-> 9-> 10-> …)
   13. The Tie-Break ends as soon as a player gets a least 7 points and 2 points more than his opponent.
   14. The player who wins the Tie-Break wins the Set.
   

👉For more information see [Tennis scoring system](https://en.wikipedia.org/wiki/Tennis_scoring_system)



### Authors 📚

* **Souheil Naceur** - [Linkedin](https://www.linkedin.com/in/souheil-naceur-abbb9622)

### Prerequisites 

To build you will need Git,JDK 8 and Maven 3.3.1 or newer. Be sure that your JAVA_HOME environment variable points to the jdk1.8.0 folder extracted from the JDK download.

### Get the Source Code

```
git clone git@github.com:artjcod/TennisMatch.git
cd TennisMatch
```
### Build from the Command Line

To build Maven:
```
mvn install
```
### Build Status in travis CI

[![Build Status](https://travis-ci.org/artjcod/TennisMatch.svg?branch=master)](https://travis-ci.org/artjcod/TennisMatch)

### Running the tests

 1. Jacoco plugin has been added to the pom configuration and it is part of the build report check the target/site/jacoco folder.

```
mvn clean test
mvn jacoco:report
```
2. Findbugs plugin is used too and it fails the build in case of violation. 
To get the html report check the folder target/site once you have run the following command :

```
mvn clean test site
```
### License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details

