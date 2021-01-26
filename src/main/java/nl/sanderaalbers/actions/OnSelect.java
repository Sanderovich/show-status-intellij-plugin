package nl.sanderaalbers.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;
import nl.sanderaalbers.database.DatabaseConnection;
import org.jetbrains.annotations.NotNull;

public class OnSelect extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        System.out.println("calling update() in OnSelect.");

        final Project project = event.getProject();
        final Editor editor = event.getData(CommonDataKeys.EDITOR);

        event.getPresentation().setEnabledAndVisible(project != null
                && editor != null
                && editor.getSelectionModel().hasSelection());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        System.out.println("calling actionPerformed() in OnSelect.");
        DatabaseConnection database = ServiceManager.getService(DatabaseConnection.class);

        final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        final Document document = editor.getDocument();

        Caret caret = editor.getCaretModel().getPrimaryCaret();
        int start = caret.getSelectionStart();
        int end = caret.getSelectionEnd();

        TextRange selection = new TextRange(start, end);
        String status = document.getText(selection);

        String result = database.getStatusDescriptions(status);
        String title = "Status Result";
        Messages.showInfoMessage(result, title);
    }
}
