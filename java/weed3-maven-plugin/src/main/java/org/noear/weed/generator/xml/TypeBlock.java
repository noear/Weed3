package org.noear.weed.generator.xml;

public class TypeBlock {
    public String oldType;
    public String newType;
    public String slmType;
    public String impType;

    public TypeBlock(String type) {
        if (type == null) {
            return;
        }

        oldType = type.replace("[","<").replace("]",">");
        slmType = oldType;

        int idx0 = oldType.lastIndexOf(".");
        if (idx0 > 0) {
            impType = oldType;

            slmType = oldType.substring(idx0 + 1);
            int idx2 = slmType.indexOf(">");
            if (idx2 > 0) {
                slmType = slmType.substring(0, idx2);
            }
        }

        if (oldType.indexOf(">") > 0) {
            int idx1 = oldType.indexOf("<");
            impType = oldType.substring(idx1 + 1, oldType.length() - 1);

            newType = oldType.substring(0, idx1) + "<" + slmType + ">";
        } else {
            newType = slmType;
        }
    }
}