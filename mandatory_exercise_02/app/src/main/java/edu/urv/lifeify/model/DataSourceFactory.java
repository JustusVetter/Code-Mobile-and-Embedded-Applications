package edu.urv.lifeify.model;

public class DataSourceFactory {
    // Factory method to return the appropriate DataSource subclass
    public static DataSource getDataSource(DataSource.DataSourceType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case HARDCODED:
                return  DataSourceHardcoded.getInstance();

            default:
                throw new IllegalArgumentException("Unknown data source type: " + type);
        }
    }
}
