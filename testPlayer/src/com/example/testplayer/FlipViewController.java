package com.example.testplayer;

import java.io.IOException;
import java.util.LinkedList;

import junit.framework.Assert;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2016年11月29日 --- 下午3:02:26
 *
 */
public class FlipViewController extends AdapterView<Adapter> {

	  public static final int VERTICAL = 0;
	  public static final int HORIZONTAL = 1;

	  public static interface ViewFlipListener {

	    void onViewFlipped(View view, int position);
	  }

	  private static final int MAX_RELEASED_VIEW_SIZE = 1;

	  static final int MSG_SURFACE_CREATED = 1;
	protected static final String TAG = "FlipViewController";

	  private Handler handler = new Handler(new Handler.Callback() {
	    @Override
	    public boolean handleMessage(Message msg) {
	      if (msg.what == MSG_SURFACE_CREATED) {
	        contentWidth = 0;
	        contentHeight = 0;
	        requestLayout();
	        return true;
	      } else {
	        Log.w(TAG,"Unknown msg.what: " + msg.what);
	      }
	      return false;
	    }
	  });

	  private GLSurfaceView surfaceView;
	  private FlipRenderer renderer;
	  private FlipCards cards;

	  private int contentWidth;
	  private int contentHeight;

	  private int flipOrientation;

	  private volatile boolean inFlipAnimation = false;

	  //AdapterView Related
	  private Adapter adapter;

	  private int adapterDataCount = 0;

	  private DataSetObserver adapterDataObserver;

	  private final LinkedList<View> bufferedViews = new LinkedList<View>();
	  private final LinkedList<View> releasedViews = new LinkedList<View>(); //XXX: use a SparseArray to keep the related view indices?
	  private int bufferIndex = -1;
	  private int adapterIndex = -1;
	  private final int sideBufferSize = 1;

	  private float touchSlop;

	  private ViewFlipListener onViewFlipListener;

	  private boolean overFlipEnabled = true;

	  private boolean flipByTouchEnabled = true;

