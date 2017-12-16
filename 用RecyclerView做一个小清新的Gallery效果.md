# 用RecyclerView做一个小清新的Gallery效果 #


## 一、简介 ##
RecyclerView现在已经是越来越强大，且不说已经被大家用到滚瓜烂熟的代替ListView的基础功能，现在RecyclerView还可以取代ViewPager实现Banner效果，当然，以下做的小清新的Gallery效果也是类似于一些轮播图的效果，如下图所示，这其中使用到了24.2.0版本后RecyclerView增加的SnapHelper这个辅助类，在实现以下效果起来也是非常简单。所以这也是为什么RecyclerView强大之处，因为Google一直在对RecyclerView不断地进行更新补充，从而它内部的API也是越来越丰富。


![小清新的Gallery水平滑动效果](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b7219464a?w=201&h=358&f=gif&s=3031397)

![小清新的Gallery垂直滑动效果](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b781841cc?w=206&h=366&f=gif&s=2045166)


那么我们从水平滑动为例，我们细分为以下几个小问题：

1. 每一次滑动都让图片保持在正中间。 
2. 第一张图片的左边距和最后一张的右边距需要保持和其他照片的左右边距一样。
3. 滑动时，中间图片滑动到左边时从大变小，右边图片滑动到中间时从小变大。
4. 背景实现高斯模糊。
5. 滑动结束时背景有一个渐变效果，从上一张图片淡入淡出到当前图片。

## 二、实现思路 ##

解决以上问题当然也不难，我们分步来讲解下实现思路：

### (1) 每一次滑动都让图片保持在正中间 ### 

保持让图片保持在正中间，正如简介中所说，在ToolsVersion24.2.0之后，Google给我们提供了一个`SnapHelper`的辅助类，它只需要几行代码就能帮助我们实现滑动结束时保持在居中位置：

	LinearSnapHelper mLinearySnapHelper = new LinearSnapHelper();
	mLinearySnapHelper.attachToRecyclerView(mGalleryRecyclerView);

`LinearSnapHelper`类继承于`SnapHelper`，当然`SnapHelper`还有一个子类，叫做`PagerSnapHelper`。它们之间的区别是，`LinearSnapHelper`可以使RecyclerView一次滑动越过多个Item，而`PagerSnapHelper`像ViewPager一样限制你一次只能滑动一个Item。

### (2) 第一张图片的左边距和最后一张的右边距需要保持和其他照片的左右边距一样 ###



由于第0个位置，和最后一个位置的图片比较特殊，其他图片都默认设置他们的**页边距**和**左右图片的可视距离**，由于第0页左边没有图片，所以左边只有1倍页边距，这样滑动到最左边时看起来就会比较奇怪，如下图所示。

![](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b72460f9a?w=387&h=475&f=png&s=16085)

让第0位置的图片左边保持和其他图片一样的距离，那么就需要动态设置第0位置图片的左边距为**2倍页边距 + 可视距离**。同理，最后一张也是做同样的操作。

动态修改图片的`LayoutParams`，由于RecyclerView对Holder的复用机制，我们最好不要在Adapter里面动态修改，这样子首先不够优雅，这里感谢`@W_BinaryTree`的建议，我们给RecyclerView添加一个自定义的Decoration会让我们的代码更加优雅，只需要重写`RecyclerView.ItemDecoration`里面的`getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state)`方法，并在里面设置每一页的参数即可，修改如下：
	
	public class GalleryItemDecoration extends RecyclerView.ItemDecoration {
	    int mPageMargin = 0;          // 每一个页面默认页边距
	    int mLeftPageVisibleWidth = 50; // 中间页面左右两边的页面可见部分宽度

	    public static int mItemComusemX = 0;  // 一页理论消耗距离

	
		@Override
	    public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
	        super.getItemOffsets(outRect, view, parent, state);
	    	// ...

	    	// 动态修改页面的宽度
	    	int itemNewWidth = parent.getWidth() - dpToPx(4 * mPageMargin + 2 * mLeftPageVisibleWidth);
	    
			// 一页理论消耗距离
	        mItemComusemX = itemNewWidth + OsUtil.dpToPx(2 * mPageMargin);

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
	}

