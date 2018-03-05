package com.github.generator.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.GeneratedKey;

import java.util.List;

public class BatchInsertElementGenerator extends
        CustomizedXmlElementGenerator {

    public BatchInsertElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", "batchInsert")); //$NON-NLS-1$

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                "list"));

        context.getCommentGenerator().addComment(answer);
        List<IntrospectedColumn> columns = null;
        GeneratedKey gk = introspectedTable.getGeneratedKey();
        if (gk != null) {
            IntrospectedColumn introspectedColumn = introspectedTable
                    .getColumn(gk.getColumn());
            if (introspectedColumn != null)
                columns = introspectedColumn.isAutoIncrement() ? ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable
                        .getAllColumns()) : introspectedTable
                        .getAllColumns();
            else
                columns = introspectedTable
                        .getAllColumns();
        }
        StringBuilder sb = new StringBuilder();

        sb.append("insert into "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
        sb.append("(");
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
        for (IntrospectedColumn introspectedColumn : columns) {
            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(',');
        }
        answer.addElement(new TextElement(sb.toString().substring(0, sb.lastIndexOf(","))));
        sb.setLength(0);
        sb.append(")");
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(new TextElement("values "));
        XmlElement valuesTrimElement = new XmlElement("foreach"); //$NON-NLS-1$
        valuesTrimElement.addAttribute(new Attribute("collection", "list")); //$NON-NLS-1$ //$NON-NLS-2$
        valuesTrimElement.addAttribute(new Attribute("item", "item")); //$NON-NLS-1$ //$NON-NLS-2$
        valuesTrimElement.addAttribute(new Attribute("index", "index")); //$NON-NLS-1$ //$NON-NLS-2$
        valuesTrimElement.addAttribute(new Attribute("separator", ",")); //$NON-NLS-1$ //$NON-NLS-2$
        answer.addElement(valuesTrimElement);
        sb.setLength(0);
        sb.append("(");
        valuesTrimElement.addElement(new TextElement(sb.toString()));
        for (IntrospectedColumn introspectedColumn : columns) {
            sb.setLength(0);
            sb.append("#{item.");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append("},");
            valuesTrimElement.addElement(new TextElement(sb.toString()));
        }
        sb.setLength(0);
        sb.append(")");
        valuesTrimElement.addElement(new TextElement(sb.toString()));
        parentElement.addElement(answer);
    }
}
