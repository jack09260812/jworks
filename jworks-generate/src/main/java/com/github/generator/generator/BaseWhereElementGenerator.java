package com.github.generator.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * Created by jinwei on 17-5-3.
 */
public class BaseWhereElementGenerator extends CustomizedXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", "Base_Where")); //$NON-NLS-1$
        context.getCommentGenerator().addComment(answer);


        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getBaseColumns()) {
            answer.addElement(getIfTestElement(introspectedColumn));
        }

        parentElement.addElement(answer);
    }
}
