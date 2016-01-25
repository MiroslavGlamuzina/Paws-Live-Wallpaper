package remingtonproductions.pawslivewallpaper;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.authorwjf.live_wallpaper_p1.R;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.sliders.LobsterOpacitySlider;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

import java.util.List;


public class SettingsActivity extends AppCompatPreferenceActivity {
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else if (preference instanceof RingtonePreference) {
                if (TextUtils.isEmpty(stringValue)) {
                    preference.setSummary(R.string.pref_ringtone_silent);
                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        preference.setSummary(null);
                    } else {
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }


    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || ColorPreferenceFragment.class.getName().equals(fragmentName)
                || AnimationPreferenceFragment.class.getName().equals(fragmentName)
                || RatePreferenceFragment.class.getName().equals(fragmentName)
                || ContactDeveloperPreferenceFragment.class.getName().equals(fragmentName)
                || SharePreferenceFragment.class.getName().equals(fragmentName);
    }

    //COLORS PREFERENCE FRAGMENT ------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ColorPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        CheckBoxPreference color1;
        CheckBoxPreference color2;
        CheckBoxPreference color3;
        CheckBoxPreference color4;
        CheckBoxPreference color5;
        CheckBoxPreference color6;
        CheckBoxPreference color7;
        CheckBoxPreference colorCustom;
        Bitmap bmcustom;
        LobsterPicker picker;
        LobsterShadeSlider shaderSlider;
        LobsterOpacitySlider opacitySlider;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_color);
            setHasOptionsMenu(true);

            color1 = (CheckBoxPreference) findPreference(getString(R.string.pref_color_color_one));
            color2 = (CheckBoxPreference) findPreference(getString(R.string.pref_color_color_two));
            color3 = (CheckBoxPreference) findPreference(getString(R.string.pref_color_color_three));
            color4 = (CheckBoxPreference) findPreference(getString(R.string.pref_color_color_four));
            color5 = (CheckBoxPreference) findPreference(getString(R.string.pref_color_color_five));
            color6 = (CheckBoxPreference) findPreference(getString(R.string.pref_color_color_six));
            color7 = (CheckBoxPreference) findPreference(getString(R.string.pref_color_color_seven));
            colorCustom = (CheckBoxPreference) findPreference(getString(R.string.pref_color_color_custom));

            Bitmap bm1 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Bitmap bm2 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Bitmap bm3 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Bitmap bm4 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Bitmap bm5 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Bitmap bm6 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Bitmap bm7 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            bmcustom = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

            bm1.eraseColor(Color.parseColor(Tools.StrCOLOR_ONE));
            color1.setIcon(new BitmapDrawable(getResources(), bm1));
            bm2.eraseColor(Color.parseColor(Tools.StrCOLOR_TWO));
            color2.setIcon(new BitmapDrawable(getResources(), bm2));
            bm3.eraseColor(Color.parseColor(Tools.StrCOLOR_THREE));
            color3.setIcon(new BitmapDrawable(getResources(), bm3));
            bm4.eraseColor(Color.parseColor(Tools.StrCOLOR_FOUR));
            color4.setIcon(new BitmapDrawable(getResources(), bm4));
            bm5.eraseColor(Color.parseColor(Tools.StrCOLOR_FIVE));
            color5.setIcon(new BitmapDrawable(getResources(), bm5));
            bm6.eraseColor(Color.parseColor(Tools.StrCOLOR_SIX));
            color6.setIcon(new BitmapDrawable(getResources(), bm6));
            bm7.eraseColor(Color.parseColor(Tools.StrCOLOR_SEVEN));
            color7.setIcon(new BitmapDrawable(getResources(), bm7));
            final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            bmcustom.eraseColor(sharedPref.getInt(getString(R.string.pref_color_color_custom_value), 0));
            colorCustom.setIcon(new BitmapDrawable(getResources(), bmcustom));

            color1.setOnPreferenceChangeListener(ColorPreferenceFragment.this);
            color2.setOnPreferenceChangeListener(ColorPreferenceFragment.this);
            color3.setOnPreferenceChangeListener(ColorPreferenceFragment.this);
            color4.setOnPreferenceChangeListener(ColorPreferenceFragment.this);
            color5.setOnPreferenceChangeListener(ColorPreferenceFragment.this);
            color6.setOnPreferenceChangeListener(ColorPreferenceFragment.this);
            color7.setOnPreferenceChangeListener(ColorPreferenceFragment.this);
            colorCustom.setOnPreferenceChangeListener(ColorPreferenceFragment.this);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            final boolean value = (Boolean) newValue;
            final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            if (value) {
                if (preference.getKey().equals((getString(R.string.pref_color_color_one)))) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_color_color_one), value);
                    editor.putBoolean(getString(R.string.pref_color_color_two), false);
                    editor.putBoolean(getString(R.string.pref_color_color_three), false);
                    editor.putBoolean(getString(R.string.pref_color_color_four), false);
                    editor.putBoolean(getString(R.string.pref_color_color_five), false);
                    editor.putBoolean(getString(R.string.pref_color_color_six), false);
                    editor.putBoolean(getString(R.string.pref_color_color_seven), false);
                    editor.putBoolean(getString(R.string.pref_color_color_custom), false);
                    editor.commit();
                    color2.setChecked(false);
                    color3.setChecked(false);
                    color4.setChecked(false);
                    color5.setChecked(false);
                    color6.setChecked(false);
                    color7.setChecked(false);
                    colorCustom.setChecked(false);
                }
                if (preference.getKey().equals((getString(R.string.pref_color_color_two)))) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_color_color_two), value);

                    editor.putBoolean(getString(R.string.pref_color_color_one), false);
                    editor.putBoolean(getString(R.string.pref_color_color_three), false);
                    editor.putBoolean(getString(R.string.pref_color_color_four), false);
                    editor.putBoolean(getString(R.string.pref_color_color_five), false);
                    editor.putBoolean(getString(R.string.pref_color_color_six), false);
                    editor.putBoolean(getString(R.string.pref_color_color_seven), false);
                    editor.putBoolean(getString(R.string.pref_color_color_custom), false);
                    editor.commit();
                    color1.setChecked(false);
                    color3.setChecked(false);
                    color4.setChecked(false);
                    color5.setChecked(false);
                    color6.setChecked(false);
                    color7.setChecked(false);
                    colorCustom.setChecked(false);
                }
                if (preference.getKey().equals((getString(R.string.pref_color_color_three)))) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_color_color_three), value);

                    editor.putBoolean(getString(R.string.pref_color_color_two), false);
                    editor.putBoolean(getString(R.string.pref_color_color_one), false);
                    editor.putBoolean(getString(R.string.pref_color_color_four), false);
                    editor.putBoolean(getString(R.string.pref_color_color_five), false);
                    editor.putBoolean(getString(R.string.pref_color_color_six), false);
                    editor.putBoolean(getString(R.string.pref_color_color_seven), false);
                    editor.putBoolean(getString(R.string.pref_color_color_custom), false);
                    editor.commit();
                    color1.setChecked(false);
                    color2.setChecked(false);
                    color4.setChecked(false);
                    color5.setChecked(false);
                    color6.setChecked(false);
                    color7.setChecked(false);
                    colorCustom.setChecked(false);
                }
                if (preference.getKey().equals((getString(R.string.pref_color_color_four)))) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_color_color_four), value);

                    editor.putBoolean(getString(R.string.pref_color_color_two), false);
                    editor.putBoolean(getString(R.string.pref_color_color_three), false);
                    editor.putBoolean(getString(R.string.pref_color_color_one), false);
                    editor.putBoolean(getString(R.string.pref_color_color_five), false);
                    editor.putBoolean(getString(R.string.pref_color_color_six), false);
                    editor.putBoolean(getString(R.string.pref_color_color_seven), false);
                    editor.putBoolean(getString(R.string.pref_color_color_custom), false);
                    editor.commit();
                    color1.setChecked(false);
                    color2.setChecked(false);
                    color3.setChecked(false);
                    color5.setChecked(false);
                    color6.setChecked(false);
                    color7.setChecked(false);
                    colorCustom.setChecked(false);
                }
                if (preference.getKey().equals((getString(R.string.pref_color_color_five)))) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_color_color_five), value);

                    editor.putBoolean(getString(R.string.pref_color_color_two), false);
                    editor.putBoolean(getString(R.string.pref_color_color_three), false);
                    editor.putBoolean(getString(R.string.pref_color_color_four), false);
                    editor.putBoolean(getString(R.string.pref_color_color_one), false);
                    editor.putBoolean(getString(R.string.pref_color_color_six), false);
                    editor.putBoolean(getString(R.string.pref_color_color_seven), false);
                    editor.putBoolean(getString(R.string.pref_color_color_custom), false);
                    editor.commit();
                    color1.setChecked(false);
                    color2.setChecked(false);
                    color3.setChecked(false);
                    color4.setChecked(false);
                    color6.setChecked(false);
                    color7.setChecked(false);
                    colorCustom.setChecked(false);
                }
                if (preference.getKey().equals((getString(R.string.pref_color_color_six)))) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_color_color_six), value);

                    editor.putBoolean(getString(R.string.pref_color_color_two), false);
                    editor.putBoolean(getString(R.string.pref_color_color_three), false);
                    editor.putBoolean(getString(R.string.pref_color_color_four), false);
                    editor.putBoolean(getString(R.string.pref_color_color_five), false);
                    editor.putBoolean(getString(R.string.pref_color_color_one), false);
                    editor.putBoolean(getString(R.string.pref_color_color_seven), false);
                    editor.putBoolean(getString(R.string.pref_color_color_custom), false);
                    editor.commit();
                    color1.setChecked(false);
                    color2.setChecked(false);
                    color3.setChecked(false);
                    color4.setChecked(false);
                    color5.setChecked(false);
                    color7.setChecked(false);
                    colorCustom.setChecked(false);
                }
                if (preference.getKey().equals((getString(R.string.pref_color_color_seven)))) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_color_color_seven), value);

                    editor.putBoolean(getString(R.string.pref_color_color_two), false);
                    editor.putBoolean(getString(R.string.pref_color_color_three), false);
                    editor.putBoolean(getString(R.string.pref_color_color_four), false);
                    editor.putBoolean(getString(R.string.pref_color_color_five), false);
                    editor.putBoolean(getString(R.string.pref_color_color_six), false);
                    editor.putBoolean(getString(R.string.pref_color_color_one), false);
                    editor.putBoolean(getString(R.string.pref_color_color_custom), false);
                    editor.commit();
                    color1.setChecked(false);
                    color2.setChecked(false);
                    color3.setChecked(false);
                    color4.setChecked(false);
                    color5.setChecked(false);
                    color6.setChecked(false);
                    colorCustom.setChecked(false);
                }
                if (preference.getKey().equals((getString(R.string.pref_color_color_custom)))) {
                    LinearLayout holder = new LinearLayout(getActivity());
                    holder.setGravity(Gravity.CENTER_HORIZONTAL);
                    holder.setOrientation(LinearLayout.VERTICAL);
                    picker = new LobsterPicker(getActivity());
                    shaderSlider = new LobsterShadeSlider(getActivity());
//                    opacitySlider = new LobsterOpacitySlider(getActivity());
                    picker.addDecorator(shaderSlider);
//                    picker.addDecorator(opacitySlider);
                    picker.setColor(sharedPref.getInt(getString(R.string.pref_color_color_custom_value), 0));
                    holder.addView(picker);
                    holder.addView(shaderSlider);
//                    holder.addView(opacitySlider);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(holder);
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean(getString(R.string.pref_color_color_custom), true);
                                    editor.putInt(getString(R.string.pref_color_color_custom_value), picker.getColor());
                                    Tools.CUSTOM_COLOR = CUSTOM_COLOR = picker.getColor();
                                    Tools.writeToFile(picker.getColor(), getActivity());
                                    editor.commit();
//                                    Toast.makeText(getActivity(), String.valueOf(picker.getColor()), Toast.LENGTH_SHORT).show();
                                    bmcustom.eraseColor(sharedPref.getInt(getString(R.string.pref_color_color_custom_value), CUSTOM_COLOR));
                                    editor.putBoolean(getString(R.string.pref_color_color_one), false);
                                    editor.putBoolean(getString(R.string.pref_color_color_two), false);
                                    editor.putBoolean(getString(R.string.pref_color_color_three), false);
                                    editor.putBoolean(getString(R.string.pref_color_color_four), false);
                                    editor.putBoolean(getString(R.string.pref_color_color_five), false);
                                    editor.putBoolean(getString(R.string.pref_color_color_six), false);
                                    editor.putBoolean(getString(R.string.pref_color_color_seven), false);
                                    editor.commit();
                                    color1.setChecked(false);
                                    color2.setChecked(false);
                                    color3.setChecked(false);
                                    color4.setChecked(false);
                                    color5.setChecked(false);
                                    color6.setChecked(false);
                                    color7.setChecked(false);
                                }
                            }
                    );
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            }
                    );
                    builder.show();
                }
            } else {
                return false;
            }
            return true;
        }
    }

    public static int CUSTOM_COLOR = 0;

    //ANIMATION PREFERENCE FRAGMENT ------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AnimationPreferenceFragment extends PreferenceFragment {
        ListPreference speed;
        ListPreference opacity;
        CheckBoxPreference paw;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_animation);
            setHasOptionsMenu(true);
            speed = (ListPreference) findPreference(getString(R.string.pref_animation_animation_speed_key));
            opacity = (ListPreference) findPreference(getString(R.string.pref_animation_opacity_key));
            paw = (CheckBoxPreference) findPreference(getString(R.string.pref_animation_paw_prints_key));

            paw.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final boolean value = (Boolean) newValue;
                    final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_animation_paw_prints_key), value);
                    editor.commit();
                    return true;
                }
            });
            speed.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final int value = Integer.parseInt((String) newValue);
                    final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(getString(R.string.pref_animation_animation_speed_key), value);
                    editor.commit();
                    return true;
                }
            });
            opacity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final int value = Integer.parseInt((String) newValue);
                    final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(getString(R.string.pref_animation_opacity_key), value);
                    editor.commit();
                    return true;
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    //SHARE PREFERENCE FRAGMENT ------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SharePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_share);
            setHasOptionsMenu(true);
//            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    //RATE PREFERENCE FRAGMENT ------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class RatePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_rate);
            setHasOptionsMenu(true);
            Tools.openAppRating(this.getActivity());
//            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    //CONTACT DEVELOPER PREFERENCE FRAGMENT ------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ContactDeveloperPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_rate);
            setHasOptionsMenu(true);

            //Email Intent
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"Remington.Productions.Feedback@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " Feedback");
            i.putExtra(Intent.EXTRA_TEXT, "Hello Remington Productions, \n \n \n");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
