package mkolaczek.elm.features.structureView;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import mkolaczek.elm.psi.ElmFile;
import org.jetbrains.annotations.NotNull;

public class ElmStructureViewModel extends StructureViewModelBase implements
        StructureViewModel.ElementInfoProvider {
    public ElmStructureViewModel(PsiFile psiFile) {
        super(psiFile, root(psiFile));
    }

    private static ElmStructureViewElement root(PsiFile psiFile) {
        if (psiFile instanceof ElmFile) {
            ElmFile file = (ElmFile) psiFile;
            return new ElmStructureViewElement(file.module());
        }
        throw new IllegalArgumentException("Elm structure view on non Elm file?!");
    }

    @Override
    @NotNull
    public Sorter[] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }


    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return false;
    }
}