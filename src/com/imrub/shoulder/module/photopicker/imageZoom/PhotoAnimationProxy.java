package com.imrub.shoulder.module.photopicker.imageZoom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridView;
import android.widget.ImageView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.module.photopicker.ITitleable;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class PhotoAnimationProxy {

	private int mShortAnimationDuration = 300;
	private Animator mCurrentAnimator;
	
	private Rect mStartBounds;
	private Rect mFinalBounds;

	private float startScale;
	private float startScaleFinal;

	private PhotoView mPhotoView;
	private ImageView mImageLayer;
	
	private ITitleable mTitleStatusListener;
	
	public void zoomImageFromThumb(Bitmap bitmap, View thumbView, int position,
			Context context, GridView gridView, ITitleable titleStatusCallBack, View view) {
		
		mTitleStatusListener = titleStatusCallBack;
		
		// If there's an animation in progress, cancel it immediately and
		// proceed with this one.
		if (mCurrentAnimator != null) {
//			mCurrentAnimator.cancel();
			return ;
		}

		if(mImageLayer == null){
			mImageLayer = (ImageView)((Activity) context).findViewById(R.id.imagegallery_layer);
		}
		
		mPhotoView = (PhotoView) ((Activity) context)
				.findViewById(R.id.imagegallery_photoview);
		mPhotoView.setImageBitmap(bitmap);
		initTargetView(gridView, context, mPhotoView, position, view);

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step
		// involves lots of math. Yay, math.
		mStartBounds = new Rect();
		mFinalBounds = new Rect();
		
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the
		// final bounds are the global visible rectangle of the container view.
		// Also
		// set the container view's offset as the origin for the bounds, since
		// that's
		// the origin for the positioning animation properties (X, Y).
		thumbView.getGlobalVisibleRect(mStartBounds);

		((Activity) context).findViewById(R.id.imagegallery_block)
				.getGlobalVisibleRect(mFinalBounds, globalOffset);
		mStartBounds.offset(-globalOffset.x, -globalOffset.y);
		mFinalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the
		// "center crop" technique. This prevents undesirable stretching during
		// the animation.
		// Also calculate the start scaling factor (the end scaling factor is
		// always 1.0).

		if ((float) mFinalBounds.width() / mFinalBounds.height() > (float) mStartBounds
				.width() / mStartBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) mStartBounds.height() / mFinalBounds.height();
			float startWidth = startScale * mFinalBounds.width();
			float deltaWidth = (startWidth - mStartBounds.width()) / 2;
			mStartBounds.left -= deltaWidth;
			mStartBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) mStartBounds.width() / mFinalBounds.width();
			float startHeight = startScale * mFinalBounds.height();
			float deltaHeight = (startHeight - mStartBounds.height()) / 2;
			mStartBounds.top -= deltaHeight;
			mStartBounds.bottom += deltaHeight;
		}

		// show the zoomed-in view. When the animation
		// begins,
		// it will position the zoomed-in view in the place of the thumbnail.
		mPhotoView.setVisibility(View.VISIBLE);
		// Set the pivot point for SCALE_X and SCALE_Y transformations to the
		// top-left corner of
		// the zoomed-in view (the default is the center of the view).

		AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(1);
		animSet.play(ObjectAnimator.ofFloat(mPhotoView, "pivotX", 0f))
				.with(ObjectAnimator.ofFloat(mPhotoView, "pivotY", 0f))
				.with(ObjectAnimator.ofFloat(mPhotoView, "alpha", 1.0f));
		animSet.start();

		// Construct and run the parallel animation of the four translation and
		// scale properties
		// (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha",
				1.0f, 0.f);
		ObjectAnimator animatorX = ObjectAnimator.ofFloat(mPhotoView, "x",
				mStartBounds.left, mFinalBounds.left);
		ObjectAnimator animatorY = ObjectAnimator.ofFloat(mPhotoView, "y",
				mStartBounds.top, mFinalBounds.top);
		ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(mPhotoView,
				"scaleX", startScale, 1f);
		ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(mPhotoView,
				"scaleY", startScale, 1f);

		set.play(alphaAnimator).with(animatorX).with(animatorY)
				.with(animatorScaleX).with(animatorScaleY);
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {

			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
				mImageLayer.setVisibility(View.VISIBLE);
				mTitleStatusListener.onTitleStatusChange(true);
			}

			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
				mImageLayer.setVisibility(View.VISIBLE);
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down to the
		// original bounds
		// and show the thumbnail instead of the expanded image.
		startScaleFinal = startScale;
	}

	private boolean getScaleFinalBounds(GridView gridView, Context context,
			int position) {
//		View childView = gridView.getChildAt(position);

//		startBounds = new Rect();
//		final Rect finalBounds = new Rect();
//		final Point globalOffset = new Point();
//
//		try {
//			childView.getGlobalVisibleRect(startBounds);
//		} catch (Exception e) {
//			return false;
//		}
//		((Activity) context).findViewById(R.id.imagegallery_block)
//				.getGlobalVisibleRect(finalBounds, globalOffset);
//		startBounds.offset(-globalOffset.x, -globalOffset.y);
//		finalBounds.offset(-globalOffset.x, -globalOffset.y);
//
//		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
//				.width() / startBounds.height()) {
//			// Extend start bounds horizontally
//			startScale = (float) startBounds.height() / finalBounds.height();
//			float startWidth = startScale * finalBounds.width();
//			float deltaWidth = (startWidth - startBounds.width()) / 2;
//			startBounds.left -= deltaWidth;
//			startBounds.right += deltaWidth;
//		} else {
//			// Extend start bounds vertically
//			startScale = (float) startBounds.width() / finalBounds.width();
//			float startHeight = startScale * finalBounds.height();
//			float deltaHeight = (startHeight - startBounds.height()) / 2;
//			startBounds.top -= deltaHeight;
//			startBounds.bottom += deltaHeight;
//		}
//		startScaleFinal = startScale;
		return true;
	}

	private void initTargetView(final GridView gridView, final Context context, final PhotoView photoView, final int position, final View view) {
//		final PhotoView photoView = new PhotoView(container.getContext());
//		photoView.setImageResource(sDrawables[position]);

		// Now just add PhotoView to ViewPager and return it
//		container.addView(photoView, LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
		photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
					public void onPhotoTap(View paramAnonymousView,
							float paramAnonymousFloat1,
							float paramAnonymousFloat2) {
						if (mCurrentAnimator != null) {
							mCurrentAnimator.cancel();
						}

						photoView.clearZoom();

						boolean scaleResult = getScaleFinalBounds(gridView, context, position);
						// Animate the four positioning/sizing properties in
						// parallel,
						// back to their
						// original values.
						AnimatorSet as = new AnimatorSet();
						ObjectAnimator containAlphaAnimator = ObjectAnimator
								.ofFloat(view,"alpha", 0.f, 1.0f);
						if (scaleResult) {
							ObjectAnimator animatorX = ObjectAnimator.ofFloat(
									photoView, "x", mStartBounds.left);
							ObjectAnimator animatorY = ObjectAnimator.ofFloat(
									photoView, "y", mStartBounds.top);
							ObjectAnimator animatorScaleX = ObjectAnimator
									.ofFloat(photoView, "scaleX",
											startScaleFinal);
							ObjectAnimator animatorScaleY = ObjectAnimator
									.ofFloat(photoView, "scaleY",
											startScaleFinal);

							as.play(containAlphaAnimator).with(animatorX)
									.with(animatorY).with(animatorScaleX)
									.with(animatorScaleY);
						} else {
							// the selected photoview is beyond the mobile
							// screen display
							// so it just fade out
							ObjectAnimator alphaAnimator = ObjectAnimator
									.ofFloat(photoView, "alpha", 0.1f);
							as.play(alphaAnimator).with(containAlphaAnimator);
						}
						as.setDuration(mShortAnimationDuration);
						as.setInterpolator(new DecelerateInterpolator());
						as.addListener(new AnimatorListenerAdapter() {

							@Override
							public void onAnimationStart(Animator animation) {
								super.onAnimationStart(animation);
								mImageLayer.setVisibility(View.GONE);
								mTitleStatusListener.onTitleStatusChange(false);
							}
							
							@Override
							public void onAnimationEnd(Animator animation) {
								photoView.clearAnimation();
								photoView.setVisibility(View.GONE);
								mCurrentAnimator = null;
							}

							@Override
							public void onAnimationCancel(Animator animation) {
								photoView.clearAnimation();
								photoView.setVisibility(View.GONE);
								mCurrentAnimator = null;
							}
						});
						as.start();
						mCurrentAnimator = as;
					}
				});
	}

}
