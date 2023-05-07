package model;

/**
 * 通信的全部消息类型
 */
public enum MsgType {
    //Start the game
    START_GAME,
    //Play chess
    PLAY_CHESS,
    //Upgraded pawn
    UPGRADE_PAWN,
    //Admit defeat
    ADMIT_DEFEAT,
    //Victory
    GAME_WIN,
    //Failure
    GAME_LOSE
}