然后，把`GalleryItemDecoration`传入即可：

	mGalleryRecyclerView.addItemDecoration(new GalleryItemDecoration());




### (3) 滑动时，中间图片滑动到左边时从大变小，右边图片滑动到中间时从小变大 ###

这个问题涉及到比较多的问题。

**(a) 获取滑动过程中当前位置。**

首先，RecyclerView当前的API，并不能让我们在滑动的过程中，简单地获取到我们图中效果中间图片的位置，或许你会说，可以通过
`mGalleryRecyclerView.getLinearLayoutManager().findFirstVisibleItemPosition()`能拿到RecyclerView中第一个可见的位置，但是通过效果可以知道，我们每一个张照片（除去第一张和最后一张）左右两边都是有前一张照片和最后一张照片的部分内容的，所以需要做区分判断是否是中间的照片还是第一张亦或最后一张，然后返回`mGalleryRecyclerView.getLinearLayoutManager().findFirstVisibleItemPosition() + 1`或者其他。 那么这样又会引出一个问题，当我们把前后照片展示的宽度设置成可配置，即前后照片的露出部分宽度是可配置，那么当我们把屏幕不显示前后照片遗留部分在屏幕的话，那么我们这一个方法又不能兼容了，所以通过这一个方法来获取，或许不那么靠谱。

我们可以这样来计算出比较准确的位置。在RecyclerView中，我们可以监听它的滑动事件：

	// 滑动监听
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

所以我们可以通过一个全局变量`mConsumeX`来累加所有dx，当这样我们就可以知道当前RecyclerView滑动的总距离。而我们Demo中每移动到下一张照片的距离(即如下图中所示的**移动一页理论消耗距离**)是一定的，那么就可以通过`当前位置 = mConsumeX / 移动一张照片所需要的距离`来获取滑动结束时的位置了。

![RecyclerView距离示意图](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b6e561f70?w=825&h=773&f=png&s=42932)

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

**(b) 根据位置获取当前页的滑动偏移率**

当我们可以准确拿到当前位置时，我们就需要明确一下几个概念。

`总的偏移距离`：意思是从第一个位置移动到现在当前位置偏移的总距离，即dx的累加结果（也就是上述的mConsumX）。

`当前页偏移距离`：意思是从上一个位置移动到当前位置偏移距离。

`总的偏移率`：意思是 总的偏移距离 / 移动一页理论消耗距离。

`当前页的偏移率`：意思是 当前页偏移距离 / 移动一页理论消耗距离。


![](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b758abedb?w=1072&h=934&f=png&s=46504)

我们都知道，获取当前位置方法里面有一个 

	float offset = (float) mConsumeX / (float) shouldConsumeX;

它的意思就是总的偏移率，例如图中我们当前位置是3，我们从3移动到4时，`onScroll`方法会不断被调用，那么这个offset就会不断变化，从3.0逐渐增加一直到4.0，图中此时的offset大概是3.2左右，我们知道这一个有什么用呢？试想一下，offset是一个浮点型数，将它向下取整，那就是变成3了，那么3.2 - 3 = 0.2就是我们当前页的偏移率了。而我们通过偏移率就可以动态设置图片的大小，就形成了我们这个问题中所说的图片大小变化效果。所以这里的关键就是获取到`当前页的偏移率`。

	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);

	    // ...	


	    // 移动一页理论消耗距离
        int shouldConsumeX = GalleryItemDecoration.mItemComusemX;


        // 获取当前的位置
        int position = getPosition(mConsumeX, shouldConsumeX);
	
	    // 位置浮点值（即总消耗距离 / 每一页理论消耗距离 = 一个浮点型的位置值）
	    float offset = (float) mConsumeX / (float) shouldConsumeX;     
	
	    // 避免offset值取整时进一，从而影响了percent值
	    if (offset >= mGalleryRecyclerView.getLinearLayoutManager().findFirstVisibleItemPosition() + 1 && slideDirct == SLIDE_RIGHT) {
	        return;
	    }
	
	    // 当前页的偏移率
	    float percent = offset - ((int) offset);


        // 设置动画变化
	    setAnimation(recyclerView, position, percent);

	    // ...
	
	}

