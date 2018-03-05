#dubbo

- XML配置方式

```
提供者：
在resources下创建dubbo/spring-dubbo.xml文件
```

- 注解方式

```
提供者：

在application.properties添加Dubbo的版本信息和客户端超时信息,如下:
spring.dubbo.application.name=credit.business.provider
spring.dubbo.registry.address=zookeeper://192.168.98.106:2181
spring.dubbo.protocol.name=dubbo
spring.dubbo.protocol.port=20880
spring.dubbo.scan = com.xingjie.business.api.impl

服务接口：
public interface EchoService {
    String echo(String str);
}

服务实现：
@Service(version = "1.0.0")
public class EchoServerImpl implements EchoService {

    public String echo(String str) {
        System.out.println(str);
        return str;
    }
}

消费者：

在application.properties添加Dubbo的版本信息和客户端超时信息,如下:
spring.dubbo.application.name=credit.cms.consumer
spring.dubbo.registry.address=zookeeper://192.168.98.106:2181
spring.dubbo.protocol.name=dubbo
spring.dubbo.protocol.port=20880
spring.dubbo.scan = com.xingjie.cms.api.impl

接口注入
@Component
public class AbcService {
    @Reference(version = "1.0.0")
    public EchoService echoService;
}

```

# log4j2

- 配置
```
pattern:

%d{HH:mm:ss.SSS} 表示输出到毫秒的时间

%t 输出当前线程名称

%-5level 输出日志级别，-5表示左对齐并且固定输出5个字符，如果不足在右边补0

%logger 输出logger名称，因为Root Logger没有名称，所以没有输出

%msg 日志文本

%n 换行

其他常用的占位符有：

%F 输出所在的类文件名，如Client.java

%L 输出行号

%M 输出所在方法名

%l  输出语句所在的行数, 包括类名、方法名、文件名、行数

RollingRandomAccessFile Appender:

RollingRandomAccessFile的属性：
fileName  指定当前日志文件的位置和文件名称
filePattern  指定当发生Rolling时，文件的转移和重命名规则
SizeBasedTriggeringPolicy  指定当文件体积大于size指定的值时，触发Rolling
DefaultRolloverStrategy  指定最多保存的文件个数
TimeBasedTriggeringPolicy  这个配置需要和filePattern结合使用，注意filePattern中配置的文件重命名规则是${FILE_NAME}-%d{yyyy-MM-dd HH-mm}-%i，最小的时间粒度是mm，即分钟，TimeBasedTriggeringPolicy指定的size是1，结合起来就是每1分钟生成一个新文件。如果改成%d{yyyy-MM-dd HH}，最小粒度为小时，则每一个小时生成一个文件。
```

- bean初始化顺序
```$xslt
一，单一Bean
装载
1. 实例化; 
2. 设置属性值; 
3. 如果实现了BeanNameAware接口,调用setBeanName设置Bean的ID或者Name; 
4. 如果实现BeanFactoryAware接口,调用setBeanFactory 设置BeanFactory; 
5. 如果实现ApplicationContextAware,调用setApplicationContext设置ApplicationContext 
6. 调用BeanPostProcessor的预先初始化方法; 
7. 调用InitializingBean的afterPropertiesSet()方法; 
8. 调用定制init-method方法； 
9. 调用BeanPostProcessor的后初始化方法;
spring容器关闭
1. 调用DisposableBean的destroy(); 
2. 调用定制的destroy-method方法;
 
二，多个Bean的先后顺序
优先加载BeanPostProcessor的实现Bean
按Bean文件和Bean的定义顺序按bean的装载顺序（即使加载多个spring文件时存在id覆盖）
“设置属性值”（第2步）时，遇到ref，则在“实例化”（第1步）之后先加载ref的id对应的bean
AbstractFactoryBean的子类，在第6步之后,会调用createInstance方法，之后会调用getObjectType方法
BeanFactoryUtils类也会改变Bean的加载顺序
```

- FAQ
1.
```$xslt
问题: 
在spring的Configuration使用@Bean注册一个BeanFactoryPostProcessor Bean,发现使用@PropertySource,并注入@Resource private Environment env;发现env为null.我调试的大概一个过程,BeanFactoryPostProcessor Bean创建得比较早,创建它之前先创建它的依赖Bean Configuration,而这时发现创建的Configuration的env就是null了.深入的就没去追究了！

解决: 
让此Configuration类实现EnvironmentAware接口,这个接口只有一个void setEnvironment(Environment environment);方法.这里的回调能得到Environment,问题解决!
```