package com.haha.universalimageloaderdemo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.graphics.Bitmap;

/**
 * 1�� Universal-ImageLoader������
 * 
 * 2����Universal-ImageLoader��������ͼƬ�ͱ���ͼƬ
 * 
 * @author HAHA
 * 
 */
public class MainActivity extends Activity {

	private ImageView mImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImg = (ImageView) this.findViewById(R.id.img_imageloader);
		// ��������
		configImageLoader();
		// String uri = "file:///" + "����·��";
		ImageLoader
				.getInstance()
				.displayImage(
						"http://cdn.duitang.com/uploads/item/201412/11/20141211224453_HVmMh.jpeg",
						mImg, new ImageLoadingListener() {
							// ���ü�������������������ͼƬʱ��״̬
							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								// TODO Auto-generated method stub
								Log.i("onLoadingStarted=", arg0);
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
								// TODO Auto-generated method stub
								Log.i("onLoadingFailed=", arg0);
							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap arg2) {
								// TODO Auto-generated method stub
								Log.i("onLoadingComplete=", arg0);
							}

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {
								// TODO Auto-generated method stub
								Log.i("onLoadingCancelled=", arg0);
							}
						});
	}

	/**
	 * ����ImageLoder
	 */
	private void configImageLoader() {
		// ��ʼ��ImageLoader
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				MainActivity.this)
				// �������ÿ�������ļ�����󳤿�
				.memoryCacheExtraOptions(480, 800)
				// ���û������ϸ��Ϣ����ò�Ҫ�������
				// .discCacheExtraOptions(480, 800, null)
				// �̳߳��ڼ��ص�����
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				// �����ͨ���Լ����ڴ滺��ʵ��
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				// �������ʱ���URI������MD5 ����
				// .discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// ������ļ�����
				.discCacheFileCount(10)
				// �Զ��建��·��
				.discCache(
						new UnlimitedDiscCache(new File(Environment
								.getExternalStorageDirectory()
								+ "/haha/imgCache")))
				.defaultDisplayImageOptions(getDisplayOptions())

				// ��ͨ��ͼƬ����
				.imageDownloader(
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
				// https��ͼƬ����
				// .imageDownloader(new AuthImageDownloader(this)) ;
				.writeDebugLogs().build();// ��ʼ����
		ImageLoader.getInstance().init(config);
	}

	//
	private DisplayImageOptions getDisplayOptions() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher) // ����ͼƬ�������ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.ic_launcher)// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.ic_launcher) // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
				.cacheInMemory(true)// �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisc(true)// �������ص�ͼƬ�Ƿ񻺴���SD����
				.considerExifParams(true) // �Ƿ���JPEGͼ��EXIF��������ת����ת��
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// ����ͼƬ����εı��뷽ʽ��ʾ
				.bitmapConfig(Bitmap.Config.RGB_565)// ����ͼƬ�Ľ�������//
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
				// ����ͼƬ���뻺��ǰ����bitmap��������
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// ����ͼƬ������ǰ�Ƿ����ã���λ
				.displayer(new RoundedBitmapDisplayer(20))// �Ƿ�����ΪԲ�ǣ�����Ϊ����
				.displayer(new FadeInBitmapDisplayer(100))// �Ƿ�ͼƬ���غú���Ķ���ʱ��
				.build();// �������
		return options;
	}

	// ����httpsͼƬ�ķ��� start
	public class AuthImageDownloader extends BaseImageDownloader {

		private SSLSocketFactory mSSLSocketFactory;

		public AuthImageDownloader(Context context) {
			super(context);
			SSLContext sslContext = sslContextForTrustedCertificates();
			mSSLSocketFactory = sslContext.getSocketFactory();
		}

		public AuthImageDownloader(Context context, int connectTimeout,
				int readTimeout) {
			super(context, connectTimeout, readTimeout);
			SSLContext sslContext = sslContextForTrustedCertificates();
			mSSLSocketFactory = sslContext.getSocketFactory();
		}

		@Override
		protected InputStream getStreamFromNetwork(String imageUri, Object extra)
				throws IOException {
			URL url = null;
			try {
				url = new URL(imageUri);
			} catch (MalformedURLException e) {
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);

			if (conn instanceof HttpsURLConnection) {
				((HttpsURLConnection) conn)
						.setSSLSocketFactory(mSSLSocketFactory);
				((HttpsURLConnection) conn)
						.setHostnameVerifier((DO_NOT_VERIFY));
			}
			return new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
		}

		// always verify the host - dont check for certificate
		final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
	}

	public SSLContext sslContextForTrustedCertificates() {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, null);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} finally {
			return sc;
		}
	}

	class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}

	// ����httpsͼƬ�ķ��� end
}
