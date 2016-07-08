package action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;
import entity.Element;
import form.EntryList;
import listener.ICancelListener;
import listener.IConfirmListener;
import utils.Definitions;
import utils.LayoutCreator;
import utils.Utils;

import javax.swing.*;
import java.util.ArrayList;

public class LayoutCreatorAction extends BaseGenerateAction implements IConfirmListener, ICancelListener {

    protected JFrame mDialog;

    @SuppressWarnings("unused")
    public LayoutCreatorAction() {
        super(null);
    }

    @SuppressWarnings("unused")
    public LayoutCreatorAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);

        actionPerformedImpl(project, editor);
    }

    @Override
    public void actionPerformedImpl(Project project, Editor editor) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiFile layout = Utils.getLayoutFileFromCaret(editor, file);

        if (layout == null) {
            Utils.showErrorNotification(project, "No layout found");
            return; // no layout found
        }

        ArrayList<Element> elements = Utils.getIDsFromLayout(layout);
        if (!elements.isEmpty()) {
            showDialog(project, editor, elements);
        } else {
            Utils.showErrorNotification(project, "No IDs found in layout");
        }
    }

    public void onConfirm(Project project, Editor editor, ArrayList<Element> elements, String fieldNamePrefix, boolean createHolder) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (file == null) {
            return;
        }
        PsiFile layout = Utils.getLayoutFileFromCaret(editor, file);

        closeDialog();

        // count selected elements
        int cnt = 0;
        for (Element element : elements) {
            if (element.used) {
                cnt++;
            }
        }

        if (cnt > 0) { // generate injections
            if (layout == null) {
                return;
            }
            new LayoutCreator(file, getTargetClass(editor, file), "Generate Injections", elements, layout.getName(), fieldNamePrefix, createHolder).execute();

            if (cnt == 1) {
                Utils.showInfoNotification(project, "One injection added to " + file.getName());
            } else {
                Utils.showInfoNotification(project, String.valueOf(cnt) + " injections added to " + file.getName());
            }
        } else { // just notify user about no element selected
            Utils.showInfoNotification(project, "No injection was selected");
        }
    }

    public void onCancel() {
        closeDialog();
    }

    protected void showDialog(Project project, Editor editor, ArrayList<Element> elements) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (file == null) {
            return;
        }
        PsiClass clazz = getTargetClass(editor, file);

        if (clazz == null) {
            return;
        }

        // get parent classes and check if it's an adapter
        boolean createHolder = false;
        PsiReferenceList list = clazz.getExtendsList();
        if (list != null) {
            for (PsiJavaCodeReferenceElement element : list.getReferenceElements()) {
                if (Definitions.adapters.contains(element.getQualifiedName())) {
                    createHolder = true;
                }
            }
        }

        EntryList panel = new EntryList(project, editor, elements, createHolder, this, this);

        mDialog = new JFrame();
        mDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mDialog.getRootPane().setDefaultButton(panel.getConfirmButton());
        mDialog.getContentPane().add(panel);
        mDialog.pack();
        mDialog.setLocationRelativeTo(null);
        mDialog.setVisible(true);
    }

    protected void closeDialog() {
        if (mDialog == null) {
            return;
        }

        mDialog.setVisible(false);
        mDialog.dispose();
    }
}
