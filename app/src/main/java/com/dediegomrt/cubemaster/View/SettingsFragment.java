package com.dediegomrt.cubemaster.View;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Switch;

import com.dediegomrt.cubemaster.Config.PrefsConfig;
import com.dediegomrt.cubemaster.Methods.PrefsMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.View.Adapters.ColorsAdapter;
import com.dediegomrt.cubemaster.View.Dialogs.RestartDialog;

public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        PrefsConfig.getInstance().setContext(v.getContext());

        final Switch beep = (Switch)v.findViewById(R.id.switchBeep);
        final Switch color = (Switch)v.findViewById(R.id.switchColor);
        final GridView gridView = (GridView)v.findViewById(R.id.colorGridView);

        if(PrefsMethods.getInstance().isBeepActivated()){
            beep.setChecked(true);
        }
        if(PrefsMethods.getInstance().isColorActivated()){
            color.setChecked(true);
        }
        ColorsAdapter adapter = new ColorsAdapter(getActivity());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position!=PrefsMethods.getInstance().getColorAccent()) {
                    final RestartDialog dialog = new RestartDialog(getActivity(), position);
                    dialog.show();
                }
            }
        });

        beep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(beep.isChecked()){
                    PrefsMethods.getInstance().activateBeep(true);
                } else {
                    PrefsMethods.getInstance().activateBeep(false);
                }
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(color.isChecked()){
                    PrefsMethods.getInstance().activateColor(true);
                } else {
                    PrefsMethods.getInstance().activateColor(false);
                }
            }
        });

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
