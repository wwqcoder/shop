package com.wwq.core.test;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2019-05-07 14:43
 **/
public class FreemarkerTest {
    public static void main(String[] args) throws Exception {
        //创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置模板所在的目录
        String dir = "H:\\IdeaProjects\\pinyougou\\parent\\test-freemarker\\src\\main\\resources\\";
        configuration.setDirectoryForTemplateLoading(new File(dir));
        //设置字符集
        configuration.setDefaultEncoding("UTF-8");
        //加载模板
        Template template = configuration.getTemplate("test.ftl");
        //创建数据模型
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","王玮琦");
        map.put("message","欢迎来到静态化的世界！！！");
        map.put("success",false);

        ArrayList<Map> list = new ArrayList<>();
        HashMap g1 = new HashMap();
        g1.put("name","苹果");
        g1.put("price",5.8);
        HashMap g2 = new HashMap();
        g2.put("name","香蕉");
        g2.put("price",6.8);
        HashMap g3 = new HashMap();
        g3.put("name","橘子");
        g3.put("price",10.8);

        list.add(g1);
        list.add(g2);
        list.add(g3);

        map.put("goodsList",list);
        map.put("date",new Date());
        map.put("point1",1233454434);

        //创建输出流对象
        FileWriter out = new FileWriter(new File(dir + "test.html"));

        //输出
        template.process(map,out);

        //关闭流
        out.close();


    }
}
