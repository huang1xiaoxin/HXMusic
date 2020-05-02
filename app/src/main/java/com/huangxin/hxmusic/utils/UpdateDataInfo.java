package com.huangxin.hxmusic.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 当弹窗改变的时候，更新相对应的信息
 * 例如：更新音乐的ViewPager音乐信息，音乐详情页的信息
 */
public class UpdateDataInfo {
    public static UpdateDataInfo INSTANCE;
    public UpdateDataInfoListener updateDataInfoListener;
    private InnerUpdateInfoLister innerUpdateInfoLister;

    private UpdateDataInfo() {
        innerUpdateInfoLister = new InnerUpdateInfoLister();
        updateDataInfoListener = innerUpdateInfoLister;
    }
    //提供单例模式
    public static UpdateDataInfo getINSTANCE() {
        if (INSTANCE==null){
            INSTANCE = new UpdateDataInfo();
        }
        return INSTANCE;
    }


    //注册
    public void registerUpdateInfoListener(UpdateDataInfoListener updateDataInfoListener) {
        innerUpdateInfoLister.addAndRemoveUpdateBottomViewPageListener(updateDataInfoListener, true);
    }
    //取消注册
    public void unRegisterUpdateBottomViewPageListener(UpdateDataInfoListener updateDataInfoListener) {
        innerUpdateInfoLister.addAndRemoveUpdateBottomViewPageListener(updateDataInfoListener, false);
    }
    //取消注册

    public interface UpdateDataInfoListener {
        void updateInfo(int position);
    }

    public class InnerUpdateInfoLister implements UpdateDataInfoListener {
        private List<UpdateDataInfoListener> mListeners;

        public void addAndRemoveUpdateBottomViewPageListener(UpdateDataInfoListener listener, boolean register) {
            if (mListeners==null){
                mListeners=new ArrayList<>();
            }
            if (register){
                if (listener!=null&&!mListeners.contains(listener)){
                    mListeners.add(listener);
                }
            }else {
                if (listener!=null){
                    mListeners.remove(listener);
                }
            }
        }

        @Override
        public void updateInfo(int position) {
            for (UpdateDataInfoListener listener : mListeners) {
                listener.updateInfo(position);
            }
        }
    }

}
