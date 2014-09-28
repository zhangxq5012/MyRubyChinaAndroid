package cn.magic.rubychina.ui.itf;

import android.os.Bundle;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface OnFragmentInteractionListener {
    public static final String SOURCE="soutce";//来源fragment的标识
    public static final String ARGS="args";

    public void onFragmentInteraction(Bundle bundle);
}
