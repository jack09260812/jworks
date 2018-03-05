package com.github.generator.runtime;

import com.github.generator.mapper.CustomizedJavaMapperGenerator;
import com.github.generator.xml.CustomizedXMLMapperGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

import java.util.List;

public class CustomizedIntrospectedTable extends IntrospectedTableMyBatis3Impl {

    @Override
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator,
                                               List<String> warnings,
                                               ProgressCallback progressCallback) {
//        if (javaClientGenerator == null) {
//            if (context.getSqlMapGeneratorConfiguration() != null) {
//                xmlMapperGenerator = new CustomizedXMLMapperGenerator();
//            }
//        } else {
//            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
//        }
        xmlMapperGenerator = new CustomizedXMLMapperGenerator();
        initializeAbstractGenerator(xmlMapperGenerator, warnings,
                progressCallback);
    }
}