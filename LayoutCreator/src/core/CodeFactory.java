package core;

import com.intellij.psi.PsiFile;
import entity.Element;
import utils.LayoutUtils;
import utils.StringUtils;

import java.util.ArrayList;

public class CodeFactory {

    /**
     * 生成RecyclerView的adapter文件内容
     */
    public static String createRecyclerViewHolder(PsiFile layoutFile) {
        ArrayList<Element> elements = LayoutUtils.getIDsFromLayout(layoutFile);

        StringBuilder sbAdapterInfo = new StringBuilder();
        sbAdapterInfo.append("\n");

        // ViewHolder class的申明处理
        sbAdapterInfo.append(StringUtils.formatSingleLine(0, "public static class ViewHolder extends RecyclerView.ViewHolder {"));
        for(Element bean : elements) {
            // public TextView item_tv;
            sbAdapterInfo.append("\t")
                    .append("public ")
                    .append(bean.name)
                    .append(" ")
                    .append(bean.id)
                    .append(";\n");
        }

        sbAdapterInfo.append(StringUtils.formatSingleLine(1, "public ViewHolder(final View itemView) {"));
        sbAdapterInfo.append(StringUtils.formatSingleLine(2, "super(itemView);"));
        for(Element bean : elements) {
            // public TextView item_tv;
            sbAdapterInfo.append("\t\t")
                    .append(bean.id)
                    .append(" = (")
                    .append(bean.name)
                    .append(") ")
                    .append("itemView.findViewById(R.id.")
                    .append(bean.id)
                    .append(");\n");
        }
        sbAdapterInfo.append(StringUtils.formatSingleLine(1, "}"));
        sbAdapterInfo.append(StringUtils.formatSingleLine(0, "}"));
        sbAdapterInfo.append("\n");

        return sbAdapterInfo.toString();
    }

}
