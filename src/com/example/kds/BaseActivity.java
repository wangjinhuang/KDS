package com.example.kds;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

public class BaseActivity extends Activity {

	public String[] company_code;
	public String[] company_name;
	public String[] company_logo;

	ProgressDialog dialog;

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		context = this;

		company_code = getResources().getStringArray(R.array.company_code);
		company_name = getResources().getStringArray(R.array.company_name);
		company_logo = getResources().getStringArray(R.array.company_logo);

		super.onCreate(savedInstanceState);

	}

	public void showToast(String text) {

		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public void showProgressDialog(String string) {

		dialog = ProgressDialog.show(context, "ÌבÊ¾", string);
		dialog.setCancelable(true);
		dialog.show();
	}

	public void dimissProgress() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
