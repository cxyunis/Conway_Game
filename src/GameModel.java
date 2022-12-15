
/*
 * RULES OF THE GAME
        1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
        2. Any live cell with two or three live neighbours lives on to the next generation.
        3. Any live cell with more than three live neighbours dies, as if by overpopulation.
        4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
 * ----------------------------------------------------------------------------------------------------
 * the brain of the game that has following responsibilities
 * 1. record the state of each cell/grid
 * 2. apply the rules of games
 * 3. received players name from Conway - as OBSERVER to Conway (may be not)
 * 4. provide notification to player on their turn - player as OBSERVER to here (may be not)
 * 5. check if the valid move of the player - player as OBSERVER to here and vice versa (may be not)
 * 6. check player must click one cell to live, and one opponent cell to dead
 * 7.
 * */
public class GameModel {

    // method to ask player name

    // method to choose the first player to play according to the name sorted in alphabetical order

    // method to ask player to choose where to put the player.

    // method to register to Conway for mouse click on cell to obtain cell location

    // method as OBSERVER to Conway for listening to event for mouse click

    //
    private boolean isRuleOneCorrect() {
        return true;
    }
    private boolean isRuleTwoCorrect() {
        return true;
    }
    private boolean isRuleThreeCorrect() {
        return true;
    }
    private boolean isRuleFourCorrect() {
        return true;
    }
}