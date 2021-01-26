package nl.sanderaalbers.settings;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ShowStatusSettingsComponent {

    private final JPanel mainPanel;
    private final JBTextField databaseURLText = new JBTextField();
    private final JBTextField usernameText = new JBTextField();
    private final JBPasswordField passwordText = new JBPasswordField();

    public ShowStatusSettingsComponent() {
        this.mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JLabel("Database URL: "), this.databaseURLText, 1, false)
                .addLabeledComponent(new JLabel("Username: "), this.usernameText, 1, false)
                .addLabeledComponent(new JLabel("Password: "), this.passwordText, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return this.mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return this.databaseURLText;
    }

    @NotNull
    public String getDatabaseURLText() {
        String databaseURL = this.databaseURLText.getText();
        return StringUtil.defaultIfEmpty(databaseURL, "");
    }

    public void setDatabaseURLText(@NotNull String databaseURLText) {
        this.databaseURLText.setText(databaseURLText);
    }

    @NotNull
    public String getUsernameText() {
        String username = this.usernameText.getText();
        return StringUtil.defaultIfEmpty(username, "");
    }

    public void setUsernameText(@NotNull String usernameText) {
        this.usernameText.setText(usernameText);
    }

    @NotNull
    public String getPasswordText() {
        String password = new String(this.passwordText.getPassword());
        return StringUtil.defaultIfEmpty(password, "");
    }

    public void setPasswordText(@NotNull String passwordText) {
        this.passwordText.setText(passwordText);
    }
}
