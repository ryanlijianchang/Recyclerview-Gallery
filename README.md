# 用RecyclerView做一个小清新的Gallery效果 #

## 一、简介 ##
RecyclerView现在已经是越来越强大，且不说已经被大家用到滚瓜烂熟的代替ListView的基础功能，现在RecyclerView还可以取代ViewPager实现Banner效果，当然，以下做的小清新的Gallery效果也是类似于一些轮播图的效果，如下图所示，这其中使用到了24.2.0版本后RecyclerView增加的SnapHelper这个辅助类，在实现以下效果起来也是非常简单。所以这也是为什么RecyclerView强大之处，因为Google一直在对RecyclerView不断地进行更新补充，从而它内部的API也是越来越丰富。


![小清新的Gallery水平滑动效果](http://onq81n53u.bkt.clouddn.com/ddswwss.gif)

![小清新的Gallery垂直滑动效果](http://onq81n53u.bkt.clouddn.com/bbb.gif)


那么我们从水平滑动为例，我们细分为以下几个小问题：

1. 每一次滑动都让图片保持在正中间。 
2. 第一张图片的左边距和最后一张的右边距需要保持和其他照片的左右边距一样。
3. 滑动时，中间图片滑动到左边时从大变小，右边图片滑动到中间时从小变大。
4. 背景实现高斯模糊。
5. 滑动结束时背景有一个渐变效果，从上一张图片淡入淡出到当前图片。

## 二、实现思路 ##

解决以上问题当然也不难，我们分布来讲解下实现思路：

### (1) 每一次滑动都让图片保持在正中间。 ### 

保持让图片保持在正中间，正如简介中所说，在ToolsVersion24.2.0之后，Google给我们提供了一个`SnapHelper`的辅助类，它只需要几行代码就能帮助我们实现滑动结束时保持在居中位置：

	LinearSnapHelper mLinearySnapHelper = new LinearSnapHelper();
	mLinearySnapHelper.attachToRecyclerView(mGalleryRecyclerView);

`LinearSnapHelper`类继承于`SnapHelper`，当然`SnapHelper`还有一个子类，叫做`PagerSnapHelper`。它们之间的区别是，`LinearSnapHelper`可以使RecyclerView一次滑动越过多个Item，而`PagerSnapHelper`像ViewPager一样限制你一次只能滑动一个Item。

### (2) 第一张图片的左边距和最后一张的右边距需要保持和其他照片的左右边距一样。 ###

这个问题涉及到比较多的问题，首先，RecyclerView当前的API，并不能让我们简单的获取到我们图中效果中间图片的位置，或许你会说，可以通过
`mGalleryRecyclerView.getLinearLayoutManager().findFirstVisibleItemPosition()`能拿到RecyclerView中第一个可见的位置，但是通过效果可以知道，我们每一个张照片（除去第一张和最后一张）左右两边都是有前一张照片和最后一张照片的部分内容的，所以需要做区分判断是否是中间的照片还是第一张亦或最后一张，然后返回`mGalleryRecyclerView.getLinearLayoutManager().findFirstVisibleItemPosition() + 1`或者其他。 那么这样又会引出一个问题，当我们把前后照片展示的宽度设置成可配置，即前后照片的露出部分宽度是可配置的话，那么我们这一个方法又不能兼容了，所以通过这一个方法来获取，或许不那么靠谱。

我们可以这样来计算出比较准确的位置。在RecyclerView中，我们可以监听它的滑动事件：

	mGalleryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
	    @Override
	    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
	        super.onScrollStateChanged(recyclerView, newState);
	    }
	
	    @Override
	    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
	        super.onScrolled(recyclerView, dx, dy);

			// 通过dx或者dy来计算位置。 
	    }
	});

里面有一个`onScrolled(int dx, int dy)`方法，这里面的dx，dy非常有用。首先，通过判断dx，dy是否大于0可以判断它是上、下、左、右滑动，dx > 0右滑，反之左滑，dy > 0 下滑，反之上滑（当然，我这里的滑动是相对于RecyclerView，即列表的滑动方向，手指的滑动方向和这里相反）。其次，dx和dy还能监听每一次滑动在x，y轴上消耗的距离。

举个例子，当我们迅速至列表右边时，`onScrolled(int dx, int dy)`会不断被调用，通过在方法里面Log输出，你会看到不断输出dx的值，而且他们的大小都是无规律的，而这里的dx就是每一次`onScroll`方法调用一次，RecyclerView在x轴上的消耗距离。

所以我们可以通过一个全局变量`mConsumeX`来累加所有dx，当这样我们就可以知道当前RecyclerView滑动的总距离。而我们Demo中每移动到下一张照片的距离(即如下图中所示的**移动一页理论消耗距离**)是一定的，那么就可以通过**` 当前位置 = mConsumeX / 移动一张照片所需要的距离 `**来获取滑动结束时的位置了。

![RecyclerView距离示意图](http://onq81n53u.bkt.clouddn.com/%E5%9B%BE1.jpg)

	/**
	 * 获取位置
	 *
	 * @param mConsumeX      实际消耗距离
	 * @param shouldConsumeX 移动一页理论消耗距离
	 * @return
	 */
	private int getPosition(int mConsumeX, int shouldConsumeX) {
	    float offset = (float) mConsumeX / (float) shouldConsumeX;
	    int position = Math.round(offset);        // 四舍五入获取位置
	    return position;
	}

解决了获取位置这个问题之后，我们就可以对第0个位置，和最后一个位置的图片做判断，其他图片都默认设置他们的**页边距**和**左右图片的可视距离**，由于第0页左边没有图片，所以左边只有1倍页边距，这样滑动到最左边时看起来就会比较奇怪，如下图所示。

![](http://onq81n53u.bkt.clouddn.com/sa.jpg)

所以就需要让第0位置的图片左边保持和其他图片一样的距离，所以需要动态设置第0位置图片的左边距为**2倍页边距 + 可视距离**。同理，最后一张也是做同样的操作。

当然，动态修改图片的`LayoutParams`，需要在RecyclerView绑定的Adapter的`onBindViewHolder(MyHolder holder, int position)`里面做这些操作，因为RecyclerView对Holder的复用机制，在加载到某些页时，Adapter并不会调用`onCreateViewHolder(ViewGroup parent, int viewType)`来创建Holder，所以如果在这个方法里做LayoutParams修改的话，就会造成一些页不会调用的，具体修改如下：
	

    int mPageMargin = 0;          // 每一个页面默认页边距
    int mLeftPageVisibleWidth = 50; // 中间页面左右两边的页面可见部分宽度

	@Override
	public void onBindViewHolder(MyHolder holder, int position) {
		// ...

		// 动态修改页面的宽度
		int itemNewWidth = parent.getWidth() - dpToPx(4 * mPageMargin + 2 * mLeftPageVisibleWidth);

        // 第0页和最后一页没有左页面和右页面，让他们保持左边距和右边距和其他项一样
        int leftMargin = position == 0 ? dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : dpToPx(mPageMargin);
        int rightMargin = position == itemCount - 1 ? dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : dpToPx(mPageMargin);

		// 设置参数
		RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        lp.setMargins(leftMargin, 0, rightMargin, 0);
        lp.width = itemWidth;
        itemView.setLayoutParams(lp);


		// ...

	}

	public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }


### (3) 滑动时，中间图片滑动到左边时从大变小，右边图片滑动到中间时从小变大。 ###