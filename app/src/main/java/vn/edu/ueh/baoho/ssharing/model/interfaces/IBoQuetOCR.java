package vn.edu.ueh.baoho.ssharing.model.interfaces;

import android.graphics.Bitmap;

public interface IBoQuetOCR {
    void trichXuatThongTinVoucher(Bitmap anhVoucher, KetQuaOCRCallback callback);

    interface KetQuaOCRCallback {
        void onSuccess(String maCode, String hanSuDung, double giaTriGiam);
        void onFailure(String errorMessage);
    }
}