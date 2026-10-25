package vn.edu.ueh.baoho.ssharing.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.ueh.baoho.ssharing.R;

public class BannerSliderAdapter extends RecyclerView.Adapter<BannerSliderAdapter.BannerViewHolder> {

    public interface LắngNgheSựKiệnBấmBanner {
        void khiBấmVàoBanner(ThôngTinBannerItem itemBanner);
    }

    public static class ThôngTinBannerItem {
        public String nhãnBadge;
        public String tiêuĐề;
        public int giáXu;
        public String emojiPoster;
        public String tênThươngHiệu;
        public String mãBíMật;
        public int màuNềnStart;
        public int màuNềnEnd;
        public boolean làChínhHãng;
        public String tênNgườiBán;
        public String avatarNgườiBán;
        public String urlHìnhẢnhThật;
        public int resDrawableLocal;

        public ThôngTinBannerItem(String nhãnBadge, String tiêuĐề, int giáXu, String emojiPoster, String tênThươngHiệu, String mãBíMật, int màuNềnStart, int màuNềnEnd, boolean làChínhHãng, String tênNgườiBán, String avatarNgườiBán) {
            this(nhãnBadge, tiêuĐề, giáXu, emojiPoster, tênThươngHiệu, mãBíMật, màuNềnStart, màuNềnEnd, làChínhHãng, tênNgườiBán, avatarNgườiBán, "", 0);
        }

        public ThôngTinBannerItem(String nhãnBadge, String tiêuĐề, int giáXu, String emojiPoster, String tênThươngHiệu, String mãBíMật, int màuNềnStart, int màuNềnEnd, boolean làChínhHãng, String tênNgườiBán, String avatarNgườiBán, String urlHìnhẢnhThật) {
            this(nhãnBadge, tiêuĐề, giáXu, emojiPoster, tênThươngHiệu, mãBíMật, màuNềnStart, màuNềnEnd, làChínhHãng, tênNgườiBán, avatarNgườiBán, urlHìnhẢnhThật, 0);
        }

        public ThôngTinBannerItem(String nhãnBadge, String tiêuĐề, int giáXu, String emojiPoster, String tênThươngHiệu, String mãBíMật, int màuNềnStart, int màuNềnEnd, boolean làChínhHãng, String tênNgườiBán, String avatarNgườiBán, String urlHìnhẢnhThật, int resDrawableLocal) {
            this.nhãnBadge = nhãnBadge;
            this.tiêuĐề = tiêuĐề;
            this.giáXu = giáXu;
            this.emojiPoster = emojiPoster;
            this.tênThươngHiệu = tênThươngHiệu;
            this.mãBíMật = mãBíMật;
            this.màuNềnStart = màuNềnStart;
            this.màuNềnEnd = màuNềnEnd;
            this.làChínhHãng = làChínhHãng;
            this.tênNgườiBán = tênNgườiBán;
            this.avatarNgườiBán = avatarNgườiBán;
            this.urlHìnhẢnhThật = urlHìnhẢnhThật;
            this.resDrawableLocal = resDrawableLocal;
        }
    }

    private final List<ThôngTinBannerItem> danhSáchBanner;
    private final LắngNgheSựKiệnBấmBanner bộLắngNgheBấm;

    public BannerSliderAdapter(List<ThôngTinBannerItem> danhSáchBanner, LắngNgheSựKiệnBấmBanner bộLắngNgheBấm) {
        this.danhSáchBanner = danhSáchBanner;
        this.bộLắngNgheBấm = bộLắngNgheBấm;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_slide, parent, false);
        return new BannerViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        ThôngTinBannerItem item = danhSáchBanner.get(position);
        holder.gắnDữLiệu(item, bộLắngNgheBấm);
    }

    @Override
    public int getItemCount() {
        return danhSáchBanner != null ? danhSáchBanner.size() : 0;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout khungNềnBackground;
        private final TextView nhãnBadge;
        private final TextView nhãnTiêuĐề;
        private final TextView nhãnGiáXu;
        private final android.widget.ImageView ảnhFullBannerThật;
        private final android.os.Handler bộXửLýThreadUI = new android.os.Handler(android.os.Looper.getMainLooper());

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            khungNềnBackground = itemView.findViewById(R.id.layoutBannerBackground);
            nhãnBadge = itemView.findViewById(R.id.tvBannerBadge);
            nhãnTiêuĐề = itemView.findViewById(R.id.tvBannerTitle);
            nhãnGiáXu = itemView.findViewById(R.id.tvBannerPriceSS);
            ảnhFullBannerThật = itemView.findViewById(R.id.imgFullBannerBackground);
        }

        public void gắnDữLiệu(ThôngTinBannerItem item, LắngNgheSựKiệnBấmBanner bộLắngNgheBấm) {
            if (nhãnBadge != null) nhãnBadge.setText(item.nhãnBadge);
            if (nhãnTiêuĐề != null) nhãnTiêuĐề.setText(item.tiêuĐề);
            if (nhãnGiáXu != null) nhãnGiáXu.setText(item.giáXu + " SS");

            if (ảnhFullBannerThật != null) {
                if (item.resDrawableLocal != 0) {
                    ảnhFullBannerThật.setImageResource(item.resDrawableLocal);
                    ảnhFullBannerThật.setVisibility(View.VISIBLE);
                } else if (item.urlHìnhẢnhThật != null && !item.urlHìnhẢnhThật.isEmpty()) {
                    String urlTệp = item.urlHìnhẢnhThật;
                    new Thread(() -> {
                        try {
                            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new java.net.URL(urlTệp).openConnection();
                            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                            conn.setConnectTimeout(8000);
                            conn.setReadTimeout(8000);
                            conn.setInstanceFollowRedirects(true);
                            java.io.InputStream inputStream = conn.getInputStream();
                            android.graphics.Bitmap bitmapPoster = android.graphics.BitmapFactory.decodeStream(inputStream);
                            bộXửLýThreadUI.post(() -> {
                                if (bitmapPoster != null) {
                                    ảnhFullBannerThật.setImageBitmap(bitmapPoster);
                                    ảnhFullBannerThật.setVisibility(View.VISIBLE);
                                }
                            });
                        } catch (Throwable ignored) {}
                    }).start();
                } else {
                    ảnhFullBannerThật.setVisibility(View.GONE);
                }
            }

            // Đổi màu Gradient nền tương ứng từng Poster
            if (khungNềnBackground != null) {
                GradientDrawable dảiMàuGradient = new GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT,
                        new int[]{item.màuNềnStart, item.màuNềnEnd}
                );
                dảiMàuGradient.setCornerRadius(32f);
                khungNềnBackground.setBackground(dảiMàuGradient);
            }

            // Bấm mở chi tiết voucher mua
            itemView.setOnClickListener(v -> {
                if (bộLắngNgheBấm != null) {
                    bộLắngNgheBấm.khiBấmVàoBanner(item);
                }
            });
        }
    }
}
