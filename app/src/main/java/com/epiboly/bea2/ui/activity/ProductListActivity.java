//package com.epiboly.bea2.ui.activity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.epiboly.bea2.R;
//import com.epiboly.bea2.app.AppActivity;
//import com.epiboly.bea2.http.model.Product;
//import com.epiboly.bea2.ui.adapter.ProductAdapter;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProductListActivity extends AppActivity {
//    private RecyclerView recyclerView;
//    private ProductAdapter adapter;
//    private List<Product> productList;
//    private Button loadMoreButton;
//    private int currentPage = 0;
//    private static final int PAGE_SIZE = 10; // 每页加载的商品数量
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product_list);
//
//        recyclerView = findViewById(R.id.recycler_view);
//        loadMoreButton = findViewById(R.id.load_more_button);
//
//        productList = new ArrayList<>();
//        adapter = new ProductAdapter(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//        loadMoreButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadMoreProducts();
//            }
//        });
//
//        loadMoreProducts(); // 初始加载
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return 0;
//    }
//
//    @Override
//    protected void initView() {
//
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//    private void loadMoreProducts() {
//        // TODO: 在这里实现从服务器或本地加载商品的逻辑
//        // 这里是模拟加载数据的示例
//        for (int i = 0; i < PAGE_SIZE; i++) {
//            int productId = currentPage * PAGE_SIZE + i + 1;
//            productList.add(new Product("https://example.com/image" + productId + ".jpg",
//                "商品 " + productId,
//                BigDecimal.TEN,
//                100 - (productId % 10)));
//        }
//        adapter.notifyDataSetChanged();
//        currentPage++;
//
//        // 如果还有更多商品，显示“加载更多”按钮
//        if (currentPage < 5) { // 假设还有5页商品
//            loadMoreButton.setVisibility(View.VISIBLE);
//        } else {
//            loadMoreButton.setVisibility(View.GONE);
//        }
//    }
//}