package nl.sanderaalbers.settings;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import nl.sanderaalbers.database.DatabaseConnection;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ShowStatusSettingsConfigurable implements Configurable {

    private ShowStatusSettingsComponent settingsComponent;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Show Status Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return this.settingsComponent.getPreferredFocusedComponent();
    }

    @Override
    public @Nullable JComponent createComponent() {
        this.settingsComponent = new ShowStatusSettingsComponent();
        return this.settingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        ShowStatusSettingsState settings = ShowStatusSettingsState.getInstance();

        return !(this.settingsComponent.getDatabaseURLText().equals(settings.getDatabaseURL())
                && this.settingsComponent.getUsernameText().equals(settings.getUsername())
                && this.settingsComponent.getPasswordText().equals(settings.getPassword()));
    }

    @Override
    public void apply() throws ConfigurationException {
        ShowStatusSettingsState settings = ShowStatusSettingsState.getInstance();
        DatabaseConnection database = ServiceManager.getService(DatabaseConnection.class);

        settings.databaseURL = this.settingsComponent.getDatabaseURLText();
        settings.setCredentials(this.settingsComponent.getUsernameText(), this.settingsComponent.getPasswordText());

        database.setConnection();
    }

    @Override
    public void reset() {
        ShowStatusSettingsState settings = ShowStatusSettingsState.getInstance();

        this.settingsComponent.setDatabaseURLText(settings.getDatabaseURL());
        this.settingsComponent.setUsernameText(settings.getUsername());
        this.settingsComponent.setPasswordText(settings.getPassword());
    }

    @Override
    public void disposeUIResources() {
        this.settingsComponent = null;
    }
}
