package nl.sanderaalbers.notifications;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import nl.sanderaalbers.settings.ShowStatusSettingsConfigurable;
import org.jetbrains.annotations.NotNull;

public class Notifier {

    public static void notifyFailedDatabaseConnection() {
        Project project = ProjectManager.getInstance().getDefaultProject();

        Notification notification = new Notification(
                "ShowStatusNotificationGroup",
                "ShowStatus No Database Connection",
                "Ensure the credentials are correct.",
                NotificationType.ERROR
        );
        notification.addAction(new NotificationAction("Click to open settings") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent event, @NotNull Notification notification) {
                DataContext dataContext = event.getDataContext();
                Project project = PlatformDataKeys.PROJECT.getData(dataContext);
                ShowSettingsUtil.getInstance().showSettingsDialog(project,
                        ShowStatusSettingsConfigurable.class);
            }
        });
        Notifications.Bus.notify(notification, project);
    }

}
