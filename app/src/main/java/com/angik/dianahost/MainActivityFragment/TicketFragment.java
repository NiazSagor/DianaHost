package com.angik.dianahost.MainActivityFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.angik.dianahost.MainActivity;
import com.angik.dianahost.R;
import com.angik.dianahost.databinding.FragmentTicketBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentTicketBinding binding;
    private Activity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketFragment newInstance(String param1, String param2) {
        TicketFragment fragment = new TicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTicketBinding.inflate(getLayoutInflater());

        binding.swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.GREEN);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.myWebView.reload();
            }
        });

        binding.myWebView.getSettings().setJavaScriptEnabled(true);
        binding.myWebView.getSettings().setLoadWithOverviewMode(true);
        binding.myWebView.getSettings().setUseWideViewPort(true);
        binding.myWebView.getSettings().setDomStorageEnabled(true);
        binding.myWebView.getSettings().setLoadsImagesAutomatically(true);


        binding.myWebView.loadUrl("https://clients.dianahost.com/submitticket.php?_gl=1*z3i6ec*_ga*MTI2Mjc4ODg4NC4xNjEyMDg3MDg0*_ga_PLZGD4E5LZ*MTYxMjM0NzEyOC43LjAuMTYxMjM0NzEyOC42MA..");


        binding.myWebView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (binding.myWebView.getScrollY() == 0) {
                    binding.swipeRefreshLayout.setEnabled(true);
                } else {
                    binding.swipeRefreshLayout.setEnabled(false);
                }
            }
        });

        binding.myWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String s, final String s1, final String s2, final String s3, long l) {

                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {


                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s));
                                request.setMimeType(s3);
                                String cookies = CookieManager.getInstance().getCookie(s);
                                request.addRequestHeader("cookie", cookies);
                                request.addRequestHeader("User-Agent", s1);
                                request.setDescription("Downloading File.....");
                                request.setTitle(URLUtil.guessFileName(s, s2, s3));
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(
                                        Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                                s, s2, s3));
                                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);
                                Toast.makeText(getContext(), "Downloading File..", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                            }

                        }).check();

            }
        });

        binding.myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.swipeRefreshLayout.setRefreshing(false);
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        binding.myWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressBar.setProgress(newProgress);
                //progressDialog.show();

                if (newProgress == 100) {
                    binding.progressBar.setVisibility(View.GONE);
                    //progressDialog.dismiss();
                }

                super.onProgressChanged(view, newProgress);
            }
        });

        activity = getActivity();

        ((MainActivity) activity).setOnBackPressedListener(new MainActivity.OnBackPressedListener() {
            @Override
            public void onBackPressed() {
                if (binding.myWebView.canGoBack()) {
                    binding.myWebView.goBack();
                } else {
                    ViewPager2 viewPager2 = activity.findViewById(R.id.viewpager);
                    viewPager2.setCurrentItem(0, true);
                }
            }
        });

        return binding.getRoot();
    }
}