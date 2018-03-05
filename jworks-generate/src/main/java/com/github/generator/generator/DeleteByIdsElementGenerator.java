package com.github.generator.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.util.List;

public class DeleteByIdsElementGenerator extends
        CustomizedXmlElementGenerator {

    public DeleteByIdsElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", "deleteByIds")); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                "list"));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("delete from "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable
                .getPrimaryKeyColumns();
        /**
         * 仅处理有唯一主键表结构
         */
        if (primaryKeyColumns == null || primaryKeyColumns.size() != 1)
            return;
        /**
         * 增加条件删除
         */
        sb.setLength(0);
        sb.append("where ").append(MyBatis3FormattingUtilities
                .getEscapedColumnName(primaryKeyColumns.get(0))).append(" in ");
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getIdsElement());
        parentElement.addElement(answer);
    }
}
