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

package nmtysh.product.cameralight;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CameraLightActivity extends Activity {
	Camera camera;
	ToggleButton tb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tb = (ToggleButton) findViewById(R.id.toggleButton1);
		tb.setOnCheckedChangeListener(listener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		tb.setChecked(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
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
}
