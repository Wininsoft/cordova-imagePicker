/**
 * An Image Picker Plugin for Cordova/PhoneGap.
 */
package com.synconset;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class ImagePicker extends CordovaPlugin {
	public static String TAG = "ImagePicker";
	 
	private CallbackContext callbackContext;
	private JSONObject params;
	 
	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
		 this.callbackContext = callbackContext;
		 this.params = args.getJSONObject(0);
		if (action.equals("getPictures")) {
			Intent intent = new Intent(cordova.getActivity(), MultiImageChooserActivity.class);
			int max = 20;
			int desiredWidth = 0;
			int desiredHeight = 0;
			int quality = 100;
			String progressCaption=null;
			String progressMessage=null;
			String maximumImageErrorTitle=null;
			String maximumImageErrorMessage=null;
			String ok=null;
			if (this.params.has("maximumImagesCount")) {
				max = this.params.getInt("maximumImagesCount");
			}
			if (this.params.has("width")) {
				desiredWidth = this.params.getInt("width");
			}
			if (this.params.has("height")) {
				desiredHeight = this.params.getInt("height");
			}
			if (this.params.has("quality")) {
				quality = this.params.getInt("quality");
			}
			if (this.params.has("localization_ProgressCaption")) {
				progressCaption = this.params.getString("localization_ProgressCaption");
			}
			if (this.params.has("localization_ProgressMessage")) {
				progressMessage = this.params.getString("localization_ProgressMessage");
			}
			if (this.params.has("localization_MaximumImageErrorTitle")) {
				maximumImageErrorTitle = this.params.getString("localization_MaximumImageErrorTitle");
			}
			if (this.params.has("localization_MaximumImageErrorMessage")) {
				maximumImageErrorMessage = this.params.getString("localization_MaximumImageErrorMessage");
			}
			if (this.params.has("localization_OK")) {
				ok = this.params.getString("localization_OK");
			}
			intent.putExtra("MAX_IMAGES", max);
			intent.putExtra("WIDTH", desiredWidth);
			intent.putExtra("HEIGHT", desiredHeight);
			intent.putExtra("QUALITY", quality);
			intent.putExtra("Localization_ProgressCaption", progressCaption);
			intent.putExtra("Localization_ProgressMessage", progressMessage);
			intent.putExtra("Localization_MaximumImageErrorTitle", maximumImageErrorTitle);
			intent.putExtra("Localization_MaximumImageErrorMessage", maximumImageErrorMessage);
			intent.putExtra("Localization_OK", ok);
			if (this.cordova != null) {
				this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
			}
		}
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && data != null) {
			ArrayList<String> fileNames = data.getStringArrayListExtra("MULTIPLEFILENAMES");
			JSONArray res = new JSONArray(fileNames);
			this.callbackContext.success(res);
		} else if (resultCode == Activity.RESULT_CANCELED && data != null) {
			String error = data.getStringExtra("ERRORMESSAGE");
			this.callbackContext.error(error);
		} else if (resultCode == Activity.RESULT_CANCELED) {
			JSONArray res = new JSONArray();
			this.callbackContext.success(res);
		} else {
			this.callbackContext.error("No images selected");
		}
	}
}
