package vn.edu.ueh.baoho.ssharing.utils;
import java.text.DecimalFormat;

public class FormatUtils {
    public static String formatXu(double xu) {
        return new DecimalFormat("#,### Xu").format(xu);
    }
}