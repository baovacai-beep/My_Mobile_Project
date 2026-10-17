package vn.edu.ueh.baoho.ssharing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MapStoreLocatorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_store_locator);

        // Khởi tạo client lấy vị trí
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Tìm SupportMapFragment trong XML và đồng bộ
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Cài đặt Bottom Sheet (Bảng vuốt hiển thị chi nhánh)
        LinearLayout bottomSheetStores = findViewById(R.id.bottomSheetStores);
        if (bottomSheetStores != null) {
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetStores);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // Mặc định ẩn 1 nửa
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 1. Kiểm tra quyền truy cập Vị trí (GPS)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa có quyền -> Yêu cầu người dùng cấp quyền
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // 2. Bật chấm xanh vị trí của tôi trên map
        mMap.setMyLocationEnabled(true);

        // 3. Lấy tọa độ hiện tại và di chuyển Camera đến đó
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));

                // Demo tạo 1 Marker chi nhánh cửa hàng gần đó
                LatLng storeLocation = new LatLng(location.getLatitude() + 0.005, location.getLongitude() + 0.005);
                mMap.addMarker(new MarkerOptions().position(storeLocation).title("Highlands 279 NVL"));
            } else {
                // Nếu lỗi GPS, mặc định ghim tại UEH
                LatLng ueh = new LatLng(10.7766, 106.6908);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ueh, 15f));
                mMap.addMarker(new MarkerOptions().position(ueh).title("Cơ sở UEH"));
            }
        });

        // 4. Bắt sự kiện bấm vào Marker để đẩy Bottom Sheet lên
        mMap.setOnMarkerClickListener(marker -> {
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            return false;
        });
    }

    // Xử lý khi người dùng ấn Đồng ý / Từ chối cấp quyền GPS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Tải lại màn hình để vẽ map
                recreate();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền vị trí để xem cửa hàng quanh đây!", Toast.LENGTH_LONG).show();
            }
        }
    }
}