package database;

public class EnrollmentSQL {


    private static volatile EnrollmentSQL instance;

    public static EnrollmentSQL getInstance() {
        if (instance == null) {
            synchronized (EnrollmentSQL.class) {
                if (instance == null) {
                    instance = new EnrollmentSQL();
                }
            }
        }
        return instance;
    }
}
