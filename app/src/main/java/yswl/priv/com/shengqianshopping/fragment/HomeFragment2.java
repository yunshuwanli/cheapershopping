package yswl.priv.com.shengqianshopping.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;

import org.json.JSONObject;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yswl.com.klibrary.base.MFragment;
import yswl.com.klibrary.http.CallBack.HttpCallback;
import yswl.com.klibrary.http.HttpClientProxy;
import yswl.priv.com.shengqianshopping.R;
import yswl.priv.com.shengqianshopping.banner.BannerBean;
import yswl.priv.com.shengqianshopping.banner.BannerUtil;
import yswl.priv.com.shengqianshopping.bean.CategoryBean;
import yswl.priv.com.shengqianshopping.bean.ResultUtil;
import yswl.priv.com.shengqianshopping.util.UrlUtil;

public class HomeFragment2 extends MFragment implements HttpCallback<JSONObject>, View.OnClickListener {
    private static final String FRAGMENT_TAG = "HomeFragment2_ItemFragment";



    ConvenientBanner mConvenientBanner;
    private BannerUtil banner;
    List<BannerBean> mImags;

    public HomeFragment2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home2, container, false);
    }


    //    LinearLayout mCrazyBuy, mAdvise, mSort, mPlan;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConvenientBanner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);
        view.findViewById(R.id.ll_fkq).setOnClickListener(this);
        view.findViewById(R.id.ll_tj).setOnClickListener(this);
        view.findViewById(R.id.ll_sort).setOnClickListener(this);
        view.findViewById(R.id.ll_plan).setOnClickListener(this);

        initBanner();
        requestCategroy();
        requestBanner();
    }

    private static final int REQUEST_ID_CATEGROY = 100;
    private static final int REQUEST_ID_BANNER = 101;

    private void requestCategroy() {
        String url = UrlUtil.getUrl(this, R.string.url_category_type_list);
        Map<String, Object> par = new HashMap<>();
        par.put("type", "1");
        HttpClientProxy.getInstance().postAsyn(url, REQUEST_ID_CATEGROY, par, this);
    }

    private void requestBanner() {
        String url = UrlUtil.getUrl(this, R.string.url_banner_list);
        HttpClientProxy.getInstance().postAsyn(url, REQUEST_ID_BANNER, null, this);
    }

    void initBanner() {
        if (null == banner) {
            banner = new BannerUtil();
        }
        banner.setConvenientBanner(mConvenientBanner);
        banner.loadPic(mImags);
    }

    @Override
    public void onClick(View v) {
//       Fragment frament = getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG);
//        ItemFragment gridFragment = null;
//        if(frament instanceof ItemFragment){
//             gridFragment = (ItemFragment)frament ;
//        }
        switch (v.getId()) {
            case R.id.ll_fkq:
                CategoryBean category = getCrazyBuyProductCategoryId("抢购");
                //TODO 新抢购页
                break;
            case R.id.ll_tj:
                CategoryBean category2 = getCrazyBuyProductCategoryId("推荐");
                //TODO 新推荐页
                break;
            case R.id.ll_sort:
                CategoryBean category3 = getCrazyBuyProductCategoryId("排名");
                //TODO 排名页
                break;
            case R.id.ll_plan:
                CategoryBean category4 = getCrazyBuyProductCategoryId("预告");
                //TODO 预告
                break;

        }
    }


    List<CategoryBean> mCategorys;

    @Override
    public void onSucceed(int requestId, final JSONObject result) {
        if (ResultUtil.isCodeOK(result)) {

            switch (requestId) {
                case REQUEST_ID_BANNER:
                    mImags = BannerBean.jsonToList(
                            ResultUtil.analysisData(result).optJSONArray(ResultUtil.LIST));
                    banner.loadPic(mImags);
                    break;
                case REQUEST_ID_CATEGROY:
                    mCategorys = CategoryBean.jsonToList(
                            ResultUtil.analysisData(result).optJSONArray(ResultUtil.LIST));

                    //TODO 赋值
                    if (mCategorys != null && mCategorys.size() > 0){

                        addProductListModule(mCategorys.get(0));

                    }
                    break;
            }
        }

    }

    private void addProductListModule(CategoryBean category) {
        getChildFragmentManager().beginTransaction().replace(R.id.content, ItemFragment.newInstance(category),FRAGMENT_TAG).commit();
    }

    @Override
    public void onFail(int requestId, String errorMsg) {

    }


    public CategoryBean getCrazyBuyProductCategoryId(String key) {
        if (mCategorys != null && mCategorys.size() > 0)
            for (CategoryBean category: mCategorys) {
                if(category.title.contains(key)){
                    return category;
                }
            }
        return null;
    }
}
