package flashcards;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    private String importFileName = "";
    private String exportFileName = "";

    public void setImportFileName(String importFileName) {
        this.importFileName = importFileName;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    public String getImportFileName() {
        return importFileName;
    }

    public String getExportFileName() {
        return exportFileName;
    }


    private final static Scanner scanner = new Scanner(System.in);
    private final static StringBuilder sb = new StringBuilder();
    private static ArrayList<FlashCard> list = new ArrayList<>();

    public void run() throws IOException {
        loadCards();
        while (true) {
            printL("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String option = scan();
            switch (option) {
                case "add":
                    add();
                    break;
                case "remove":
                    delete();
                    break;
                case "import":
                    load();
                    break;
                case "export":
                    export();
                    break;
                case "ask":
                    ask();
                    break;
                case "log":
                    log();

                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    resetStates();
                    break;

                case "exit":
                    printL("Bye bye!");
                    saveCards();
                    System.exit(0);
                    break;
                default:
                    System.out.print("Invalid operation Try again!");
                    break;

            }


        }
    }

    private void saveCards() {
        if (!exportFileName.equals("")) {
            try {
                SerializationUtils.serialize(list, exportFileName);
            } catch (FileNotFoundException e) {
                printL("File not found.");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            printF(String.valueOf(list.size()), "%s cards have been saved.\n");

        }
    }

    private void loadCards() {
        if (!importFileName.equals("")) {
            ArrayList<FlashCard> newList = new ArrayList<>();
            try {

                newList = (ArrayList<FlashCard>) SerializationUtils.deserialize(importFileName);
                addCards(newList);
            } catch (FileNotFoundException e) {
                printL("File not found.");
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
            printF(String.valueOf(newList.size()), "%s cards have been loaded.\n");
            printEmptyLine();
        }
    }

    private void resetStates() {
        for (FlashCard card : list) {
            card.setMistakes(0);
        }

        printL("Card statistics have been reset.");

    }

    private void hardestCard() {
        StringBuilder sbMis = new StringBuilder();
        int count = 0;
        int max = getMaxMistake();
        if (max != 0) {
            for (FlashCard card : list) {
                if (card.getMistakes() == max) {
                    sbMis.append("\"").append(card.getTerm()).append("\", ");
                    count++;
                }
            }

            sbMis.replace(sbMis.length() - 2, sbMis.length(), "");
            String str;
            if (count == 1) {
                str = String.format("The hardest card is \"%s\". You have %d errors answering it.\n", sbMis, max);
            } else {
                str = String.format("The hardest cards are %s. You have %d errors answering them.\n", sbMis, max);
            }
            printL(str);
        } else {
            printL("There are no cards with errors.");
        }
    }


    private int getMaxMistake() {
        int Max = 0;
        for (FlashCard card : list) {
            Max = Math.max(Max, card.getMistakes());
        }
        return Max;
    }


    private void log() {
        printL("File name:");
        String fileName = scan();
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(sb.toString());
            printL("The log has been saved.");
        } catch (FileNotFoundException e) {
            printL("File not found.");
        } catch (Exception e) {
            printF(e.getMessage(), "An exception occurs %s");
        }

    }

    private String scan() {
        String input = scanner.nextLine();
        sb.append(input).append("\n");
        return input;
    }

    private void ask() {
        printL("How many times to ask?");
        int n = scanner.nextInt();
        scan();
        int count = 0;

        for (FlashCard card : list) {
            printF(card.getTerm(), "Print the definition of \"%s\":\n");
            String answer = scan();
            if (card.getDefinition().equals(answer)) {
                printL("Correct!");
            } else if (isContainsDef(answer)) {
                printF(card.getDefinition(), getCorrectTerm(answer), "Wrong. The right answer is \"%s\"," +
                        "but your definition is correct for \"%s\".\n");
                card.setMistakes(card.getMistakes() + 1);
            } else {
                printF(card.getDefinition(), "Wrong. The right answer is \"%s\".\n");
                card.setMistakes(card.getMistakes() + 1);
            }

            count++;
            if (count == n) {
                break;
            }

        }
    }

    public String getCorrectTerm(String answer) {
        for (FlashCard card : list) {
            if (card.getDefinition().equals(answer)) {
                return card.getTerm();
            }
        }
        return null;
    }


    public boolean isContainsDef(String answer) {
        for (FlashCard card : list) {
            if (card.getDefinition().equals(answer)) {
                return true;
            }
        }
        return false;
    }


    private void export() {
        printL("File name:");
        String fileName = scan();
        try {
            SerializationUtils.serialize(list, fileName);
        } catch (FileNotFoundException e) {
            printL("File not found.");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        printF(String.valueOf(list.size()), "%s cards have been saved.\n");


    }


    private void load() {
        printL("File name:");
        String fileName = scan();
        ArrayList<FlashCard> newList = new ArrayList<>();
        try {

            newList = (ArrayList<FlashCard>) SerializationUtils.deserialize(fileName);
            addCards(newList);
        } catch (FileNotFoundException e) {
            printL("File not found.");
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        printF(String.valueOf(newList.size()), "%s cards have been loaded.\n");
        printEmptyLine();
    }

    private void addCards(ArrayList<FlashCard> newList) {

        for (FlashCard card : newList) {
            boolean over = false;
            for (FlashCard mainCard : list) {
                if (mainCard.getTerm().equals(card.getTerm())) {
                    mainCard.setDefinition(card.getDefinition());
                    over = true;
                }
            }
            if (!over) {
                list.add(card);
            }


        }
    }


    public void addNewCard(String term, String def) {
        boolean added = false;
        for (FlashCard card : list) {
            if (card.getTerm().equals(term)) {
                card.setDefinition(def);
                added = true;
            } else if (card.getDefinition().equals(def)) {
                card.setTerm(term);
                added = true;
            }
        }

        if (!added) {
            list.add(new FlashCard(term, def));
        }
    }

    private void delete() {
        printL("Which card?");
        String choice = scan();
        boolean isRemoved = false;
        for (FlashCard card : list) {
            if (card.getTerm().equals(choice)) {
                list.remove(card);
                isRemoved = true;
                printL("The card has been removed.");
                break;
            }
        }
        if (!isRemoved) {
            printF(choice, "Can't remove \"%s\": there is no such card.\n");
        }

        printEmptyLine();
    }

    private static boolean isContainsTerm(String term) {
        for (FlashCard card : list) {
            if (card.getTerm().equals(term)) {
                return true;
            }
        }
        return false;
    }


    private void add() {
        printL("The card:");
        String term = scan();
        if (isContainsTerm(term)) {
            printF(term, "The card \"%s\" already exists.\n");
        } else {
            printL("The definition of the card:");
            String definition = scan();
            if (isContainsDef(definition)) {
                printF(definition, "The definition \"%s\" already exists.\n");
            } else {
                list.add(new FlashCard(term, definition));
                printF(term, definition, "The pair (\"%s\":\"%s\") has been added.\n");
            }
        }
        printEmptyLine();
    }

    private void printEmptyLine() {
        System.out.println();
    }


    private void printF(String term, String definition, String s) {
        String str = String.format(s, term, definition);
        sb.append(str).append("\n");
        System.out.printf(s, term, definition);
    }

    private void printL(String s) {
        sb.append(s).append("\n");
        System.out.println(s);
    }

    private void printF(String choice, String s) {
        String str = String.format(s, choice);
        sb.append(str).append("\n");
        System.out.printf(s, choice);
    }
}
