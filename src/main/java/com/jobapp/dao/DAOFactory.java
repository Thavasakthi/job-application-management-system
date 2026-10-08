package com.jobapp.dao;

import com.jobapp.config.DatabaseConfig;

public class DAOFactory {
    private static UserDAO userDAO;
    private static JobDAO jobDAO;
    private static ApplicationDAO applicationDAO;

    public static synchronized UserDAO getUserDAO() {
        if (userDAO == null) {
            if (DatabaseConfig.isDatabaseAvailable()) {
                userDAO = new UserDAOImpl();
            } else {
                userDAO = new UserDAOInMemoryImpl();
            }
        }
        return userDAO;
    }

    public static synchronized JobDAO getJobDAO() {
        if (jobDAO == null) {
            if (DatabaseConfig.isDatabaseAvailable()) {
                jobDAO = new JobDAOImpl();
            } else {
                jobDAO = new JobDAOInMemoryImpl();
            }
        }
        return jobDAO;
    }

    public static synchronized ApplicationDAO getApplicationDAO() {
        if (applicationDAO == null) {
            if (DatabaseConfig.isDatabaseAvailable()) {
                applicationDAO = new ApplicationDAOImpl();
            } else {
                applicationDAO = new ApplicationDAOInMemoryImpl();
            }
        }
        return applicationDAO;
    }

    public static synchronized void reset() {
        userDAO = null;
        jobDAO = null;
        applicationDAO = null;
    }
}
