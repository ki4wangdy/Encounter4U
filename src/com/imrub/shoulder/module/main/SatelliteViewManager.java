package com.imrub.shoulder.module.main;

public class SatelliteViewManager implements ISatelliteViewListener{

	private ISatelliteViewListener mListener;
	
	private static SatelliteViewManager minstance;
	private SatelliteViewManager(){
	}
	
	public static final SatelliteViewManager getInstance(){
		if(minstance == null){
			minstance = new SatelliteViewManager();
		}
		return minstance ;
	}
	
	public void setSatelliteView(ISatelliteViewListener listener){
		mListener = listener;
	}

	@Override
	public void showOrHide(boolean isShow) {
		if(mListener != null){
			mListener.showOrHide(isShow);
		}
	}
	
}
