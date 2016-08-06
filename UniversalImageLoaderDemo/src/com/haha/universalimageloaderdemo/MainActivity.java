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
 * 1、 Universal-ImageLoader的配置
 * 
 * 2、用Universal-ImageLoader加载网络图片和本地图片
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
		// 声明配置
		configImageLoader();
		// String uri = "file:///" + "本地路径";
		ImageLoader
				.getInstance()
				.displayImage(
						"http://cdn.duitang.com/uploads/item/201412/11/20141211224453_HVmMh.jpeg",
						mImg, new ImageLoadingListener() {
							// 设置监听器，监听加载网络图片时的状态
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
	 * 配置ImageLoder
	 */
	private void configImageLoader() {
		// 初始化ImageLoader
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				MainActivity.this)
				// 即保存的每个缓存文件的最大长宽
				.memoryCacheExtraOptions(480, 800)
				// 设置缓存的详细信息，最好不要设置这个
				// .discCacheExtraOptions(480, 800, null)
				// 线程池内加载的数量
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				// 你可以通过自己的内存缓存实现
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				// 将保存的时候的URI名称用MD5 加密
				// .discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 缓存的文件数量
				.discCacheFileCount(10)
				// 自定义缓存路径
				.discCache(
						new UnlimitedDiscCache(new File(Environment
								.getExternalStorageDirectory()
								+ "/haha/imgCache")))
				.defaultDisplayImageOptions(getDisplayOptions())

				// 普通的图片加载
				.imageDownloader(
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
				// https的图片加载
				// .imageDownloader(new AuthImageDownloader(this)) ;
				.writeDebugLogs().build();// 开始构建
		ImageLoader.getInstance().init(config);
	}

	//
	private DisplayImageOptions getDisplayOptions() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillis为你设置的下载前的延迟时间
				// 设置图片加入缓存前，对bitmap进行设置
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成
		return options;
	}

	// 加载https图片的方法 start
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

	// 加载https图片的方法 end
}
