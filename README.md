# README #

使用RecyclerView实现Gallery效果。实现效果如下：

![小清新的Gallery水平滑动效果](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b7219464a?w=201&h=358&f=gif&s=3031397)
![小清新的Gallery垂直滑动效果](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b781841cc?w=206&h=366&f=gif&s=2045166)

# 用法 #

首先，在你的`build.gradle`中添加依赖。

    compile 'com.ryan.rv_gallery:rv-gallery:1.0.4'

第二，在你的layout文件中使用`GalleryRecyclerView`。

	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    android:id="@+id/rl_container"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:background="@color/colorPrimary"
	    android:fitsSystemWindows="true"
	    android:gravity="center"
	    android:orientation="vertical">
	
		<!-- PagerSnapHelper一次只能滑动一页 -->
		<!-- LinearySnapHelper一次能滑动多页 -->
	    <com.ryan.rv_gallery.GalleryRecyclerView
	        android:id="@+id/rv_list"
	        android:layout_width="match_parent"
	        android:layout_height="480dp"
        	app:helper="PagerSnapHelper/LinearySnapHelper" />

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

# API #

**Java API**

1. `initFlingSpeed(int speed)`：修改滑动速度（像素/s）
2. `setAnimFactor(float factor)`：修改切换动画的参数因子
3. `setAnimType(int type)`：配置动画类型 //ANIM_BOTTOM_TO_TOP、ANIM_TOP_TO_BOTTOM
4. `setOnItemClickListener(OnItemClickListener mListener)`：设置点击事件
5. `initPageParams(int pageMargin, int leftPageVisibleWidth)`：动态配置页边距和左右页可视宽度/高度
6. `getScrolledPosition()`：获取当前位置
7. `getLinearLayoutManager()`：获取LayoutManager
8. `getOrientation()`：获取当前的滑动方向 HORIZONTAL:0 VERTICAL:1

**XML API**

1. `app:helper="PagerSnapHelper/LinearySnapHelper"`：PagerSnapHelper一次只能滑动一页，LinearySnapHelper一次滑动多页。

# 实现 #

具体实现过程已在掘金上发布了，如果你感兴趣，可以跳转到[这里](https://juejin.im/post/5a30fe5a6fb9a045132ab1bf)。如果你觉得可以帮助到你，不妨点个Star。

# 版本特性 #

查看更多，请转移至[Releases](https://github.com/ryanlijianchang/Recyclerview-Gallery/releases)。

**V1.0.4**

1. 增加helper属性，包括LinearySnapHelper和PagerSnapHelper。

**V1.0.3**

1. 修复了移动一页理论消耗距离应该是图片宽度加上2倍页边距。
2. 修复了修改页边距和可视宽度之后，没有生效。

**V1.0.2**

1. BUG FIX。修复LayoutManager使用非LinearyLayoutManager时不抛出异常。 

**V1.0.1**

1. BUG FIX。首次打开，获得焦点后滑动至第0项，避免第0项的margin不对。

**V1.0.0**

1. GalleryRecyclerview支持实现Gallery效果。
2. 支持动态修改滑动速度（像素/s）。
3. 支持动态修改切换动画的参数因子。
4. 支持配置动画类型。
5. 支持点击事件。
6. 支持动态配置页边距和左右页可视宽度/高度。



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