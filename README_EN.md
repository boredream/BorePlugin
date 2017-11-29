[**中文文档**](https://github.com/boredream/BorePlugin/blob/master/README.md)

# Index
[**Features**](https://github.com/boredream/BorePlugin/blob/master/README_EN.md#features)  
[**Installion**](https://github.com/boredream/BorePlugin/blob/master/README_EN.md#installion)  
[**Usage**](https://github.com/boredream/BorePlugin#usage)  
[**Usage Screenshot**](https://github.com/boredream/BorePlugin#Usage Screenshot)  
[**Generation Rule**](https://github.com/boredream/BorePlugin#Generation Rule)  
[**Vertion**](https://github.com/boredream/BorePlugin#vertion)  

# Features
auto create the "findViewById", "btn_submit.setOnClickListener" etc. code in Activity/Fragment or Adapter  
> if you have any question, you can make a issue~

# Installion
Search "Layoutcreator" in Android Studio plugin repositories, then install it!


# Usage
1. create a Activity, then write the "onCreate" method and setContentView(R.layout.YOUR_LAYOUT)  
2. choose the layout name, then select "Generate"-"LayoutCreator"  
    (or select "Code" - "LayoutCreator" in Menu bar  
3. then it will show a dialog with all views  
4. press "Confirm" to generate code


# Usage Screenshot
![Layout Creator](http://upload-images.jianshu.io/upload_images/1513977-30cad82d6668b799.gif?imageMogr2/auto-orient/strip)


# Generation Rule
1. scan all views in layout witch only has the "id" attr  
2. all the views will show in the dialog, then you can rename it  
3. all the Button or the views whitch has clickable=true will generate "Clickc" code  
4. all the EditText will generate validate code  
5. the layout in <include> will be scaned  


# Vertion
* 1.0 init
* 1.1 support Fragment and ViewHolder code  
* 1.2 add EditText validate code  
* 1.3 fix the bug: duplicate generate field  
* 1.4 add "select/unselect all"  
    support difference type of name  
