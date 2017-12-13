# 用RecyclerView做一个小清新的Gallery效果 #

## 一、简介 ##
RecyclerView现在已经是越来越强大，且不说已经被大家用到滚瓜烂熟的代替ListView的基础功能，现在RecyclerView还可以取代ViewPager实现Banner效果，当然，以下做的小清新的Gallery效果也是类似于一些轮播图的效果，如下图所示，这其中使用到了24.2.0版本后RecyclerView增加的SnapHelper这个辅助类，在实现以下效果起来也是非常简单。所以这也是为什么RecyclerView强大之处，因为Google一直在对RecyclerView不断地进行更新补充，从而它内部的API也是越来越丰富。


![小清新的Gallery水平滑动效果](http://onq81n53u.bkt.clouddn.com/ddswwss.gif)

![小清新的Gallery垂直滑动效果](http://onq81n53u.bkt.clouddn.com/bbb.gif)


那么我们从水平滑动为例，我们细分为以下几个小问题：

（1）每一次滑动都让图片保持在正中间。 
（2）第一张图片的左边距和最后一张的右边距需要保持和其他照片的左右边距一样。
 (3) 滑动时，中间图片滑动到左边时从大变小，右边图片滑动到中间时从小变大。
 (4) 背景实现高斯模糊。
 (5) 滑动结束时背景有一个渐变效果，从上一张图片淡入淡出到当前图片。

## 二、实现思路 ##

解决以上问题当然也不难，我们分布来讲解下实现思路：

**(1) 每一次滑动都让图片保持在正中间。** 

保持让图片保持在正中间，正如简介中所说，在ToolsVersion24.2.0之后，Google给我们提供了一个`SnapHelper`的辅助类，它只需要几行代码就能帮助我们实现滑动结束时保持在居中位置：

	LinearSnapHelper mLinearySnapHelper = new LinearSnapHelper();
	mLinearySnapHelper.attachToRecyclerView(mGalleryRecyclerView);

`LinearSnapHelper`类继承于`SnapHelper`，当然`SnapHelper`还有一个子类，叫做`PagerSnapHelper`。它们之间的区别是，`LinearSnapHelper`可以使RecyclerView一次滑动越过多个Item，而`PagerSnapHelper`像ViewPager一样限制你一次只能滑动一个Item。

 **(2) 第一张图片的左边距和最后一张的右边距需要保持和其他照片的左右边距一样。**

这个问题涉及到比较多的问题，首先，RecyclerView当前的API，并不能让我们简单的获取到我们图中效果中间图片的位置，或许你会说，可以通过
`mGalleryRecyclerView.getLinearLayoutManager().findFirstVisibleItemPosition()`能拿到


 