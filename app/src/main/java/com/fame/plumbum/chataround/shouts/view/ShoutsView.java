package com.fame.plumbum.chataround.shouts.view;

import com.fame.plumbum.chataround.shouts.model.Posts;

import java.util.List;

/**
 * Created by meghalagrawal on 10/06/17.
 */

public interface ShoutsView {

    void showMessage(String message);
    void showLoader(boolean show);

    void setData(List<Posts> posts);
}
