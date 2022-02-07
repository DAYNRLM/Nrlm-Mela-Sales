package com.nrlm.melasalesoffline.fragment;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.activity.HomeActivity;
import com.nrlm.melasalesoffline.database.entity.MainHelperEntity;
import com.nrlm.melasalesoffline.database.entity.ShgDetailEntity;
import com.nrlm.melasalesoffline.database.repository.CommonRepository;
import com.nrlm.melasalesoffline.utils.AppUtility;

import java.util.List;

import butterknife.BindView;

public class ProfileFragment extends BaseFragment implements HomeActivity.OnBackPressedListener{
    CommonRepository commonRepository;
    @BindView(R.id.main_participant)
    TextView mainparticipantTV;
    @BindView(R.id.shg_name)
    TextView shgNameTV;
    @BindView(R.id.helper_name)
    TextView helperNameTv;
    @BindView(R.id.helper_ph)
    TextView helperPhTV;
    @BindView(R.id.main_ph)
    TextView mainPhTv;

    public static ProfileFragment getInstance()
    {
        ProfileFragment profileFragment=new ProfileFragment();
        return profileFragment;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onFragmentReady() {
        ((HomeActivity) getActivity()).setToolBarTitle("Profile");
        ((HomeActivity) getActivity()).setOnBackPressedListener(this);
        fatchingDataFromLocalDB();


    }

    /****************fatching the data from the local DB and set on the profile****************/
    private void fatchingDataFromLocalDB() {
        commonRepository=CommonRepository.getInstance(getContext());
        MainHelperEntity mainHelperEntityData= commonRepository.mainHelperEntityData();
        mainparticipantTV.setText(mainHelperEntityData.getMain_name());
        shgNameTV.setText(commonRepository.getShgName(mainHelperEntityData.getShg_reg_id()));
        helperNameTv.setText(mainHelperEntityData.getAss_name());
        helperPhTV.setText(mainHelperEntityData.getAss_mobile());
        mainPhTv.setText(mainHelperEntityData.getMain_participant_mobile());
    }

    @Override
    public void doBack() {
        AppUtility.getInstance().replaceFragment(getFragmentManager(),new DashBoardFragment(),ProfileFragment.class.getSimpleName(),false,R.id.fragmentContainer);

    }
}
