package com.test.nss;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class SignupActivity extends AppCompatActivity {

    AutoCompleteTextView dropdownClg;

    ArrayList<String> clgList;
    Context mContext;

    private EditText fName;
    private EditText fathName;
    private EditText mName;
    private EditText lName;

    private EditText email;
    private EditText vec;
    private TextView vecClgPref;

    private Button signupPost;
    private EditText contactNo;
    private EditText password;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mContext = SignupActivity.this;
        vec = findViewById(R.id.vec_in);

        fName = findViewById(R.id.fname);
        fathName = findViewById(R.id.fath_name);
        mName = findViewById(R.id.mom_name);
        lName = findViewById(R.id.last_name);
        email = findViewById(R.id.email_in);
        signupPost = findViewById(R.id.signup_post);
        dropdownClg = findViewById(R.id.dropdown_clg);
        contactNo = findViewById(R.id.contact_no);
        vecClgPref = findViewById(R.id.vec_clg_pref);
        password = findViewById(R.id.password);

        clgList = new ArrayList<>();

        //clgList.add("--- Select ---");
        //dropdownClg.setAdapter(adapter);

        if (isNetworkAvailable()) {
            Call<ResponseBody> clg = RetrofitClient.getInstance().getApi().getClgList();
            clg.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            //JSONObject j = new JSONObject(response.body().string());
                            JSONArray jArry = new JSONArray(response.body().string());
                            for (int i = 0; i < jArry.length(); i++) {
                                clgList.add(jArry.getJSONObject(i).getString("CollegeName") + "-" + jArry.getJSONObject(i).getString("CollegeCode"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.drop_down_start, clgList);
                            dropdownClg.setAdapter(adapter);
                            Log.e("Added college", "");
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    clgList.add("");
                }
            });

            Call<ResponseBody> clg2 = RetrofitClient.getInstance().getApi().getClgList();
            clg2.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            //JSONObject j = new JSONObject(response.body().string());
                            JSONArray j = new JSONArray(response.body().string());
                            if (j.length() >= 0) {
                                DatabaseAdapter mDbHelper = new DatabaseAdapter(mContext);
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("CollegeNames");
                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertClgList(
                                            j.getJSONObject(i).getString("CollegeCode"),
                                            j.getJSONObject(i).getString("CollegeName"),
                                            j.getJSONObject(i).getString("State")
                                    );
                                }
                                //mDbHelper.getCampDetails();
                                mDbHelper.close();
                            }
                            //ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupActivity.this, R.layout.drop_down_start, clgList);
                            //dropdownClg.setAdapter(adapter);
                            Log.e("Added college", "");
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String date = sdf.format(new Date());

            //dropdownClg.setHintTextColor(mContext.getColor(R.color.colorPrimary));
            dropdownClg.setDropDownBackgroundResource(R.drawable.drpdwn_clg_bg);
            //dropdownClg.

            dropdownClg.setOnClickListener(view -> dropdownClg.showDropDown());
            /*dropdownClg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(event.getRawX() >= (dropdownClg.getRight() - dropdownClg.getCompoundDrawables()[R.drawable.ic_arrow_down].getBounds().width())) {
                            // your action here
                            dropdownClg.showDropDown();
                            return true;
                        }
                    }
                    return false;
                }
            });*/

            dropdownClg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    dropdownClg.showDropDown();
                    return false;
                }
            });

            dropdownClg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String clgCode = adapterView.getItemAtPosition(i).toString();

                    vecClgPref.setText("");
                    clgCode = clgCode.substring(clgCode.indexOf("-") + 1);
                    //vecClgPref.append(clgCode);

                    String s = adapterView.getItemAtPosition(i).toString();
                    //spannable.toString().indexOf("-"), spannable.toString().length();
                    s = s.substring(0, s.indexOf("-"));
                    DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                    mdb.createDatabase();
                    mdb.open();
                    Cursor c = mdb.getClgState(s);
                    c.moveToFirst();
                    if (!c.getString(c.getColumnIndex("State")).equals("Closed")) {
                        dropdownClg.setText(s);
                        vecClgPref.append(clgCode);
                    } else {
                        dropdownClg.setError("The college is not accepting");
                        dropdownClg.setText("");
                        vecClgPref.append("");
                    }
                    mdb.close();
                }
            });

            signupPost.setOnClickListener(v -> {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String passPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";

                final EditText vec = findViewById(R.id.vec_in);

                if (!isEmpty(fName) && !isEmpty(fathName) &&
                        !isEmpty(mName) && !isEmpty(lName) && !isEmpty(vec) && !isEmpty(password) &&
                        !isEmpty(email) && !isEmpty(contactNo) && !vecClgPref.getText().toString().equals("")
                        && !isEmpty(dropdownClg) && vec.getText().toString().trim().length() > 0) {

                    if (!email.getText().toString().trim().matches(emailPattern))
                        Toast.makeText(mContext, "Please enter proper email", Toast.LENGTH_SHORT).show();
                    if (vec.getText().toString().trim().length() < 5 && vec.getText().toString().trim().length() != 5)
                        vec.setError("Enter only 5 digits");
                    if (!password.getText().toString().trim().matches(passPattern)) {
                        password.setError("Error");
                        Snackbar.make(v, "Password should contain at least a number, capital letter and a special character", Snackbar.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext, R.style.delDialog);
                        builder2.setTitle("Confirm");
                        builder2.setMessage("Your vec is: " + "MH09" + vecClgPref.getText().toString() + vec.getText().toString());

                        builder2.setPositiveButton("Yes", (dialog, which) -> {
                            dialog.dismiss();

                            String clgItem = dropdownClg.getText().toString();
                            //clgItem = clgItem.substring(0, clgItem.indexOf("-"));
                            Call<ResponseBody> signup = RetrofitClient.getInstance().getApi().signup(
                                    "1",
                                    date,
                                    fName.getText().toString(),
                                    fathName.getText().toString(),
                                    mName.getText().toString(),
                                    lName.getText().toString(),
                                    "MH09" + vecClgPref.getText().toString() + vec.getText().toString(),
                                    email.getText().toString(),
                                    clgItem,
                                    "+91" + contactNo.getText().toString(),
                                    ""
                            );

                            signup.enqueue(new Callback<ResponseBody>() {
                                @EverythingIsNonNull
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        finish();
                                        Snackbar.make(v, "Signing you up!", Snackbar.LENGTH_SHORT).show();
                                        Toast.makeText(SignupActivity.this, "An email will be sent once PO confirms", Toast.LENGTH_SHORT).show();
                                    } else if (response.errorBody() != null) {
                                        Log.e("onResponse:error", response.errorBody().toString());
                                        try {
                                            JSONObject j = new JSONObject(response.errorBody().string());

                                            if (!j.isNull("Contact")) {
                                                Toast.makeText(mContext, "" + j.getJSONArray("Contact").getString(0), Toast.LENGTH_SHORT).show();
                                                contactNo.setError("Enter correct number");
                                                contactNo.requestFocus();
                                            } if (!j.isNull("Email")) {
                                                Toast.makeText(mContext, "" + j.getJSONArray("Email").getString(0), Toast.LENGTH_SHORT).show();
                                                contactNo.setError("Enter correct number");
                                                email.setError("");
                                            } else
                                                Toast.makeText(mContext, "Recheck and sign up again", Toast.LENGTH_SHORT).show();
                                            Log.e("error", j.toString());
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @EverythingIsNonNull
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("SignUp", t.toString());
                                }
                            });
                        });
                        builder2.setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        builder2.show();
                    }
                } else
                    Toast.makeText(mContext, "Please Enter all details", Toast.LENGTH_SHORT).show();
            });
        } else
            Toast.makeText(mContext, "Device is offline", Toast.LENGTH_SHORT).show();
    }

    public boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() <= 0;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void deleteData(String table) {
        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(mContext);
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(mContext);
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL("DELETE FROM " + table);
        mDbHelper2.close();
        m.close();
        mDb2.close();
    }
}