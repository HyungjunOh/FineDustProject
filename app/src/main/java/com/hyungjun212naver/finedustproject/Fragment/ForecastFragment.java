package com.hyungjun212naver.finedustproject.Fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hyungjun212naver.finedustproject.R;


public class ForecastFragment extends Fragment {

    ImageCast imageCast;

    WebView iv1;
    WebView iv2;
    WebView iv3;
    WebView iv4;

    private OnFragmentInteractionListener mListener;

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        iv1 = (WebView)view.findViewById(R.id.notifications_image1);
        iv2 = (WebView)view.findViewById(R.id.notifications_image2);
        iv3 = (WebView)view.findViewById(R.id.notifications_image3);
        iv4 = (WebView)view.findViewById(R.id.notifications_image4);

        imageCast = new ImageCast();
        imageCast.execute();

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    private class ImageCast extends AsyncTask {

        public ImageCast() {
            super();
        }

        @Override
        protected void onPreExecute() {

            WebSettings webSettings1 = iv1.getSettings();
            webSettings1.setAllowFileAccess(false);
            webSettings1.setJavaScriptEnabled(true);
            webSettings1.setUseWideViewPort(true);
            iv1.setInitialScale(1);
            iv1.setWebViewClient(new InitWebViewClient());

            WebSettings webSettings2 = iv2.getSettings();
            webSettings2.setAllowFileAccess(false);
            webSettings2.setJavaScriptEnabled(true);
            webSettings2.setUseWideViewPort(true);
            iv2.setInitialScale(1);
            iv2.setWebViewClient(new InitWebViewClient());

            WebSettings webSettings3 = iv3.getSettings();
            webSettings3.setAllowFileAccess(false);
            webSettings3.setJavaScriptEnabled(true);
            webSettings3.setUseWideViewPort(true);
            iv3.setInitialScale(1);
            iv3.setWebViewClient(new InitWebViewClient());

            WebSettings webSettings4 = iv4.getSettings();
            webSettings4.setAllowFileAccess(false);
            webSettings4.setJavaScriptEnabled(true);
            webSettings4.setUseWideViewPort(true);
            iv4.setInitialScale(1);
            iv4.setWebViewClient(new InitWebViewClient());

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {

            iv1.loadUrl("http://www.webairwatch.com/kaq/modelimg_CASE4/PM10.09km.animation.gif");
            iv2.loadUrl("http://www.webairwatch.com/kaq/modelimg_CASE4/PM2_5.09km.animation.gif");
            iv3.loadUrl("http://www.webairwatch.com/kaq/modelimg_CASE4/O3.09km.animation.gif");
            iv4.loadUrl("http://www.webairwatch.com/kaq/modelimg_CASE4/VECTOR.09km.animation.gif");

            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Object o) {
            super.onCancelled(o);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            return params;
        }
    }
    class InitWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
