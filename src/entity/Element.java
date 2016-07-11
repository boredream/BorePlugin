package entity;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Element {

    // constants
    private static final Pattern sIdPattern = Pattern.compile("@\\+?(android:)?id/([^$]+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern sValidityPattern = Pattern.compile("^([a-zA-Z_\\$][\\w\\$]*)$", Pattern.CASE_INSENSITIVE);
    public String id;
    public boolean isAndroidNS = false;
    public String nameFull; // element name with package
    public String name; // element name
    public int fieldNameType = 1; // 1 aa_bb_cc; 2 aaBbCc 3 mAaBbCc
    public boolean isValid = false;
    public boolean used = true;
    public boolean isClickable = false; // Button, view_having_clickable_attr etc.
    public boolean isItemClickable = false; // ListView, GridView etc.
    public boolean isEditText = false; // EditText
    public XmlTag xml;

    /**
     * Constructs new element
     *
     * @param name Class name of the view
     * @param id   Value in android:id attribute
     * @throws IllegalArgumentException When the arguments are invalid
     */
    public Element(String name, String id, XmlTag xml) {
        // id
        final Matcher matcher = sIdPattern.matcher(id);
        if (matcher.find() && matcher.groupCount() > 1) {
            this.id = matcher.group(2);

            String androidNS = matcher.group(1);
            this.isAndroidNS = !(androidNS == null || androidNS.length() == 0);
        }

        if (this.id == null) {
            throw new IllegalArgumentException("Invalid format of view id");
        }

        // name
        String[] packages = name.split("\\.");
        if (packages.length > 1) {
            this.nameFull = name;
            this.name = packages[packages.length - 1];
        } else {
            this.nameFull = null;
            this.name = name;
        }

        this.xml = xml;

        // clickable
        XmlAttribute clickable = xml.getAttribute("android:clickable", null);
        boolean hasClickable = clickable != null &&
                clickable.getValue() != null &&
                clickable.getValue().equals("true");
        String xmlName = xml.getName();
        if (xmlName.contains("RadioButton")) {
            // TODO check
        } else {
            if ((xmlName.contains("ListView") || xmlName.contains("GridView")) && hasClickable) {
                isItemClickable = true;
            } else if (xmlName.contains("Button") || hasClickable) {
                isClickable = true;
            }
        }

        // isEditText
        isEditText = xmlName.contains("EditText");
    }

    /**
     * Create full ID for using in layout XML files
     *
     * @return
     */
    public String getFullID() {
        StringBuilder fullID = new StringBuilder();
        String rPrefix;

        if (isAndroidNS) {
            rPrefix = "android.R.id.";
        } else {
            rPrefix = "R.id.";
        }

        fullID.append(rPrefix);
        fullID.append(id);

        return fullID.toString();
    }

    /**
     * Generate field name if it's not done yet
     *
     * @return
     */
    public String getFieldName() {
        String fieldName = id;
        String[] names = id.split("_");
        if (fieldNameType == 2) {
            // aaBbCc
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < names.length; i++) {
                if (i == 0) {
                    sb.append(names[i]);
                } else {
                    sb.append(StringUtils.firstToUpperCase(names[i]));
                }
            }
            fieldName = sb.toString();
        } else if (fieldNameType == 3) {
            // mAaBbCc
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < names.length; i++) {
                if (i == 0) {
                    sb.append("m");
                }
                sb.append(StringUtils.firstToUpperCase(names[i]));
            }
            fieldName = sb.toString();
        }
        return fieldName;
    }

    /**
     * Check validity of field name
     *
     * @return
     */
    public boolean checkValidity() {
        Matcher matcher = sValidityPattern.matcher(getFieldName());
        isValid = matcher.find();

        return isValid;
    }
}
