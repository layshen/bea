# 忽略警告
#-ignorewarning

# 混淆保护自己项目的部分代码以及引用的第三方jar包
#-libraryjars libs/xxxxxxxxx.jar

# 不混淆这个包下的类
-keep class com.epiboly.bea2.http.api.** {
    <fields>;
}
-keep class com.epiboly.bea2.http.response.** {
    <fields>;
}
-keep class com.epiboly.bea2.http.model.** {
    <fields>;
}


# 不混淆被 Log 注解的方法信息
-keepclassmembernames class ** {
    @com.epiboly.bea2.aop.Log <methods>;
}