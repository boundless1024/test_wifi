### 测试附近wifi列表

<center class = "half">
<img src="/Users/libo/垃圾项目/TestDemo/images/1.png" alt="1" style="zoom:25%;" align=left/>
<img src="/Users/libo/垃圾项目/TestDemo/images/2.png" alt="2" style="zoom:25%;"align=left/>
<img src="/Users/libo/垃圾项目/TestDemo/images/3.png" alt="3" style="zoom:25%;"align=right/>
<img src="/Users/libo/垃圾项目/TestDemo/images/4.png" alt="4" style="zoom:25%;"align=right/>
</center>

####  权限

- 动态权限


```java
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```
- 清单权限

```java
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

- 其他，查看代码