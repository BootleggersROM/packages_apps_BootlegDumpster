package com.bootleggers.dumpster.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.settings.R;

import static android.provider.Settings.System.DEVICE_INTRODUCTION_COMPLETED;

public class BootlegWelcome extends Activity {
  
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private String releaseVer;
    private String deviceCodename;
    private String buildType;
    private String endSpecial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.btlg_welcome_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        layouts = new int[]{
                R.layout.btlg_welcome_intro01,
                R.layout.btlg_welcome_intro02,
                R.layout.btlg_welcome_intro03,
                R.layout.btlg_welcome_intro04};
        addBottomDots(0);
        changeStatusBarColor();
	    boolean mAlreadyWelcomed = Settings.System.getIntForUser(getContentResolver(),
                DEVICE_INTRODUCTION_COMPLETED, 0, UserHandle.USER_CURRENT) == 1;
        if (mAlreadyWelcomed) {
            launchHomeScreen();
        }

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(0x3fffffff);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(0xBAFFFFFF);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        Settings.System.putInt(getContentResolver(), DEVICE_INTRODUCTION_COMPLETED, 1);
        finish();
    }
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }


        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Resources res = getResources();
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
	    if (position == 1) {
                Button getDump = (Button) view.findViewById(R.id.intro2_button);
                if (getDump != null) {
                    getDump.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setClassName("com.android.settings",
                                  "com.android.settings.Settings$BootlegDumpsterActivity");
                            view.getContext().startActivity(intent);
                        }
                    });
                }
            } else if (position == 2) {
                Button getThemes = (Button) view.findViewById(R.id.intro3_button);
                if (getThemes != null) {
                	String stylePackage = res.getString(R.string.config_wallpaper_picker_package);
                	String styleWallClass = res.getString(R.string.config_styles_and_wallpaper_picker_class);
                	if (stylePackage != null && styleWallClass != null) {
	                    getThemes.setOnClickListener(new View.OnClickListener() {
	                        @Override
	                        public void onClick(View view) {
	                            Intent intent = new Intent(Intent.ACTION_MAIN);
	                            intent.setClassName(stylePackage,
	                                  styleWallClass);
	                            view.getContext().startActivity(intent);
	                        }
	                    });
                    }
                }
            } else if (position == 3) {
                TextView endingString = view.findViewById(R.id.intro4_end);
                ImageView siivawoodman = view.findViewById(R.id.intro4_wood);
                releaseVer = SystemProperties.get("ro.bootleggers.version_number","f");
                deviceCodename = SystemProperties.get("ro.bootleggers.device","device");
                buildType = SystemProperties.get("ro.bootleggers.releasetype","Aidonnou");
                if (endingString != null) {
                    if (releaseVer.contains("Stable") || releaseVer.contains("MadStinky")) {
                        switch (buildType) {
                            case "Shishufied":
                                endSpecial = res.getString(R.string.btlg_intro4_ending_shishufied);
                                if (siivawoodman != null) 
                                    siivawoodman.setImageResource(R.drawable.ic_btlgintro_nice);

                                break;
                            case "Unshishufied":
                                endSpecial = res.getString(R.string.btlg_intro4_ending_unshishufied);
                                if (siivawoodman != null) 
                                    siivawoodman.setImageResource(R.drawable.ic_btlgintro_notnice);

                                break;
                            default:
                                endSpecial = res.getString(R.string.btlg_intro4_ending_unsupported);
                                if (siivawoodman != null) 
                                    siivawoodman.setImageResource(R.drawable.ic_btlgintro_notatallnice);

                                break;
                        }
                    } else {
                        endSpecial = res.getString(R.string.btlg_intro4_ending_unsupported);
                        if (siivawoodman != null) 
                            siivawoodman.setImageResource(R.drawable.ic_btlgintro_notatallnice);
                    }
                    endingString.setText(res.getString(R.string.btlg_intro4_summary, deviceCodename, endSpecial));
                }
            }
            return view;
        }


        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
