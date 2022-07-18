package models;

/**
 * Used for initalising the scenes with player's information regardless of the
 * actual controller
 * Used for polymorphism
 */
public interface Initialise {
    void initialise(Player p);
}
