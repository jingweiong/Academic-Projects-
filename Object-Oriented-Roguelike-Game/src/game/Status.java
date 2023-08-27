package game;

/**
 * Use this enum class to give `buff` or `debuff`.
 * Created by:
 * @author Riordan D. Alfredo
 * Modified by:
 * @author Jun Lim
 */
public enum Status {
    FRIENDLY, // actors which can enter floor ground
    CAN_GAIN_RUNES, // for allowing players to gain runes from killing enemies
    CAN_DROP_RUNES, // for allowing players to drop runes on death and recover them
    CAN_TRADE, // for allowing actors to trade with traders
    HOSTILE_TO_ENEMY,
    HOSTILE_TO_PLAYER,
    ENEMY_CANINE,   // enemy types to prevent attacking each other
    ENEMY_CRUSTACEAN,
    ENEMY_SKELETAL,
    ENEMY_REVIVER, // pile of bones status to prevent turning into another pile of bones on death
    ENEMY_GIANT, // enemies which can use the slam attack
}
