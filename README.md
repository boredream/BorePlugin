### [ENGLISH README](https://github.com/boredream/BorePlugin/README-EN.md)

# 目录
[功能](https://github.com/boredream/BorePlugin#功能)  
[安装](https://github.com/boredream/BorePlugin#安装)  
[用法](https://github.com/boredream/BorePlugin#用法)  
[用法截图](https://github.com/boredream/BorePlugin#用法截图)  
[代码生成规则](https://github.com/boredream/BorePlugin#代码生成规则)  
[历史版本](https://github.com/boredream/BorePlugin#历史版本)  

# 功能
可以让你在Activity/Fragment中自动生成findViewById等布局相关初始化代码<br/>
或者在Adapter中自动生成ViewHolder代码
> 如果你有任何新的需求或者想法意见,也可以在issue中提出~

# Installion
方式一. 下载项目中的jar包,通过本地disk方式导入<br/>
方式二. 在Android Studio的插件中心搜索LayoutCreator下载安装


# 用法
1. 新建好Activity后自行编写onCreate并setContentView设置对应布局<br/>
2. 选中layout布局,快捷键alt+Insert,然后选择LayoutCreator或者选中布局后在菜单栏中的Code中选择LayoutCreator<br/>
3. 插件会自动遍历布局列出所有带id的控件,你可以在弹出的对话框中选择需要自动生成的控件<br/>
4. 弹出的对话框中还可以勾选是否生成ViewHolder<br/>
5. 选择好后Confirm确认即可


# 用法截图
![image](https://github.com/boredream/BorePlugin/blob/master/screenshot/LayoutCreator.gif)


# 代码生成规则
1. 自动遍历目标布局中所有带id的文件, 无id的不会识别处理
2. 控件生成的变量名默认为id名称, 可以在弹出确认框右侧的名称输入栏中自行修改
3. 所有的Button或者带clickable=true的控件, 都会自动在代码中生成setOnClickListener相关代码
4. 所有EditText控件, 都会在代码中生成非空判断代码, 如果为空会提示EditText的hint内容, 如果hint为空则提示xxx字符串不能为空字样, 最后会把所有输入框的验证合并到一个submit方法中
5. 会自动识别布局中的include标签, 并读取对应布局中的控件


# 历史版本
* 1.0 初始化版本
* 1.1 添加了Fragment ViewHolder等支持
* 1.2 添加了EditText的验证代码生成, 详细见 代码生成规则4
* 1.3 优化了二次生成布局代码时重复问题<br/>
    根据当前类中成员变量判断控件是否已经存在, 避免重复添加
* 1.4 添加了全选功能<br/>
    添加了不同的变量命名方式可供选择（匈牙利、驼峰、m驼峰）<br/>
