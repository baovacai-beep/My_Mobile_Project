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

        public ThôngTinBannerItem(String nhãnBadge, String tiêuĐề, int giáXu, String emojiPoster, String tênThươngHiệu, String mãBíMật, int màuNềnStart, int màuNềnEnd, boolean làChínhHãng, String tênNgườiBán, String avatarNgườiBán) {
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
        private final TextView nhãnEmoji;
        private final TextView nhãnThươngHiệu;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            khungNềnBackground = itemView.findViewById(R.id.layoutBannerBackground);
            nhãnBadge = itemView.findViewById(R.id.tvBannerBadge);
            nhãnTiêuĐề = itemView.findViewById(R.id.tvBannerTitle);
            nhãnGiáXu = itemView.findViewById(R.id.tvBannerPriceSS);
            nhãnEmoji = itemView.findViewById(R.id.tvPosterEmoji);
            nhãnThươngHiệu = itemView.findViewById(R.id.tvPosterBrand);
        }

        public void gắnDữLiệu(ThôngTinBannerItem item, LắngNgheSựKiệnBấmBanner bộLắngNgheBấm) {
            if (nhãnBadge != null) nhãnBadge.setText(item.nhãnBadge);
            if (nhãnTiêuĐề != null) nhãnTiêuĐề.setText(item.tiêuĐề);
            if (nhãnGiáXu != null) nhãnGiáXu.setText(item.giáXu + " SS");
            if (nhãnEmoji != null) nhãnEmoji.setText(item.emojiPoster);
            if (nhãnThươngHiệu != null) nhãnThươngHiệu.setText(item.tênThươngHiệu);

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
