package com.github.generator.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class BatchUpdateElementGenerator extends
        CustomizedXmlElementGenerator {

    public BatchUpdateElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer
                .addAttribute(new Attribute(
                        "id", "batchUpdate")); //$NON-NLS-1$

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                "list"));

        context.getCommentGenerator().addComment(answer);
        XmlElement foreachElement = new XmlElement("foreach"); //$NON-NLS-1$
        foreachElement.addAttribute(new Attribute("collection", "list")); //$NON-NLS-1$ //$NON-NLS-2$
        foreachElement.addAttribute(new Attribute("item", "item")); //$NON-NLS-1$ //$NON-NLS-2$
        foreachElement.addAttribute(new Attribute("index", "index")); //$NON-NLS-1$ //$NON-NLS-2$
        foreachElement.addAttribute(new Attribute("open", "")); //$NON-NLS-1$ //$NON-NLS-2$
        foreachElement.addAttribute(new Attribute("close", "")); //$NON-NLS-1$ //$NON-NLS-2$
        foreachElement.addAttribute(new Attribute("separator", ";")); //$NON-NLS-1$ //$NON-NLS-2$
        answer.addElement(foreachElement);
        StringBuilder sb = new StringBuilder();

        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        foreachElement.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
        foreachElement.addElement(dynamicElement);
        sb.setLength(0);
        for (IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(introspectedTable
                .getNonPrimaryKeyColumns())) {
            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append("#{item.");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append("},");

        }
        dynamicElement.addElement(new TextElement(sb.toString().substring(0,sb.lastIndexOf(","))));
        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append("#{item.");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append("},");
            foreachElement.addElement(new TextElement(sb.toString()));
        }

        //增加节点
        parentElement.addElement(answer);
    }
}
