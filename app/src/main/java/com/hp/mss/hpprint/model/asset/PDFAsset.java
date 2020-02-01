/*
 * Hewlett-Packard Company
 * All rights reserved.
 *
 * This file, its contents, concepts, methods, behavior, and operation
 * (collectively the "Software") are protected by trade secret, patent,
 * and copyright laws. The use of the Software is governed by a license
 * agreement. Disclosure of the Software to third parties, in any form,
 * in whole or in part, is expressly prohibited except as authorized by
 * the license agreement.
 */

package com.hp.mss.hpprint.model.asset;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PDFAsset implements Asset, Parcelable {

    String uriString;
    Boolean fromAsset;
    Uri uri;

    public PDFAsset(String uriString) {
        this.uriString = uriString;
        this.fromAsset = false;
    }

    public PDFAsset(String uriString, Boolean fromAsset) {
        this.uriString = uriString;
        this.fromAsset = fromAsset;
    }

    public PDFAsset(Uri uri, Boolean fromAsset) {
        this.uri = uri;
        this.fromAsset = fromAsset;
    }

    protected PDFAsset(Parcel in) {
        uriString = in.readString();
        byte tmpFromAsset = in.readByte();
        fromAsset = tmpFromAsset == 0 ? null : tmpFromAsset == 1;
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<PDFAsset> CREATOR = new Creator<PDFAsset>() {
        @Override
        public PDFAsset createFromParcel(Parcel in) {
            return new PDFAsset(in);
        }

        @Override
        public PDFAsset[] newArray(int size) {
            return new PDFAsset[size];
        }
    };

    @Override
    public Bitmap getPrintableBitmap() {
        return null;
    }

    @Override
    public String getAssetUri() {
        return uriString;
    }

    @Override
    public int getAssetWidth() {
        return 0;
    }

    @Override
    public int getAssetHeight() {
        return 0;
    }

    @Override
    public String getContentType() {
        return "pdf";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uriString);
        parcel.writeByte((byte) (fromAsset == null ? 0 : fromAsset ? 1 : 2));
        parcel.writeParcelable(uri, i);
    }

    public InputStream getInputStream(Context context) {
        InputStream input = null;
        try {
            if (fromAsset) {
                // get InputStream from AssetManager
                if (context != null) {
                    input = context.getAssets().open(this.uriString);

                    if (input == null) {
                        Log.e("PDFAsset", "Unable to open asset: " + this.uriString);
                    }
                } else {
                    Log.e("PDFAsset", "Error opening file. Context was null.");
                }
            } else {
                if(this.uri != null) {
                    input = context.getContentResolver().openInputStream(uri);
                } else if (this.uriString != null) {
                    File file = new File(this.uriString);
                    input = new FileInputStream(file);
                }

                if (input == null) {
                    Log.e("PDFAsset", "Unable to open file: " + this.uriString);
                }
            }
        } catch (IOException e) {
            Log.e("PDFAsset", "Error opening file: " + this.uriString);
            e.printStackTrace();
        }

        return input;
    }
}
