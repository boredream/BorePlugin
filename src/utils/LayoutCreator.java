package utils;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.EverythingGlobalScope;
import entity.Element;

import java.util.ArrayList;


public class LayoutCreator extends WriteCommandAction.Simple {

    protected PsiFile mFile;
    protected Project mProject;
    protected PsiClass mClass;
    protected ArrayList<Element> mElements;
    protected PsiElementFactory mFactory;
    protected String mLayoutFileName;
    protected String mFieldNamePrefix;
    protected boolean mCreateHolder;

    public LayoutCreator(PsiFile file, PsiClass clazz, String command, ArrayList<Element> elements, String layoutFileName, String fieldNamePrefix, boolean createHolder) {
        super(clazz.getProject(), command);

        mFile = file;
        mProject = clazz.getProject();
        mClass = clazz;
        mElements = elements;
        mFactory = JavaPsiFacade.getElementFactory(mProject);
        mLayoutFileName = layoutFileName;
        mFieldNamePrefix = fieldNamePrefix;
        mCreateHolder = createHolder;
    }

    @Override
    public void run() throws Throwable {
        if (mCreateHolder) {
            generateAdapter();
        } else {
            generateFields();
            generateFindViewById();
        }

        // reformat class
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        styleManager.optimizeImports(mFile);
        styleManager.shortenClassReferences(mClass);
        new ReformatCodeProcessor(mProject, mClass.getContainingFile(), null, false).runWithoutProgress();
    }

    /**
     * Create ViewHolder for adapters with injections
     */
    protected void generateAdapter() {
        // view holder class
        String holderClassName = Utils.getViewHolderClassName();
        StringBuilder holderBuilder = new StringBuilder();

        // generator of view holder class
        StringBuilder generator = new StringBuilder();
        generator.append("public " + holderClassName + "(android.view.View rootView) {\n");

        // rootView
        String rootViewName = "rootView";
        holderBuilder.append("public " + "android.view.View " + rootViewName + ";\n");
        generator.append("this." + rootViewName + " = " + rootViewName + ";\n");

        for (Element element : mElements) {
            if (!element.used) {
                continue;
            }

            // field
            holderBuilder.append("public " + element.name + " " + element.fieldName + ";\n");

            // findViewById in generator
            generator.append("this." + element.fieldName + " = (" + element.name + ") "
                    + rootViewName + ".findViewById(" + element.getFullID() + ");\n");
        }
        generator.append("}\n");

        holderBuilder.append(generator.toString());

        PsiClass viewHolder = mFactory.createClassFromText(holderBuilder.toString(), mClass);
        viewHolder.setName(holderClassName);
        mClass.add(viewHolder);
        mClass.addBefore(mFactory.createKeyword("public", mClass), mClass.findInnerClassByName(holderClassName, true));
        mClass.addBefore(mFactory.createKeyword("static", mClass), mClass.findInnerClassByName(holderClassName, true));
    }

    /**
     * Create fields for injections inside main class
     */
    protected void generateFields() {
        for (Element element : mElements) {
            if (!element.used) {
                continue;
            }

            mClass.add(mFactory.createFieldFromText("private " + element.name + " " + element.fieldName + ";", mClass));
        }
    }

