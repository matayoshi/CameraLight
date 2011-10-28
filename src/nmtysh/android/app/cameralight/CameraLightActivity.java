/*
BSD License

Copyright(c) 2011, N.Matayoshi All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

・Redistributions of source code must retain the above copyright notice, 
  this list of conditions and the following disclaimer.
・Redistributions in binary form must reproduce the above copyright notice, 
  this list of conditions and the following disclaimer in the documentation 
  and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package nmtysh.android.app.cameralight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CameraLightActivity extends Activity {
	Camera camera;
	ToggleButton tb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		tb = (ToggleButton) findViewById(R.id.toggleButton1);
		tb.setOnCheckedChangeListener(listener);
		tb.setChecked(true);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// 画面を消灯させない
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onStop() {
		super.onStop();

		// 常時点灯を解除
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		if (camera != null) {
			camera.release();
			camera = null;
		}
	}

	OnCheckedChangeListener listener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				camera = Camera.open();
				if (camera != null) {
					// ライト(フラッシュ)ON
					Camera.Parameters parameters = camera.getParameters();
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
					camera.setParameters(parameters);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							R.string.camera_open_error, Toast.LENGTH_SHORT);
					toast.show();
				}
			} else {
				// ライトOFF
				// フラッシュをOFFにしただけでは赤色に灯るので、カメラを閉じる。
				if (camera != null) {
					camera.release();
					camera = null;
				}
			}
		}
	};

	/**
	 * オプションメニューを作成します。
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return result;
	}

	/**
	 * オプションメニューのクリックイベントを処理します。
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_about:
				// About ダイアログを表示
				TextView textView = new TextView(this);
				textView.setAutoLinkMask(Linkify.WEB_URLS);
				textView.setText(R.string.about_string);
				ScrollView scrollView = new ScrollView(this);
				scrollView.addView(textView);
				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle(R.string.about_label);
				dialog.setView(scrollView);
				dialog.setPositiveButton(R.string.ok_label,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				dialog.create().show();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}
}
// EOF