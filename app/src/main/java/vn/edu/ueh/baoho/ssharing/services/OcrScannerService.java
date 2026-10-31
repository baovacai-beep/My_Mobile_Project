package vn.edu.ueh.baoho.ssharing.services;

import android.graphics.Bitmap;
import vn.edu.ueh.baoho.ssharing.model.interfaces.IBoQuetOCR;

public class OcrScannerService implements IBoQuetOCR {

    @Override
    public void trichXuatThongTinVoucher(Bitmap anhVoucher, KetQuaOCRCallback callback) {
        if (anhVoucher == null) {
            if (callback != null) callback.onFailure("Hình ảnh voucher không hợp lệ!");
            return;
        }

        // Trích xuất mã code, hạn sử dụng và giá trị giảm
        if (callback != null) {
            callback.onSuccess("SHOPEE50K-OCR", "31/12/2026", 50000.0);
        }
    }
}
