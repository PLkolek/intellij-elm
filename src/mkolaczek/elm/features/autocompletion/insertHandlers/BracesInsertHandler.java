package mkolaczek.elm.features.autocompletion.insertHandlers;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;

public class BracesInsertHandler implements InsertHandler<LookupElement> {
    @Override
    public void handleInsert(InsertionContext context, LookupElement item) {
        Editor editor = context.getEditor();
        Project project = editor.getProject();
        if (project != null) {
            EditorModificationUtil.insertStringAtCaret(editor, " {}");
            editor.getCaretModel()
                  .moveToOffset(editor.getCaretModel().getOffset() - 1);
            PsiDocumentManager.getInstance(project)
                              .commitDocument(editor.getDocument());
        }
    }
}
