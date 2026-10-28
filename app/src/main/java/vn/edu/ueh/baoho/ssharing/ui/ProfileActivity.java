package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import vn.edu.ueh.baoho.ssharing.R;

public class ProfileActivity extends AppCompatActivity {

    private static final int MÃ_CHỌN_ẢNH_ĐẠI_DIỆN_AVATAR = 301;

    private TextView nhãnHọTênTàiKhoản;
    private TextView nhãnChiTiếtTênEmail;
    private TextView nhãnChữCáiAvatar;
    private ImageView ảnhĐạiDiệnDướiPhoto;
    private TextView nhãnEmojiHạngTínNhiệm;
    private TextView nhãnGiáTrịTrustScore;
    private TextView nhãnTiêuĐềHạngTínNhiệm;
    private TextView nhãnMôTảHạngTínNhiệm;
    private ProgressBar thanhTiếnTrìnhTrustScore;
    private View thẻThànhViênHồSơ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        khởiTạoÁnhXạGiaoDiện();
        cậpNhậtThôngTinCáNhân();
        càiĐặtSựKiệnLắngNghe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cậpNhậtThôngTinCáNhân();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        nhãnHọTênTàiKhoản = findViewById(R.id.tvProfileFullName);
        nhãnChiTiếtTênEmail = findViewById(R.id.tvProfileEmailPhone);
        nhãnChữCáiAvatar = findViewById(R.id.tvAvatarInitials);
        ảnhĐạiDiệnDướiPhoto = findViewById(R.id.imgProfileAvatarPhoto);

        nhãnEmojiHạngTínNhiệm = findViewById(R.id.tvTrustTierEmoji);
        nhãnGiáTrịTrustScore = findViewById(R.id.tvTrustScoreValue);
        nhãnTiêuĐềHạngTínNhiệm = findViewById(R.id.tvTrustTierTitle);
        nhãnMôTảHạngTínNhiệm = findViewById(R.id.tvTrustTierDesc);
        thanhTiếnTrìnhTrustScore = findViewById(R.id.progressTrustScore);

