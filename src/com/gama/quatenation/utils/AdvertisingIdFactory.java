package com.gama.quatenation.utils;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public final class AdvertisingIdFactory {

	public final static String TAG = "AdvertisingIdFactory";
	
	public static class AdvertisingId {
		private String id = "";

		public String getId() {
			return id;
		}

		public void setId(String advertisingId) {
			this.id = advertisingId;
		}

	}

	public static AdvertisingId getAdvertisingId(Context context) {
		AdvertisingId advertisingId = new AdvertisingId();
		
		try {
			AdInfo adInfo = getAdvertisingIdFromDevice(context);
			advertisingId.setId(adInfo.getId());
		} catch (Exception e) {
			StackTraceElement[] stackTrace = e.getStackTrace();
			Log.e(TAG, "Error getting advertising id. stackTrace:");
			for (int i = 0; i < stackTrace.length; i++) {
				Log.e(TAG, stackTrace[i].toString());
			}
			advertisingId.setId(Constants.ERROR_GETTING_ADVERTISING_ID);
		}

		return advertisingId;
	}

	public static final class AdInfo {
		private final String advertisingId;

		AdInfo(String advertisingId) {
			this.advertisingId = advertisingId;
		}

		public String getId() {
			return this.advertisingId;
		}

	}
	
	private static AdInfo getAdvertisingIdFromDevice(Context context)
			throws Exception {
		if (Looper.myLooper() == Looper.getMainLooper())
			throw new IllegalStateException(
					"Cannot be called from the main thread");

		try {
			PackageManager pm = context.getPackageManager();
			pm.getPackageInfo("com.android.vending", 0);
		} catch (Exception e) {
			throw e;
		}

		AdvertisingConnection connection = new AdvertisingConnection();
		Intent intent = new Intent(
				"com.google.android.gms.ads.identifier.service.START");
		intent.setPackage("com.google.android.gms");
		if (context.getApplicationContext().bindService(intent, connection,
				Context.BIND_AUTO_CREATE)) {
			try {
				AdvertisingInterface adInterface = new AdvertisingInterface(
						connection.getBinder());
				AdInfo adInfo = new AdInfo(adInterface.getId());
				return adInfo;
			} catch (Exception exception) {
				throw exception;
			} finally {
				context.getApplicationContext().unbindService(connection);
			}
		}
		throw new IOException("Google Play connection failed");
	}

	private static final class AdvertisingConnection implements
			ServiceConnection {
		boolean retrieved = false;
		private final LinkedBlockingQueue<IBinder> queue = new LinkedBlockingQueue<IBinder>(
				1);

		public void onServiceConnected(ComponentName name, IBinder service) {
			try {
				this.queue.put(service);
			} catch (InterruptedException localInterruptedException) {
			}
		}

		public void onServiceDisconnected(ComponentName name) {
		}

		public IBinder getBinder() throws InterruptedException {
			if (this.retrieved)
				throw new IllegalStateException();
			this.retrieved = true;
			return (IBinder) this.queue.take();
		}
	}

	private static final class AdvertisingInterface implements IInterface {
		private IBinder binder;

		public AdvertisingInterface(IBinder pBinder) {
			binder = pBinder;
		}

		public IBinder asBinder() {
			return binder;
		}

		public String getId() throws RemoteException {
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			String id;
			try {
				data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
				binder.transact(1, data, reply, 0);
				reply.readException();
				id = reply.readString();
			} finally {
				reply.recycle();
				data.recycle();
			}
			return id;
		}
	}
}