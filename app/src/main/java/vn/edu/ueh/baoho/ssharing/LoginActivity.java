package vn.edu.ueh.baoho.ssharing;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ui);

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        ImageView btnTogglePassword = findViewById(R.id.btnTogglePassword);

        if (btnTogglePassword != null && etPassword != null) {
            btnTogglePassword.setOnClickListener(v -> {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                etPassword.setSelection(etPassword.getText().length());
            });
        }

        if (findViewById(R.id.btnLogin) != null) {
            findViewById(R.id.btnLogin).setOnClickListener(v -> {
                String email = etEmail != null ? etEmail.getText().toString().trim() : "";
                if (email.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập Email / Tên đăng nhập", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        }

        if (findViewById(R.id.tvForgotPassword) != null) {
            findViewById(R.id.tvForgotPassword).setOnClickListener(v ->
                    startActivity(new Intent(this, TwoFactorAuthActivity.class))
            );
        }
    }
}
