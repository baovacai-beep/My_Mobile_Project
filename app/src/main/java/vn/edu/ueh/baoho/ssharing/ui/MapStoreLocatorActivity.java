package vn.edu.ueh.baoho.ssharing.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import vn.edu.ueh.baoho.ssharing.R;

public class MapStoreLocatorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private WebView webViewMap;

    // Tọa độ chi nhánh Highlands mẫu gần UEH
    private final LatLng highlandsNVL = new LatLng(10.762622, 106.660172); // Highlands 279 Nguyễn Tri Phương (UEH B)
    private final LatLng highlandsVanHanhMall = new LatLng(10.770200, 106.669800); // Highlands Vạn Hạnh Mall
    private final LatLng highlandsNDC = new LatLng(10.781800, 106.691200); // Highlands Nguyễn Đình Chiểu (UEH A)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_store_locator);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        webViewMap = findViewById(R.id.webViewMap);

        View btnBackMap = findViewById(R.id.btnBackMap);
        if (btnBackMap != null) {
            btnBackMap.setOnClickListener(v -> finish());
        }

        // Bottom Sheet Chi Nhánh
        LinearLayout bottomSheetStores = findViewById(R.id.bottomSheetStores);
        if (bottomSheetStores != null) {
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetStores);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        // Nút mở Google Maps chỉ đường trực tiếp cho Chi nhánh 1 (279 Nguyễn Tri Phương)
        View btnNavGoogleMaps1 = findViewById(R.id.btnNavGoogleMaps1);
        if (btnNavGoogleMaps1 != null) {
            btnNavGoogleMaps1.setOnClickListener(v ->
                    moGoogleMapsChiDuong("Highlands Coffee 279 Nguyễn Tri Phương", 10.762622, 106.660172)
            );
        }

        // Nút mở Google Maps chỉ đường trực tiếp cho Chi nhánh 2 (Vạn Hạnh Mall)
        View btnNavGoogleMaps2 = findViewById(R.id.btnNavGoogleMaps2);
        if (btnNavGoogleMaps2 != null) {
            btnNavGoogleMaps2.setOnClickListener(v ->
                    moGoogleMapsChiDuong("Highlands Coffee Vạn Hạnh Mall", 10.770200, 106.669800)
            );
        }

        // Bấm chọn thẻ chi nhánh 1
        View cardStore1 = findViewById(R.id.cardStore1);
        if (cardStore1 != null) {
            cardStore1.setOnClickListener(v -> {
                if (mMap != null) {
                    try {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(highlandsNVL, 17f));
                    } catch (Exception ignored) {}
                }
                Toast.makeText(this, "Đang định vị: Highlands Coffee 279 Nguyễn Tri Phương", Toast.LENGTH_SHORT).show();
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
        }

        // Bấm chọn thẻ chi nhánh 2
        View cardStore2 = findViewById(R.id.cardStore2);
        if (cardStore2 != null) {
            cardStore2.setOnClickListener(v -> {
                if (mMap != null) {
                    try {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(highlandsVanHanhMall, 17f));
                    } catch (Exception ignored) {}
                }
                Toast.makeText(this, "Đang định vị: Highlands Coffee Vạn Hạnh Mall", Toast.LENGTH_SHORT).show();
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
        }

        // 1. Tải bản đồ đường xá giao thông thật 100% bằng OpenStreetMap Leaflet trực tiếp trên WebView
        loadRealTileMapWebView();

        // 2. Thử nạp Google Maps Native SDK
        try {
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commitAllowingStateLoss();
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Log.e("MapStoreLocator", "Không thể nạp Google Maps Native SDK: " + e.getMessage());
        }
    }

    /**
     * Tải bản đồ thực tế đường xá thực 100% với ghim đỏ Highlands Coffee
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void loadRealTileMapWebView() {
        if (webViewMap == null) return;

        WebSettings webSettings = webViewMap.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webViewMap.setWebViewClient(new WebViewClient());

        // Mã HTML Leaflet / OpenStreetMap hiển thị bản đồ thật 100% kèm Marker Highlands Coffee 279 Nguyễn Tri Phương
        String mapHtml = "<!DOCTYPE html><html><head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>" +
                "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>" +
                "<style>html, body, #map { height: 100%; margin: 0; padding: 0; background: #121018; }</style>" +
                "</head><body>" +
                "<div id='map'></div>" +
                "<script>" +
                "var map = L.map('map').setView([10.762622, 106.660172], 16);" +
                "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {" +
                "    maxZoom: 19," +
                "    attribution: '© OpenStreetMap'" +
                "}).addTo(map);" +
                "var m1 = L.marker([10.762622, 106.660172]).addTo(map).bindPopup('<b>Highlands Coffee 279 NVL</b><br>Cạnh UEH Campus B').openPopup();" +
                "var m2 = L.marker([10.770200, 106.669800]).addTo(map).bindPopup('<b>Highlands Coffee Vạn Hạnh Mall</b><br>Tầng L1');" +
                "var m3 = L.marker([10.781800, 106.691200]).addTo(map).bindPopup('<b>Highlands Coffee 59 Nguyễn Đình Chiểu</b><br>Cạnh UEH Campus A');" +
                "</script></body></html>";

        webViewMap.loadDataWithBaseURL("https://openstreetmap.org", mapHtml, "text/html", "UTF-8", null);
    }

    /**
     * Mở ứng dụng Google Maps trực tiếp để dẫn đường thực tế
     */
    private void moGoogleMapsChiDuong(String tenCuaHang, double lat, double lng) {
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + Uri.encode(tenCuaHang));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        try {
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Nếu chưa cài app Google Maps -> Mở bằng Trình duyệt Web Google Maps
                Uri webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(tenCuaHang));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
                startActivity(webIntent);
            }
        } catch (Exception e) {
            Uri webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(tenCuaHang));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
            startActivity(webIntent);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            mMap = googleMap;

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);

            // Đặt Marker các chi nhánh Highlands Coffee gần nhất
            mMap.addMarker(new MarkerOptions()
                    .position(highlandsNVL)
                    .title("Highlands Coffee - 279 Nguyễn Tri Phương")
                    .snippet("📍 Cạnh UEH Campus B • Áp dụng Voucher 15% - 50K")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            mMap.addMarker(new MarkerOptions()
                    .position(highlandsVanHanhMall)
                    .title("Highlands Coffee - Vạn Hạnh Mall")
                    .snippet("📍 Tầng L1 Vạn Hạnh Mall • Áp dụng Voucher 15% - 50K")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            mMap.addMarker(new MarkerOptions()
                    .position(highlandsNDC)
                    .title("Highlands Coffee - 59 Nguyễn Đình Chiểu")
                    .snippet("📍 Cạnh UEH Campus A • Áp dụng Voucher 15% - 50K")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(highlandsNVL, 16f));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                try {
                    mMap.setMyLocationEnabled(true);
                } catch (Exception ignored) {}

                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null && mMap != null) {
                        try {
                            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
                        } catch (Exception ignored) {}
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }

            mMap.setOnMarkerClickListener(marker -> {
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                marker.showInfoWindow();
                return false;
            });
        } catch (Exception e) {
            Log.e("MapStoreLocator", "Lỗi khởi tạo Google Maps: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            } else {
                Toast.makeText(this, "Hiển thị vị trí cửa hàng Highlands Coffee gần bạn nhất!", Toast.LENGTH_LONG).show();
            }
        }
    }
}