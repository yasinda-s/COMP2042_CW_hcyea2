package test.levels;

import test.game.GamePlay;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class LevelScore {

    Writer writerLvlOne;
    Writer writerLvlTwo;
    Writer writerLvlThree;
    Writer writerLvlFour;
    Writer writerLvlFive;
    Writer writerLvlSix;

    private List<Integer> lvlOneScoresFromFile;
    private List<Integer> lvlTwoScoresFromFile;
    private List<Integer> lvlThreeScoresFromFile;
    private List<Integer> lvlFourScoresFromFile;
    private List<Integer> lvlFiveScoresFromFile;
    private List<Integer> lvlSixScoresFromFile;

    private GamePlay gamePlay;

    public LevelScore(GamePlay gamePlay) throws IOException {
        this.gamePlay = gamePlay;
        writerLvlOne = new BufferedWriter(new FileWriter("src/test/levelOneScore.txt", true));
        lvlOneScoresFromFile = new ArrayList<>();

        writerLvlTwo = new BufferedWriter(new FileWriter("src/test/levelTwoScore.txt", true));
        lvlTwoScoresFromFile = new ArrayList<>();

        writerLvlThree = new BufferedWriter(new FileWriter("src/test/levelThreeScore.txt", true));
        lvlThreeScoresFromFile = new ArrayList<>();

        writerLvlFour = new BufferedWriter(new FileWriter("src/test/levelFourScore.txt", true));
        lvlFourScoresFromFile = new ArrayList<>();

        writerLvlFive = new BufferedWriter(new FileWriter("src/test/levelFiveScore.txt", true));
        lvlFiveScoresFromFile = new ArrayList<>();

        writerLvlSix = new BufferedWriter(new FileWriter("src/test/levelSixScore.txt", true));
        lvlSixScoresFromFile = new ArrayList<>();
    }

    public void popUpLevelScore() throws FileNotFoundException {
        String pathOne = "src/test/levelOneScore.txt";
        String pathTwo = "src/test/levelTwoScore.txt";
        String pathThree = "src/test/levelThreeScore.txt";
        String pathFour = "src/test/levelFourScore.txt";
        String pathFive = "src/test/levelFiveScore.txt";
        String pathSix = "src/test/levelSixScore.txt";

        if(gamePlay.getLevel()==1){
            getLevelScoreFromFile(pathOne, lvlOneScoresFromFile);
            createScoreMessagePopUp(lvlOneScoresFromFile);
        }else if(gamePlay.getLevel()==2){
            getLevelScoreFromFile(pathTwo, lvlTwoScoresFromFile);
            createScoreMessagePopUp(lvlTwoScoresFromFile);
        }else if(gamePlay.getLevel()==3){
            getLevelScoreFromFile(pathThree, lvlThreeScoresFromFile);
            createScoreMessagePopUp(lvlThreeScoresFromFile);
        }else if(gamePlay.getLevel()==4){
            getLevelScoreFromFile(pathFour, lvlFourScoresFromFile);
            createScoreMessagePopUp(lvlFourScoresFromFile);
        }else if(gamePlay.getLevel()==5){
            getLevelScoreFromFile(pathFive, lvlFiveScoresFromFile);
            createScoreMessagePopUp(lvlFiveScoresFromFile);
        }else if(gamePlay.getLevel()==6){
            getLevelScoreFromFile(pathSix, lvlSixScoresFromFile);
            createScoreMessagePopUp(lvlSixScoresFromFile);
        }
    }

    private void getLevelScoreFromFile(String filePath, java.util.List<Integer> lvlScoresFromFile) throws FileNotFoundException{
        Scanner inputLevelScore = new Scanner(new File(filePath));
        while(inputLevelScore.hasNext()){
            lvlScoresFromFile.add(Integer.parseInt(inputLevelScore.next()));
        }
        lvlScoresFromFile.sort(Collections.reverseOrder());

        if(lvlScoresFromFile.size()>5){
            lvlScoresFromFile = lvlScoresFromFile.subList(0,6);
        }
    }

    public void createScoreMessagePopUp(java.util.List<Integer> lvlScoresFromFile) {
        String message = "";
        if(gamePlay.isBallLost()) {
            if (gamePlay.ballEnd()) {
                message = "Game over!";
            }
        }else if(gamePlay.isDone()){ //if all bricks broken
            if(gamePlay.hasLevel()){ //if user has more levels left
                message = "You can advance to the next level!";
            } else{ //if no more levels
                message = "You completed the game!";}
        }

        StringBuilder displayScore = new StringBuilder("<html>");
        displayScore.append(message).append("<br>");
        displayScore.append("Your score for this level was : ").append(getLevelScore()).append("<br>");
        displayScore.append("<br>").append("Here are the high scores for this level - ").append("<br>").append("<br>");
        if(lvlScoresFromFile.size()>5){
            for(int i=0;i<5;i++){
                String scoreString = String.valueOf(lvlScoresFromFile.get(i));
                displayScore.append(" ").append(scoreString).append("<br>");
            }
        }else{
            for (Integer integer : lvlScoresFromFile){
                displayScore.append(" ").append(integer).append("<br>");
            }
        }
        JOptionPane.showMessageDialog(null, displayScore, "Level " + gamePlay.getLevel() + " High Scores", JOptionPane.PLAIN_MESSAGE);
    }

    private int getLevelScore(){
            int currentLevel = gamePlay.getLevel();
        int levelScore = 0;
        if (currentLevel == 1) {
            levelScore = gamePlay.getScoreLvlOne();
        } else if (currentLevel == 2){
            levelScore = gamePlay.getScoreLvlTwo();
        } else if (currentLevel == 3){
            levelScore = gamePlay.getScoreLvlThree();
        } else if (currentLevel == 4){
            levelScore = gamePlay.getScoreLvlFour();
        } else if (currentLevel == 5) {
            levelScore = gamePlay.getScoreLvlFive();
        } else if (currentLevel == 6) {
            levelScore = gamePlay.getScoreLvlSix();
        }
        return levelScore;
    }

    public Writer getWriterLvlOne() {
        return writerLvlOne;
    }

    public Writer getWriterLvlTwo() {
        return writerLvlTwo;
    }

    public Writer getWriterLvlThree() {
        return writerLvlThree;
    }

    public Writer getWriterLvlFour() {
        return writerLvlFour;
    }

    public Writer getWriterLvlFive() {
        return writerLvlFive;
    }

    public Writer getWriterLvlSix() {
        return writerLvlSix;
    }
}
