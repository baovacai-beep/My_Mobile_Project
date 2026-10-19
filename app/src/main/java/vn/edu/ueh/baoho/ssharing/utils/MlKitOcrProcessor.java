package vn.edu.ueh.baoho.ssharing.utils;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class MlKitOcrProcessor {

    public interface OcrCallback {
        void onTextExtracted(String recognizedText);
        void onError(Exception e);
    }

    /**
     * Quét bóc tách ký tự OCR trực tiếp trên thiết bị (On-Device AI) bằng ML Kit
     */
    public static void quetAnhVoucher(Bitmap bitmap, @NonNull OcrCallback callback) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        com.google.mlkit.vision.text.TextRecognizer recognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    StringBuilder extracted = new StringBuilder();
                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                        extracted.append(block.getText()).append("\n");
                    }
                    callback.onTextExtracted(extracted.toString().trim());
                })
                .addOnFailureListener(callback::onError);
    }
}