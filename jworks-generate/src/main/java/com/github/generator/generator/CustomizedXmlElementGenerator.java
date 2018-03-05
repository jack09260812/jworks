package com.github.generator.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * Created by jinwei on 17-5-2.
 */
public abstract class CustomizedXmlElementGenerator extends
        AbstractXmlElementGenerator {
    public CustomizedXmlElementGenerator() {
        super();
    }

    protected XmlElement getBaseWhereElement() {
        XmlElement answer = new XmlElement("include"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("refid", //$NON-NLS-1$
                "Base_Where"));
        return answer;
    }

    protected XmlElement getExtraWhereElement() {
        XmlElement answer = new XmlElement("include"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("refid", //$NON-NLS-1$
                "Extra_Where"));
        return answer;
    }

    protected XmlElement getTrimWhereElement() {
        XmlElement answer = new XmlElement("trim"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("prefix", //$NON-NLS-1$
                "WHERE"));
        answer.addAttribute(new Attribute("prefixOverrides", //$NON-NLS-1$
                "AND |OR"));
        answer.addElement(getBaseWhereElement());
        return answer;
    }

    protected XmlElement getTrimExtraWhereElement() {
        XmlElement answer = new XmlElement("trim"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("prefix", //$NON-NLS-1$
                "WHERE"));
        answer.addAttribute(new Attribute("prefixOverrides", //$NON-NLS-1$
                "AND |OR"));
        answer.addElement(getExtraWhereElement());
        return answer;
    }

    protected XmlElement getIdsElement() {
        XmlElement answer = new XmlElement("foreach"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("collection", //$NON-NLS-1$
                "list"));
        answer.addAttribute(new Attribute("item", //$NON-NLS-1$
                "item"));
        answer.addAttribute(new Attribute("index", //$NON-NLS-1$
                "index"));
        answer.addAttribute(new Attribute("open", //$NON-NLS-1$
                "("));
        answer.addAttribute(new Attribute("close", //$NON-NLS-1$
                ")"));
        answer.addAttribute(new Attribute("separator", //$NON-NLS-1$
                ","));
        answer.addElement(new TextElement("#{item}"));
        return answer;
    }

    protected XmlElement getIfTestElement(IntrospectedColumn introspectedColumn) {
        XmlElement answer = new XmlElement("if"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("test", //$NON-NLS-1$
                introspectedColumn.getJavaProperty() + " != null"));
        //and条件
        StringBuilder sb = new StringBuilder();
        sb.append("  and "); //$NON-NLS-1$
        sb.append(MyBatis3FormattingUtilities
                .getAliasedEscapedColumnName(introspectedColumn));
        sb.append(" = "); //$NON-NLS-1$
        sb.append(MyBatis3FormattingUtilities
                .getParameterClause(introspectedColumn));
        answer.addElement(new TextElement(sb.toString()));
        return answer;
    }

}
