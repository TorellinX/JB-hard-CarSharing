package carsharing;

public class Main {

    public static void main(String[] args) {
        String dbName = null;
        if (args.length > 0 && args[0].equals("-databaseFileName")) {
            dbName = args[1];
        }
        new App().start(dbName);
    }
}