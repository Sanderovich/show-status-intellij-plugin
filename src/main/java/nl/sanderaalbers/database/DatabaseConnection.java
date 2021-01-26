package nl.sanderaalbers.database;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import nl.sanderaalbers.notifications.Notifier;
import nl.sanderaalbers.settings.ShowStatusSettingsState;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseConnection implements Disposable {

    private String databaseURL;
    private ShowStatusSettingsState settings;

    private Connection connection;

    public DatabaseConnection() {
        this.settings = ShowStatusSettingsState.getInstance();
        this.databaseURL = "jdbc:mysql://" + this.settings.getDatabaseURL();

        this.setConnection();
    }

    public String getStatusDescriptions(String statuses) {
        List<String> statusesList = this.statusesToList(statuses);
        String query = this.getQuery(statusesList.size());
        String statusDescriptions = "";
        String newLine = System.getProperty("line.separator");
        String defaultDescription = "no status descriptions found.";

        if (this.connection == null) {
            return defaultDescription;
        }

        try (PreparedStatement statement = this.connection.prepareStatement(query)) {

            for (int i = 0; i < statusesList.size(); i++) {
                int parameterIndex = (i + 1);
                String status = statusesList.get(i);
                statement.setString(parameterIndex, status);
            }

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                statusDescriptions += "id: " + result.getString(1) + "; name: " + result.getString(2) + "." + newLine;
            }

        } catch (SQLException exception) {
            exception.printStackTrace();

            System.out.println("failed to receive status from database.");
        }

        return statusDescriptions.isEmpty() ? defaultDescription : statusDescriptions;
    }

    public String getQuery(int length) {
        String query = "SELECT cs.status_id, csd.description FROM `cscart_statuses` AS cs LEFT JOIN `cscart_status_descriptions` AS csd ON cs.status_id = csd.status_id WHERE cs.type = 'O' AND cs.status IN (";
        StringBuilder queryBuilder = new StringBuilder(query);

        for (int i = 0; i < length; i++) {
            queryBuilder.append("?");

            if (i != length - 1)
                queryBuilder.append(",");
        }

        queryBuilder.append(")");

        return queryBuilder.toString();
    }

    public List<String> statusesToList(String statuses) {
        List<String> statusArray = Arrays.asList(statuses.split(","));

        return statusArray.stream()
                .map(s -> s.replaceAll("\\.|\\s|'|\"|\\[|\\]", ""))
                .collect(Collectors.toList());
    }

    @Override
    public void dispose() {
        try {
            this.connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();

            System.out.println("failed to close database connection.");
        }
    }

    public void setConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.databaseURL, this.settings.getUsername(), this.settings.getPassword());

            if (this.connection != null) {
                System.out.println("database connection was successful.");
            }
        } catch (SQLException | ClassNotFoundException exception) {
            System.out.println("database connection failed.");

            this.connection = null;

            Notifier.notifyFailedDatabaseConnection();
        }
    }
}
