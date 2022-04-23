package flashcards;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
       Application app = new Application();
        if (args.length == 2) {
            if (args[0].equals("-import")) {
                app.setImportFileName(args[1]);
            } else {
                app.setExportFileName(args[1]);
            }
        } else if (args.length == 4) {
            if (args[0].equals("-import")) {
                app.setImportFileName(args[1]);
                app.setExportFileName(args[3]);
            } else {
                app.setImportFileName(args[3]);
                app.setExportFileName(args[1]);
            }
        }

        app.run();
    }
}
