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
import android.view.MenuItem;
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
    private PhotoView mImageView;
    private int mOriginalOrientation;
    private int mStartThumbnailTop;
    private int mStartThumbnailLeft;
    private int mStartThumbnailWidth;
    private int mStartThumbnailHeight;
    private boolean mEnableRotation;
    private View mParentLayout;

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
        mStartThumbnailTop = bundle.getInt(PACKAGE_NAME + ".top");
        mStartThumbnailLeft = bundle.getInt(PACKAGE_NAME + ".left");
        mStartThumbnailWidth = bundle.getInt(PACKAGE_NAME + ".width");
        mStartThumbnailHeight = bundle.getInt(PACKAGE_NAME + ".height");
        mOriginalOrientation = bundle.getInt(PACKAGE_NAME + ".orientation");
        mEnableRotation = ActivityAnimations.sEnableRotate; // bundle.getBoolean(PACKAGE_NAME + ".rotation", true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    runEnterAnimation();

                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * The enter animation scales the picture in from its previous thumbnail
     * size/location, colorizing it in parallel. In parallel, the background of the
     * activity is fading in. When the pictue is in place, the text description
     * drops down.
     */
    public void runEnterAnimation() {
        final long duration = (long) (ANIM_DURATION * ActivityAnimations.sAnimatorScale);

        // Figure out where the thumbnail and full size versions are, relative
        // to the screen and each other
        int[] screenLocation = new int[2];
        mImageView.getLocationOnScreen(screenLocation);
        float leftDelta = (mStartThumbnailLeft + mStartThumbnailWidth / 2f) - (screenLocation[0] + (mImageView.getWidth() / 2f));
        float topDelta = (mStartThumbnailTop + mStartThumbnailHeight / 2f) - (screenLocation[1] + (mImageView.getHeight() / 2f));

        // Scale factors to make the large version the same size as the thumbnail
        float widthScale = (float) mStartThumbnailWidth / mImageView.getWidth();
        float heightScale = (float) mStartThumbnailHeight / mImageView.getHeight();

        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
//        mImageView.setPivotX(0);
//        mImageView.setPivotY(0);
        mImageView.setScaleX(widthScale);
        mImageView.setScaleY(heightScale);
        mImageView.setTranslationX(leftDelta);
        mImageView.setTranslationY(topDelta);

        // Animate scale and translation to go from thumbnail to full size
        mImageView.animate().setDuration(duration).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Expand view height to enable scale image by pinch in/out
                        ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
                        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                        if (mEnableRotation) {
////                            lp.width = mParentLayout.getHeight();
//                            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
////                            lp.height = mParentLayout.getWidth();
//                            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                        } else {
//                            lp.width = mParentLayout.getWidth();
//                            lp.height = mParentLayout.getHeight();
//                        }
//                        Log.d("PictureDetailsActivity", "lp.width:" + lp.width);
//                        Log.d("PictureDetailsActivity", "lp.height:" + lp.height);
                        mImageView.setLayoutParams(lp);

                        if (mEnableRotation) {
                            mImageView.setRotation(0f);
                            mImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                @Override
                                public boolean onPreDraw() {
                                    mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                                    mImageView.setRotationTo(90f);
                                    return true;
                                }
                            });
                        }
                    }
                }).
                setInterpolator(sDecelerator);

        if (mEnableRotation) {
            mImageView.animate().rotation(90f);
//            ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0, 90f)
//                    .setDuration(duration);
//            rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate((Float) animation.getAnimatedValue(), mImageView.getWidth() / 2f, mImageView.getHeight() / 2f);
//                    mImageView.setDisplayMatrix(matrix);
//                }
//            });
//            rotateAnimator.setInterpolator(sDecelerator);
//            rotateAnimator.start();
        }

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

        // get view raw position in display
        int[] xy = new int[2];
        mImageView.getLocationOnScreen(xy);
        Log.d("PictureDetailsActivity", Arrays.toString(xy));

        int viewX = xy[0];
        int viewY = xy[1];

        // get scaled rect
        float scale = mImageView.getScale();
        Log.d("PictureDetailsActivity", "scale:" + scale);
        RectF scaledRect = new RectF(mImageView.getDisplayRect());
        Log.d("PictureDetailsActivity", "scaledRect:" + scaledRect);

        // reset PhotoView scale
        mImageView.setScale(1.0f, false);
        if (mEnableRotation) {
            mImageView.setRotationTo(90f);
        }
        RectF rotatedRect = new RectF(mImageView.getDisplayRect());
        Log.d("PictureDetailsActivity", "rotatedRect:" + rotatedRect);
        if (mEnableRotation) {
            mImageView.setRotationTo(0f);
        }
        RectF defaultRect = new RectF(mImageView.getDisplayRect());

        // adjust view size to thumbnail aspect ratio
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mImageView.getLayoutParams();
        lp.height = (int) (mImageView.getWidth() * ((float) mStartThumbnailHeight / mStartThumbnailWidth));
        mImageView.setLayoutParams(lp);

        // adjust scaled size and position
        mImageView.setPivotX(0);
        mImageView.setPivotY(0);
        mImageView.setScaleX(scale);
        mImageView.setScaleY(scale);

        float translationX = scaledRect.left - rotatedRect.left;
        if (mEnableRotation) {
            translationX = scaledRect.top - rotatedRect.top;
        }
        Log.d("PictureDetailsActivity", "translationX:" + translationX);
        mImageView.setTranslationX(translationX);
        float translationY = scaledRect.top - rotatedRect.top;
        if (mEnableRotation) {
            translationY = rotatedRect.right - scaledRect.right;
        }
        Log.d("PictureDetailsActivity", "translationY:" + translationY);
        mImageView.setTranslationY(translationY);

        float imageWidth = mEnableRotation ? rotatedRect.height() : rotatedRect.width();
        float imageHeight = mEnableRotation ? rotatedRect.width() : rotatedRect.height();

        float pivotX = imageWidth / 2f;
        Log.d("PictureDetailsActivity", "pivotX:" + pivotX);
        mImageView.setPivotX(pivotX);
        float pivotY = imageHeight / 2f;
        Log.d("PictureDetailsActivity", "pivotY:" + pivotY);
        mImageView.setPivotY(pivotY);

        float widthScale = mStartThumbnailWidth / imageWidth;
        float heightScale = mStartThumbnailHeight / imageHeight;
        float leftDelta = (mStartThumbnailLeft + mStartThumbnailWidth / 2f) - (viewX + defaultRect.left + (imageWidth / 2f));
        float topDelta = (mStartThumbnailTop + mStartThumbnailHeight / 2f) - (viewY + defaultRect.top + (imageHeight / 2f));

        Log.d("PictureDetailsActivity", "widthScale:" + widthScale);
        Log.d("PictureDetailsActivity", "heightScale:" + heightScale);
        Log.d("PictureDetailsActivity", "leftDelta:" + leftDelta);
        Log.d("PictureDetailsActivity", "topDelta:" + topDelta);

        // Animate image back to thumbnail size/location
        mImageView.animate().setDuration(duration).
                scaleX(widthScale).
                scaleY(heightScale).
                translationX(leftDelta).
                translationY(topDelta).
//                withEndAction(endAction);
        withEndAction(null);

//        if (mEnableRotation) {
//            mImageView.setRotation(90f);
//            mImageView.animate().rotation(0f);
//        }
//
////        mImageView.animate().alpha(0);
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
