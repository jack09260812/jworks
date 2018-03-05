 XSS、CSRF、SQL 注入、访问控制、
认证、URL 跳转

![spring bean生命周期](http://dl2.iteye.com/upload/attachment/0099/3887/fbe424a3-c67a-356d-bfec-be8c030ec0a6.jpg)
- applicationcontextaware
```
可以获得ApplicationContext
```
- ApplicationContext
```
getbean
getEnvironment
```
- ApplicationListener
```
实现ApplicationEvent接口定义事件，实现ApplicationListener监听自定义事件
```
- InitializingBean
```
当需要在bean的全部属性设置成功后做些特殊的处理，可以让该bean实现InitializingBean接口,效果等同于bean的init-method属性的使用或者@PostContsuct注解的使用。       
三种方式的执行顺序：先注解，然后执行InitializingBean接口中定义的方法，最后执行init-method属性指定的方法。
```
- BeanPostProcessor
```
需要对受管bean进行预处理时，可以新建一个实现BeanPostProcessor接口的类
实现BeanPostProcessor接口时，需要实现以下两个方法：
postProcessBeforeInitialization 在受管bean的初始化动作之前调用
postProcessAfterInitialization 在受管bean的初始化动作之后调用容器中的每个Bean在创建时都会恰当地调用它们
```
- InstantiationAwareBeanPostProcessorAdapter
```
InstantiationAwareBeanPostProcessor 接口本质是BeanPostProcessor的子接口，一般我们继承Spring为其提供的适配器类InstantiationAwareBeanPostProcessorAdapter来使用
```
- BeanFactoryPostProcessor
```
需要对Bean工厂进行预处理时，可以新建一个实现BeanFactoryPostProcessor接口的类
public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

}
```

- ResourceBundleMessageSource
```
提供国际化支持，bean的名字必须为messageSource
```
- FactoryBean
```
```
- BeanFactory
```
```
- ApplicationContextInitializer
```
实现接口获得ConfigurableApplicationContext
```
- ConfigurableApplicationContext
```
extends ApplicationContext, Lifecycle, Closeable
addBeanFactoryPostProcessor
getBeanFactory:ConfigurableListableBeanFactory,ConfigurableListableBeanFactory可以注册bean
```
- BeanUtils
```
实例化类
```


![流程图](http://img.my.csdn.net/uploads/201304/13/1365825529_4693.png)
![时序图](http://img.my.csdn.net/uploads/201304/13/1365825551_8302.png)

- HandlerMethodArgumentResolver
```
@RequestParam、@RequestHeader、@RequestBody、@PathVariable、@ModelAttribute
参数解析接口
```
- HandlerMethodReturnValueHandler
```
结果封装接口
```
RequestResponseBodyMethodProcessor
```
实现HandlerMethodReturnValueHandler，@ResponseBody处理类
```

- RequestMappingHandlerAdapter
```
持有HandlerMethodArgumentResolver集合，进行请求参数映射
持有HandlerMethodReturnValueHandler集合，进行结果封装
持有HttpMessageConverter集合，进行转换
持有ModelAndViewResolver集合，负责定制返回类型
```
- WebMvcConfigurerAdapter
```
添加拦截器、消息转换、controller...
```
- HttpMessageConverter
```
消息装换，通过canread和canwrite来处理对应消息格式
```
AbstractHttpMessageConverter
```
HttpMessageConverter代理类
```
MappingJackson2HttpMessageConverter
```
json转换
```
HttpServletRequestWrapper 
```
通过对HttpServletRequest的封装，解决HttpServletRequest对象不可变的情况，通过在filter中对HttpServletRequest进行处理来解决参数问题
```

RequestContextHolder
```
获取ServletRequestAttributes,然后获取request、response
```

- FilterRegistrationBean
```
@bean注解方式增加过滤器
```