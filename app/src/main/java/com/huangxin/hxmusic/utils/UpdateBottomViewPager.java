package com.huangxin.hxmusic.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 当点开弹窗之后回到有底部的数据栏的时候更新数据的接口
 * 因为有多个地方存在 所以将放到集合当中操作
 */
public class UpdateBottomViewPager {
    public  static UpdateBottomViewPager INSTANCE;
    private InnerUpdateBottomViewPageLister innerUpdateBottomViewPageLister;
    public UpdateBottomViewPageListener updateBottomViewPageListener;
    private UpdateBottomViewPager(){
        innerUpdateBottomViewPageLister=new InnerUpdateBottomViewPageLister();
        updateBottomViewPageListener=innerUpdateBottomViewPageLister;
    }
    //提供单例模式
    public static UpdateBottomViewPager getINSTANCE(){
        if (INSTANCE==null){
            INSTANCE=new UpdateBottomViewPager();
        }
        return INSTANCE;
    }


    //注册
    public void registerUpdateBottomViewPageListener(UpdateBottomViewPageListener updateBottomViewPageListener){
        innerUpdateBottomViewPageLister.addAndRemoveUpdateBottomViewPageListener(updateBottomViewPageListener,true);
    }
    //取消注册
    public void unRegisterUpdateBottomViewPageListener(UpdateBottomViewPageListener updateBottomViewPageListener){
        innerUpdateBottomViewPageLister.addAndRemoveUpdateBottomViewPageListener(updateBottomViewPageListener,false);
    }
    //取消注册

    public class InnerUpdateBottomViewPageLister implements  UpdateBottomViewPageListener{
        private List<UpdateBottomViewPageListener> mListeners;
        public  void addAndRemoveUpdateBottomViewPageListener(UpdateBottomViewPageListener listener,boolean register){
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
        public void updateViewPager() {
            for (UpdateBottomViewPageListener listener:mListeners){
                listener.updateViewPager();
            }
        }
        //清空的list
        public void clear(){
            mListeners.clear();
        }
    }


    public interface UpdateBottomViewPageListener{
        void updateViewPager();
    }

}
