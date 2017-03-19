/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sakuna63.sandbox;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.Arrays;

import uk.co.senab.photoview.PhotoView;

/**
 * This sub-activity shows a zoomed-in view of a specific photo, along with the
 * picture's text description. Most of the logic is for the animations that will
 * be run when the activity is being launched and exited. When launching,
 * the large version of the picture will resize from the thumbnail version in the
 * main activity, colorizing it from the thumbnail's grayscale version at the
 * same time. Meanwhile, the black background of the activity will fade in and
 * the description will eventually slide into place. The exit animation runs all
 * of this in reverse.
 */
public class PictureDetailsActivity extends AppCompatActivity {

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
    private static final String PACKAGE_NAME = "com.example.android.activityanim";
    private static final int ANIM_DURATION = 300;
    ColorDrawable mBackground;
    int mLeftDelta;
    int mTopDelta;
    float mWidthScale;
    float mHeightScale;
    private PhotoView mImageView;
    private int mOriginalOrientation;
    private int mThumbnailWidth;
    private int mThumbnailHeight;
    private View mParentLayout;
    private int mThumbnailTop;
    private int mThumbnailLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_info);
        mImageView = (PhotoView) findViewById(R.id.imageView);

        // Retrieve the data we need for the picture/description to display and
        // the thumbnail to animate it from
        Bundle bundle = getIntent().getExtras();
        Bitmap bitmap = BitmapUtils.getBitmap(getResources(),
                bundle.getInt(PACKAGE_NAME + ".resourceId"));
        mThumbnailTop = bundle.getInt(PACKAGE_NAME + ".top");
        mThumbnailLeft = bundle.getInt(PACKAGE_NAME + ".left");
        mThumbnailWidth = bundle.getInt(PACKAGE_NAME + ".width");
        mThumbnailHeight = bundle.getInt(PACKAGE_NAME + ".height");
        mOriginalOrientation = bundle.getInt(PACKAGE_NAME + ".orientation");

        mImageView.setImageBitmap(bitmap);

        mParentLayout = findViewById(R.id.topLevelLayout);
        mBackground = new ColorDrawable(Color.BLACK);
        ViewCompat.setBackground(mParentLayout, mBackground);

        // Only run the animation if we're coming from the parent activity, not if
        // we're recreated automatically by the window manager (e.g., device rotation)
        if (savedInstanceState == null) {
            ViewTreeObserver observer = mImageView.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    mImageView.getViewTreeObserver().removeOnPreDrawListener(this);

                    // Figure out where the thumbnail and full size versions are, relative
                    // to the screen and each other
                    int[] screenLocation = new int[2];
                    mImageView.getLocationOnScreen(screenLocation);
                    mLeftDelta = mThumbnailLeft - screenLocation[0];
                    mTopDelta = mThumbnailTop - screenLocation[1];
                    Log.d("PictureDetailsActivity", Arrays.toString(screenLocation));

//                    ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
//                    lp.height = lp.width * (mThumbnailHeight / mThumbnailWidth);
//                    mImageView.setLayoutParams(lp);

                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = (float) mThumbnailWidth / mImageView.getWidth();
                    mHeightScale = (float) mThumbnailHeight / mImageView.getHeight();

                    runEnterAnimation();

                    return true;
                }
            });
        }
    }

    /**
     * The enter animation scales the picture in from its previous thumbnail
     * size/location, colorizing it in parallel. In parallel, the background of the
     * activity is fading in. When the pictue is in place, the text description
     * drops down.
     */
    public void runEnterAnimation() {
        final long duration = (long) (ANIM_DURATION * ActivityAnimations.sAnimatorScale);

        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
        mImageView.setPivotX(0);
        mImageView.setPivotY(0);
        mImageView.setScaleX(mWidthScale);
        mImageView.setScaleY(mHeightScale);
        mImageView.setTranslationX(mLeftDelta);
        mImageView.setTranslationY(mTopDelta);

        // Animate scale and translation to go from thumbnail to full size
        mImageView.animate().setDuration(duration).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
                        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        mImageView.setLayoutParams(lp);
                    }
                }).
                setInterpolator(sDecelerator);

        // Fade in the black background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0, 200);
        bgAnim.setDuration(duration);
        bgAnim.start();
    }

    /**
     * The exit animation is basically a reverse of the enter animation, except that if
     * the orientation has changed we simply scale the picture back into the center of
     * the screen.
     *
     * @param endAction This action gets run after the animation completes (this is
     *                  when we actually switch activities)
     */
    public void runExitAnimation(final Runnable endAction) {
        final long duration = (long) (ANIM_DURATION * ActivityAnimations.sAnimatorScale);

        // No need to set initial values for the reverse animation; the image is at the
        // starting size/location that we want to start from. Just animate to the
        // thumbnail size/location that we retrieved earlier

        // Caveat: configuration change invalidates thumbnail positions; just animate
        // the scale around the center. Also, fade it out since it won't match up with
        // whatever's actually in the center
        final boolean fadeOut;
        if (getResources().getConfiguration().orientation != mOriginalOrientation) {
            mImageView.setPivotX(mImageView.getWidth() / 2);
            mImageView.setPivotY(mImageView.getHeight() / 2);
            mLeftDelta = 0;
            mTopDelta = 0;
            fadeOut = true;
        } else {
            fadeOut = false;
        }

        // get view raw position in display
        int[] xy = new int[2];
        mImageView.getLocationOnScreen(xy);
        Log.d("PictureDetailsActivity", Arrays.toString(xy));

        // get scaled rect
        RectF scaledRect = new RectF(mImageView.getDisplayRect());
        Log.d("PictureDetailsActivity", "scaledRect:" + scaledRect);
        float scale = mImageView.getScale();
        Log.d("PictureDetailsActivity", "scale:" + scale);

        // reset PhotoView scale
        mImageView.setScale(1.0f, false);
        RectF defaultRect = new RectF(mImageView.getDisplayRect());
        Log.d("PictureDetailsActivity", "defaultRect:" + defaultRect);

        // adjust view size to thumbnail aspect ratio
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mImageView.getLayoutParams();
        lp.height = (int) (mImageView.getWidth() * ((float) mThumbnailHeight / mThumbnailWidth));

        // adjust scaled size and position
        mImageView.setPivotX(0);
        mImageView.setPivotY(0);
        mImageView.setScaleX(scale);
        mImageView.setScaleY(scale);

        float translationX = scaledRect.left - defaultRect.left;
        Log.d("PictureDetailsActivity", "translationX:" + translationX);
//        mImageView.setTranslationX(translationX);
        float translationY = scaledRect.top - defaultRect.top;
        Log.d("PictureDetailsActivity", "translationY:" + translationY);
//        mImageView.setTranslationY(translationY);

//        lp.leftMargin = (int) scaledRect.left;
//        lp.topMargin = (int) scaledRect.top;
//        lp.rightMargin = Math.min((int) (mParentLayout.getWidth() - scaledRect.right), 0);
//        lp.bottomMargin = Math.min((int) (mParentLayout.getHeight() - scaledRect.bottom), 0);
//        mImageView.setLayoutParams(lp);
//        mImageView.setTop((int) scaledRect.top);
//        mImageView.setLeft((int) scaledRect.left);
//
//        float widthScale = mThumbnailWidth / scaledRect.width();
//        float heightScale = mThumbnailHeight / scaledRect.height();
//        float leftDelta = mThumbnailLeft - scaledRect.left - xy[0];
//        float topDelta = mThumbnailTop - scaledRect.top - xy[1];
//
//        // Animate image back to thumbnail size/location
//        mImageView.animate().setDuration(duration).
//                scaleX(widthScale).scaleY(heightScale).
//                translationX(leftDelta).translationY(topDelta).
//                withEndAction(endAction);
//        if (fadeOut) {
//            mImageView.animate().alpha(0);
//        }
//        // Fade out background
//        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0);
//        bgAnim.setDuration(duration);
//        bgAnim.start();
    }

    /**
     * Overriding this method allows us to run our exit animation first, then exiting
     * the activity when it is complete.
     */
    @Override
    public void onBackPressed() {
        runExitAnimation(new Runnable() {
            public void run() {
                // *Now* go ahead and exit the activity
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();

        // override transitions to skip the standard window animations
        overridePendingTransition(0, 0);
    }
}