**(c) 根据偏移率实现动画**

现在我们拿到了偏移率，就可以动态修改它们的尺寸大小了，首先，我们需要拿到当前View，前一个View和后一个View，并同时对它们做Scale伸缩。即上面的`setAnimation(recyclerView, position, percent)`方法里面进行动画操作。

        View mCurView = recyclerView.getLayoutManager().findViewByPosition(position);       // 中间页
        View mRightView = recyclerView.getLayoutManager().findViewByPosition(position + 1); // 左边页
        View mLeftView = recyclerView.getLayoutManager().findViewByPosition(position - 1);  // 右边页

认真观察图中变化，两种变化：

1. 位置的变化：第一张图片是从mCurView慢慢变成mLeftView，而第二张图片是从mRightView慢慢变成mCurView。
2. 大小变化：第一张图是从大变小，第二张图是从小变大。

理解了以上的变化之后，我们就可以做动画了。

![](https://user-gold-cdn.xitu.io/2017/12/13/1604f61b7472c6f0?w=202&h=366&f=gif&s=1395373)

首先说明一点，大家观察我的`getPosition(mConsumeX, shouldConsumeX)`方法，里面的实现是，当一页滑动的偏移率超过了0.5之后，position就会自动切换到下一页。当然你的实现逻辑不一样，那么后面你的设置动画的方法就不一样。为什么需要明确这一点呢？因为当我滑动超过图片超过它的一半宽度之后，上面的mCurView就会切换成下一张图片了，所以我在设置动画的方法里以0.5为一个临界点，因为0.5临界点的两边，`mCurView`，`mRightView`，`mLeftView`的指向都已经不一样了。

假如我们定义大小变化因子 `float mAnimFactor = 0.2f`，它的意思就是控制我们的图片从1.0伸缩至0.8。以上图为例，当percent <= 0.5时，`mCurView`的ScaleX和ScaleY从大慢慢变小，至于这个变化范围，就根据我们定义的变化因子和percent来修改；而当percent > 0.5时，刚才那个View就变成了`mLeftView`，此时我们继续刚才的操作，整个过程我们就实现了第一张图片的Scale从1.0变化到了0.8。而另外两张图片也是同理，大概代码逻辑如下：


	private void setBottomToTopAnim(RecyclerView recyclerView, int position, float percent) {
        View mCurView = recyclerView.getLayoutManager().findViewByPosition(position);       // 中间页
        View mRightView = recyclerView.getLayoutManager().findViewByPosition(position + 1); // 左边页
        View mLeftView = recyclerView.getLayoutManager().findViewByPosition(position - 1);  // 右边页


        if (percent <= 0.5) {
            if (mLeftView != null) {
				// 变大
                mLeftView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mLeftView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
            if (mCurView != null) {
				// 变小
                mCurView.setScaleX(1 - percent * mAnimFactor);
                mCurView.setScaleY(1 - percent * mAnimFactor);
            }
            if (mRightView != null) {
				// 变大
                mRightView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mRightView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
        } else {
            if (mLeftView != null) {
                mLeftView.setScaleX(1 - percent * mAnimFactor);
                mLeftView.setScaleY(1 - percent * mAnimFactor);
            }
            if (mCurView != null) {
                mCurView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mCurView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
            if (mRightView != null) {
                mRightView.setScaleX(1 - percent * mAnimFactor);
                mRightView.setScaleY(1 - percent * mAnimFactor);
            }
        }
    }

### (4)背景实现高斯模糊 ###

高斯模糊有挺多种实现方法的，Google一下就出来了。但是还是推荐Native层的实现算法，因为Java层的实现对性能影响实在太大了，例子里使用的是`RenderScript`，当然是参考博主`湫水`的[教你一分钟实现动态模糊效果](http://wl9739.github.io/2016/07/14/%E6%95%99%E4%BD%A0%E4%B8%80%E5%88%86%E9%92%9F%E5%AE%9E%E7%8E%B0%E6%A8%A1%E7%B3%8A%E6%95%88%E6%9E%9C/)，大家感兴趣可以过去看看，用法也是非常简单。直接调用`blurBitmap(Context context, Bitmap image, float blurRadius)`方法即可。

	public class BlurBitmapUtil {
	    //图片缩放比例
	    private static final float BITMAP_SCALE = 0.4f;
	
	    /**
	     * 模糊图片的具体方法
	     *
	     * @param context 上下文对象
	     * @param image   需要模糊的图片
	     * @return 模糊处理后的图片
	     */
	    public static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) {
	        // 计算图片缩小后的长宽
	        int width = Math.round(image.getWidth() * BITMAP_SCALE);
	        int height = Math.round(image.getHeight() * BITMAP_SCALE);
	
	        // 将缩小后的图片做为预渲染的图片
	        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
	        // 创建一张渲染后的输出图片
	        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
	
	        // 创建RenderScript内核对象
	        RenderScript rs = RenderScript.create(context);
	        // 创建一个模糊效果的RenderScript的工具对象
	        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
	
	        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
	        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
	        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
	        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
	
	        // 设置渲染的模糊程度, 25f是最大模糊度
	        blurScript.setRadius(blurRadius);
	        // 设置blurScript对象的输入内存
	        blurScript.setInput(tmpIn);
	        // 将输出数据保存到输出内存中
	        blurScript.forEach(tmpOut);
	
	        // 将数据填充到Allocation中
	        tmpOut.copyTo(outputBitmap);
	
	        return outputBitmap;
	    }
	}

这个方法只要传入Context，Bitmap，和一个模糊程度即可，然后返回一个高斯模糊后的Bitmap给我们，我们只需要将RecyclerView的父布局设置背景为这个Bitmap即可。

### (5)滑动结束时背景有一个渐变效果，从上一张图片淡入淡出到当前图片 ###

实现这个效果最好不要使用Tween动画，因为它的实现效果比较生硬，使用`TransitionDrawable`会让效果更佳接近淡入淡出效果。那我们怎么记录前后两个位置的照片呢？方法很多种，这里就使用了一个Map<String, Drwable>来记录每一次显示的图片，在它切换到下一个图片时，便从上一次记录的图片淡入淡出到本次的图片。

	// 获取当前位置的图片资源ID
	int resourceId = ((RecyclerAdapter) mRecyclerView.getAdapter()).getResId(mRecyclerView.getScrolledPosition());
	// 将该资源图片转为Bitmap
	Bitmap resBmp = BitmapFactory.decodeResource(getResources(), resourceId);
	// 将该Bitmap高斯模糊后返回到resBlurBmp
	Bitmap resBlurBmp = BlurBitmapUtil.blurBitmap(mRecyclerView.getContext(), resBmp, 15f);
	// 再将resBlurBmp转为Drawable
	Drawable resBlurDrawable = new BitmapDrawable(resBlurBmp);
	// 获取前一页的Drawable
	Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);
	
	/* 以下为淡入淡出效果 */
	Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
	TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
	mContainer.setBackgroundDrawable(transitionDrawable);
	transitionDrawable.startTransition(500);
	
	// 存入到cache中
	mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);

## 更多 ##

以上所讲的都是实现的一个思路，虽然效果和小清新搭不上关系哈，但是配了几张小清新的图片还是让我们的程序员生活增添一丝精彩。其实大家实现了基础效果之后，还可以深挖更多辅助功能，例如不同的切换效果，支持横屏，动态修改滑动速度等，相信这个过程可以让你收获良多。

Github：[Recyclerview-Gallery](https://github.com/ryanlijianchang/Recyclerview-Gallery)

