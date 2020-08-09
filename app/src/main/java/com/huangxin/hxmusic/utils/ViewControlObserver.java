package com.huangxin.hxmusic.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 利用观察者模式实现对停止和播放的按钮
 */
public class ViewControlObserver {
    private int state = -1;
    private List<ButtonStateSubject> buttonStateSubjectList;

    public ViewControlObserver() {
        buttonStateSubjectList = new ArrayList<>();
    }

    //注册
    public void subscribe(ButtonStateSubject buttonStateSubject) {
        if (buttonStateSubject == null) {
            return;
        }
        if (buttonStateSubjectList != null && !buttonStateSubjectList.contains(buttonStateSubjectList)) {
            buttonStateSubjectList.add(buttonStateSubject);
        }
    }


    /**
     * 取消注册
     *
     * @param buttonStateSubject
     */
    public void unSubscribe(ButtonStateSubject buttonStateSubject) {
        if (buttonStateSubject == null) {
            return;
        }
        if (buttonStateSubject != null && buttonStateSubjectList.contains(buttonStateSubjectList)) {
            buttonStateSubjectList.remove(buttonStateSubject);
        }
    }


    /**
     * 通知所有的观察者按钮的状态发生改变
     */
    private void notifyUpdate(int state) {
        for (ButtonStateSubject buttonStateSubject : buttonStateSubjectList) {
            if (buttonStateSubject != null) {
                buttonStateSubject.updateButtonState(state);
            }

        }
    }

    /**
     * 更新按钮的状态，并发出通知
     *
     * @param state
     * @return
     */
    public int updateButtonState(int state) {
        notifyUpdate(state);
        this.state = state;
        return state;
    }

    public int getState() {
        return state;
    }
}
