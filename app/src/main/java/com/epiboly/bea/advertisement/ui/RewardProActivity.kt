package com.epiboly.bea.advertisement.ui

import android.content.Context
import android.content.Intent
import com.epiboly.bea.advertisement.AdProviderType
import com.epiboly.bea.advertisement.TogetherAdAlias
import com.epiboly.bea.rich.R
import com.epiboly.bea.action.StatusAction
import com.epiboly.bea.app.AppActivity
import com.epiboly.bea.cache.UserHelper
import com.epiboly.bea.http.api.FinishAdvertiseWatchApi
import com.epiboly.bea.http.model.HttpData
import com.epiboly.bea.http.model.IntegralServer
import com.epiboly.bea.widget.StatusLayout
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.ifmvo.togetherad.core.helper.AdHelperRewardPro
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import kotlinx.android.synthetic.main.activity_ad_splash.*
import okhttp3.Call
import org.jetbrains.annotations.NotNull

/**
 * 激励广告使用实例
 *
 * Created by Matthew Chen on 2020-04-22.
 */
const val TYPE_SORT = "sort"
class RewardProActivity : AppActivity() ,StatusAction {

    private var mStatusLayout: StatusLayout? = null
    private val tag = "RewardProActivity"

    companion object {
        fun action(
            context: @NotNull Context,
            i: Int
        ) {
            val intent = Intent(context, RewardProActivity::class.java)
            intent.putExtra(TYPE_SORT,i.toString())
            context.startActivity(intent)
        }
    }


    override fun getLayoutId() = R.layout.activity_reward_pro
    override fun initView() {
        mStatusLayout = findViewById(R.id.status_layout)
    }

    override fun initData() {
        //使用 Map<String, Int> 配置广告商 权重，通俗的讲就是 随机请求的概率占比
        val ratioMapReward = linkedMapOf(
            AdProviderType.GDT.type to 1,
            AdProviderType.CSJ.type to 1
        )

        /**
         * 穿山甲请求激励广告可选参数
         * 以下参数均为（ 非必传 ）
         */
//        CsjProvider.Reward.userID = "userId_123"
//        CsjProvider.Reward.supportDeepLink = true//默认为true
//        CsjProvider.Reward.rewardName = "金币"//奖励名称
//        CsjProvider.Reward.rewardAmount = 30//奖励数量
//        CsjProvider.Reward.mediaExtra = "TogetherAd"//用户透传的信息
//        //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL。默认是TTAdConstant.VERTICAL
//        CsjProvider.Reward.orientation = TTAdConstant.VERTICAL//默认 TTAdConstant.VERTICAL
//        CsjProvider.Reward.showDownLoadBar = true//不设置默认为true

        /**
         * activity: 必传。
         * alias: 必传。广告位的别名。初始化的时候是根据别名设置的广告ID，所以这里TogetherAd会根据别名查找对应的广告位ID。
         * ratioMap: 非必传。广告商的权重。可以不传或传null，空的情况 TogetherAd 会自动使用初始化时 TogetherAd.setPublicProviderRatio 设置的全局通用权重。
         * listener: 非必传。如果你不需要监听结果可以不传或传空。各个回调方法也可以选择性添加
         */
        AdHelperRewardPro.show(activity = this, alias = TogetherAdAlias.AD_REWARD, ratioMap = ratioMapReward, listener = object :
            RewardListener {
            override fun onAdStartRequest(providerType: String) {
                //在开始请求之前会回调此方法，失败切换的情况会回调多次
                "onAdStartRequest: $providerType".logi(tag)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //请求失败的回调，失败切换的情况会回调多次
                "onAdFailed: $providerType: $failedMsg".loge(tag)
            }

            override fun onAdFailedAll(failedMsg: String?) {
                //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
                "onAdFailedAll".loge(tag)
            }

            override fun onAdClicked(providerType: String) {
                //点击广告的回调
                "onAdClicked: $providerType".logi(tag)
            }

            override fun onAdShow(providerType: String) {
                //广告展示展示的回调
                "onAdShow: $providerType".logi(tag)
            }

            override fun onAdLoaded(providerType: String) {
                //广告请求成功的回调，每次请求只回调一次
                "onAdLoaded: $providerType".logi(tag)
            }

            override fun onAdExpose(providerType: String) {
                //广告展示曝光的回调
                "onAdExpose: $providerType".logi(tag)
            }

            override fun onAdVideoComplete(providerType: String) {
                //视频播放完成的回调
//                addLog("视频播放完成: $providerType")
                "onAdVideoComplete: $providerType".logi(tag)
            }

            override fun onAdVideoCached(providerType: String) {
                //视频缓存完成的回调
                "onAdVideoCached: $providerType".logi(tag)
            }

            override fun onAdRewardVerify(providerType: String) {
                //激励结果验证成功的回调
                "onAdRewardVerify，$providerType".logi(tag)
                adVideoComplete();

//                //如果 csj 使用到服务器到服务器回调功能，需要进行以下判断，gdt、baidu都不需要此判断
//                if (providerType == AdProviderType.CSJ.type && CsjProvider.Reward.rewardVerify) {
//                    ... 验证成功后的操作
//                }
            }


            override fun onAdClose(providerType: String) {
                //广告被关闭的回调
//                addLog("关闭了: $providerType")
                finish()
                "onAdClose: $providerType".logi(tag)
            }
        })
    }

    private fun adVideoComplete() {
        EasyHttp.post(RewardProActivity@this)
            .server(IntegralServer())
            .api(FinishAdvertiseWatchApi()
                .setToken(UserHelper.getInstance().token)
                .setSort(getString(TYPE_SORT)))
            .request(object : HttpCallback<HttpData<Void?>>(null) {
                override fun onStart(call: Call) {

                }

                override fun onEnd(call: Call) {}
                override fun onSucceed(data: HttpData<Void?>?) {
                    if (data != null && data.isRequestSucceed){
                        post(Runnable {
                            toast("任务完成")
                        })
                    }
                }

                override fun onFail(e: Exception) {
                    super.onFail(e)
                }
            })
    }

    override fun getStatusLayout(): StatusLayout {
        return mStatusLayout!!
    }
}