package com.zaimus.manager;

import java.io.Serializable;




import com.zaimus.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class Headermanager.
 */
public class Headermanager implements Serializable {

	/** The context. */
	private Activity context;

	/** The inflator. */
	private LayoutInflater inflator;

	/** The header view. */
	public View headerView;

	/** The m heading1. */
	private TextView mHeading1;

	/** The relative params. */
	private RelativeLayout.LayoutParams relativeParams;

	/** The heading1. */
	private String heading1;

	/** The is cancel. */
	private boolean isCancel = false;

	private EditText edtText;
	/* FOR HOME SCREEN */
	/** The is home. */
	private boolean isHome = false;

	/** The m right text. */
	private TextView mLeftText, mRightText;

	/** The m left. */
	private ImageView mLeftImage, mRightImage, mRight, mLeft;

	/**
	 * Instantiates a new headermanager.
	 * 
	 * @param context
	 *            the context
	 * @param heading1
	 *            the heading1
	 */

	public Headermanager(Activity context, String heading1) {
		this.setContext(context);

		inflator = LayoutInflater.from(context);
		this.heading1 = heading1;

	}

	/*
	 * public Headermanager(Activity context,String heading1) {
	 * this.setContext(context); inflator = LayoutInflater.from(context);
	 * this.heading1=heading1; this.isCancel=isCancel; }
	 */
	/**
	 * Instantiates a new headermanager.
	 * 
	 * @param home
	 *            the home
	 * @param context
	 *            the context
	 */
	public Headermanager(boolean home, Activity context) {
		this.setContext(context);
		inflator = LayoutInflater.from(context);
		this.isHome = home;
	}

	/**
	 * Gets the left text.
	 * 
	 * @return the left text
	 */
	public TextView getLeftText() {
		return mLeftText;
	}

	/**
	 * Sets the left text.
	 * 
	 * @param mLeftText
	 *            the new left text
	 */
	public void setLeftText(TextView mLeftText) {
		this.mLeftText = mLeftText;
	}

	/**
	 * Gets the right text.
	 * 
	 * @return the right text
	 */
	public TextView getRightText() {
		return mRightText;
	}

	/**
	 * Sets the right text.
	 * 
	 * @param mLeftText
	 *            the new right text
	 */
	public void setRightText(TextView mLeftText) {
		this.mRightText = mLeftText;
	}

	// image view
	/**
	 * Gets the left image.
	 * 
	 * @return the left image
	 */
	public ImageView getLeftImage() {
		return mLeftImage;
	}

	/**
	 * Sets the left image.
	 * 
	 * @param mLeftImage
	 *            the new left image
	 */
	public void setLeftImage(ImageView mLeftImage) {
		this.mLeftImage = mLeftImage;
	}

	/**
	 * Gets the right image.
	 * 
	 * @return the right image
	 */
	public ImageView getRightImage() {
		return mLeftImage;
	}

	/**
	 * Sets the right image.
	 * 
	 * @param mLeftImage
	 *            the new right image
	 */
	public void setRightImage(ImageView mLeftImage) {
		this.mRightImage = mLeftImage;
	}

	/**
	 * Sets the visible.
	 * 
	 * @param v
	 *            the new visible
	 */
	public void setVisible(View v) {
		v.setVisibility(View.VISIBLE);
	}

	/**
	 * Sets the invisible.
	 */
	public void setInvisible() {
		headerView.setVisibility(View.INVISIBLE);
	}

	/**
	 * Sets the invisible.
	 * 
	 * @param v
	 *            the new invisible
	 */
	public void setInvisible(View v) {
		v.setVisibility(View.INVISIBLE);
	}

	/**
	 * Gets the header.
	 * 
	 * @param headerHolder
	 *            the header holder
	 * @return the header
	 */
	public int getHeader(LinearLayout headerHolder, int type,
			boolean viewVisibility) {
		initializeUI(type, viewVisibility);
		relativeParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		//relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL, R.id.relativeHeader);

		headerHolder.addView(headerView, relativeParams);
		return headerView.getId();
	}

	/**
	 * Gets the header.
	 * 
	 * @param headerHolder
	 *            the header holder
	 * @return the header
	 */
	public int getHeader(RelativeLayout headerHolder, boolean getHeading,
			int type, boolean viewVisibility) {
		initializeUI(getHeading, type, viewVisibility);
		relativeParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		headerHolder.addView(headerView, relativeParams);
		return headerView.getId();
	}

	/**
	 * Initialize ui.
	 */
	private void initializeUI(int type, boolean viewVisibility) {
		inflator = LayoutInflater.from(getContext());
		headerView = inflator.inflate(R.layout.common_header_single, null);
		RelativeLayout logoHeader = ((RelativeLayout) headerView
				.findViewById(R.id.relative_logo_header));
		mHeading1 = (TextView) headerView.findViewById(R.id.heading);
		mHeading1.setText(heading1);
		mHeading1.setTextColor(context.getResources().getColor(R.color.white));
		mRight = (ImageView) headerView.findViewById(R.id.btn_right);
		mLeft = (ImageView) headerView.findViewById(R.id.btn_left);
		if (type == 0) {
			logoHeader.setBackgroundResource(R.color.violet);// two
																		// buttons
			//logoHeader.setBackgroundColor(Color.WHITE);;
			mRight.setVisibility(View.INVISIBLE);
		} else if (type == 1) {
			logoHeader.setBackgroundResource(R.color.violet);// left
		mRight.setVisibility(View.INVISIBLE);	
		mLeft.setVisibility(View.INVISIBLE);// button
		}
		/*mHeading1 = (TextView) headerView.findViewById(R.id.heading);
		mHeading1.setText(heading1);
		mRight = (ImageView) headerView.findViewById(R.id.btn_right);
		mLeft = (ImageView) headerView.findViewById(R.id.btn_left);*/
	}

