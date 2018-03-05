/**
 * Copyright 2006-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.generator.xml;

import com.github.generator.generator.BaseWhereElementGenerator;
import com.github.generator.generator.BatchInsertElementGenerator;
import com.github.generator.generator.BatchUpdateElementGenerator;
import com.github.generator.generator.DeleteByIdsElementGenerator;
import com.github.generator.generator.DeleteElementGenerator;
import com.github.generator.generator.ExtraWhereElementGenerator;
import com.github.generator.generator.InsertElementGenerator;
import com.github.generator.generator.SelectByConditionElementGenerator;
import com.github.generator.generator.SelectElementGenerator;
import com.github.generator.generator.UpdateElementGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BaseColumnListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BlobColumnListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.DeleteByPrimaryKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertSelectiveElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByPrimaryKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeySelectiveElementGenerator;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * 定制XML生成
 * BaseResultMap ResultMapWithBLOBs Base_Where Base_Column_List Blob_Column_List
 * insert batchInsert update delete select pageCount
 */
public class CustomizedXMLMapperGenerator extends AbstractXmlGenerator {

    public CustomizedXMLMapperGenerator() {
        super();
    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString(
                "Progress.12", table.toString())); //$NON-NLS-1$
        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", //$NON-NLS-1$
                namespace));

        context.getCommentGenerator().addRootComment(answer);

        addResultMapWithoutBLOBsElement(answer);
        addResultMapWithBLOBsElement(answer);
        addBaseColumnListElement(answer);
        addBlobColumnListElement(answer);
        addBaseWhereElement(answer);
        addExtraWhereElement(answer);
        //DML
        addSelectByPrimaryKeyElement(answer);
        addSelectElement(answer);
        addSelectByConditionElement(answer);
        addInsertElement(answer);
//        addBatchInsertElement(answer);
        addDeleteByPrimaryKeyElement(answer);
//        addDeleteByIdsElement(answer);
        addDeleteElement(answer);
        addUpdateElement(answer);
//        addBatchUpdateElement(answer);
        return answer;
    }


    /**
     * 添加select方法
     *
     * @param parentElement
     */
    protected void addSelectElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new SelectElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addSelectByConditionElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new SelectByConditionElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addInsertElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new InsertElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addBatchInsertElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new BatchInsertElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addUpdateElement(
            XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new UpdateElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addBatchUpdateElement(
            XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new BatchUpdateElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addDeleteByIdsElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new DeleteByIdsElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addDeleteElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new DeleteElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    /**
     * 添加base where
     *
     * @param parentElement
     */
    protected void addBaseWhereElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new BaseWhereElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addExtraWhereElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new ExtraWhereElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addResultMapWithoutBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseResultMap()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithoutBLOBsElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addResultMapWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addBaseColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseColumnList()) {
            AbstractXmlElementGenerator elementGenerator = new BaseColumnListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addBlobColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBlobColumnList()) {
            AbstractXmlElementGenerator elementGenerator = new BlobColumnListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new SelectByPrimaryKeyElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);

        }
    }

    protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new DeleteByPrimaryKeyElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addInsertSelectiveElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new InsertSelectiveElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }


    protected void addUpdateByPrimaryKeyElement(
            XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeySelectiveElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void initializeAndExecuteGenerator(
            AbstractXmlElementGenerator elementGenerator,
            XmlElement parentElement) {
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.setProgressCallback(progressCallback);
        elementGenerator.setWarnings(warnings);
        elementGenerator.addElements(parentElement);
    }

    @Override
    public Document getDocument() {
        Document document = new Document(
                XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElement());

        if (!context.getPlugins().sqlMapDocumentGenerated(document,
                introspectedTable)) {
            document = null;
        }

        return document;
    }
}
