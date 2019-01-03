# README #

[![Download](https://img.shields.io/badge/Download-V1.1.2-blue.svg)](https://bintray.com/ryanlijianchang/maven/RecyclerView-Gallery)
[![License](https://img.shields.io/badge/license-Apache2.0-green.svg)](https://github.com/ryanlijianchang/Recyclerview-Gallery)
[![Build](https://img.shields.io/circleci/project/github/RedSparr0w/node-csgo-parser.svg)](https://github.com/ryanlijianchang/Recyclerview-Gallery)

[中文版文档](https://github.com/ryanlijianchang/Recyclerview-Gallery/blob/master/README_CN.md)

This library shows you a gallery using RecyclerView.

![小清新的Gallery水平滑动效果](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b7219464a?w=201&h=358&f=gif&s=3031397)

![小清新的Gallery垂直滑动效果](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b781841cc?w=206&h=366&f=gif&s=2045166)


# Usage #

First step, add dependence in your `build.gradle`.

    compile 'com.ryan.rv_gallery:rv-gallery:1.1.2'

Second step, using `GalleryRecyclerView` in your layout file.

	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    android:id="@+id/rl_container"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:background="@color/colorPrimary"
	    android:fitsSystemWindows="true"
	    android:gravity="center"
	    android:orientation="vertical">
	
		<!-- PagerSnapHelper can move just one page when scroll -->
		<!-- LinearSnapHelper can move serveral page when scroll-->
	    <com.ryan.rv_gallery.GalleryRecyclerView
	        android:id="@+id/rv_list"
	        android:layout_width="match_parent"
	        android:layout_height="480dp"
        	app:helper="PagerSnapHelper/LinearSnapHelper" />

	</RelativeLayout>

Third step, init your GalleryRecyclerView in your java code just like using the normal RecyclerView. Note that you must use the LinearLayoutManager as your LayoutManager. At the same time, you must set the orientation like `HORIZONTAL` or `VERTICAL`, to make your `GalleryRecyclerView` scroll horizontally or vertically.


	GalleryRecyclerView mRecyclerView = findViewById(R.id.rv_list);
	RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), getDatas());
	mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    mRecyclerView.setAdapter(adapter);

Finally, set some params of `GalleryRecyclerView` if your need, and you must use the `setUp()` method to make the `GalleryRecyclerView` to work.

    mRecyclerView
            // set scroll speed（pixel/s）
            .initFlingSpeed(9000)
			// set page distance and visible distance of the nearby.
            .initPageParams(0, 40)
            // set the animation factor
            .setAnimFactor(0.1f)
			// set animation type. you can choose AnimManager.ANIM_BOTTOM_TO_TOP or AnimManager.ANIM_TOP_TO_BOTTOM
            .setAnimType(AnimManager.ANIM_BOTTOM_TO_TOP)
            // set click listener
            .setOnItemClickListener(this)
            // set whether auto play
            .autoPlay(false)
            // set auto play intervel 
            .intervalTime(2000)
            // set default position
            .initPosition(1)
            // finally call method
            .setUp();

You also can release `GalleryRecyclerView` if you need


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecyclerView != null) {
            // release
            mRecyclerView.release();
        }
    }

# API #

**Java API**

1. `initFlingSpeed(int speed)`：set scroll speed（pixel/s）
2. `setAnimFactor(float factor)`：set the animation factor
3. `setAnimType(int type)`：set animation type. you can choose `AnimManager.ANIM_BOTTOM_TO_TOP` or `AnimManager.ANIM_TOP_TO_BOTTOM`
4. `setOnItemClickListener(OnItemClickListener mListener)`：set click listener
5. `initPageParams(int pageMargin, int leftPageVisibleWidth)`：set page distance and visible distance of the nearby.
6. `getScrolledPosition()`： get current position
7. `getLinearLayoutManager()`：get LayoutManager
8. `getOrientation()`：get current scroll orientation(HORIZONTAL:0 VERTICAL:1)
9. `autoPlay(boolean)`：set can it auto play
10. `intervalTime(int interval)`：set auto play intervel 
11. `initPosition(int position)`： set default position

**XML API**

1. `app:helper="PagerSnapHelper/LinearSnapHelper"`：PagerSnapHelper can move just one page when scroll，LinearSnapHelper can move serveral page when scroll.

# Version feature #

see more in [Releases](https://github.com/ryanlijianchang/Recyclerview-Gallery/releases)。


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