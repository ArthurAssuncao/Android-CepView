package com.arthurassuncao.cepview_exemplo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CepViewExemploActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cep_view_exemplo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cep_view_exemplo, menu);
		return true;
	}

}
