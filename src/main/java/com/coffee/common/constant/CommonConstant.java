package com.coffee.common.constant;
/**
 * This is the class contains common constants of application.
 *
 * @author Sheranga Perera
 * @date 2025-05-28
 */
public final class CommonConstant {

    public static final String PLACED = "PLACED";
    public static final String CANCELLED = "CANCELLED";
    public static final String IN_PROGRESS = "IN PROGRESS";
    public static final String COMPLETED = "COMPLETED";

    private CommonConstant() {throw new IllegalStateException("CommonConstant class");}


    public static final class Database{

        private Database() {
            throw new IllegalStateException("Database class");
        }

        public static final String RECORD_SUCCESS = "Record Saved Successfully.";
        public static final String DELETE_SUCCESS = "Record Deleted Successfully.";
    }

    public static final class Error{

        private Error() {throw new IllegalStateException("Exception class");}
        public static final String RECORD_EXISTS = "Record Already exists";

    }
}
