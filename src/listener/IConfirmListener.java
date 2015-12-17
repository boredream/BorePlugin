package listener;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import entity.Element;

import java.util.ArrayList;

public interface IConfirmListener {

    void onConfirm(Project project, Editor editor, ArrayList<Element> elements, String mPrefix, boolean mCreateHolder);
}