        thẻThànhViênHồSơ = findViewById(R.id.cardUserProfileInfo);
    }

    private void cậpNhậtThôngTinCáNhân() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String họTênUser = bộLưuTrữCụcBộ.getString("CURRENT_USER_NAME", "Gia Bảo Hồ");
        String emailUser = bộLưuTrữCụcBộ.getString("CURRENT_USER_EMAIL", "baoho123@ssharing.vn");
        String usernameUser = bộLưuTrữCụcBộ.getString("CURRENT_USERNAME", "baoho123");
        String sđtUser = bộLưuTrữCụcBộ.getString("CURRENT_USER_PHONE", "0908123456");

        String usernameClean = usernameUser.toLowerCase().trim();
        // Đọc đường dẫn tệp Avatar riêng theo từng tài khoản (Tách biệt độc lập 100%)
        String keyAvatarCáNhân = "CURRENT_USER_AVATAR_PATH_" + usernameClean;
        String đườngDẫnẢnhLưu = bộLưuTrữCụcBộ.getString(keyAvatarCáNhân, "");
        int điểmTrustScore = bộLưuTrữCụcBộ.getInt("USER_TRUST_SCORE", 96);

        if (nhãnHọTênTàiKhoản != null) {
            nhãnHọTênTàiKhoản.setText(họTênUser);
        }
        if (nhãnChiTiếtTênEmail != null) {
            nhãnChiTiếtTênEmail.setText("@" + usernameUser + " • " + emailUser + " • " + sđtUser);
        }

        // Cập nhật Danh hiệu & Biểu tượng Trust Score theo mốc 20 điểm
        cậpNhậtTrustScoreVàDanhHiệu(điểmTrustScore);

        // Cập nhật Avatar Ảnh hoặc Chữ cái riêng của tài khoản hiện tại
        đặtChữCáiAvatarChung(họTênUser);
        vn.edu.ueh.baoho.ssharing.utils.SocialHelper.loadUserAvatar(this, ảnhĐạiDiệnDướiPhoto, usernameClean);

        cậpNhậtThốngKêBạnBèProfile(bộLưuTrữCụcBộ, usernameClean);
    }

    private void cậpNhậtThốngKêBạnBèProfile(SharedPreferences bộLưuTrữCụcBộ, String usernameUser) {
        TextView tvFollowing = findViewById(R.id.tvProfileFollowingCount);
        TextView tvFriends = findViewById(R.id.tvProfileFriendsCount);
        View btnStats = findViewById(R.id.btnProfileSocialStats);

        int[] counts = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.calculateFollowingAndMutualCounts(this, usernameUser);
        int sốTheoDõi = counts[0];
        int sốBạnBè = counts[1];

        if (tvFollowing != null) tvFollowing.setText(String.valueOf(sốTheoDõi));
        if (tvFriends != null) tvFriends.setText(String.valueOf(sốBạnBè));

        if (btnStats != null) {
            btnStats.setOnClickListener(v -> startActivity(new Intent(this, SocialFriendsActivity.class)));
        }
    }

    private static Bitmap giảiMãẢnhThuNhỏAnToàn(String path, int maxDim) {
        if (path == null || path.isEmpty()) return null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            if (opts.outWidth <= 0 || opts.outHeight <= 0) return null;

            int sample = 1;
            while ((opts.outWidth / sample) > maxDim || (opts.outHeight / sample) > maxDim) {
                sample *= 2;
            }
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = sample;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeFile(path, opts);
        } catch (Throwable ignored) {}
        return null;
    }

    private void tảiẢnhAvatarAnToàn(ImageView viewTarget, String đườngDẫnTệp, String họTên) {
        if (viewTarget == null) return;
        viewTarget.setImageDrawable(null);
        if (đườngDẫnTệp != null && !đườngDẫnTệp.isEmpty() && new File(đườngDẫnTệp).exists()) {
            Bitmap bitmapAvatar = giảiMãẢnhThuNhỏAnToàn(đườngDẫnTệp, 200);
            if (bitmapAvatar != null) {
                viewTarget.setImageBitmap(bitmapAvatar);
                viewTarget.setVisibility(View.VISIBLE);
                return;
            }
        }
        viewTarget.setVisibility(View.GONE);
        đặtChữCáiAvatarChung(họTên);
    }

    private void cậpNhậtTrustScoreVàDanhHiệu(int điểm) {
        if (nhãnGiáTrịTrustScore != null) {
            nhãnGiáTrịTrustScore.setText(điểm + " / 100");
        }
        if (thanhTiếnTrìnhTrustScore != null) {
            thanhTiếnTrìnhTrustScore.setProgress(điểm);
        }

        String emoji = "💎";
        String tiêuĐềHạng = "Tín Nhiệm Kim Cương (Tuyệt Vời)";
        String môTả = "Tài khoản eKYC chính chủ, 100% giao dịch Escrow thành công";
        int màuHex = Color.parseColor("#00E676");

        if (điểm >= 81) {
            emoji = "💎";
            tiêuĐềHạng = "Tín Nhiệm Kim Cương (Tuyệt Vời)";
            môTả = "Tài khoản eKYC chính chủ, 100% giao dịch Escrow thành công";
            màuHex = Color.parseColor("#00E676");
        } else if (điểm >= 61) {
            emoji = "🏆";
            tiêuĐềHạng = "Tín Nhiệm Hạng Vàng (Tốt)";
            môTả = "Tài khoản uy tín cao, giao dịch an toàn trên nền tảng";
            màuHex = Color.parseColor("#FFD54F");
        } else if (điểm >= 41) {
            emoji = "🛡️";
            tiêuĐềHạng = "Tín Nhiệm Hạng Bạc (Khá)";
            môTả = "Tài khoản mức độ trung bình, duy trì lịch sử giao dịch";
            màuHex = Color.parseColor("#E0E0E0");
        } else if (điểm >= 21) {
            emoji = "⚠️";
            tiêuĐềHạng = "Tín Nhiệm Hạng Đồng (Cảnh Báo)";
            môTả = "Tài khoản có tỷ lệ khiếu nại, hạn chế một số tính năng";
            màuHex = Color.parseColor("#FF9100");
        } else {
            emoji = "🚫";
            tiêuĐềHạng = "Rủi Ro Cao / Tạm Phong Tỏa";
            môTả = "Tài khoản vi phạm quy chế Escrow, tạm dừng giao dịch";
            màuHex = Color.parseColor("#FF1744");
        }

        if (nhãnEmojiHạngTínNhiệm != null) nhãnEmojiHạngTínNhiệm.setText(emoji);
        if (nhãnTiêuĐềHạngTínNhiệm != null) {
            nhãnTiêuĐềHạngTínNhiệm.setText(tiêuĐềHạng);
            nhãnTiêuĐềHạngTínNhiệm.setTextColor(màuHex);
        }
        if (nhãnMôTảHạngTínNhiệm != null) nhãnMôTảHạngTínNhiệm.setText(môTả);
    }

    private void đặtChữCáiAvatarChung(String họTên) {
        if (nhãnChữCáiAvatar != null) {
            String chữCái = "GB";
            if (họTên != null && họTên.length() >= 2) {
                String[] mảngTừ = họTên.trim().split(" ");
                if (mảngTừ.length >= 2) {
                    chữCái = (mảngTừ[0].substring(0, 1) + mảngTừ[mảngTừ.length - 1].substring(0, 1)).toUpperCase();
                } else {
                    chữCái = họTên.substring(0, 2).toUpperCase();
                }
            }
            nhãnChữCáiAvatar.setText(chữCái);
        }
    }

    private void càiĐặtSựKiệnLắngNghe() {
        View nútQuayLại = findViewById(R.id.btnBackProfile);
        if (nútQuayLại != null) {
            nútQuayLại.setOnClickListener(v -> finish());
        }

        if (thẻThànhViênHồSơ != null) {
            thẻThànhViênHồSơ.setOnClickListener(v -> hiểnThịHộpThoạiChỉnhSửaHồSơ());
        }

        View nútMenuEkyc = findViewById(R.id.btnMenuEkyc);
        if (nútMenuEkyc != null) {
            nútMenuEkyc.setOnClickListener(v ->
                    startActivity(new Intent(this, EkycVerificationActivity.class))
            );
        }

        View nútMenu2FA = findViewById(R.id.btnMenu2FA);
        if (nútMenu2FA != null) {
            nútMenu2FA.setOnClickListener(v ->
                    startActivity(new Intent(this, TwoFactorAuthActivity.class))
            );
        }

        View nútMenuSocialFriends = findViewById(R.id.btnMenuSocialFriends);
        if (nútMenuSocialFriends != null) {
            nútMenuSocialFriends.setOnClickListener(v ->
                    startActivity(new Intent(this, SocialFriendsActivity.class))
            );
        }

        View nútMenuP2PTrade = findViewById(R.id.btnMenuP2PTrade);
        if (nútMenuP2PTrade != null) {
            nútMenuP2PTrade.setOnClickListener(v ->
                    startActivity(new Intent(this, P2PVoucherTradeActivity.class))
            );
        }

        View nútMenuSeller = findViewById(R.id.btnMenuSeller);
        if (nútMenuSeller != null) {
            nútMenuSeller.setOnClickListener(v ->
                    startActivity(new Intent(this, SellerDashboardActivity.class))
            );
        }

        View nútMenuWallet = findViewById(R.id.btnMenuWallet);
        if (nútMenuWallet != null) {
            nútMenuWallet.setOnClickListener(v ->
                    startActivity(new Intent(this, WalletActivity.class))
            );
        }

        View nútMenuArbiter = findViewById(R.id.btnMenuArbiter);
        if (nútMenuArbiter != null) {
            nútMenuArbiter.setOnClickListener(v ->
                    startActivity(new Intent(this, activity_staff_arbiter.class))
            );
        }

        View nútĐăngXuất = findViewById(R.id.btnLogout);
        if (nútĐăngXuất != null) {
            nútĐăngXuất.setOnClickListener(v -> thựcHiệnĐăngXuấtTàiKhoản());
        }
    }

    private void hiểnThịHộpThoạiChỉnhSửaHồSơ() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String họTênHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USER_NAME", "Gia Bảo Hồ");
        String emailHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USER_EMAIL", "baoho123@ssharing.vn");
        String sđtHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USER_PHONE", "0908123456");

        LinearLayout khungCustomDialog = new LinearLayout(this);
        khungCustomDialog.setOrientation(LinearLayout.VERTICAL);
        khungCustomDialog.setPadding(40, 30, 40, 20);

        TextView nhãnTênGợiÝ = new TextView(this);
        nhãnTênGợiÝ.setText("Họ và Tên hiển thị:");
        nhãnTênGợiÝ.setTextColor(Color.WHITE);
        nhãnTênGợiÝ.setTextSize(13);

        final EditText ôChỉnhSửaHọTên = new EditText(this);
        ôChỉnhSửaHọTên.setText(họTênHiệnTại);
        ôChỉnhSửaHọTên.setTextColor(Color.WHITE);
        ôChỉnhSửaHọTên.setBackgroundResource(R.drawable.bg_input_field);
        ôChỉnhSửaHọTên.setPadding(20, 20, 20, 20);

        TextView nhãnEmailGợiÝ = new TextView(this);
        nhãnEmailGợiÝ.setText("\nEmail tài khoản:");
        nhãnEmailGợiÝ.setTextColor(Color.WHITE);
        nhãnEmailGợiÝ.setTextSize(13);

        final EditText ôChỉnhSửaEmail = new EditText(this);
        ôChỉnhSửaEmail.setText(emailHiệnTại);
        ôChỉnhSửaEmail.setTextColor(Color.WHITE);
        ôChỉnhSửaEmail.setBackgroundResource(R.drawable.bg_input_field);
        ôChỉnhSửaEmail.setPadding(20, 20, 20, 20);

        TextView nhãnSđtGợiÝ = new TextView(this);
        nhãnSđtGợiÝ.setText("\nSố điện thoại xác nhận:");
        nhãnSđtGợiÝ.setTextColor(Color.WHITE);
        nhãnSđtGợiÝ.setTextSize(13);

        final EditText ôChỉnhSửaSốĐiệnThoại = new EditText(this);
        ôChỉnhSửaSốĐiệnThoại.setText(sđtHiệnTại);
        ôChỉnhSửaSốĐiệnThoại.setTextColor(Color.WHITE);
        ôChỉnhSửaSốĐiệnThoại.setBackgroundResource(R.drawable.bg_input_field);
        ôChỉnhSửaSốĐiệnThoại.setPadding(20, 20, 20, 20);

        Button nútĐổiAvatar = new Button(this);
        nútĐổiAvatar.setText("📷 Tải / Đổi ảnh đại diện từ Gallery");
        nútĐổiAvatar.setTextColor(Color.WHITE);
        nútĐổiAvatar.setBackgroundResource(R.drawable.bg_outline_button);
        LinearLayout.LayoutParams thôngSốNút = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 110
        );
        thôngSốNút.topMargin = 20;
        nútĐổiAvatar.setLayoutParams(thôngSốNút);
        nútĐổiAvatar.setOnClickListener(v -> mởBộSưuTậpChọnAvatar());

        khungCustomDialog.addView(nhãnTênGợiÝ);
        khungCustomDialog.addView(ôChỉnhSửaHọTên);
        khungCustomDialog.addView(nhãnEmailGợiÝ);
        khungCustomDialog.addView(ôChỉnhSửaEmail);
        khungCustomDialog.addView(nhãnSđtGợiÝ);
        khungCustomDialog.addView(ôChỉnhSửaSốĐiệnThoại);
        khungCustomDialog.addView(nútĐổiAvatar);

        new AlertDialog.Builder(this)
                .setTitle("✎ Chỉnh Sửa Hồ Sơ Cá Nhân")
                .setView(khungCustomDialog)
                .setPositiveButton("Lưu thay đổi", (hộpThoại, vịTrí) -> {
                    String tênMới = ôChỉnhSửaHọTên.getText().toString().trim();
                    String emailMới = ôChỉnhSửaEmail.getText().toString().trim();
                    String sđtMới = ôChỉnhSửaSốĐiệnThoại.getText().toString().trim();

                    if (tênMới.isEmpty()) {
                        Toast.makeText(this, "Họ và tên không được để trống!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    bộLưuTrữCụcBộ.edit()
                            .putString("CURRENT_USER_NAME", tênMới)
                            .putString("CURRENT_USER_EMAIL", emailMới)
                            .putString("CURRENT_USER_PHONE", sđtMới)
                            .apply();

                    cậpNhậtThôngTinCáNhân();
                    Toast.makeText(this, "🎉 Đã cập nhật hồ sơ cá nhân thành công!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void mởBộSưuTậpChọnAvatar() {
        Intent ýĐịnhChọnẢnh = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(ýĐịnhChọnẢnh, MÃ_CHỌN_ẢNH_ĐẠI_DIỆN_AVATAR);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở bộ sưu tập ảnh.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MÃ_CHỌN_ẢNH_ĐẠI_DIỆN_AVATAR && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uriAvatarChọn = data.getData();

            SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
            String usernameUser = bộLưuTrữCụcBộ.getString("CURRENT_USERNAME", "baoho123");
            String usernameClean = usernameUser.toLowerCase().trim();
            String keyAvatarCáNhân = "CURRENT_USER_AVATAR_PATH_" + usernameClean;

            File tệpAvatarNộiBộ = new File(getFilesDir(), "user_avatar_" + usernameClean + ".jpg");

            try (InputStream inputStream = getContentResolver().openInputStream(uriAvatarChọn);
                 FileOutputStream outputStream = new FileOutputStream(tệpAvatarNộiBộ)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                bộLưuTrữCụcBộ.edit()
                        .putString(keyAvatarCáNhân, tệpAvatarNộiBộ.getAbsolutePath())
                        .apply();

                vn.edu.ueh.baoho.ssharing.utils.SocialHelper.syncAvatarToCloud(this, usernameClean, tệpAvatarNộiBộ);

                cậpNhậtThôngTinCáNhân();
                Toast.makeText(this, "📷 Đã cập nhật ảnh đại diện lên Cloud Firebase cho tài khoản " + usernameUser + "!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Không thể lưu ảnh đại diện vào bộ nhớ.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void thựcHiệnĐăngXuấtTàiKhoản() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        bộLưuTrữCụcBộ.edit()
                .putBoolean("IS_LOGGED_IN", false)
                .apply();

        Toast.makeText(this, "Đã đăng xuất tài khoản thành công.", Toast.LENGTH_SHORT).show();
        Intent ýĐịnhChuyển = new Intent(this, LoginActivity.class);
        ýĐịnhChuyển.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ýĐịnhChuyển);
        finish();
    }
}
