package com.application.spotreview.map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.application.spotreview.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Tm128;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private NaverMap naverMap;
    private List<Marker> markerList = new ArrayList<>();

    // ⚠️ 네이버 개발자 센터 검색용 키
    private final String SEARCH_CLIENT_ID = "Ivz6wdspLMyzf7vNuFxe";
    private final String SEARCH_CLIENT_SECRET = "fVDadg0EQ_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 디자인 수정으로 Button이 아닌 ImageButton으로 변경되었습니다.
        View btnMainMenu = findViewById(R.id.btn_main_menu);
        if (btnMainMenu != null) {
            btnMainMenu.setOnClickListener(v -> finish());
        }

        View btnMajorSpot = findViewById(R.id.btn_major_spot);
        if (btnMajorSpot != null) {
            btnMajorSpot.setOnClickListener(v -> {
                if (naverMap != null) {
                    fetchRestaurantData("고척동 맛집");
                }
            });
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.getUiSettings().setZoomControlEnabled(true);

        LatLng initialPos = new LatLng(37.500397, 126.866599);
        CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(initialPos, 15);
        naverMap.moveCamera(cameraUpdate);

        // 데이터 로딩 시작
        fetchRestaurantData("고척동 맛집");
    }

    private void fetchRestaurantData(String keyword) {
        Toast.makeText(this, keyword + " 검색 중...", Toast.LENGTH_SHORT).show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NaverSearchService service = retrofit.create(NaverSearchService.class);

        // 검색 개수를 50개로 늘려 더 많은 마커가 보이게 합니다.
        service.getLocalSearch(SEARCH_CLIENT_ID, SEARCH_CLIENT_SECRET, keyword, 50)
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().items == null || response.body().items.isEmpty()) {
                                Toast.makeText(MapActivity.this, "검색 결과가 0건입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                showMarkersOnMap(response.body());
                            }
                        } else {
                            String errorMsg = "API 오류 발생: " + response.code();
                            Toast.makeText(MapActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                        Toast.makeText(MapActivity.this, "네트워크 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showMarkersOnMap(SearchResponse searchResponse) {
        if (naverMap == null || searchResponse.items == null) return;

        // 기존 마커 제거
        for (Marker marker : markerList) {
            marker.setMap(null);
        }
        markerList.clear();

        for (SearchItem item : searchResponse.items) {
            // 요청하신 대로 나누기 방식으로 좌표 변환
            LatLng latLng = new LatLng(item.mapy / 10000000.0, item.mapx / 10000000.0);

            Marker marker = new Marker();
            marker.setPosition(latLng);
            String cleanTitle = item.title.replaceAll("<[^>]*>", "");
            marker.setCaptionText(cleanTitle);
            marker.setMap(naverMap);

            // 마커 클릭 시 해당 장소의 정보를 전달하기 위해 tag에 저장
            marker.setTag(item);

            // 마커 클릭 리스너 추가
            marker.setOnClickListener(overlay -> {
                SearchItem clickedItem = (SearchItem) marker.getTag();
                if (clickedItem != null) {
                    Intent intent = new Intent(MapActivity.this, SpotDetailActivity.class);
                    intent.putExtra("title", clickedItem.title.replaceAll("<[^>]*>", ""));
                    intent.putExtra("address", clickedItem.roadAddress);
                    // 좌표 데이터 추가 전달
                    intent.putExtra("lat", latLng.latitude);
                    intent.putExtra("lng", latLng.longitude);
                    startActivity(intent);
                }
                return true;
            });

            markerList.add(marker);
        }

        // 데이터 로딩 완료 후 첫 번째 위치로 부드럽게 이동
        if (!searchResponse.items.isEmpty()) {
            SearchItem firstItem = searchResponse.items.get(0);
            LatLng firstLatLng = new LatLng(firstItem.mapy / 10000000.0, firstItem.mapx / 10000000.0);
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(firstLatLng)
                    .animate(CameraAnimation.Easing, 1000);
            naverMap.moveCamera(cameraUpdate);
        }
    }
}
