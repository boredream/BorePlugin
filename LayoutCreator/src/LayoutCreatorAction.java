import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import core.CodeFactory;
import ui.ShowCodeDialog;

public class LayoutCreatorAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        PsiFile file = e.getData(PlatformDataKeys.PSI_FILE);

        String content = CodeFactory.createRecyclerViewHolder(file);
        ShowCodeDialog dialog = new ShowCodeDialog(content);
        dialog.pack();
        dialog.setVisible(true);
    }
}
