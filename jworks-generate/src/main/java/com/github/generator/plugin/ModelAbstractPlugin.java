package com.github.generator.plugin;

import org.apache.commons.beanutils.BeanUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModelAbstractPlugin extends PluginAdapter {
      
    @Override  
    public boolean validate(List<String> list) {  
        return true;  
    }  
      
    @Override  
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> list = new ArrayList<GeneratedJavaFile>();
          
        List<TopLevelClass> units = new ArrayList<TopLevelClass>();  
        if (introspectedTable.getRules().generateBaseRecordClass())  
            units.add(generateRealRecordClass(introspectedTable, new FullyQualifiedJavaType(introspectedTable.getBaseRecordType())));
        if (introspectedTable.getRules().generateRecordWithBLOBsClass())  
            units.add(generateRealRecordClass(introspectedTable, new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType())));  
          
        CompilationUnit unit;
        for (Iterator<TopLevelClass> iterator = units.iterator(); iterator.hasNext();) {
            unit = (CompilationUnit)iterator.next();  
            list.add(new GeneratedJavaFile(unit, getContext().getJavaModelGeneratorConfiguration().getTargetProject(), getContext().getJavaFormatter()));  
        }  
        return list;  
    }  
      
    @Override  
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {  
        makeSerializable(topLevelClass, introspectedTable);
        try {
            BeanUtils.setProperty(topLevelClass, "type", getAbstractType(topLevelClass));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;  
    }  
      
    @Override  
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {  
        makeSerializable(topLevelClass, introspectedTable);
        try {
            BeanUtils.setProperty(topLevelClass, "type", getAbstractType(topLevelClass));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;  
    }  
      
    protected void makeSerializable(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {  
        Field field = new Field();
        field.setFinal(true);  
        field.setInitializationString("1L");  
        field.setName("serialVersionUID");  
        field.setStatic(true);  
        field.setType(new FullyQualifiedJavaType("long"));  
        field.setVisibility(JavaVisibility.PRIVATE);  
          
        List<Field> fields = topLevelClass.getFields();  
        fields.add(0, field);  
    }  
      
    protected TopLevelClass generateRealRecordClass(IntrospectedTable introspectedTable, FullyQualifiedJavaType recordType) {  
        TopLevelClass answer = new TopLevelClass(recordType);  
        answer.setSuperClass(getAbstractType(answer));  
        answer.setVisibility(JavaVisibility.PUBLIC);
        makeSerializable(answer, introspectedTable);  
        return answer;  
    }  
      
    protected FullyQualifiedJavaType getAbstractType(TopLevelClass topLevelClass) {  
        return new FullyQualifiedJavaType(getAbstractTypeName(topLevelClass));  
    }  
      
    protected String getAbstractTypeName(TopLevelClass topLevelClass) {
        String prefix = "Abstract";  
        return (new StringBuilder(String.valueOf(topLevelClass.getType().getPackageName()))).append(".").append(prefix).append(topLevelClass.getType().getShortName()).toString();  
    }

}