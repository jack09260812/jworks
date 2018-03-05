package com.github.generator.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.sql.Types;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * Created by jinwei on 17-5-3.
 */
public class ExtraWhereElementGenerator extends CustomizedXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$
        //
        answer.addAttribute(new Attribute(
                "id", "Extra_Where")); //$NON-NLS-1$
        context.getCommentGenerator().addComment(answer);

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getBaseColumns()) {
            answer.addElement(getIfTestElement(introspectedColumn));
            if (introspectedColumn.getJdbcType() == Types.VARCHAR || introspectedColumn.getJdbcType() == Types.CHAR) {
                getIfTestLikeElement(answer, introspectedColumn);
            } else if (introspectedColumn.getJdbcType() == Types.DATE || introspectedColumn.getJdbcType() == Types.TIMESTAMP) {
                getIfTestBetweenElement(answer, introspectedColumn);
            } else if (introspectedColumn.getJdbcType() == Types.DOUBLE || introspectedColumn.getJdbcType() == Types.FLOAT) {
                getIfTestCompareElement(answer, introspectedColumn);
            }
        }
        parentElement.addElement(answer);
    }

    /**
     * 模糊匹配，默认支持全模糊
     *
     * @param introspectedColumn
     * @return
     */
    protected void getIfTestLikeElement(XmlElement root, IntrospectedColumn introspectedColumn) {
        //java属性
        String javaType = introspectedColumn.getJavaProperty();
        //数据库字段
        String dbType = MyBatis3FormattingUtilities
                .getAliasedEscapedColumnName(introspectedColumn);
        javaType += "Like";
        XmlElement answer = new XmlElement("if"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("test", //$NON-NLS-1$
                javaType + " != null"));
        //and条件
        StringBuilder sb = new StringBuilder();
        sb.append("  and "); //$NON-NLS-1$
        sb.append(dbType);
        sb.append(" like CONCAT('%',"); //$NON-NLS-1$
        sb.append("#{"); //$NON-NLS-1$
        sb.append(javaType);
        sb.append('}');
        sb.append(",'%')");
        answer.addElement(new TextElement(sb.toString()));
        root.addElement(answer);
    }


    /**
     * 比较
     *
     * @param introspectedColumn
     * @return
     */
    protected void getIfTestCompareElement(XmlElement root, IntrospectedColumn introspectedColumn) {
        //java属性
        String javaType = introspectedColumn.getJavaProperty();
        //数据库字段
        String dbType = MyBatis3FormattingUtilities
                .getAliasedEscapedColumnName(introspectedColumn);
        String from = javaType + "From";
        String to = javaType + "To";
        XmlElement fromAnswer = new XmlElement("if"); //$NON-NLS-1$
        fromAnswer.addAttribute(new Attribute("test", //$NON-NLS-1$
                from + " != null"));
        fromAnswer.addElement(new TextElement("<![CDATA["));
        //and条件
        StringBuilder sb = new StringBuilder();
        sb.append("  and "); //$NON-NLS-1$
        sb.append(dbType);
        sb.append(" >= "); //$NON-NLS-1$
        sb.append("#{"); //$NON-NLS-1$
        sb.append(from);
        sb.append('}');
        fromAnswer.addElement(new TextElement(sb.toString()));
        fromAnswer.addElement(new TextElement("]]>"));
        XmlElement toAnswer = new XmlElement("if"); //$NON-NLS-1$
        toAnswer.addAttribute(new Attribute("test", //$NON-NLS-1$
                to + " != null"));
        toAnswer.addElement(new TextElement("<![CDATA["));
        //and条件
        sb.setLength(0);
        sb.append("  and "); //$NON-NLS-1$
        sb.append(dbType);
        sb.append(" < "); //$NON-NLS-1$
        sb.append("#{"); //$NON-NLS-1$
        sb.append(to);
        sb.append('}');
        toAnswer.addElement(new TextElement(sb.toString()));
        toAnswer.addElement(new TextElement("]]>"));
        //增加属性到父节点
        root.addElement(fromAnswer);
        root.addElement(toAnswer);
    }

    /**
     * 日期比较
     *
     * @param introspectedColumn
     * @return
     */
    protected void getIfTestBetweenElement(XmlElement root, IntrospectedColumn introspectedColumn) {
        //java属性
        String javaType = introspectedColumn.getJavaProperty();
        //数据库字段
        String dbType = MyBatis3FormattingUtilities
                .getAliasedEscapedColumnName(introspectedColumn);
        String begin = javaType + "Begin";
        String end = javaType + "End";
        XmlElement answer = new XmlElement("if"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("test", //$NON-NLS-1$
                begin + " != null and " + end + " != null"));
        //and条件
        StringBuilder sb = new StringBuilder();
        sb.append("  and "); //$NON-NLS-1$
        sb.append(dbType);
        sb.append(" BETWEEN "); //$NON-NLS-1$
        sb.append("#{"); //$NON-NLS-1$
        sb.append(begin);
        sb.append('}');
        sb.append(" AND "); //$NON-NLS-1$
        sb.append("#{"); //$NON-NLS-1$
        sb.append(end);
        sb.append('}');
        answer.addElement(new TextElement(sb.toString()));
        root.addElement(answer);
    }

}
