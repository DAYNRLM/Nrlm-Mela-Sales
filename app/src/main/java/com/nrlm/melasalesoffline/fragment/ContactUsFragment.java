package com.nrlm.melasalesoffline.fragment;

import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.activity.HomeActivity;
import com.nrlm.melasalesoffline.utils.AppUtility;

public class ContactUsFragment extends BaseFragment implements HomeActivity.OnBackPressedListener{

    public static ContactUsFragment getInstance()
    {
        ContactUsFragment contactUsFragment=new ContactUsFragment();
        return contactUsFragment;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_contact_us;
    }

    @Override
    public void onFragmentReady() {
        ((HomeActivity) getActivity()).setToolBarTitle("Contact us");
        ((HomeActivity) getActivity()).setOnBackPressedListener(this);

    }

    @Override
    public void doBack() {
        AppUtility.getInstance().replaceFragment(getFragmentManager(), new DashBoardFragment(),ContactUsFragment.class.getSimpleName(),false,R.id.fragmentContainer);

    }
}
