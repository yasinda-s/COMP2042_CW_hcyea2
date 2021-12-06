package brickdestroy.levels;

import brickdestroy.game.GamePlay;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * This class is responsible for saving both level scores and high scores into files so that they can be stored permanently.
 */
public class SaveScores {
    Writer writerLvlOne;
    Writer writerLvlTwo;
    Writer writerLvlThree;
    Writer writerLvlFour;
    Writer writerLvlFive;
    Writer writerLvlSix;

    private GamePlay gamePlay;
    private HighScore highScore;

    /**
     * This is the constructor for the SaveScores class.
     * @param gamePlay GamePlay object.
     * @param highScore HighScore object.
     * @throws IOException In case the level score files cannot be found.
     */
    public SaveScores(GamePlay gamePlay, HighScore highScore) throws IOException {
        this.gamePlay = gamePlay;
        this.highScore = highScore;

        writerLvlOne = new BufferedWriter(new FileWriter("src/main/resources/levelOneScore.txt", true));
        writerLvlTwo = new BufferedWriter(new FileWriter("src/main/resources/levelTwoScore.txt", true));
        writerLvlThree = new BufferedWriter(new FileWriter("src/main/resources/levelThreeScore.txt", true));
        writerLvlFour = new BufferedWriter(new FileWriter("src/main/resources/levelFourScore.txt", true));
        writerLvlFive = new BufferedWriter(new FileWriter("src/main/resources/levelFiveScore.txt", true));
        writerLvlSix = new BufferedWriter(new FileWriter("src/main/resources/levelSixScore.txt", true));
    }

    /**
     * This method is used to save the final scores permanently into the text file so that a permanent list of high scores can be made.
     * @throws IOException In case the file cannot be found in the directory.
     */
    public void saveTotalScore() throws IOException { //this works properly!!
        if(highScore.getWriter()==null){
            assert false;
            highScore.getWriter().write(gamePlay.getScore() + " ");
        }else{
            highScore.getWriter().write(" " + gamePlay.getScore() + " ");
        }
        highScore.getWriter().close();
    }

    /**
     * This method is responsible for saving the individual level scores onto text files so that a permanent list of level scores can be made.
     * @throws IOException In case the file is not accessible.
     */
    public void saveLevelScores() throws IOException{
        if(gamePlay.getLevel()==1){
            saveLevelScoreToFile(writerLvlOne, gamePlay.getScoreLvlOne());
        }else if(gamePlay.getLevel()==2){
            saveLevelScoreToFile(writerLvlTwo, gamePlay.getScoreLvlTwo());
        }else if(gamePlay.getLevel()==3){
            saveLevelScoreToFile(writerLvlThree, gamePlay.getScoreLvlThree());
        }else if(gamePlay.getLevel()==4){
            saveLevelScoreToFile(writerLvlFour, gamePlay.getScoreLvlFour());
        }else if(gamePlay.getLevel()==5){
            saveLevelScoreToFile(writerLvlFive, gamePlay.getScoreLvlFive());
        }else if(gamePlay.getLevel()==6){
            saveLevelScoreToFile(writerLvlSix, gamePlay.getScoreLvlSix());
        }
    }

    /**
     * Method used to save the score of one level to text file.
     * @param writer The writer of the specific level.
     * @param scoreForLevel The score to be saved in that file.
     * @throws IOException In case the file is not accessible.
     */
    private void saveLevelScoreToFile(Writer writer, int scoreForLevel) throws IOException{
        if(writer==null){
            assert false;
            writer.write(scoreForLevel + " ");
        }else{
            writer.write(" " + scoreForLevel + " ");
        }
        writer.close();
    }
}
