package database;

public class StudentSQL {
    private static volatile  StudentSQL instance;

    public static StudentSQL getInstance() {
        if (instance == null) {
            synchronized (StudentSQL.class) {
                if (instance == null) {
                    instance = new StudentSQL();
                }
            }
        }
        return instance;
    }


}