    protected void generateFindViewById() {
        PsiClass activityClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.app.Activity", new EverythingGlobalScope(mProject));
        PsiClass fragmentClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.app.Fragment", new EverythingGlobalScope(mProject));
        PsiClass supportFragmentClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.support.v4.app.Fragment", new EverythingGlobalScope(mProject));

        // Check for Activity class
        if (activityClass != null && mClass.isInheritor(activityClass, true)) {
            if (mClass.findMethodsByName("onCreate", false).length == 0) {
                // Add an empty stub of onCreate()
                StringBuilder method = new StringBuilder();
                method.append("@Override protected void onCreate(android.os.Bundle savedInstanceState) {\n");
                method.append("super.onCreate(savedInstanceState);\n");
                method.append("\t// TODO: add setContentView(...) and run LayoutCreator again\n");
                method.append("}");

                mClass.add(mFactory.createMethodFromText(method.toString(), mClass));
            } else {
                PsiMethod onCreate = mClass.findMethodsByName("onCreate", false)[0];
                for (PsiStatement statement : onCreate.getBody().getStatements()) {
                    // Search for setContentView()
                    if (statement.getFirstChild() instanceof PsiMethodCallExpression) {
                        PsiReferenceExpression methodExpression
                                = ((PsiMethodCallExpression) statement.getFirstChild())
                                .getMethodExpression();
                        if (methodExpression.getText().equals("setContentView")) {
                            // Insert initView() after setContentView()
                            onCreate.getBody().addAfter(mFactory.createStatementFromText("initView();", mClass), statement);

                            // generator findViewById code in initView() method
                            StringBuilder initView = new StringBuilder();
                            initView.append("private void initView() {\n");

                            boolean hasClickable = false;
                            for (Element element : mElements) {
                                if (!element.used) {
                                    continue;
                                }

                                initView.append(element.fieldName + " = (" + element.name + ") findViewById(" + element.getFullID() + ");\n");

                                // set flag
                                if (!hasClickable) {
                                    hasClickable = element.isClickable();
                                }
                            }

                            // generator clickable code if need
                            StringBuilder clickable = new StringBuilder();
                            if(hasClickable) {
                                // let class implement OnClickListener
                                PsiReferenceList implementsList = mClass.getImplementsList();
                                if(implementsList != null) {
                                    PsiJavaCodeReferenceElement[] referenceElements = implementsList.getReferenceElements();
                                    boolean hasImpl = false;
                                    for(PsiJavaCodeReferenceElement re : referenceElements) {
                                        hasImpl = re.getText().contains("OnClickListener");
                                    }
                                    // add implement if not exist
                                    if(!hasImpl) {
                                        PsiJavaCodeReferenceElement pjcre = mFactory.createReferenceElementByFQClassName(
                                                "android.view.View.OnClickListener", mClass.getResolveScope());
                                        implementsList.add(pjcre);
                                    }
                                }

                                if(implementsList != null &&
                                        (implementsList.getText() != null && implementsList.getText().contains("OnClickListener"))) {
                                }

                                initView.append("\n");

                                clickable.append("@Override public void onClick(View v) {\n")
                                        .append("switch (v.getId()) {\n");

                                for (Element element : mElements) {
                                    if (!element.used || !element.isClickable()) {
                                        continue;
                                    }

                                    // generator setOnClickListener code in initView()
                                    initView.append(element.fieldName + ".setOnClickListener(this);\n");

                                    // generator override public void onClick(View v) method
                                    clickable.append("case " + element.getFullID() + " :\n\nbreak;\n");
                                }

                                clickable.append("}\n}");
                            }

                            initView.append("}");
                            mClass.add(mFactory.createMethodFromText(initView.toString(), mClass));

                            if(hasClickable) {
                                mClass.add(mFactory.createMethodFromText(clickable.toString(), mClass));
                            }
                            break;
                        }
                    }
                }
            }
            // Check for Fragment class
        } else if ((fragmentClass != null && mClass.isInheritor(fragmentClass, true)) || (supportFragmentClass != null && mClass.isInheritor(supportFragmentClass, true))) {
            if (mClass.findMethodsByName("onCreateView", false).length == 0) {
                // Add an empty stub of onCreateView()
                StringBuilder method = new StringBuilder();
                method.append("@Override public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {\n");
                method.append("\t// TODO: inflate a fragment view\n");
                method.append("android.view.View rootView = super.onCreateView(inflater, container, savedInstanceState);\n");
//                method.append(butterKnife.getCanonicalBindStatement());
                method.append("(this, rootView);\n");
                method.append("return rootView;\n");
                method.append("}");

                mClass.add(mFactory.createMethodFromText(method.toString(), mClass));
            } else {
                PsiMethod onCreateView = mClass.findMethodsByName("onCreateView", false)[0];
                for (PsiStatement statement : onCreateView.getBody().getStatements()) {
                    if (statement instanceof PsiReturnStatement) {
                        String returnValue = ((PsiReturnStatement) statement).getReturnValue().getText();
                        if (returnValue.contains("R.layout")) {
                            onCreateView.getBody().addBefore(mFactory.createStatementFromText("android.view.View view = " + returnValue + ";", mClass), statement);
//                                onCreateView.getBody().addBefore(mFactory.createStatementFromText(butterKnife.getCanonicalBindStatement() + "(this, view);", mClass), statement);
                            statement.replace(mFactory.createStatementFromText("return view;", mClass));
                        } else {
                            StringBuilder findViewById = new StringBuilder();
                            for (Element element : mElements) {
                                findViewById.append(element.fieldName + " = (" + element.name + ") findViewById(" + element.getFullID() + ");\n");
                            }
                            onCreateView.getBody().addAfter(mFactory.createStatementFromText(findViewById.toString(), mClass), statement);
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }
}