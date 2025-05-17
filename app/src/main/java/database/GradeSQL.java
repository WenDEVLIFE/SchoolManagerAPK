package database;

public class GradeSQL {

    private static volatile GradeSQL instance;

    public static GradeSQL getInstance() {
        if (instance == null) {
            synchronized (GradeSQL.class) {
                if (instance == null) {
                    instance = new GradeSQL();
                }
            }
        }
        return instance;
    }


}
