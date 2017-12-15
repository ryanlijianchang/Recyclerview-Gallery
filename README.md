# README #

使用RecyclerView实现Gallery效果。实现效果如下：

![小清新的Gallery水平滑动效果](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b7219464a?w=201&h=358&f=gif&s=3031397)
![小清新的Gallery垂直滑动效果](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b781841cc?w=206&h=366&f=gif&s=2045166)

# 用法 #

首先，在你的`build.gradle`中添加依赖。

    compile 'com.ryan.rv_gallery:rv-gallery:1.0.0'

第二，在你的layout文件中使用`GalleryRecyclerView`。

	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:id="@+id/rl_container"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:background="@color/colorPrimary"
	    android:fitsSystemWindows="true"
	    android:gravity="center"
	    android:orientation="vertical">
	
	    <com.ryan.rv_gallery.GalleryRecyclerView
	        android:id="@+id/rv_list"
	        android:layout_width="match_parent"
	        android:layout_height="480dp" />

	</RelativeLayout>

第三，在代码中像使用普通的RecyclerView一样，初始化你的GalleryRecyclerView。LayoutManager必须使用LinearLayoutManager，同时在创建LinearLayoutManager需要指定你的方向为`HORIZONTAL`或者`VERTICAL`，让`GalleryRecyclerView`水平或者垂直方向滑动。

	GalleryRecyclerView mRecyclerView = findViewById(R.id.rv_list);
	RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), getDatas());
	mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    mRecyclerView.setAdapter(adapter);

最后，指定`GalleryRecyclerView`的参数（非必须，不指定的话会使用默认值）。
	
	mRecyclerView.initFlingSpeed(5000)                                   // 设置滑动速度（像素/s）
	             .initPageParams(0, 60)     							 // 设置页边距和左右图片的可见宽度，单位dp
	             .setAnimFactor(0.15f)                                   // 设置切换动画的参数因子
	             .setAnimType(AnimManager.ANIM_BOTTOM_TO_TOP)            // 设置切换动画类型，目前有AnimManager.ANIM_BOTTOM_TO_TOP和目前有AnimManager.ANIM_TOP_TO_BOTTOM
	             .setOnItemClickListener(this);                          // 设置点击事件


# 实现 #

具体实现过程已在掘金上发布了，如果你感兴趣，可以跳转到[这里](https://juejin.im/post/5a30fe5a6fb9a045132ab1bf)。如果你觉得可以帮助到你，不妨点个Star。

# License #

    
    Copyright 2017 ryanlijianchang
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.