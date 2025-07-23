package enums;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static enums.CliMenuHandler.loggingIn;
import static enums.CliMenuHandler.registration;
import static enums.CliMenuHandler.loginUser;


public enum MainMenuOption {

    REGISTER("1","Register a New User",scanner -> registration()),
    LOGIN_USER("2","Login if an existing User", scanner -> loginUser()),
    LOGIN_ACCOUNT("3","Login to an Account",scanner -> loggingIn()),
    EXIT("0", "Exit", scanner -> {
        System.out.println("<<<<<<<Exiting>>>>>>>");
        CliMenuHandler.setRunning(false);
    }),
    INVALID("-1","Invalid Option",scanner -> System.out.println("Invalid Input try again"));


    private final String code;
    private final String description;
    private final MenuAction action;


    MainMenuOption(String code, String description, MenuAction action) {
        this.code = code;
        this.description = description;
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public MenuAction getAction() {
        return action;
    }


    private static final Map<String, MainMenuOption> LOOKUP_MAP = new LinkedHashMap<>();

    static { // Static initializer block runs once when the enum is loaded
        for (MainMenuOption option : values()) { // Iterates through all enum constants
            if (option != INVALID) { // We don't want users to select the 'INVALID' code
                LOOKUP_MAP.put(option.getCode(), option);
            }
        }
    }

    // Method to find an enum option from a given code (user input)
    public static Optional<MainMenuOption> fromCode(String code) {
        return Optional.ofNullable(LOOKUP_MAP.get(code)); // Returns Optional to handle not found
    }

    // Method to print the menu dynamically
    public static void printMenu() {
        System.out.printf("%n************************%nWelcome to the Banking app%n************************%n");
        LOOKUP_MAP.forEach((code, option) ->
                System.out.println(option.getCode() + "." + option.getDescription()));
        System.out.print("Enter your choice: ");
    }

}
