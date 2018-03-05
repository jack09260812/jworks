- pageHelper
````
PageHelper只对紧跟着的第一个SQL语句起作用
如上：所以若一个方法中涉及到多个查询，需要小心，避免为不需要分页的添加了分页，而真正需要分页的却没有被分页
1)、统计总数，（将SQL语句变为 select count(0) from xxx,只对简单SQL语句其效果，复杂SQL语句需要自己写）
    Page<?> page = PageHelper.startPage(1,-1);
    long count = page.getTotal();
2)、分页，pageNum - 第N页， pageSize - 每页M条数
    A、只分页不统计(每次只执行分页语句)
    PageHelper.startPage([pageNum],[pageSize]);
    或 PageHelper.offsetPage(offset,limit,false);
    List<?> pagelist = queryForList( xxx.class, "queryAll" , param);
    //pagelist就是分页之后的结果
    B、分页并统计（每次执行2条语句，一条select count语句，一条分页语句）适用于查询分页时数据发生变动，需要将实时的变动信息反映到分页结果上
    Page<?> page = PageHelper.startPage([pageNum],[pageSize],[iscount]);
    List<?> pagelist = queryForList( xxx.class , "queryAll" , param);
    long count = page.getTotal();
    //也可以 List<?> pagelist = page.getList();  获取分页后的结果集
3)、使用PageHelper查全部（不分页）
    PageHelper.startPage(1,0);
    List<?> alllist = queryForList( xxx.class , "queryAll" , param);
4)、PageHelper的其他API
    String orderBy = PageHelper.getOrderBy();    //获取orderBy语句
    Page<?> page = PageHelper.startPage(Object params);
    Page<?> page = PageHelper.startPage(int pageNum, int pageSize);
    Page<?> page = PageHelper.startPage(int pageNum, int pageSize, boolean isCount);
    Page<?> page = PageHelper.startPage(pageNum, pageSize, orderBy);
    Page<?> page = PageHelper.startPage(pageNum, pageSize, isCount, isReasonable);    //isReasonable分页合理化,null时用默认配置
    Page<?> page = PageHelper.startPage(pageNum, pageSize, isCount, isReasonable, isPageSizeZero);    //isPageSizeZero是否支持PageSize为0，true且pageSize=0时返回全部结果，false时分页,null时用默认配置
5)、默认值
    //RowBounds参数offset作为PageNum使用 - 默认不使用
    private boolean offsetAsPageNum = false;
    //RowBounds是否进行count查询 - 默认不查询
    private boolean rowBoundsWithCount = false;
    //当设置为true的时候，如果pagesize设置为0（或RowBounds的limit=0），就不执行分页，返回全部结果
    private boolean pageSizeZero = false;
    //分页合理化
    private boolean reasonable = false;
    //是否支持接口参数来传递分页参数，默认false
    private boolean supportMethodsArguments = false;  

````
- 动态数据源
```
系统根据customized.datasource.names属性判断是否启动多数据源，多数据源之间逗号分隔
customized.datasource.names=ds1
customized.datasource.ds1.url=jdbc:mysql://192.168.98.106:3306/dubbo-monitor?useUnicode=true&characterEncoding=UTF-8
customized.datasource.ds1.username=root
customized.datasource.ds1.password=admin
customized.datasource.ds1.driver-class-name=com.mysql.jdbc.Driver

启动多数据源后在service方法体上使用DataSource("ds1")注解动态切换数据源
```