	/**
	 * Initialize ui.
	 */
	private void initializeUI(boolean getHeading, int type,
			boolean viewVisibility) {
		inflator = LayoutInflater.from(getContext());

		headerView = inflator.inflate(R.layout.common_header_single, null);
		RelativeLayout logoHeader = ((RelativeLayout) headerView
				.findViewById(R.id.relative_logo_header));
		if (type == 0) {
			logoHeader.setBackgroundResource(R.drawable.titlebarinner);// left
			mRight.setVisibility(View.INVISIBLE);
		} else if (type == 1) {
			logoHeader.setBackgroundResource(R.drawable.titlebar);// left
			mRight.setVisibility(View.INVISIBLE);	
			mLeft.setVisibility(View.INVISIBLE);
		}
		mHeading1 = (TextView) headerView.findViewById(R.id.heading);
		mHeading1.setText(heading1);
		mHeading1.setTextSize(8);
		mHeading1.setTextColor(context.getResources().getColor(R.color.white));

		mRight = (ImageView) headerView.findViewById(R.id.btn_right);
		mLeft = (ImageView) headerView.findViewById(R.id.btn_left);
	}

	/**
	 * Sets the title bar.
	 * 
	 * @param titleBar
	 *            the new title bar
	 */
	public void setTitleBar(int titleBar) {
		this.headerView.setBackgroundResource(titleBar);
	}

	public void setTitle(String title) {
		mHeading1.setText(title);
	}

	/**
	 * Gets the left button.
	 * 
	 * @return the left button
	 */
	public ImageView getLeftButton() {
		return mLeft;
	}

	/**
	 * Sets the left button.
	 * 
	 * @param right
	 *            the new left button
	 */
	public void setLeftButton(ImageView right) {
		this.mLeft = right;
	}

	/**
	 * Gets the right button.
	 * 
	 * @return the right button
	 */
	public ImageView getRightButton() {
		return mRight;
	}

	public EditText getEditText() {
		mHeading1.setVisibility(View.GONE);
		edtText.setVisibility(View.VISIBLE);
		return edtText;
	}

	/**
	 * Sets the edits the text.
	 * 
	 * @param editText
	 *            the new edits the text
	 */
	public void setEditText(EditText editText) {
		this.edtText = editText;
		setVisible(edtText);
	}

	/**
	 * Sets the right button.
	 * 
	 * @param right
	 *            the new right button
	 */
	public void setRightButton(ImageView right) {
		this.mRight = right;
	}

	/**
	 * Sets the button right selector.
	 * 
	 * @param normalStateResID
	 *            the normal state res id
	 * @param pressedStateResID
	 *            the pressed state res id
	 */
	public void setButtonRightSelector(int normalStateResID,
			int pressedStateResID) {
		mRight.setImageDrawable(getButtonDrawableByScreenCathegory(
				normalStateResID, pressedStateResID));
		setVisible(mRight);
	}

	/**
	 * Sets the button left selector.
	 * 
	 * @param normalStateResID
	 *            the normal state res id
	 * @param pressedStateResID
	 *            the pressed state res id
	 */
	public void setButtonLeftSelector(int normalStateResID,
			int pressedStateResID) {
		mLeft.setImageDrawable(getButtonDrawableByScreenCathegory(
				normalStateResID, pressedStateResID));
		setVisible(mLeft);
	}

	/**
	 * Gets the button drawable by screen cathegory.
	 * 
	 * @param normalStateResID
	 *            the normal state res id
	 * @param pressedStateResID
	 *            the pressed state res id
	 * @return the button drawable by screen cathegory
	 */
	public Drawable getButtonDrawableByScreenCathegory(int normalStateResID,
			int pressedStateResID) {

		Drawable state_normal = context.getResources()
				.getDrawable(normalStateResID).mutate();

		Drawable state_pressed = context.getResources()
				.getDrawable(pressedStateResID).mutate();

		StateListDrawable drawable = new StateListDrawable();

		drawable.addState(new int[] { android.R.attr.state_pressed },
				state_pressed);
		drawable.addState(new int[] { android.R.attr.state_enabled },
				state_normal);

		return drawable;
	}

	// public void setCancelButton()
	// {
	// mRight.setBackgroundResource(R.drawable.close_button_selector);
	// setVisible(mRight);
	// }

	/**
	 * Sets the context.
	 * 
	 * @param context
	 *            the new context
	 */
	public void setContext(Activity context) {
		this.context = context;
	}

	/**
	 * Gets the context.
	 * 
	 * @return the context
	 */
	public Activity getContext() {
		return context;
	}

}