	  private Bitmap.Config animationBitmapFormat = Bitmap.Config.ARGB_8888;
	  private Handler mHandler;
	  private int mMediaPlayerIndex = 2;
	  private MediaPlayer mMediaPlayer = new MediaPlayer();
	  private AssetManager am = null;
	  private Runnable doStepToNextPicRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.w(TAG,"doStepToNextPicRunnable mMediaPlayerIndex = " + mMediaPlayerIndex);
			if(mMediaPlayer.isPlaying()){
				Log.w(TAG,"  mMediaPlayer.pause()");
				mMediaPlayer.pause();
			}
			mMediaPlayer.reset();
			Log.w(TAG,"  mMediaPlayer.reset()");
			if((mMediaPlayerIndex > 0)&&(mMediaPlayerIndex < 6)){
				if(am != null){
						String pathString = null;
						switch(mMediaPlayerIndex){
						case 1:
							pathString = "audio_1.mp3";
							break;
						case 2:
							pathString = "audio_2.mp3";
							break;
						case 3:
							pathString = "audio_3.mp3";
							break;
						case 4:
							pathString = "audio_4.mp3";
							break;
						case 5:
							pathString = "audio_5.mp3";
							break;
						default:
							break;
						}
					if(pathString != null){
						try{
							AssetFileDescriptor afd = am.openFd(pathString);
							Log.w(TAG," mMediaPlayer.setDataSource() pathString = " + pathString);
							mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
							mMediaPlayer.prepare();
							Log.w(TAG," mMediaPlayer.prepare()");
							mMediaPlayer.start();
							Log.w(TAG," mMediaPlayer.start()");
						}catch(IOException e){
							e.printStackTrace();
						}
					}
				}
			}
			mMediaPlayerIndex ++;
			if(mMediaPlayerIndex == 6){
				mMediaPlayerIndex = 1;
			}
			//	flipto = 1;
			//}else if(adapterIndex == adapterDataCount){
			//	flipto = -1;
			//}
			//int currIndex = adapterIndex + flipto;
			//Log.w(TAG,"doStepToNextPicRunnable currIndex = " + currIndex);
			//setSelection(currIndex);
			//flippedToView(currIndex,true);
			cards.handleTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 447, 245, 0), true);
			cards.handleTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, 406, 245, 0), true);
			cards.handleTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, 384, 245, 0), true);
			cards.handleTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, 328, 245, 0), true);
			cards.handleTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 300, 245, 0), true);
            mHandler.postDelayed(this, 10000);
		}
		  
	  };

	  public FlipViewController(Context context) {
	    this(context, VERTICAL);
	  }

	  public FlipViewController(Context context, int flipOrientation) {
	    super(context);
	    init(context, flipOrientation);
        mHandler = new Handler();
	  }

	  /**
	   * Constructor required for XML inflation.
	   */
	  public FlipViewController(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);

	    int orientation = VERTICAL;

	    TypedArray
	        a =
	        context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlipViewController, 0, 0);

	    try {
	      int value = a.getInteger(R.styleable.FlipViewController_orientation, VERTICAL);
	      if (value == HORIZONTAL) {
	        orientation = HORIZONTAL;
	      }

	      value = a.getInteger(R.styleable.FlipViewController_animationBitmapFormat, 0);
	      if (value == 1) {
	        setAnimationBitmapFormat(Bitmap.Config.ARGB_4444);
	      } else if (value == 2) {
	        setAnimationBitmapFormat(Bitmap.Config.RGB_565);
	      } else {
	        setAnimationBitmapFormat(Bitmap.Config.ARGB_8888);
	      }
	    } finally {
	      a.recycle();
	    }

	    init(context, orientation);
	  }

	  /**
	   * Constructor required for XML inflation.
	   */
	  public FlipViewController(Context context, AttributeSet attrs) {
	    this(context, attrs, 0);
	  }

	  private void init(Context context, int orientation) {
	    ViewConfiguration configuration = ViewConfiguration.get(getContext());
	    touchSlop = configuration.getScaledTouchSlop();
	    this.flipOrientation = orientation;
	    setupSurfaceView(context);
	    am = context.getAssets();
	  }

	  public Bitmap.Config getAnimationBitmapFormat() {
	    return animationBitmapFormat;
	  }

	  /**
	   * Set the bitmap config for the animation, default is ARGB_8888, which provides the best quality
	   * with large peak memory consumption.
	   *
	   * @param animationBitmapFormat ALPHA_8 is not supported and will throw exception when binding
	   *                              textures
	   */
	  public void setAnimationBitmapFormat(Bitmap.Config animationBitmapFormat) {
	    this.animationBitmapFormat = animationBitmapFormat;
	  }

	  public ViewFlipListener getOnViewFlipListener() {
	    return onViewFlipListener;
	  }

	  public void setOnViewFlipListener(ViewFlipListener onViewFlipListener) {
	    this.onViewFlipListener = onViewFlipListener;
	  }

	  public void onResume() {
	    surfaceView.onResume();
        mHandler.removeCallbacks(doStepToNextPicRunnable );
        mHandler.post(doStepToNextPicRunnable);
	  }

	  public void onPause() {
	    surfaceView.onPause();
        mHandler.removeCallbacks(doStepToNextPicRunnable );
        if(mMediaPlayer.isPlaying()){
        	mMediaPlayer.pause();
        }
	  }

	  /**
	   * Request the animator to update display if the pageView has been preloaded. <p/> If the pageView
	   * is being used in the animation or its content has been buffered, the animator forcibly reloads
	   * it. <p/> The reloading process is a bit heavy for an active page, so please don't invoke it too
	   * frequently for an active page. The cost is trivial for inactive pages.
	   */
	  public void refreshPage(View pageView) {
	    if (cards.refreshPageView(pageView)) {
	      requestLayout();
	    }
	  }

	  /**
	   * @see #refreshPage(android.view.View)
	   */
	  public void refreshPage(int pageIndex) {
	    if (cards.refreshPage(pageIndex)) {
	      requestLayout();
	    }
	  }

	  /**
	   * Force the animator reload all preloaded pages
	   */
	  public void refreshAllPages() {
	    cards.refreshAllPages();
	    requestLayout();
	  }

	  public boolean isOverFlipEnabled() {
	    return overFlipEnabled;
	  }

	  public void setOverFlipEnabled(boolean overFlipEnabled) {
	    this.overFlipEnabled = overFlipEnabled;
	  }

	  public boolean isFlipByTouchEnabled() {
	    return flipByTouchEnabled;
	  }

	  public void setFlipByTouchEnabled(boolean flipByTouchEnabled) {
	    this.flipByTouchEnabled = flipByTouchEnabled;
	  }

	  //--------------------------------------------------------------------------------------------------------------------
	  // Touch Event
	  @Override
	  public boolean onInterceptTouchEvent(MotionEvent event) {
	    if (flipByTouchEnabled) {
	      return cards.handleTouchEvent(event, false);
	    } else {
	      return false;
	    }
	  }

	  @Override
	  public boolean onTouchEvent(MotionEvent event) {
	    if (flipByTouchEnabled) {
	      return cards.handleTouchEvent(event, true);
	    } else {
	      return false;
	    }
	  }

	  //--------------------------------------------------------------------------------------------------------------------
	  // Orientation
	  @Override
	  protected void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    //XXX: adds a global layout listener?
	  }

	  //--------------------------------------------------------------------------------------------------------------------
	  // AdapterView<Adapter>
	  @Override
	  public Adapter getAdapter() {
	    return adapter;
	  }

	  @Override
	  public void setAdapter(Adapter adapter) {
	    setAdapter(adapter, 0);
	  }

	  public void setAdapter(Adapter adapter, int initialPosition) {
	    if (this.adapter != null) {
	      this.adapter.unregisterDataSetObserver(adapterDataObserver);
	    }

	    Assert.assertNotNull("adapter should not be null", adapter);

	    this.adapter = adapter;
	    adapterDataCount = adapter.getCount();

	    adapterDataObserver = new MyDataSetObserver();
	    this.adapter.registerDataSetObserver(adapterDataObserver);
	    if (adapterDataCount > 0) {
	      setSelection(initialPosition);
	    }
	  }

	  @Override
	  public View getSelectedView() {
	    return (bufferIndex < bufferedViews.size() && bufferIndex >= 0) ? bufferedViews.get(bufferIndex)
	                                                                    : null;
	  }

	  @Override
	  public void setSelection(int position) {
	    if (adapter == null) {
	      return;
	    }

	    Assert.assertTrue("Invalid selection position", position >= 0 && position < adapterDataCount);

	    releaseViews();

	    View selectedView = viewFromAdapter(position, true);
	    bufferedViews.add(selectedView);

	    for (int i = 1; i <= sideBufferSize; i++) {
	      int previous = position - i;
	      int next = position + i;

	      if (previous >= 0) {
	        bufferedViews.addFirst(viewFromAdapter(previous, false));
	      }
	      if (next < adapterDataCount) {
	        bufferedViews.addLast(viewFromAdapter(next, true));
	      }
	    }

	    bufferIndex = bufferedViews.indexOf(selectedView);
	    adapterIndex = position;

	    requestLayout();
	    updateVisibleView(inFlipAnimation ? -1 : bufferIndex);

	    cards.resetSelection(position, adapterDataCount);
	  }

	  @Override
	  public int getSelectedItemPosition() {
	    return adapterIndex;
	  }

	  //--------------------------------------------------------------------------------------------------------------------
	  // Layout
	  @Override
	  protected void onLayout(boolean changed, int l, int t, int r, int b) {
	    //if (AphidLog.ENABLE_DEBUG) {
	    //  AphidLog.d("onLayout: %d, %d, %d, %d; child %d", l, t, r, b, bufferedViews.size());
	    //}

	    for (View child : bufferedViews) {
	      child.layout(0, 0, r - l, b - t);
	    }

	    if (changed || contentWidth == 0) {
	      int w = r - l;
	      int h = b - t;
	      surfaceView.layout(0, 0, w, h);

	      if (contentWidth != w || contentHeight != h) {
	        contentWidth = w;
	        contentHeight = h;
	      }
	    }

	    if (bufferedViews.size() >= 1) {
	        View frontView = bufferedViews.get(bufferIndex);
	        View backView = null;
	        if (bufferIndex < bufferedViews.size() - 1) {
	          backView = bufferedViews.get(bufferIndex + 1);
	        }
	        renderer.updateTexture(adapterIndex, frontView, backView == null ? -1 : adapterIndex + 1,
	                               backView);
	    }
	  }

	  @Override
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	    for (View child : bufferedViews) {
	      child.measure(widthMeasureSpec, heightMeasureSpec);
	    }

	    surfaceView.measure(widthMeasureSpec, heightMeasureSpec);
	  }

	  //--------------------------------------------------------------------------------------------------------------------
	  //internal exposed properties & methods
	  float getTouchSlop() {
	    return touchSlop;
	  }

	  GLSurfaceView getSurfaceView() {
	    return surfaceView;
	  }

	  FlipRenderer getRenderer() {
	    return renderer;
	  }

	  int getContentWidth() {
	    return contentWidth;
	  }

	  int getContentHeight() {
	    return contentHeight;
	  }

	  void sendMessage(int message) {
	    handler.sendMessage(Message.obtain(handler, message));
	  }

	  //--------------------------------------------------------------------------------------------------------------------
	  // Internals
	  private void setupSurfaceView(Context context) {
	    surfaceView = new GLSurfaceView(getContext());

	    cards = new FlipCards(this, flipOrientation == VERTICAL);
	    renderer = new FlipRenderer(this, cards);

	    surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
	    surfaceView.setZOrderOnTop(true);
	    surfaceView.setRenderer(renderer);
	    surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	    surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

	    addViewInLayout(surfaceView, -1, new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
	                                                                  LayoutParams.FILL_PARENT), false);
	  }

	  private void releaseViews() {
	    for (View view : bufferedViews) {
	      releaseView(view);
	    }
	    bufferedViews.clear();
	    bufferIndex = -1;
	    adapterIndex = -1;
	  }

	  private void releaseView(View view) {
	    Assert.assertNotNull(view);
	    detachViewFromParent(view);
	    addReleasedView(view);
	  }

	  private void addReleasedView(View view) {
	    Assert.assertNotNull(view);
	    if (releasedViews.size() < MAX_RELEASED_VIEW_SIZE) {
	      releasedViews.add(view);
	    }
	  }

	  private View viewFromAdapter(int position, boolean addToTop) {
	    Assert.assertNotNull(adapter);

	    View releasedView = releasedViews.isEmpty() ? null : releasedViews.removeFirst();

	    View view = adapter.getView(position, releasedView, this);
	    if (releasedView != null && view != releasedView) {
	      addReleasedView(releasedView);
	    }

	    setupAdapterView(view, addToTop, view == releasedView);
	    return view;
	  }

	  private void setupAdapterView(View view, boolean addToTop, boolean isReusedView) {
	    LayoutParams params = view.getLayoutParams();
	    if (params == null) {
	      params =
	          new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
	                                       ViewGroup.LayoutParams.WRAP_CONTENT, 0);
	    }

	    if (isReusedView) {
	      attachViewToParent(view, addToTop ? 0 : 1, params);
	    } else {
	      addViewInLayout(view, addToTop ? 0 : 1, params, true);
	    }
	  }

	  private void updateVisibleView(int index) {
	    for (int i = 0; i < bufferedViews.size(); i++) {
	      bufferedViews.get(i).setVisibility(index == i ? VISIBLE : GONE);
	    }
	  }

	  private void debugBufferedViews() {
	    //if (AphidLog.ENABLE_DEBUG) {
	    //  AphidLog.d("bufferedViews: %s; buffer index %d, adapter index %d", bufferedViews, bufferIndex,
	    //             adapterIndex);
	    //}
	  }

	  void postFlippedToView(final int indexInAdapter) {
	    handler.post(new Runnable() {
	      @Override
	      public void run() {
	        flippedToView(indexInAdapter, true);
	      }
	    });
	  }

	  void flippedToView(final int indexInAdapter, boolean isPost) {
	    //if (AphidLog.ENABLE_DEBUG) {
	    //  AphidLog.d("flippedToView: %d, isPost %s", indexInAdapter, isPost);
	    //}

	    debugBufferedViews();

	    if (indexInAdapter >= 0 && indexInAdapter < adapterDataCount) {

	      if (indexInAdapter == adapterIndex + 1) { //forward one page
	        if (adapterIndex < adapterDataCount - 1) {
	          adapterIndex++;
	          View old = bufferedViews.get(bufferIndex);
	          if (bufferIndex + 1 > sideBufferSize) {
	            releaseView(bufferedViews.removeFirst());
	          }
	          if (adapterIndex + sideBufferSize < adapterDataCount) {
	            bufferedViews.addLast(viewFromAdapter(adapterIndex + sideBufferSize, true));
	          }
	          bufferIndex = bufferedViews.indexOf(old) + 1;
	          requestLayout();
	          updateVisibleView(bufferIndex);
	        }
	      } else if (indexInAdapter == adapterIndex - 1) {
	        if (adapterIndex > 0) {
	          adapterIndex--;
	          View old = bufferedViews.get(bufferIndex);
	          if (bufferIndex - 1 + sideBufferSize < bufferedViews.size() - 1) {
	            releaseView(bufferedViews.removeLast());
	          }
	          if (adapterIndex - sideBufferSize >= 0) {
	            bufferedViews.addFirst(viewFromAdapter(adapterIndex - sideBufferSize, false));
	          }
	          bufferIndex = bufferedViews.indexOf(old) - 1;
	          requestLayout();
	          updateVisibleView(bufferIndex);
	        }
	      } else {
	        //AphidLog.e("Should not happen: indexInAdapter %d, adapterIndex %d", indexInAdapter,
	        //           adapterIndex);
	      }
	    } else {
	      Assert.fail("Invalid indexInAdapter: " + indexInAdapter);
	    }
	    //debugBufferedViews();
	  }

	  void showFlipAnimation() {
	    if (!inFlipAnimation) {
	      inFlipAnimation = true;

	      cards.setVisible(true);
	      cards.setFirstDrawFinished(false);
	      surfaceView.requestRender();
	    }
	  }

	  void postHideFlipAnimation() {
	    if (inFlipAnimation) {
	      handler.post(new Runnable() {
	        @Override
	        public void run() {
	          hideFlipAnimation();
	        }
	      });
	    }
	  }

	  private void hideFlipAnimation() {
	    if (inFlipAnimation) {
	      inFlipAnimation = false;

	      updateVisibleView(bufferIndex);

	      if (onViewFlipListener != null) {
	        onViewFlipListener.onViewFlipped(bufferedViews.get(bufferIndex), adapterIndex);
	      }

	      handler.post(new Runnable() {
	        public void run() {
	          if (!inFlipAnimation) {
	            cards.setVisible(false);
	            surfaceView.requestRender(); //ask OpenGL to clear its display
	          }
	        }
	      });
	    }
	  }

	  private void onDataChanged() {
	    adapterDataCount = adapter.getCount();
	    int activeIndex;
	    if (adapterIndex < 0) {
	      activeIndex = 0;
	    } else {
	      activeIndex = Math.min(adapterIndex, adapterDataCount - 1);
	    }

	    releaseViews();
	    setSelection(activeIndex);
	  }

	  private class MyDataSetObserver extends DataSetObserver {

	    @Override
	    public void onChanged() {
	      onDataChanged();
	    }

	    @Override
	    public void onInvalidated() {
	      onDataChanged();
	    }
	  }
	}
