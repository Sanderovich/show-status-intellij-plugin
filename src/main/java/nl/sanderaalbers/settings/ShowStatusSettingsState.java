package nl.sanderaalbers.settings;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.passwordSafe.impl.providers.EncryptionUtil;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.Pass;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
    name = "nl.sanderaalbers.settings.ShowStatusSettingsState",
    storages = {@Storage("ShowStatusSettings.xml")}
)
public class ShowStatusSettingsState implements PersistentStateComponent<ShowStatusSettingsState> {

    public String databaseURL = "";

    private String key = "showstatuscredentials";
    private CredentialAttributes credentialAttributes = this.createCredentialAttributes(this.key);
    private Credentials credentials = PasswordSafe.getInstance().get(this.credentialAttributes);

    public static ShowStatusSettingsState getInstance() {
        return ServiceManager.getService(ShowStatusSettingsState.class);
    }

    @Override
    public @Nullable ShowStatusSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ShowStatusSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public String getDatabaseURL() {
        return this.databaseURL;
    }

    public String getUsername() {
        if (this.credentials == null) {
            return "";
        }

        String username = this.credentials.getUserName();
        return StringUtil.defaultIfEmpty(username, "");
    }

    public String getPassword() {
        if (this.credentials == null) {
            return "";
        }

        String password = this.credentials.getPasswordAsString();
        return StringUtil.defaultIfEmpty(password, "");
    }

    public void setCredentials(String username, String password) {
        this.credentials = new Credentials(username, password);
        PasswordSafe.getInstance().set(this.credentialAttributes, this.credentials);
    }

    private CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName("ShowDateStore", key));
    }
}
