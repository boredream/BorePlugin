# 功能
可以让你在Activity中自动生成findViewById等代码<br/>
或者在Adapter中自动生成ViewHolder代码


# 安装
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
