#!/bin/sh
rm -f MinesHighScores.txt
javac HighScores.java &&
#javac HighScoresTest.java -cp /usr/share/java/junit.jar:. &&
#java -cp /usr/share/java/junit.jar:. org.junit.runner.JUnitCore HighScoresTest
java -cp /usr/share/java/junit.jar:libs/KaboomUtils.jar:. org.junit.runner.JUnitCore HighScoresTest

